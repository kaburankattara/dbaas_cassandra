package com.dbaas.cassandra.domain.cassandra.table;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.shared.validation.constraints.MaxLength;
import com.dbaas.cassandra.shared.validation.constraints.Required;

import static com.dbaas.cassandra.utils.StringUtils.isEquals;

import java.util.List;

public class Column {

	/**
	 * クラス名
	 */
	public static final String CLASS_NAME = Keyspace.class.getSimpleName();

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

	/**
	 * 最大桁数
	 */
	private static final int COLUMN_NAME_MAX_LENGTH = 10;

	@Required(message = "カラム名の入力は必須です。")
	@MaxLength(max = COLUMN_NAME_MAX_LENGTH, message = "カラム名は" + COLUMN_NAME_MAX_LENGTH + "文字以下で入力してください。")
	public String columnName;

	@Required(message = "型の入力は必須です。")
	public String columnType;

	public boolean isRowKey;

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
