package com.dbaas.cassandra.domain.cassandra.cql;

import java.util.ArrayList;
import java.util.List;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.table.Column;
import com.dbaas.cassandra.domain.cassandra.table.Table;

public class CqlFactory {
	
	public List<String> createAlterCqlForAddColumns(Keyspace keyspace, Table oldTable, Table newTable) {
		List<String> alterCqlList = new ArrayList<String>();
		// 現行、次期を比較し、追加するカラムを抜き出す
		for (Column column : newTable.getColumnList()) {
			if (oldTable.hasColumn(column)) {
				continue;
			}
			alterCqlList.add("ALTER TABLE \"" + keyspace.getKeyspace() + "\".\"" + newTable.getTableName() + "\" ADD (\""
					+ column.getColumnName() + "\" " + column.getColumnType() + ");");
		}
		return alterCqlList;
	}
}

