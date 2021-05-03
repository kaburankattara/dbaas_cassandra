package com.dbaas.cassandra.domain.cassandra.table;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.shared.validation.ValidateResult;
import com.dbaas.cassandra.shared.validation.Validator;
import com.dbaas.cassandra.shared.validation.constraints.MaxLength;
import com.dbaas.cassandra.shared.validation.constraints.Required;
import com.dbaas.cassandra.utils.StringUtils;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.dbaas.cassandra.domain.cassandra.table.Columns.createEmptyColumns;

public class Table {

	/**
	 * コンストラクタ
	 */
	public static Table createEmptyTable() {
		return new Table(null, createEmptyColumns());
	}

	/**
	 * テーブル名
	 */
	@Required(message = "テーブル名の入力は必須です。")
	@MaxLength(max = TABLE_NAME_MAX_LENGTH, message = "テーブル名は" + TABLE_NAME_MAX_LENGTH + "文字以下で入力してください。")
	private String tableName;

	/**
	 * カラムリスト
	 */
	@Valid
	private Columns columns;

	/**
	 * コンストラクタ
	 */
	public Table() {
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
		String tmpTableName = "";
		boolean isGetTableName = true;
		for (List<String> columnInfo : columnInfoList) {
			
			// 初回のみテーブル名取得
			if (isGetTableName) {
				int tableNameIndex = 1;
				tmpTableName = columnInfo.get(tableNameIndex);
				isGetTableName = false;
			}
			Column column = new Column(columnInfo);
			columnList.add(column);
		}
		
		// フィールドセット
		this.tableName =  tmpTableName;
		this.columns = new Columns(columnList);
	}

	/**
	 * クラス名
	 */
	public static final String CLASS_NAME = Table.class.getSimpleName();

	/**
	 * 最大桁数
	 */
	private static final int TABLE_NAME_MAX_LENGTH = 10;

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
	
	public String getCreateCql(Keyspace keyspace) {
		StringBuilder sb = new StringBuilder();
		// キースペース選択
		sb.append("use " + keyspace.getKeyspace() + ";");
		
		
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

	public boolean hasColumn() {
		return !columns.isEmpty();
	}

	public boolean hasColumn(Column column) {
		return columns.hasColumn(column);
	}

	public boolean hasRowKeyColumn() {
		return columns.hasRowKeyColumn();
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(tableName) || columns.isEmpty();
	}

	public boolean hasDuplicateColumnName() {
		return columns.hasDuplicateColumnName();
	}

	public ValidateResult validate(Validator validator) {
		return validator.validate(this, CLASS_NAME);
	}
}

