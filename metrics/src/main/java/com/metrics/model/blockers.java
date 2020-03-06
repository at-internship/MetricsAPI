package com.metrics.model;


//this is a commit of Irvin
public class blockers {

	private boolean blocked;
	private String comments;
	public boolean isBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public blockers(boolean blocked, String comments) {
		this.blocked = blocked;
		this.comments = comments;
	}
}
