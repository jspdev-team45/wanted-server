package com.wanted.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Junjian Xie
 * Email: junjianx@andrew.cmu.edu
 * Date: 15/11/12
 */
public class Seeker extends User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String college;
    private String major;
    private List<Experience> experiences;

    public Seeker(String name, String passWord, String email, Integer role) {
        super(name, passWord, email, role);
        this.experiences = new ArrayList<Experience>();
    }
    
    public Seeker(int id, String name, String email, int role, String phone, String avatar, String realName) {
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

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }
}
