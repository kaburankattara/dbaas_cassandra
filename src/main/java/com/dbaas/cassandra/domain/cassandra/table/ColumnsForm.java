package com.dbaas.cassandra.domain.cassandra.table;

import java.util.ArrayList;
import java.util.List;

public class ColumnsForm {
	
	public ColumnsForm() {
		this.columnList = new ArrayList<ColumnForm>();
	}
	
	public ColumnsForm(List<ColumnForm> columnList) {
		this.columnList = columnList;
	}
	
	public List<ColumnForm> columnList;
	
	public boolean isUpdate;

	public List<ColumnForm> getColumnList() {
		return columnList;
	}
	
	public void setColumnList(List<ColumnForm> columnList) {
		this.columnList = columnList;
	}
	
	public boolean getIsUpdate() {
		return isUpdate;
	}
	
	public boolean isUpdate() {
		return isUpdate;
	}
	
	public void setIsUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
	
	public boolean isEmpty() {
		return columnList == null || columnList.size() <= 0;
	}
	
	public Columns toColumns() {
		List<Column> toColumnList = new ArrayList<Column>();
		for (ColumnForm columnForm : this.columnList) {
			toColumnList.add(columnForm);
		}
		return new Columns(toColumnList);
	}
	
	public static ColumnsForm toColumnsForm(Columns Columns) {
		List<ColumnForm> columnList = new ArrayList<ColumnForm>();
		for (Column column : Columns.getColumnList()) {
			columnList.add(new ColumnForm(column));
		}
		return new ColumnsForm(columnList);
	}
	
	public void setAllDisabled() {
		if (isEmpty()) {
			return;
		}
		
		for (ColumnForm column : columnList) {
			column.setDisabled();
		}
	}

}
