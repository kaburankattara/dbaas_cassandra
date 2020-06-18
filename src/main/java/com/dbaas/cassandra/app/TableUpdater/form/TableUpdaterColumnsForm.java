package com.dbaas.cassandra.app.TableUpdater.form;

import java.util.ArrayList;
import java.util.List;

public class TableUpdaterColumnsForm {
	
	public TableUpdaterColumnsForm() {
		this.columnList = new ArrayList<TableUpdaterColumnForm>();
	}
	
	public TableUpdaterColumnsForm(List<TableUpdaterColumnForm> columnList) {
		this.columnList = columnList;
	}
	
	public List<TableUpdaterColumnForm> columnList;

	public List<TableUpdaterColumnForm> getColumnList() {
		return columnList;
	}
	
	public void setColumnList(List<TableUpdaterColumnForm> columnList) {
		this.columnList = columnList;
	}

}
