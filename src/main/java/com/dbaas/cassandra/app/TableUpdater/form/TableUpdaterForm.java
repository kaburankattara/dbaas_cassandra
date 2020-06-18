package com.dbaas.cassandra.app.TableUpdater.form;

import static com.dbaas.cassandra.utils.NumberUtils.toInt;

import java.util.List;

import com.dbaas.cassandra.domain.cassandra.table.Column;
import com.dbaas.cassandra.domain.cassandra.table.Columns;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.utils.NumberUtils;

public class TableUpdaterForm {
	
	public TableUpdaterForm() {
		columns = new Columns();
	}
	
	public String keySpace;
	
	public String tableName;
	
	public Columns columns;
	
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

	public List<Column> getColumnList() {
		return columns.getColumnList();
	}
	
	public void setColumnList(List<Column> columnList) {
		this.columns.setColumnList(columnList);
	}
	
	public void init() {
		// 初期表示では1カラム1分の入力項目を用意する
		addColumn();
	}
	
	public void addColumn() {
		this.columns.getColumnList().add(new Column());
	}
	
	public void deleteColumn(String index) {
		List<Column> columnList = columns.getColumnList();
		
		// 数値型に変換出来ない、
		// または対象インデックスがリストのサイズを超えている場合は処理しない
		if (!NumberUtils.isInt(index) || toInt(index) > columnList.size()) {
			return;
		}
		
		columnList.remove(toInt(index));
	}
	
	public Table toTable() {
		return new Table(tableName, columns);
	}
}
