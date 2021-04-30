package com.dbaas.cassandra.app.TableUpdater.form;

import static com.dbaas.cassandra.domain.cassandra.table.ColumnsForm.toColumnsForm;
import static com.dbaas.cassandra.utils.NumberUtils.toInt;

import java.util.List;

import com.dbaas.cassandra.domain.cassandra.table.Column;
import com.dbaas.cassandra.domain.cassandra.table.ColumnForm;
import com.dbaas.cassandra.domain.cassandra.table.Columns;
import com.dbaas.cassandra.domain.cassandra.table.ColumnsForm;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.utils.NumberUtils;

public class TableUpdaterForm {
	
	public TableUpdaterForm() {
		columns = new ColumnsForm();
	}
	
	public String keyspace;
	
	public String tableName;
	
	public ColumnsForm columns;
	
	public String getKeyspace() {
		return keyspace;
	}
	
	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ColumnsForm getColumns() {
		return columns;
	}
	
	public void setColumns(ColumnsForm columns) {
		this.columns = columns;
	}

	public List<ColumnForm> getColumnList() {
		return columns.getColumnList();
	}
	
	public void setColumnList(List<ColumnForm> columnList) {
		this.columns.setColumnList(columnList);
	}
	
	public void init() {
		columns.setIsUpdate(true);
	}
	
	public void addColumn() {
		this.columns.getColumnList().add(new ColumnForm());
	}
	
	public void deleteColumn(String index) {
		List<? extends Column> columnList = columns.getColumnList();
		
		// 数値型に変換出来ない、
		// または対象インデックスがリストのサイズを超えている場合は処理しない
		if (!NumberUtils.isInt(index) || toInt(index) > columnList.size()) {
			return;
		}
		
		columnList.remove(toInt(index));
	}
	
	public Table toTable() {
		return new Table(tableName, columns.toColumns());
	}
	
	public void setColumns(Columns columns) {
		this.columns = toColumnsForm(columns);
	}
}
