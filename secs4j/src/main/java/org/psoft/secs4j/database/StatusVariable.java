package org.psoft.secs4j.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatusVariable extends Model {

	private static final String TABLE = "secs_status_variables";

	private Long SVID;

	private String SV = null;
	
	public StatusVariable() {
		
	}

	public StatusVariable(Long ID, String Value) {
		this.setVariableID(ID);
		this.setVariableValue(Value);
	}
	
	/**
	 * get the newest records form all variables
	 * @param params
	 * @return
	 */
	public static List<?> getNewestRecords(ArrayList<Long> params)
	{
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM " + TABLE + " WHERE id IN ( ");
		sqlBuilder.append("SELECT MAX(id) FROM " + TABLE + " WHERE SVID IN (");
		int paramCount = params.size();
		for(int i = 0; i < paramCount; i++) {
			
			sqlBuilder.append("?");
			if(paramCount - 1 != i) {
				sqlBuilder.append(",");
			}
		}
		sqlBuilder.append(")  GROUP BY SVID) ORDER BY FIELD(SVID");
		for(int i = 0; i < paramCount; i++) {
			
			sqlBuilder.append(",").append(params.get(i));
		}
		sqlBuilder.append(")");
		
		StatusVariable ev = new StatusVariable();
		
		return ev.getDataFromDB(sqlBuilder.toString(), params);
	}

	/**
	 * get conditional data collection: TODO change
	 * @param params
	 * @return
	 */
	public static List<?> getRecords(ArrayList<Long> params)
	{
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM " + TABLE + " a WHERE ");
		sqlBuilder.append("(SELECT COUNT(*) FROM ");
		sqlBuilder.append(TABLE);
		sqlBuilder.append(" b WHERE b.id >= a.id AND b.variable_id = a.variable_id AND b.variable_id IN (");
		for(int i = 0; i < params.size(); i++) {
			
			sqlBuilder.append("?,");
		}
		sqlBuilder.append(")  AND b.variable_id = a.variable_id) = 1");
		StatusVariable ev = new StatusVariable();
		
		return ev.getDataFromDB(sqlBuilder.toString(), params);
	}
	
	protected List<StatusVariable> formatRecords(ResultSet rs)
	{
		List<StatusVariable> records = new ArrayList<StatusVariable>();
		try {
			while (rs.next()) {

				Long id = rs.getLong("SVID");
				String value = rs.getString("SV");
				records.add(new StatusVariable(id, value));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return records;
	}

	public Long getVariableID() {
		return SVID;
	}

	public void setVariableID(Long variableID) {
		this.SVID = variableID;
	}

	public String getVariableValue() {
		return SV;
	}

	public void setVariableValue(String variableValue) {
		this.SV = variableValue;
	}

}
