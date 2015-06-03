package com.bgh.clarify.dt;

import java.util.Date;

public class CaseWGQueryDt {
	private String caseID;
	private String caseTitle;
	private String condition;
	private String status;
	private String priority;
	private String wg;
	private String stageDiscovered;
	private String environment;
	private Date creationTime;
	private Date lastModified;
	private String owner;
	private String type;
	
	public String getCaseID() {
		return caseID;
	}
	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getStageDiscovered() {
		return stageDiscovered;
	}
	public void setStageDiscovered(String stageDiscovered) {
		this.stageDiscovered = stageDiscovered;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWg() {
		return wg;
	}
	public void setWg(String wg) {
		this.wg = wg;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getCaseTitle() {
		return caseTitle;
	}
	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
}
