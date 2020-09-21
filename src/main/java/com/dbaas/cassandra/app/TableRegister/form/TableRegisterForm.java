package com.dbaas.cassandra.app.TableRegister.form;

import static com.dbaas.cassandra.utils.NumberUtils.toInt;
import static com.dbaas.cassandra.utils.StringUtils.toLowerCase;

import java.util.List;

import com.dbaas.cassandra.domain.cassandra.table.ColumnForm;
import com.dbaas.cassandra.domain.cassandra.table.ColumnsForm;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.utils.NumberUtils;

public class TableRegisterForm {
	
	public TableRegisterForm() {
		columns = new ColumnsForm();
	}
	
	public String keySpace;
	
	public String tableName;
	
	public ColumnsForm columns;
	
	public String getKeySpace() {
		return keySpace;
	}
	
	public void setKeySpace(String keySpace) {
		this.keySpace = keySpace;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<ColumnForm> getColumnList() {
		return columns.getColumnList();
	}
	
	public void setColumnList(List<ColumnForm> columnList) {
		this.columns.setColumnList(columnList);
	}
	
	public void init() {
		// 初期表示では1カラム1分の入力項目を用意する
		addColumn();
	}
	
	public void addColumn() {
		this.columns.getColumnList().add(new ColumnForm());
	}
	
	public void deleteColumn(String index) {
		List<ColumnForm> columnList = columns.getColumnList();
		
		// 数値型に変換出来ない、
		// または対象インデックスがリストのサイズを超えている場合は処理しない
		if (!NumberUtils.isInt(index) || toInt(index) > columnList.size()) {
			return;
		}
		
		columnList.remove(toInt(index));
	}
	
	public Table toTable() {
		return new Table(toLowerCase(tableName), columns.toColumns());
	}
}
