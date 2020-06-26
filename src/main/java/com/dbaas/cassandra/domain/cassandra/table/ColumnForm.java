package com.dbaas.cassandra.domain.cassandra.table;

import static com.dbaas.cassandra.utils.BeanUtils.copyCrossTypeProperties;

public class ColumnForm extends Column {
	
	public ColumnForm() {
	}
	
	public ColumnForm(Column column) {
		copyCrossTypeProperties(column, this);
	}
	
	private boolean isAreadyRegist;
	
	public boolean getIsAreadyRegist() {
		return isAreadyRegist;
	}
	
	public void setIsAreadyRegist(boolean isAreadyRegist) {
		this.isAreadyRegist = isAreadyRegist;
	}
	
	public boolean isDisabled() {
		// 登録済みのカラムは非活性
		return isAreadyRegist;
	}
	
	public void setDisabled() {
		isAreadyRegist = true;
	}
}
