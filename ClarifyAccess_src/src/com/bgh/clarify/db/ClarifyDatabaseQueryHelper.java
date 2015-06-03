package com.bgh.clarify.db;

import com.bgh.clarify.dt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ClarifyDatabaseQueryHelper {
	private Connection jdbcConn;

        private static final String CASE_ID = " id_number = ";
        private static final String S_QUOTE = "'";
        private static final String OR = " OR ";
        private static final String EMPTY = "";
        
	private final int CASE_COLUMNS_COUNT = 12;

        private final String CLOSE_CASE_SUCCESS = "GOOD";
        private final String CHANGE_STATUS_SUCCESS = "GOOD";
        //private final String PLEASE_SPECIFY = "Please Specify";
        
	private final String CASE_LIST_QUERY_FOR_WG = "SELECT id_number,title, condition, status, priority, x_wg_name, "+
		 " x_stage_discov, x_misc, x_creation_time, x_modify_stmp, "+
		 " owner, x_type "+
            " FROM table_qry_case_view "+
             " WHERE elm_objid IN "+
			 " (SELECT table_case.objid "+
				 " FROM table_case, table_x_wg, table_queue, table_condition "+
				" WHERE table_condition.objid = table_case.case_state2condition "+
						" AND table_condition.title NOT IN "+
								 " ('Open-Dispatch', 'Closed') "+
						" AND table_case.x_case2wg = table_x_wg.objid "+
						" AND table_queue.x_queue2wg = table_x_wg.objid "+
						" AND table_x_wg.objid IN "+
								 " (SELECT wg.objid "+
									 " FROM table_queue queue1, "+
											" table_user user1, "+
											" mtm_queue4_user23 mtm, "+
											" table_x_wg wg "+
									" WHERE 	 queue1.objid = mtm.queue2user  "+
											" AND user1.objid = mtm.user_assigned2queue "+
											" AND queue1.x_queue2wg = wg.objid "+
											" AND user1.s_login_name = ?) "+
			  " UNION "+
			  " SELECT table_case.objid "+
				 " FROM table_case, table_queue, table_condition, table_x_wg "+
				" WHERE 	 table_condition.objid = table_case.case_state2condition "+
						" AND table_condition.title = 'Open-Dispatch' "+
						" AND table_case.case_currq2queue = table_queue.objid "+
						" AND table_queue.x_queue2wg = table_x_wg.objid "+
						" AND table_x_wg.objid IN "+
								 " (SELECT wg.objid "+
									 " FROM table_queue queue1, "+
											" table_user user1, "+
											" mtm_queue4_user23 mtm, "+
											" table_x_wg wg "+
									" WHERE 	 queue1.objid = mtm.queue2user "+
											" AND user1.objid = mtm.user_assigned2queue "+
											" AND queue1.x_queue2wg = wg.objid "+
											" AND user1.s_login_name = ?)) order by x_creation_time desc";

        private final String CASE_CONDITION_UPDATE_FOR_ACCEPT = "UPDATE table_condition "+
                 " SET condition = 16386, "+
		 " wipbin_time = sysdate, "+
		 " title = 'Open', "+
		 " s_title = 'OPEN' "+
                " WHERE objid = (select case_state2condition from table_case where id_number = ?) "+
		 " AND (	(MOD (condition, 16) >= 8) "+
				" OR (MOD (condition, 64) >= 32) "+
				" OR (MOD (condition, 1024) >= 512))";
        
        private final String USER_WIPBIN = "SELECT objid "+
                            " FROM table_wipbin "+
                            " WHERE wipbin_owner2user IN (SELECT objid FROM table_user "+
                            " WHERE s_login_name = ?) ORDER BY objid ASC";
        
        private final String CASE_UPDATE_FOR_ACCEPT = "update table_case "+
            " set case_wip2wipbin=?,case_owner2user=(select objid from table_user where s_login_name = ?), "+
            " X_CASE2WG=?, alt_contact2contact=NULL,case_distr2site=NULL, case_currq2queue = NULL "
                + " where id_number = ? and case_currq2queue is not null";
        
        private final String CASE_OBJID_FROM_ID = "select objid from table_case where id_number =?";
        
        private final String WG_FROM_CASE_QUEUE = "SELECT wg.objid "+
                " FROM table_queue queue1, table_x_wg wg "+
                " WHERE queue1.x_queue2wg = wg.objid AND queue1.objid =  "+
                " (SELECT case_currq2queue FROM table_case WHERE id_number = ?)";
        
        private final String CASE_QUERY_FOR_CLOSURE = "SELECT id_number, "+
		 " title, "+
		 " x_inc_impact_start, "+
		 " x_inc_impact_end, "+
		 " x_business_impact, "+
		 " x_restoration_action, "+
                 " creation_time "+
                " FROM table_case c "+
                " WHERE  ";
        
        private final String CASE_UPDATE_FOR_CLOSURE = "UPDATE table_case "+
                " SET x_inc_impact_start = ?, "+
		 " x_inc_impact_end = ?, "+
		 " x_business_impact = ?, "+
		 " x_restoration_action = ? "+
                " WHERE id_number = ? ";
        
        private final String CASE_CURR_STATUS_OBJID = "select CASESTS2GBST_ELM from table_case where id_number = ?";
        
        private final String CASE_NEXT_STATUS_VALID_VALUES = "SELECT distinct to_status_objid, "+
            " to_status_title "+
        " FROM table_x_trans_wg_v tta "+
        " WHERE (      (upper(tta.x_object_type) = 'CASE') "+
             " AND (upper(tta.x_call_type) = ?) "+
             " AND (tta.from_status_objid = ?) "+
             " AND (tta.x_wg_objid in (SELECT wg.objid  "+
                                    " FROM table_queue queue1,  "+
                                             " table_user user1,  "+
                                             " mtm_queue4_user23 mtm, "+
                                             " table_x_wg wg "+
                                     " WHERE queue1.objid = mtm.queue2user "+
                                             " AND user1.objid = mtm.user_assigned2queue "+
                                             " AND queue1.x_queue2wg = wg.objid "+
                                             " AND user1.s_login_name = ?)) "+
                " AND (upper(tta.x_to_cond_title) = 'OPEN') " +
                " AND (to_status_state in (0,2))) ";
        
        private final String NT_USER_CLFY_USER_MAP = "select clfy_login_name from table_nt_clfy_user_mapping where nt_login_name = ?";
        
        public ArrayList<CaseCloseDt> closeCaseList(ArrayList<CaseCloseDt> caseIdList){
            jdbcConn = getJDBCConnection();
                
                ArrayList<CaseCloseDt> closeSuccessCases = new ArrayList<CaseCloseDt>();
                
                for (int i = 0; i < caseIdList.size(); i++) {
                    CaseCloseDt closeCaseDt = caseIdList.get(i);
                    boolean success = closeCase(jdbcConn,closeCaseDt.getCaseId());
                    if(success){
                        closeSuccessCases.add(closeCaseDt);
                    }  
                }
                return closeSuccessCases;
        }
        
        public ArrayList<String> acceptCaseList(ArrayList<String> caseIdList, String userName){
        	jdbcConn = getJDBCConnection();
                
                ArrayList<String> acceptSuccessCases = new ArrayList<String>();
                
                for (int i = 0; i < caseIdList.size(); i++) {
                    String caseId = caseIdList.get(i);
                    boolean success = acceptCase(jdbcConn,caseId, userName);
                    if(success){
                        acceptSuccessCases.add(caseId);
                    }  
                }
                return acceptSuccessCases;
	}
        
        private boolean acceptCase(Connection jdbcConn, String caseId, String userName){
            PreparedStatement pstmtCond = null;
            PreparedStatement pstmtCase = null;
            
            long caseObjid = getCaseObjid(jdbcConn,caseId);
            String wipbinObjid = getUserWipbinObjid(userName);
            long wgObjid = getCaseWGObjid(caseId);
            try {
                pstmtCond = jdbcConn.prepareStatement(CASE_CONDITION_UPDATE_FOR_ACCEPT);
                pstmtCond.setString(1, caseId);
                int affectedRowsCond = pstmtCond.executeUpdate();
                
                pstmtCase = jdbcConn.prepareStatement(CASE_UPDATE_FOR_ACCEPT);
                pstmtCase.setString(1,wipbinObjid);
                pstmtCase.setString(2, userName.toUpperCase());
                pstmtCase.setLong(3, wgObjid);
                pstmtCase.setString(4, caseId);
                int affectedRowsCase = pstmtCase.executeUpdate();
                
                
                if(affectedRowsCond > 0 && affectedRowsCase > 0){
                    //Connection jdbcConn, long rank, String info, long caseObjid, String relationToObject
                    //(100,'from Queue ADM - TC CRM to WIP default.',case_objid, 'ACT_ENTRY2CASE',act_entry_objid)
                    
                    String info = "from Queue of " + userName + " to WIPBIN with objid "+wipbinObjid;
                    
                    createActEntry(jdbcConn, 100, info, caseObjid,"ACT_ENTRY2CASE");
                    jdbcConn.commit();
                    return true;
                }else{
                    jdbcConn.rollback();
                    return false;
                }
                
            } catch (SQLException e) {
			System.out.println("Error in acceptCase : "+e.getMessage());
			System.out.println(e);
                        java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
			//e.printStackTrace();			
		}finally{
			try {
				if(pstmtCond != null){
					pstmtCond.close();
				}
			} catch (SQLException e) {
				System.out.println("Error while closing prepared stmt in acceptCase : "+e);
                                java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
				//e.printStackTrace();
			}
		}
            
            return false;
        }
        
        private String getUserWipbinObjid(String loginName){
                System.out.println("Entry:getUserWipbin");
                PreparedStatement pstmt = null;
		ResultSet resultSet =null;
		try {
			pstmt = jdbcConn.prepareStatement(USER_WIPBIN);
			System.out.println("--SQL: "+USER_WIPBIN);
			pstmt.setString(1, loginName.toUpperCase());
                       resultSet = pstmt.executeQuery();

                       String wipBin = null;
                       
			if(resultSet.next()) {				
                            wipBin = resultSet.getString(1);
                        }
			System.out.println("Exit:getUserWipbin");
			return wipBin;

		} catch (SQLException e) {
			System.out.println("Error in getUserWipbin : "+e.getMessage());
			System.out.println(e);
                        java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
			//e.printStackTrace();			
		}finally{
			try {
				if(resultSet != null){
					resultSet.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
			} catch (SQLException e) {
				System.out.println("Error while closing prepared stmt in getUserWipbin : "+e);
                                java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
				//e.printStackTrace();
			}
		}	
		System.out.println("Exit:getUserWipbin with null");
		return null;            
        }
        
	public Object[][] getCaseList(String loginName){

		jdbcConn = getJDBCConnection();

		ArrayList<CaseWGQueryDt> caseList = getCaseListForWGInArray(jdbcConn,CASE_LIST_QUERY_FOR_WG,loginName.toUpperCase());

		Object [][] caseListArr =  covertArrayListToObjectArr(caseList);

		return caseListArr;
	}

	private Object[][] covertArrayListToObjectArr(ArrayList<CaseWGQueryDt> caseList) {
		int size = caseList.size();
		Object [][] caseListArr = new Object[size][CASE_COLUMNS_COUNT];

		int j=0;

		for(int i=0;i<size;i++){
			CaseWGQueryDt caseQueryDt = caseList.get(i);
			caseListArr[i][j]= caseQueryDt.getCaseID();
			j++;
			caseListArr[i][j]= caseQueryDt.getCaseTitle();
			j++;
			caseListArr[i][j]= caseQueryDt.getCondition();
			j++;
			caseListArr[i][j]= caseQueryDt.getStatus();
			j++;
			caseListArr[i][j]= caseQueryDt.getPriority();
			j++;
			caseListArr[i][j]= caseQueryDt.getWg();
			j++;
			caseListArr[i][j]= caseQueryDt.getStageDiscovered();
			j++;
			caseListArr[i][j]= caseQueryDt.getEnvironment();
			j++;
			caseListArr[i][j]= caseQueryDt.getCreationTime(); // 8
			j++;
			caseListArr[i][j]= caseQueryDt.getLastModified(); // 9
			j++;
			caseListArr[i][j]= caseQueryDt.getOwner();
			j++;
			caseListArr[i][j]= caseQueryDt.getType();
			j=0;
		}

		return caseListArr;
	}

	private ArrayList<CaseWGQueryDt> getCaseListForWGInArray(Connection jdbcConn, String caseListQuery, String loginName) {
		System.out.println("Entry:getCaseListInArray");
		PreparedStatement pstmt = null;
		ResultSet resultSet =null;
		ArrayList<CaseWGQueryDt> caseList = new ArrayList<CaseWGQueryDt>();
		try {
			pstmt = jdbcConn.prepareStatement(caseListQuery);
			System.out.println("--SQL: "+caseListQuery);
			pstmt.setString(1, loginName);
                        pstmt.setString(2, loginName);
			resultSet = pstmt.executeQuery();

			while(resultSet.next()) {				

				String idNumber = resultSet.getString(1);
                                String caseTitle = resultSet.getString(2);
                                String condition = resultSet.getString(3);
                                String status = resultSet.getString(4);
                                String priority = resultSet.getString(5);
                                String wg = resultSet.getString(6);
                                String stageDisc = resultSet.getString(7);
                                String environment = resultSet.getString(8);
                                Timestamp creationTime = resultSet.getTimestamp(9);
                                Timestamp lastModified = resultSet.getTimestamp(10);
                                String ownerLoginName = resultSet.getString(11);
                                String type = resultSet.getString(12);

				CaseWGQueryDt caseQueryDt = new CaseWGQueryDt();

				if(idNumber != null){
					caseQueryDt.setCaseID(idNumber);
				}
				if(caseTitle != null){
					caseQueryDt.setCaseTitle(caseTitle);
				}
				if(condition != null){
					caseQueryDt.setCondition(condition);
				}
				if(status != null){
					caseQueryDt.setStatus(status);
				}
				if(priority != null){
					caseQueryDt.setPriority(priority);
				}
				if(wg != null){
					caseQueryDt.setWg(wg);
				}
				if(stageDisc != null){
					caseQueryDt.setStageDiscovered(stageDisc);
				}
				if(environment  != null){
					caseQueryDt.setEnvironment(environment);
				}
				if(creationTime != null){
					caseQueryDt.setCreationTime(new java.util.Date(creationTime.getTime()));
				}
				if(lastModified != null){
					caseQueryDt.setLastModified(new java.util.Date(lastModified.getTime()));
				}
				if(ownerLoginName != null){
					caseQueryDt.setOwner(ownerLoginName);
				}
				if(type != null){
					caseQueryDt.setType(type);
				}				
				caseList.add(caseQueryDt);
			}
			System.out.println("Exit:getCaseListInArray");
			return caseList;

		} catch (SQLException e) {
			System.out.println("Error in getCaseListInArray : "+e.getMessage());
			System.out.println(e);
                        java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
			//e.printStackTrace();			
		}finally{
			try {
				if(resultSet != null){
					resultSet.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
			} catch (SQLException e) {
				System.out.println("Error while closing prepared stmt in getCaseListInArray : "+e);
                                java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
				//e.printStackTrace();
			}
		}	
		System.out.println("Exit:getCaseListInArray with null");
		return null;
	}

    private long createActEntry(Connection jdbcConn, long rank, String info, long caseObjid, String relationToObject) {
        long actEntryObjid = 0;
        
        CallableStatement cs = null;
        try{
            //(100,'from Queue ADM - TC CRM to WIP default.',case_objid, 'ACT_ENTRY2CASE',act_entry_objid)
            cs = jdbcConn.prepareCall("call acms_globals_pg.glb_create_activity_log(?,?,?,?,?)");
            cs.setLong(1, rank);
            cs.setString(2, info);
            cs.setLong(3, caseObjid);
            cs.setString(4, relationToObject);
            cs.registerOutParameter(5, Types.BIGINT);
            cs.execute();

            actEntryObjid = cs.getLong(5);
        
        } catch (SQLException e) {
			System.out.println("Error in createActEntry : "+e.getMessage());
			System.out.println(e);			
		}finally{
			try {
				
				if(cs != null){
					cs.close();
				}
			} catch (SQLException e) {
				System.out.println("Error while closing prepared stmt in createActEntry : "+e);				
			}
		}
        
            return actEntryObjid;                
    }

    private long getCaseObjid(Connection jdbcConn, String caseId) {
        PreparedStatement pstmt = null;
		ResultSet resultSet =null;
                long caseObjid = 0;
		try {
			pstmt = jdbcConn.prepareStatement(CASE_OBJID_FROM_ID);
			System.out.println("--SQL: "+CASE_OBJID_FROM_ID);
			pstmt.setString(1, caseId);                        
			resultSet = pstmt.executeQuery();

			if(resultSet.next()) {				
                        	caseObjid = resultSet.getLong(1);
                        }
			System.out.println("Exit:getCaseListInArray");
			

		} catch (SQLException e) {
			System.out.println("Error in getCaseListInArray : "+e.getMessage());
			System.out.println(e);
                        java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
			//e.printStackTrace();			
		}finally{
			try {
				if(resultSet != null){
					resultSet.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
			} catch (SQLException e) {
				System.out.println("Error while closing prepared stmt in getCaseListInArray : "+e);
                                java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
				//e.printStackTrace();
			}
		}
                return caseObjid;
    }

    private long getCaseWGObjid(String caseId) {
            PreparedStatement pstmt = null;
            ResultSet resultSet =null;
            long wgObjid = 0;
            try {
                    pstmt = jdbcConn.prepareStatement(WG_FROM_CASE_QUEUE);
                    System.out.println("--SQL: "+WG_FROM_CASE_QUEUE);
                    pstmt.setString(1, caseId);                        
                    resultSet = pstmt.executeQuery();

                    if(resultSet.next()) {				
                            wgObjid = resultSet.getLong(1);
                    }
                    System.out.println("Exit:getCaseWGObjid");


            } catch (SQLException e) {
                    System.out.println("Error in getCaseWGObjid : "+e.getMessage());
                    System.out.println(e);
                    java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                    //e.printStackTrace();			
            }finally{
                    try {
                            if(resultSet != null){
                                    resultSet.close();
                            }
                            if(pstmt != null){
                                    pstmt.close();
                            }
                    } catch (SQLException e) {
                            System.out.println("Error while closing prepared stmt in getCaseWGObjid : "+e);
                            java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                            //e.printStackTrace();
                    }
            }
            return wgObjid;
    }

    private boolean closeCase(Connection jdbcConn, String caseId) {
        String status = null;
        
        CallableStatement cs = null;
        try{
            // xl_clarify_close_case.main('009070',Status);
            cs = jdbcConn.prepareCall("call xl_clarify_close_case.main(?,?)");
            cs.setString(1, caseId);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.execute();

            status = cs.getString(2);
        
            if(CLOSE_CASE_SUCCESS.equals(status)){
                return true;
            }
            
        } catch (SQLException e) {
			System.out.println("Error in closeCase : "+e.getMessage());
			System.out.println(e);			
		}finally{
			try {
				
				if(cs != null){
					cs.close();
				}
			} catch (SQLException e) {
				System.out.println("Error while closing prepared stmt in closeCase : "+e);				
			}
		}
        
            return false;
    }
    
    private String getCaseQueryFilter(ArrayList<CaseCloseDt> eligibleIncidentCases) {
    
        int eligibleIncidentCasesSize = eligibleIncidentCases.size();
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<eligibleIncidentCasesSize;i++){
            
            if(i != 0){
                builder.append(OR);
            }
        	
            builder.append(CASE_ID);
            builder.append(S_QUOTE);
            builder.append(eligibleIncidentCases.get(i).getCaseId());
            builder.append(S_QUOTE);
            
            
        }
        return builder.toString().trim();
    }
    
   public ArrayList<CaseCloseDt> getCaseDetailForClosure(ArrayList<CaseCloseDt> eligibleIncidentCases) {
        String filter = getCaseQueryFilter(eligibleIncidentCases);
        ArrayList<CaseCloseDt> resultIncidentCaseList = new ArrayList<CaseCloseDt>();
        if(EMPTY.equals(filter)){
           return resultIncidentCaseList;  
        }
        
        String finalQuery = CASE_QUERY_FOR_CLOSURE + filter;
        
        Statement stmt = null;
	ResultSet resultSet =null;
        try {
                stmt = jdbcConn.createStatement();
                System.out.println("--SQL: "+finalQuery);
                resultSet = stmt.executeQuery(finalQuery);

                while(resultSet.next()) {				

                        String idNumber = resultSet.getString(1);
                        String caseTitle = resultSet.getString(2);
                        Timestamp ts1 = resultSet.getTimestamp(3);
                        java.util.Date impactStartTime = null;
                        if(ts1 != null){
                            impactStartTime = new java.util.Date(ts1.getTime()); 
                        }
                        
                        Timestamp ts2 = resultSet.getTimestamp(4);
                        java.util.Date impactEndTime = null;
                        if(ts2 != null){
                            impactEndTime = new java.util.Date(ts2.getTime());
                        }
                        String businessImpact = resultSet.getString(5);
                        String restorationAction = resultSet.getString(6);

                        Timestamp ts3 = resultSet.getTimestamp(7);
                        java.util.Date creationTime = null;
                        if(ts2 != null){
                            creationTime = new java.util.Date(ts3.getTime());
                        }
                        
                        CaseCloseDt casecloseDt = new CaseCloseDt();

                        if(idNumber != null){
                                casecloseDt.setCaseId(idNumber);
                        }
                        if(caseTitle != null){
                                casecloseDt.setCaseTitle(caseTitle);
                        }
                        if(impactStartTime != null){
                                casecloseDt.setImpactStartTime(impactStartTime);
                        }
                        if(impactEndTime != null){
                                casecloseDt.setImpactEndTime(impactEndTime);
                        }
                        if(businessImpact != null){
                                casecloseDt.setBusinessImpact(businessImpact);
                        }
                        if(restorationAction != null){
                                casecloseDt.setRestorationAction(restorationAction);
                        }
                        if(creationTime != null){
                                casecloseDt.setCreationTime(creationTime);
                        }
                        resultIncidentCaseList.add(casecloseDt);
                }
                System.out.println("Exit:getCaseDetailForClosure");
                return resultIncidentCaseList;

        } catch (SQLException e) {
                System.out.println("Error in getCaseDetailForClosure : "+e.getMessage());
                System.out.println(e);
                java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                //e.printStackTrace();			
        }finally{
                try {
                        if(resultSet != null){
                                resultSet.close();
                        }
                        if(stmt != null){
                                stmt.close();
                        }
                } catch (SQLException e) {
                        System.out.println("Error while closing prepared stmt in getCaseDetailForClosure : "+e);
                        java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                        //e.printStackTrace();
                }
        }	
        System.out.println("Exit:getCaseDetailForClosure with null");              
        return null;
    }

   public boolean saveSingleCaseCloseDetails(String caseId, java.util.Date impactStartTime, 
           java.util.Date impactEndTime, String businessImpact, String restorationAction) {
            PreparedStatement pstmt = null;
            
            try {
                pstmt = jdbcConn.prepareStatement(CASE_UPDATE_FOR_CLOSURE);
                pstmt.setTimestamp(1,new Timestamp(impactStartTime.getTime()));
                pstmt.setTimestamp(2, new Timestamp(impactEndTime.getTime()));
                pstmt.setString(3, businessImpact);
                pstmt.setString(4, restorationAction);
                pstmt.setString(5, caseId);
                int affectedRowsCase = pstmt.executeUpdate();
                
                if(affectedRowsCase > 0){
                    jdbcConn.commit();
                    return true;
                }else{
                    jdbcConn.rollback();
                    return false;
                }
                
            } catch (SQLException e) {
			System.out.println("Error in saveSingleCaseCloseDetails : "+e.getMessage());
			System.out.println(e);
                        java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
			//e.printStackTrace();			
		}finally{
			try {
				if(pstmt != null){
					pstmt.close();
				}
			} catch (SQLException e) {
				System.out.println("Error while closing prepared stmt in saveSingleCaseCloseDetails : "+e);
				java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                                //e.printStackTrace();
			}
		}
            
            return false;
    }
   
   public String getCurrentStatusObjid(String caseId) {
        PreparedStatement pstmt = null;
            ResultSet resultSet =null;
            String statusObjid = null;
            try {
                    pstmt = jdbcConn.prepareStatement(CASE_CURR_STATUS_OBJID);
                    System.out.println("--SQL: "+CASE_CURR_STATUS_OBJID);
                    pstmt.setString(1, caseId);                        
                    resultSet = pstmt.executeQuery();

                    if(resultSet.next()) {				
                            statusObjid = resultSet.getString(1);
                    }
                    System.out.println("Exit:getCurrentStatusObjid");


            } catch (SQLException e) {
                    System.out.println("Error in getCurrentStatusObjid : "+e.getMessage());
                    System.out.println(e);
                    java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                    //e.printStackTrace();			
            }finally{
                    try {
                            if(resultSet != null){
                                    resultSet.close();
                            }
                            if(pstmt != null){
                                    pstmt.close();
                            }
                    } catch (SQLException e) {
                            System.out.println("Error while closing prepared stmt in getCurrentStatusObjid : "+e);
                            java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                            //e.printStackTrace();
                    }
            }
            return statusObjid;
    }

    private ArrayList<CaseSingleStatusDt> getNewStatusList(String caseType, String currentStatusObjid, String username) {
        System.out.println("Entry:getNewStatusList");
		PreparedStatement pstmt = null;
		ResultSet resultSet =null;
		ArrayList<CaseSingleStatusDt> newStatusList = new ArrayList<CaseSingleStatusDt>();
		try {
			pstmt = jdbcConn.prepareStatement(CASE_NEXT_STATUS_VALID_VALUES);
			System.out.println("--SQL: "+CASE_NEXT_STATUS_VALID_VALUES);
			pstmt.setString(1, caseType.toUpperCase());
                        pstmt.setString(2, currentStatusObjid);
                        pstmt.setString(3, username.toUpperCase());
			resultSet = pstmt.executeQuery();

			while(resultSet.next()) {				

				String toStatusObjid = resultSet.getString(1);
                                String toStatus = resultSet.getString(2);
                                
				CaseSingleStatusDt caseStatusDt = new CaseSingleStatusDt();

				if(toStatusObjid != null){
					caseStatusDt.setStatusObjid(toStatusObjid);
				}
				if(toStatus != null){
					caseStatusDt.setStatus(toStatus);
				}
				newStatusList.add(caseStatusDt);
			}
                        
                        /*CaseSingleStatusDt caseStatusDtDummy = new CaseSingleStatusDt();
                        caseStatusDtDummy.setStatus(PLEASE_SPECIFY);
                        caseStatusDtDummy.setStatusObjid("0");
                        newStatusList.add(caseStatusDtDummy);*/
                        
			System.out.println("Exit:getNewStatusList");
			return newStatusList;

		} catch (SQLException e) {
			System.out.println("Error in getNewStatusList : "+e.getMessage());
			System.out.println(e);
                        java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
			//e.printStackTrace();			
		}finally{
			try {
				if(resultSet != null){
					resultSet.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
			} catch (SQLException e) {
				System.out.println("Error while closing prepared stmt in getNewStatusList : "+e);
                                java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
				//e.printStackTrace();
			}
		}	
		System.out.println("Exit:getNewStatusList with null");
		return null;
    }
   
    public ArrayList<CaseChangeStatusDt> getCaseTypesWithValidNextStatusValues(ArrayList<CaseChangeStatusDt> eligibleCaseTypes, String username) {
        int size = eligibleCaseTypes.size();
        
        for (int i = 0; i < size; i++) {
            CaseChangeStatusDt currentItem = eligibleCaseTypes.get(i);
            
            String caseType = currentItem.getCaseType();
            String currentStatusObjid = currentItem.getCurrentStatusObjid();
            
            ArrayList<CaseSingleStatusDt> newStatusList = getNewStatusList(caseType,currentStatusObjid,username);
            currentItem.setNewStatusList(newStatusList);
                        
        }
        
        return eligibleCaseTypes;
    }
   
    private boolean changeStatus(Connection jdbcConn, String caseId, CaseSingleStatusDt newStatus) {
        String status = null;
        
        CallableStatement cs = null;
        try{
            // xl_clarify_change_status.main('009070','Working',2132312,Status);
            cs = jdbcConn.prepareCall("call xl_clarify_change_status.main(?,?,?,?)");
            cs.setString(1, caseId);
            cs.setString(2, newStatus.getStatus());
            cs.setString(3, newStatus.getStatusObjid());
            cs.registerOutParameter(4, Types.VARCHAR);
            cs.execute();

            status = cs.getString(4);
        
            if(CHANGE_STATUS_SUCCESS.equals(status)){
                return true;
            }
            
        } catch (SQLException e) {
			System.out.println("Error in changeStatus : "+e.getMessage());
			System.out.println(e);			
		}finally{
			try {
				
				if(cs != null){
					cs.close();
				}
			} catch (SQLException e) {
				System.out.println("Error while closing prepared stmt in changeStatus : "+e);				
			}
		}
        
            return false;
    }
    
    public ArrayList<CaseSimpleDt> changeStatusForCaseList(ArrayList<CaseSimpleDt> eligibleCases) {
        jdbcConn = getJDBCConnection();
                
        ArrayList<CaseSimpleDt> changeStatusSuccessCases = new ArrayList<CaseSimpleDt>();

        for (int i = 0; i < eligibleCases.size(); i++) {
            CaseSimpleDt caseSimpleDt = eligibleCases.get(i);
            CaseSingleStatusDt newStatus = caseSimpleDt.getNewStatus();
            if(newStatus == null){
                continue;
            }            
            boolean success = changeStatus(jdbcConn,caseSimpleDt.getCaseId(), newStatus);
            if(success){
                changeStatusSuccessCases.add(caseSimpleDt);
            }  
        }
        return changeStatusSuccessCases;
    }
    
    public String getClarifyUserForNtUser(String loggedInUserName) {
        jdbcConn = getJDBCConnection();
        String clfyUser = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet =null;
            
        try {
                pstmt = jdbcConn.prepareStatement(NT_USER_CLFY_USER_MAP);
                //System.out.println("--SQL: "+NT_USER_CLFY_USER_MAP);
                pstmt.setString(1, loggedInUserName);                        
                resultSet = pstmt.executeQuery();

                if(resultSet.next()) {				
                        clfyUser = resultSet.getString(1);
                }
                System.out.println("Exit:getClarifyUserForNtUser");


        } catch (SQLException e) {
                System.out.println("Error in getClarifyUserForNtUser : "+e.getMessage());
                System.out.println(e);
                java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                //e.printStackTrace();			
        }finally{
                try {
                        if(resultSet != null){
                                resultSet.close();
                        }
                        if(pstmt != null){
                                pstmt.close();
                        }
                } catch (SQLException e) {
                        System.out.println("Error while closing prepared stmt in getClarifyUserForNtUser : "+e);
                        java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                        //e.printStackTrace();
                }
        }
        return clfyUser;
    }
    
    private Connection getJDBCConnection() {
		if(jdbcConn != null){
			return jdbcConn;
		}

		ResourceBundle bundle = ResourceBundle.getBundle("dbconfig");
		String jdbcHost = bundle.getString("db_jdbc_host").trim();
		String jdbcPort = bundle.getString("db_jdbc_port").trim();
		String jdbcSID = bundle.getString("db_jdbc_sid").trim();
		String username = bundle.getString("db_username").trim();
		String password = bundle.getString("db_password").trim();


		System.out.println("Entry:getJDBCConnection");
		//Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println(e);
			return null;
		}
		String url = new StringBuilder().append("jdbc:oracle:thin:@").append(jdbcHost).
		append(":").append(jdbcPort).append(":").append(jdbcSID).toString();

		try {
			jdbcConn = DriverManager.getConnection(url,username, password);
			jdbcConn.setAutoCommit(false);
                        CallableStatement stmt = jdbcConn.prepareCall("{call dbms_output.disable()}");
                        stmt.execute();
                        System.out.println("Dbms output disabled..");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println(e);
			return null;
		}
		System.out.println("Exit:getJDBCConnection");
		return jdbcConn;  

	}
    
    public void closeJdbcConnection() {
        if(jdbcConn != null){
                try {
                        jdbcConn.close();
                        System.out.println("JDBC Connection closed successfully");
                } catch (SQLException e) {
                        System.err.println("error in closeConnection: "+e);
                        java.util.logging.Logger.getLogger(ClarifyDatabaseQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                        //e.printStackTrace();
                }
        }
    }
  
}
