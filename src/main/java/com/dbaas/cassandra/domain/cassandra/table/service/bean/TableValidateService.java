package com.dbaas.cassandra.domain.cassandra.table.service.bean;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.cassandra.table.dto.RegistTableResultDto;
import com.dbaas.cassandra.domain.message.MessageSourceService;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.shared.validation.ValidateResult;
import com.dbaas.cassandra.shared.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.SmartValidator;

import static com.dbaas.cassandra.domain.message.Message.*;

@Service
@Transactional
public class TableValidateService {

	private ServerService serverService;

	private TableFindService tableFindService;

	private Validator validator;

    @Autowired
	public TableValidateService(ServerService serverService, TableFindService tableFindService
			, SmartValidator smartValidator , MessageSourceService messageSource) {
    	this.serverService = serverService;
    	this.tableFindService = tableFindService;
		this.validator = Validator.getInstance(smartValidator, messageSource);
	}

	public RegistTableResultDto validateForRegist(LoginUser user, Keyspace keyspace, Table table) {

		// 単項目チェック
		ValidateResult validateResult = table.validate(validator);
		RegistTableResultDto result = new RegistTableResultDto(validateResult);
		if (validateResult.hasErrors()){
			return result;
		}

		// カラムを保持していない場合、エラー
		if (!table.hasColumn()) {
			validateResult.addError(MSG006E);
			return result;
		}

		// 主キーカラムを保持していない場合、エラー
		if (!table.hasRowKeyColumn()) {
			validateResult.addError(MSG007E);
			return result;
		}

		// カラム名の重複がある場合、エラー
		if (table.hasDuplicateColumnName()) {
			validateResult.addError(MSG009E);
			return result;
		}

		// 対象のキースペースから引数のテーブルを取得する
		Instances instances = serverService.getInstances(user);
		Table registedTable = tableFindService.findTableByKeyspace(instances, keyspace, table.getTableName());

		// 引数のテーブルが登録済の場合、エラーとして処理を中断する
		if (!registedTable.isEmpty()) {
			validateResult.addError(MSG008E);
			return result;
		}

		return result;
	}

	public RegistTableResultDto validateForUpdate(LoginUser user, Keyspace keyspace, Table table) {

		// 単項目チェック
		ValidateResult validateResult = table.validate(validator);
		RegistTableResultDto result = new RegistTableResultDto(validateResult);
		if (validateResult.hasErrors()){
			return result;
		}

		// カラムを保持していない場合、エラー
		if (!table.hasColumn()) {
			validateResult.addError(MSG006E);
			return result;
		}

		// 主キーカラムを保持していない場合、エラー
		if (!table.hasRowKeyColumn()) {
			validateResult.addError(MSG007E);
			return result;
		}

		// カラム名の重複がある場合、エラー
		if (table.hasDuplicateColumnName()) {
			validateResult.addError(MSG009E);
			return result;
		}

		return result;
	}
}
