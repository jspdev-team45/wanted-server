package com.wanted.database;

import java.io.FileInputStream;
import java.util.Properties;

public class SQLCommand {
	private Properties prop;
	private String fileName = "src/data/sql.properties";
	
	public SQLCommand() {
		try (FileInputStream in = new FileInputStream(this.fileName)) {
			prop = new Properties();
			prop.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SQLCommand(String fileName) {
		this.fileName = fileName;
		try (FileInputStream in = new FileInputStream(this.fileName)) {
			prop = new Properties();
			prop.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String createDB() {
		return prop.getProperty("CREATE_DB");
	}
	
	public String checkTableExist() {
		return prop.getProperty("CHECK_TABLE_EXISTS");
	}
	
	public String createTableUser() {
		return prop.getProperty("CREATE_TABLE_USER");
	}
	
	public String createTableSeeker() {
		return prop.getProperty("CREATE_TABLE_SEEKER");
	}
	
	public String createTableRecruiter() {
		return prop.getProperty("CREATE_TABLE_RECRUITER");
	}
	
	public String createTableCompany() {
		return prop.getProperty("CREATE_TABLE_COMPANY");
	}
	
	public String createTableExper() {
		return prop.getProperty("CREATE_TABLE_EXPER");
	}
	
	public String createTablePost() {
		return prop.getProperty("CREATE_TABLE_POST");
	}

	public String createTableFollow() {
		return prop.getProperty("CREATE_TABLE_FOLLOW");
	}
	
	public String createTableLike() {
		return prop.getProperty("CREATE_TABLE_LIKE");
	}
	
	public String createTableApply() {
		return prop.getProperty("CREATE_TABLE_APPLY");
	}
	
	public String insertUser() {
		return prop.getProperty("INSERT_USER");
	}
	
	public String insertSeeker() {
		return prop.getProperty("INSERT_SEEKER");
	}
	
	public String insertRecruiter() {
		return prop.getProperty("INSERT_RECRUITER");
	}
	
	public String insertCompany() {
		return prop.getProperty("INSERT_COMPANY");
	}
	
	public String insertExper() {
		return prop.getProperty("INSERT_EXPER");
	}
	
	public String insertPost() {
		return prop.getProperty("INSERT_POST");
	}

	public String insertFollow() {
		return prop.getProperty("INSERT_FOLLOW");
	}
	
	public String deleteUser() {
		return prop.getProperty("DELETE_USER");
	}
	
	public String deleteExper() {
		return prop.getProperty("DELETE_EXPER");
	}
	
	public String deletePost() {
		return prop.getProperty("DELETE_POST");
	}
	
	public String deleteFollow() {
		return prop.getProperty("DELETE_FOLLOW");
	}
	
	public String deleteFollowing() {
		return prop.getProperty("DELETE_FOLLOWING");
	}

	public String updateUser() {
		return prop.getProperty("UPDATE_USER");
	}
	
	public String updateSeeker() {
		return prop.getProperty("UPDATE_SEEKER");
	}
	
	public String updateRecruiter() {
		return prop.getProperty("UPDATE_RECRUITER");
	}
	
	public String updateCompany() {
		return prop.getProperty("UPDATE_COMPANY");
	}
	
	public String updateExper() {
		return prop.getProperty("UPDATE_EXPER");
	}
	
	public String updatePost() {
		return prop.getProperty("UPDATE_POST");
	}
	
	public String getFollowingById() {
		return prop.getProperty("GET_FOLLOWING_BY_ID");
	}

	public String getFollowerById() {
		return prop.getProperty("GET_FOLLOWER_BY_ID");
	}

	public String getUserById() {
		return prop.getProperty("GET_USER_BY_ID");
	}

	public String getUserByName() {
		return prop.getProperty("GET_USER_BY_NAME");
	}

	public String getUserByEmail() {
		return prop.getProperty("GET_USER_BY_EMAIL");
	}
	
	public String getAvatarByHash() {
		return prop.getProperty("GET_AVATAR_BY_HASH");
	}

	public String getSeeker() {
		return prop.getProperty("GET_SEEKER");
	}

	public String getRecruiter() {
		return prop.getProperty("GET_RECRUITER");
	}
	
	public String getCompanyId() {
		return prop.getProperty("GET_COMPANY_ID");
	}

	public String getCompanyById() {
		return prop.getProperty("GET_COMPANY_BY_ID");
	}

	public String getCompanyByName() {
		return prop.getProperty("GET_COMPANY_BY_NAME");
	}
	
	public String getBannerByHash() {
		return prop.getProperty("GET_BANNER_BY_HASH");
	}
	
	public String getExperByEId() {
		return prop.getProperty("GET_EXPER_BY_EID");
	}
	
	public String getExperByUId() {
		return prop.getProperty("GET_EXPER_BY_UID");
	}
	
	public String getPostByPId() {
		return prop.getProperty("GET_POST_BY_PID");
	} 
	
	public String getPostByUId() {
		return prop.getProperty("GET_POST_BY_UID");
	}
	
	public String dropDB() {
		return prop.getProperty("DROP_DB");
	}
	
	public String getIDByEmail() {
		return prop.getProperty("GET_USER_BY_EMAIL");
	}

	public String getIDByUserName() {
		return prop.getProperty("GET_USER_BY_NAME");
	}

	public String getCompanyIDByName() {
		return prop.getProperty("GET_COMPANY_BY_NAME");
	}
	
	public String getMorePost() {
		return prop.getProperty("GET_MORE_POST");
	}

	public String getNewPost() {
		return prop.getProperty("GET_NEW_POST");
	}

	public String getCompanyByUId() {
		return prop.getProperty("GET_COMPANY_BY_UID");
	}

	public String getMorePeople() {
		return prop.getProperty("GET_MORE_PEOPLE");
	}

	public String getNewPeople() {
		return prop.getProperty("GET_NEW_PEOPLE");
	}

	public String getMyNewPost() {
		return prop.getProperty("GET_MYNEW_POST");
	}

	public String getMyMorePost() {
		return prop.getProperty("GET_MYMORE_POST");
	}

	public String findLike() {
		return prop.getProperty("FIND_LIKE");
	}

	public String insertLike() {
		return prop.getProperty("INSERT_LIKE");
	}

	public String getLikeById() {
		return prop.getProperty("GET_LIKE_BY_ID");
	}

	public String findApply() {
		return prop.getProperty("FIND_APPLY");
	}

	public String insertApply() {
		return prop.getProperty("INSERT_APPLY");
	}

	public String getApplyById() {
		return prop.getProperty("GET_APPLY_BY_ID");
	}
}
