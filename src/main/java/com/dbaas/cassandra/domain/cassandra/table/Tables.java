package com.dbaas.cassandra.domain.cassandra.table;

import static com.dbaas.cassandra.utils.StringUtils.isEquals;
import static com.dbaas.cassandra.utils.StringUtils.replaceAll;
import static com.dbaas.cassandra.utils.StringUtils.split;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbaas.cassandra.utils.ObjectUtils;

public class Tables {

	/**
	 * リストの一番目要素のインデックス
	 */
	private static final int INDEX_FIRST = 0;

	/**
	 * テーブルリスト
	 */
	private List<Table> tableList;

	/**
	 * コンストラクタ
	 */
	public Tables() {
		tableList = new ArrayList<>();
	}

	/**
	 * コンストラクタ
	 */
	public Tables(List<Table> tableList) {
		this.tableList = tableList;
	}

	/**
	 * コンストラクタ
	 */
	public Tables(String cqlResult) {
		// TODO テーブル名に\nと半角スペースが可能か判断
		// どのみち、画面の禁止文字として入力チェックに含める
		
		
		// カラム情報部分の抽出
		// 空白を除去し、String→行ごとのListに変換
		List<String> resultRowList = new ArrayList<String>(asList(split(replaceAll(cqlResult, " ", ""), '\n')));
		// ヘッダー2行、行数行分のデータがなければエラー
		if (resultRowList.size() < 3) {
			throw new RuntimeException();
		}
		// ヘッダー2行を削除
		int HEADER_ROW_NO = 0;
		resultRowList.remove(HEADER_ROW_NO);
		resultRowList.remove(HEADER_ROW_NO);
		// 行数行の削除
		resultRowList.remove(resultRowList.size() - 1);
		
		// Mapにテーブル単位で整理
		int tableNameIndex = 1;
		Map<String, List<List<String>>> tableMap = new HashMap<String, List<List<String>>>();
		for (String resultRow : resultRowList) {
			List<String> columnInfoList = new ArrayList<String>(asList(split(resultRow, '|')));
			
			String tableName = columnInfoList.get(tableNameIndex);
			if (tableMap.containsKey(tableName)) {
				tableMap.get(tableName).add(columnInfoList);
				continue;
			}
			List<List<String>> columnList = new ArrayList<List<String>>();
			columnList.add(columnInfoList);
			tableMap.put(tableName, columnList);
		}
		
		// インスタンス化
		List<Table> tableList = new ArrayList<Table>();
		for(List<List<String>> columnInfoList : tableMap.values()) {
			Table table = new Table(columnInfoList);
			tableList.add(table);
		}
		
		// フィールドセット
		this.tableList = tableList;
	}

	public List<Table> getTableList() {
		return tableList;
	}

	public Table getFirstTable() {
		if (tableList.isEmpty()) {
			return new Table();
		}
		return tableList.get(INDEX_FIRST);
	}
	
	public void setTableList(List<Table> tableList) {
		this.tableList = tableList;
	}
	
	public Table getTable(String tableName) {
		if (tableList == null || tableList.size() <= 0) {
			return null;
		}
		
		for(Table table : tableList) {
			if (isEquals(table.getTableName(), tableName)) {
				return table;
			}
		}
		return null;
	}

	public boolean isEmpty() {
		return ObjectUtils.isEmpty(tableList) || tableList.isEmpty();
	}

}

