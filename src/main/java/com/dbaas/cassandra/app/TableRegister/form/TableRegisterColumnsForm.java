package com.dbaas.cassandra.app.TableRegister.form;

import java.util.ArrayList;
import java.util.List;

public class TableRegisterColumnsForm {
	
	public TableRegisterColumnsForm() {
		this.columnList = new ArrayList<TableRegisterColumnForm>();
	}
	
	public TableRegisterColumnsForm(List<TableRegisterColumnForm> columnList) {
		this.columnList = columnList;
	}
	
	public List<TableRegisterColumnForm> columnList;

	public List<TableRegisterColumnForm> getColumnList() {
		return columnList;
	}
	
	public void setColumnList(List<TableRegisterColumnForm> columnList) {
		this.columnList = columnList;
	}

}
