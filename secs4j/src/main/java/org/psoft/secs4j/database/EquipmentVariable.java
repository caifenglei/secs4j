package org.psoft.secs4j.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipmentVariable extends Model {

	private static final String TABLE = "equipment_variables";

	private Long variableID;

	private String variableName = null;

	private String variableValue = null;
	
	public EquipmentVariable() {
		
	}

	public EquipmentVariable(Long ID, String Name, String Value) {
		this.setVariableID(ID);
		this.setVariableName(Name);
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
		sqlBuilder.append("SELECT MAX(id) FROM " + TABLE + " WHERE variable_id IN (");
		int paramCount = params.size();
		for(int i = 0; i < paramCount; i++) {
			
			sqlBuilder.append("?");
			if(paramCount - 1 != i) {
				sqlBuilder.append(",");
			}
		}
		sqlBuilder.append(")  GROUP BY variable_id) ORDER BY FIELD(variable_id");
		for(int i = 0; i < paramCount; i++) {
			
			sqlBuilder.append(",").append(params.get(i));
		}
		sqlBuilder.append(")");
		
		EquipmentVariable ev = new EquipmentVariable();
		
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
		EquipmentVariable ev = new EquipmentVariable();
		
		return ev.getDataFromDB(sqlBuilder.toString(), params);
	}
	
	protected List<EquipmentVariable> formatRecords(ResultSet rs)
	{
		List<EquipmentVariable> records = new ArrayList<EquipmentVariable>();
		try {
			while (rs.next()) {

				Long id = rs.getLong("variable_id");
				String name = rs.getString("variable_name");
				String value = rs.getString("variable_value");
				records.add(new EquipmentVariable(id, name,value));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return records;
	}

	public Long getVariableID() {
		return variableID;
	}

	public void setVariableID(Long variableID) {
		this.variableID = variableID;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableValue() {
		return variableValue;
	}

	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}

}
