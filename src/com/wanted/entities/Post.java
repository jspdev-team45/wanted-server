package com.wanted.entities;

import java.io.Serializable;

/**
 * Author: Junjian Xie
 * Email: junjianx@andrew.cmu.edu
 * Date: 15/11/12
 */
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;
	private int pid;
	private int uid;
    private Company company;
    private Recruiter recruiter;
    private String title;
    private String description;
    private String suitMajor;

    public Post(String title, String description, String suitMajor) {
        this.title = title;
        this.description = description;
        this.suitMajor = suitMajor;
    }
    
    public Post(int pid, int uid, String title, String description, String suitMajor) {
		super();
		this.pid = pid;
		this.uid = uid;
		this.title = title;
		this.description = description;
		this.suitMajor = suitMajor;
	}

    public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getTitle() {
        return title;
    }

	public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getSuitMajor() {
		return suitMajor;
	}


	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Recruiter getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(Recruiter recruiter) {
		this.recruiter = recruiter;
	}

	public void setSuitMajor(String suitMajor) {
		this.suitMajor = suitMajor;
	}
}
