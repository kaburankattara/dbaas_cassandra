package com.dbaas.cassandra.app.TableUpdater.form;

public class TableUpdaterColumnForm {
	
	public boolean isRowKey;

	public String columnName;

	public String columnType;

	public boolean getIsRowKey() {
		return isRowKey;
	}
	
	public void setIsRowKey(boolean isRowKey) {
		this.isRowKey = isRowKey;
	}

	public String getColumnName() {
		return columnName;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}
	
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
}
