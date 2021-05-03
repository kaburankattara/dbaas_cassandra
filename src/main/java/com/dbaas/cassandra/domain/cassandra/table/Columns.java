package com.dbaas.cassandra.domain.cassandra.table;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.dbaas.cassandra.utils.StringUtils.isEquals;

public class Columns {
	
	public Columns() {
		this.columnList = new ArrayList<Column>();
	}
	
	public static Columns createEmptyColumns() {
		return new Columns();
	}
	
	public Columns(List<Column> columnList) {
		this.columnList = columnList;
	}

	@Valid
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

	public boolean hasRowKeyColumn() {
		if (isEmpty()) {
			return false;
		}

		for (Column column : columnList) {
			if (column.isRowKey()) {
				return true;
			}
		}
		return false;
	}

	public boolean hasDuplicateColumnName() {
		if (isEmpty()) {
			return false;
		}

		for (Column columnMain : columnList) {
			int count = 0;
			for (Column columnSub : columnList) {
				if (isEquals(columnMain.getColumnName(), columnSub.getColumnName())) {
					count++;
				}

				if (count >= 2) {
					return true;
				}
			}
		}
		return false;
	}

}
