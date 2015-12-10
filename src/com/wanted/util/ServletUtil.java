package com.wanted.util;

import javax.servlet.ServletContext;

import com.wanted.database.Database;

public class ServletUtil {
	public Database getDatabase(ServletContext context) {
		String dbProFile = context.getRealPath("/WEB-INF/db.properties");
		String sqlProFile = context.getRealPath("/WEB-INF/sql.properties");
		Database database = new Database(dbProFile, sqlProFile);
		return database;
	}
}
