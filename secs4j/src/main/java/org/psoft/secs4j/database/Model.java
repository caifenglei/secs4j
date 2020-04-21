package org.psoft.secs4j.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.ozsoft.secs4j.SecsEquipment;
import org.ozsoft.secs4j.util.ApplicationProperties;

public abstract class Model {

	private static final Logger LOG = Logger.getLogger(SecsEquipment.class);
	
	private final String URL = ApplicationProperties.getDBConnectionURL();

	private final String USER = ApplicationProperties.getDBUser();

	private final String PASS = ApplicationProperties.getDBPassword();

	Connection conn = null;
	PreparedStatement stmt = null;

	/**
	 * format DB query result set to record List
	 * 
	 * @param st
	 * @return
	 * @throws SQLException
	 */
	protected abstract List<?> formatRecords(ResultSet st) throws SQLException;

	/**
	 * SQL query
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	protected List<?> getDataFromDB(String sql, ArrayList<?> params) {

		List<?> data = new ArrayList<Model>();

		try {

			conn = DriverManager.getConnection(URL, USER, PASS);
			stmt = conn.prepareStatement(sql);

			for (int i = 0; i < params.size(); i++) {
				stmt.setString(i + 1, String.valueOf(params.get(i)));
			}

			ResultSet rs = stmt.executeQuery();
			data = formatRecords(rs);

			rs.close();
			stmt.close();
			conn.close();

			return data;

		} catch (SQLException e) {
			LOG.error("MySQL query error: " + e.getMessage());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				if (stmt != null) {

					stmt.close();
				}
			} catch (SQLException e) {
				// nothing we can do
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

}
