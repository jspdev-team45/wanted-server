package com.wanted.entities;

import java.io.Serializable;

/**
 * Author: Junjian Xie
 * Email: junjianx@andrew.cmu.edu
 * Date: 15/11/12
 */
public class Experience implements Serializable {
	private static final long serialVersionUID = 1L;
    private int userID;
    private String startDate;
    private String endDate;
    private String description;
    private byte exType;

    public Experience(int userID, String startDate, String endDate, String description, byte exType) {
		this.userID = userID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.exType = exType;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte getExType() {
        return exType;
    }

    public void setExType(byte exType) {
        this.exType = exType;
    }

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
}
