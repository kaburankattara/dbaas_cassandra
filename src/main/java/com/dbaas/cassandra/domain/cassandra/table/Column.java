package com.dbaas.cassandra.domain.cassandra.table;

import static com.dbaas.cassandra.utils.StringUtils.isEquals;

import java.util.List;

public class Column {

	public String columnName;

	public String columnType;
	
	public boolean isRowKey;

	/**
	 * コンストラクタ
	 */
	public Column() {
	}

	/**
	 * コンストラクタ
	 */
	public Column(List<String> cqlResult) {
		int columnNameIndex = 2;
		int columnTypeIndex = 7;
		int isRowKeyIndex = 6;
		columnName = cqlResult.get(columnNameIndex);
		columnType = cqlResult.get(columnTypeIndex);
		// TODO 定数化
		isRowKey = isEquals(cqlResult.get(isRowKeyIndex), "0");
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

	public boolean isRowKey() {
		return isRowKey;
	}

	public boolean getIsRowKey() {
		return isRowKey;
	}
	
	public void setIsRowKey(boolean isRowKey) {
		this.isRowKey = isRowKey;
	}
}
