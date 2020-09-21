package com.dbaas.cassandra.domain.cassandra.table;

import static com.dbaas.cassandra.domain.cassandra.table.Columns.createEmptyColumns;

import java.util.ArrayList;
import java.util.List;

import com.dbaas.cassandra.utils.StringUtils;

public class Table {

	/**
	 * テーブル名
	 */
	private String tableName;

	/**
	 * カラムリスト
	 */
	private Columns columns;

	/**
	 * コンストラクタ
	 */
	public Table() {
	}

	/**
	 * コンストラクタ
	 */
	public static Table createEmptyTable() {
		return new Table(null, createEmptyColumns());
	}

	/**
	 * コンストラクタ
	 * 
	 * @param tableName
	 * @param columns
	 */
	public Table(String tableName, Columns columns) {
		this.tableName = tableName;
		this.columns = columns;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param columnInfoList
	 */
	public Table(List<List<String>> columnInfoList) {
		
		// カラムリストの生成とテーブル名の抽出
		List<Column> columnList = new ArrayList<Column>();
		String tableName = "";
		boolean isGetTableName = true;
		for (List<String> columnInfo : columnInfoList) {
			
			// 初回のみテーブル名取得
			if (isGetTableName) {
				int tableNameIndex = 1;
				tableName = columnInfo.get(tableNameIndex);
				isGetTableName = false;
			}
			Column column = new Column(columnInfo);
			columnList.add(column);
		}
		
		// フィールドセット
		this.tableName =  tableName;
		this.columns = new Columns(columnList);
	}

	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Columns getColumns() {
		return columns;
	}
	
	public void setColumns(Columns columns) {
		this.columns = columns;
	}
	
	public String getCreateCql(String keySpace) {
		StringBuilder sb = new StringBuilder();
		// キースペース選択
		sb.append("use " + keySpace + ";");
		
		
		// cretate文
		sb.append("create table ")
		.append(tableName)
		.append(" ( ");

		boolean isNotFirst = false;
		for (Column column : columns.getColumnList()) {
			if (isNotFirst) {
				sb.append(", ");
			}
			
			sb.append(column.getColumnName() + " " + column.getColumnType() + " ");
			
			isNotFirst = true;
		}
		sb.append(", primary key(" + getPrimaryKeyClause() + ")");
		
		sb.append(");");
		
		return sb.toString();
	}
	
	public List<String> getPrimaryKey() {
		List<String> primaryKeyList = new ArrayList<String>();
		for (Column column : columns.getColumnList()) {
			if (column.isRowKey) {
				primaryKeyList.add(column.getColumnName());
			}
		}
		return primaryKeyList;
	}
	
	public String getPrimaryKeyClause() {
		List<String> primaryKeyList = getPrimaryKey();
		StringBuilder sb = new StringBuilder();

		boolean isNotFirst = false;
		for (String primaryKey : primaryKeyList) {
			if (isNotFirst) {
				sb.append(", ");
			}
			
			sb.append(primaryKey);
			
			isNotFirst = true;
		}
		return sb.toString();
	}

	public List<Column> getColumnList() {
		return columns.getColumnList();
	}

	public boolean hasColumn(Column column) {
		return columns.hasColumn(column);
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(tableName) || columns.isEmpty();
	}
}

