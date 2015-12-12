// The driver that test the server side functionality
package com.wanted.database;

import java.sql.SQLException;

import com.wanted.entities.Company;
import com.wanted.entities.Post;
import com.wanted.entities.Recruiter;
import com.wanted.entities.Role;
import com.wanted.entities.Seeker;

public class Driver {
	public static void main(String[] args) throws SQLException {
		Database database = new Database();
		database.initialize();
		
		// insert seekers
		Seeker people1 = new Seeker("Alice", "123456", "alice@aa.com", Role.SEEKER);
        people1.setAvatar("people_ann.jpg");
        people1.setPhone("111111");
        people1.setCollege("Carnegie Mellon University");
        people1.setMajor("Computer Science");
		int id1 = database.insertSeeker(people1);
		database.updateUser(id1, people1.getAvatar(), people1.getRealName(), people1.getPhone(), null);
		database.updateSeeker(id1, people1.getCollege(), people1.getMajor());
        
        Seeker people2 = new Seeker("John", "111111", "john@aa.com", Role.SEEKER);
        people2.setAvatar("people_john.jpg");
        people2.setPhone("22222222");
        people2.setCollege("Carnegie Mellon University");
        people2.setMajor("Computer Science");
		int id2 = database.insertSeeker(people2);
		database.updateUser(id2, people2.getAvatar(), people2.getRealName(), people2.getPhone(), null);
		database.updateSeeker(id2, people2.getCollege(), people2.getMajor());
		
		Seeker people5 = new Seeker("Keith", "111111", "keith@aa.com", Role.SEEKER);
        people5.setAvatar("people_keith.png");
        people5.setPhone("55555");
        people5.setCollege("Carnegie Mellon University");
        people5.setMajor("Computer Science");
		int id5 = database.insertSeeker(people5);
		database.updateUser(id5, people5.getAvatar(), people5.getRealName(), people5.getPhone(), null);
		database.updateSeeker(id5, people5.getCollege(), people5.getMajor());
		
		// insert recruiters
		Company company1 = new Company("Google", "google.jpg", "This is description", "Pittsburgh");
		int cid1 = database.insertCompany(company1);

		Recruiter people3 = new Recruiter("Tom", "111111", "tom@aa.com", Role.RECRUITER);
        people3.setCompanyID(cid1);
        people3.setAvatar("people_tom.jpg");
        people3.setPhone("333333333");
        people3.setDepartment("Some department");
		int id3 = database.insertRecruiter(people3);
		database.updateUser(id3, people3.getAvatar(), people3.getRealName(), people3.getPhone(), null);
		database.updateRecruiter(id3, people3.getCompanyID(), people3.getDepartment());
		
		Company company2 = new Company("Facebook", "facebook.jpg", "This is description", "Pittsburgh");
		int cid2 = database.insertCompany(company2);

        Recruiter people4 = new Recruiter("Mike", "111111", "mike@aa.com", Role.RECRUITER);
        people4.setCompanyID(cid2);
        people4.setAvatar("people_mike.jpg");
        people4.setPhone("44444444");
        people4.setDepartment("Some department");
		int id4 = database.insertRecruiter(people4);
		database.updateUser(id4, people4.getAvatar(), people4.getRealName(), people4.getPhone(), null);
		database.updateRecruiter(id4, people4.getCompanyID(), people4.getDepartment());
		
		Company company3 = new Company("Oracle", "oracle.png", "This is description", "Pittsburgh");
		int cid3 = database.insertCompany(company3);

		Recruiter people6 = new Recruiter("Joan", "111111", "joan@aa.com", Role.RECRUITER);
        people6.setCompanyID(cid3);
        people6.setAvatar("people_joan.jpg");
        people6.setPhone("333333333");
        people6.setDepartment("Some department");
		int id6 = database.insertRecruiter(people6);
		database.updateUser(id6, people6.getAvatar(), people6.getRealName(), people6.getPhone(), null);
		database.updateRecruiter(id6, people6.getCompanyID(), people6.getDepartment());

		Company company4 = new Company("Microsoft", "microsoft.jpg", "This is description", "Pittsburgh");
		int cid4 = database.insertCompany(company4);
		
        Recruiter people7 = new Recruiter("Bill", "111111", "bill@aa.com", Role.RECRUITER);
        people7.setCompanyID(cid4);
        people7.setAvatar("people_bill.jpg");
        people7.setPhone("7777777777");
        people7.setDepartment("Some department");
		int id7 = database.insertRecruiter(people7);
		database.updateUser(id7, people7.getAvatar(), people7.getRealName(), people7.getPhone(), null);
		database.updateRecruiter(id7, people7.getCompanyID(), people7.getDepartment());
		
		Recruiter people8 = new Recruiter("Lisa", "111111", "lisa@aa.com", Role.RECRUITER);
        people8.setCompanyID(cid4);
        people8.setAvatar("people_lisa.jpg");
        people8.setPhone("88888888888888");
        people8.setDepartment("Some department");
		int id8 = database.insertRecruiter(people8);
		database.updateUser(id8, people8.getAvatar(), people8.getRealName(), people8.getPhone(), null);
		database.updateRecruiter(id8, people8.getCompanyID(), people8.getDepartment());

		Company company5 = new Company("Sharp", "sharp.jpg", "This is description", "Pittsburgh");
		int cid5 = database.insertCompany(company5);
		
        Recruiter people9 = new Recruiter("Rose", "111111", "rose@aa.com", Role.RECRUITER);
        people9.setCompanyID(cid5);
        people9.setAvatar("people_rose.jpg");
        people9.setPhone("9999999999");
        people9.setDepartment("Some department");
		int id9 = database.insertRecruiter(people9);
		database.updateUser(id9, people9.getAvatar(), people9.getRealName(), people9.getPhone(), null);
		database.updateRecruiter(id9, people9.getCompanyID(), people9.getDepartment());
		
		// insert posts
		Post post1 = new Post("Searching for a software engineer", "Description description description", "Computer Science");
		post1.setUid(id3);
		int pid1 = database.insertPost(post1);
		
		Post post2 = new Post("Hi, come for a software engineer", "Description this is description", "Computer Science");
		post2.setUid(id4);
		int pid2 = database.insertPost(post2);
		
		Post post3 = new Post("We want a software engineer", "Description this is description", "Computer Science");
		post3.setUid(id6);
		int pid3 = database.insertPost(post3);
		
		Post post4 = new Post("Looking for a software engineer", "Description description description", "Computer Science");
		post4.setUid(id7);
		int pid4 = database.insertPost(post4);
		
		Post post5 = new Post("Searching for a software engineer", "Description description description", "Computer Science");
		post5.setUid(id8);
		int pid5 = database.insertPost(post5);
		
		Post post6 = new Post("Searching for a software engineer", "Description description description", "Computer Science");
		post6.setUid(id9);
		database.insertPost(post6);
		
		// insert follow
		Seeker people10 = new Seeker("Annk", "1111111", "annk@aa.com", Role.SEEKER);
        people10.setAvatar("people_annk.jpg");
        people10.setPhone("111111");
        people10.setCollege("Carnegie Mellon University");
        people10.setMajor("Computer Science");
		int id10 = database.insertSeeker(people10);
		database.updateUser(id10, people10.getAvatar(), people10.getRealName(), people10.getPhone(), null);
		database.updateSeeker(id10, people10.getCollege(), people10.getMajor());
        
		Seeker people11 = new Seeker("Alex", "1111111", "alex@aa.com", Role.SEEKER);
        people11.setAvatar("people_alex.jpg");
        people11.setPhone("111111");
        people11.setCollege("Carnegie Mellon University");
        people11.setMajor("Computer Science");
		int id11 = database.insertSeeker(people11);
		database.updateUser(id11, people11.getAvatar(), people11.getRealName(), people11.getPhone(), null);
		database.updateSeeker(id11, people11.getCollege(), people11.getMajor());
		
		Seeker people12 = new Seeker("Cap", "1111111", "cap@aa.com", Role.SEEKER);
        people12.setAvatar("people_cap.jpg");
        people12.setPhone("111111");
        people12.setCollege("Carnegie Mellon University");
        people12.setMajor("Computer Science");
		int id12 = database.insertSeeker(people12);
		database.updateUser(id12, people12.getAvatar(), people12.getRealName(), people12.getPhone(), null);
		database.updateSeeker(id12, people12.getCollege(), people12.getMajor());
		
		Seeker people13 = new Seeker("Jike", "1111111", "jike@aa.com", Role.SEEKER);
        people13.setAvatar("people_jike.jpg");
        people13.setPhone("111111");
        people13.setCollege("Carnegie Mellon University");
        people13.setMajor("Computer Science");
		int id13 = database.insertSeeker(people13);
		database.updateUser(id13, people13.getAvatar(), people13.getRealName(), people13.getPhone(), null);
		database.updateSeeker(id13, people13.getCollege(), people13.getMajor());
		
		Seeker people14 = new Seeker("Maxim", "1111111", "maxim@aa.com", Role.SEEKER);
        people14.setAvatar("people_maxim.jpg");
        people14.setPhone("111111");
        people14.setCollege("Carnegie Mellon University");
        people14.setMajor("Computer Science");
		int id14 = database.insertSeeker(people14);
		database.updateUser(id14, people14.getAvatar(), people14.getRealName(), people14.getPhone(), null);
		database.updateSeeker(id14, people14.getCollege(), people14.getMajor());
		
		database.insertFollow(id1, id10);
		database.insertFollow(id1, id11);
		database.insertFollow(id1, id12);
		database.insertFollow(id1, id13);
		database.insertFollow(id1, id14);
		
		database.insertFollow(id8, id1);
		database.insertFollow(id9, id1);
		database.insertFollow(id10, id1);
		database.insertFollow(id11, id1);
		database.insertFollow(id12, id1);
		
		database.insertLike(id1, pid1);
		database.insertLike(id1, pid3);
		
		database.insertApply(pid1, id1);
		database.insertApply(pid2, id1);
		database.insertApply(pid3, id1);
		database.insertApply(pid4, id1);

		database.insertApply(pid1, id2);
		database.insertApply(pid2, id2);
		database.insertApply(pid3, id2);
		database.insertApply(pid4, id2);
		database.insertApply(pid5, id2);
		database.insertApply(pid5, id2);
	}
}
