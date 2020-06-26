package com.dbaas.cassandra.domain.cassandra.table;

import static com.dbaas.cassandra.utils.StringUtils.isEquals;

import java.util.ArrayList;
import java.util.List;

public class Columns {
	
	public Columns() {
		this.columnList = new ArrayList<Column>();
	}
	
	public Columns(List<Column> columnList) {
		this.columnList = columnList;
	}
	
	public List<Column> columnList;

	public List<Column> getColumnList() {
		return columnList;
	}
	
	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}
	
	public boolean isEmpty() {
		return columnList == null || columnList.size() <= 0;
	}

	public boolean hasColumn(Column columnArg) {
		if (isEmpty()) {
			return false;
		}
		
		for (Column column : columnList) {
			if (isEquals(column.getColumnName(), columnArg.columnName)) {
				return true;
			}
		}
		return false;
	}

}
