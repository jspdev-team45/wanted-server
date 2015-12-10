package com.wanted.entities;

import java.io.Serializable;

/**
 * Author: Junjian Xie
 * Email: junjianx@andrew.cmu.edu
 * Date: 15/11/12
 */
public class Recruiter extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer companyID;
    private String department;

    public Recruiter(String name, String passWord, String email, Integer role) {
        super(name, passWord, email, role);
    }

    public Recruiter(int id, String name, String email, int role, String phone, String avatar, String realName) {
    	super(id, name, email, role, phone, avatar, realName);
    } 

    public String getEmail() {
        return super.getEmail();
    }

    public void setEmail(String email) {
        super.setEmail(email);
    }

    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
    }

    public String getPassWord() {
        return super.getPassWord();
    }

    public void setPassWord(String passWord) {
    	super.setPassWord(passWord);
    }

    public String getAvatar() {
        return super.getAvatar();
    }

    public void setAvatar(String avatar) {
        super.setAvatar(avatar);
    }

    public Integer getRole() {
        return super.getRole();
    }

    public void setRole(Integer role) {
        super.setRole(role);
    }

    public String getRealName() {
        return super.getRealName();
    }

    public void setRealName(String realName) {
        super.setRealName(realName);
    }
    
    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
