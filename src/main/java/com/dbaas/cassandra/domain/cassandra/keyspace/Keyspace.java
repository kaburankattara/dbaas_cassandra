package com.dbaas.cassandra.domain.cassandra.keyspace;

import com.dbaas.cassandra.shared.validation.ValidateResult;
import com.dbaas.cassandra.shared.validation.Validator;
import com.dbaas.cassandra.shared.validation.constraints.MaxLength;
import com.dbaas.cassandra.shared.validation.constraints.Required;
import com.dbaas.cassandra.utils.StringUtils;

public class Keyspace {

    public static Keyspace createEmptyInstance() {
        return new Keyspace();
    }

    public static Keyspace createInstance(String keyspace) {
        return new Keyspace(keyspace);
    }

    public Keyspace() {
    }

    public Keyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    /**
     * クラス名
     */
    public static final String CLASS_NAME = Keyspace.class.getSimpleName();

    /**
     * 最大桁数
     */
    private static final int MAX_LENGTH = 10;

    @Required(message = "キースペースの入力は必須です。")
    @MaxLength(max = MAX_LENGTH, message = "キースペースは" + MAX_LENGTH + "文字以下で入力してください。")
    private String keyspace;

    public String getKeyspace() {
        return keyspace;
    }

    public String setKeyspace(String keyspace) {
        return this.keyspace = keyspace;
    }

    public ValidateResult validate(Validator validator) {
        return validator.validate(this, CLASS_NAME);
    }

    public boolean isEquals(Keyspace keyspace) {
        return StringUtils.isEquals(this.keyspace, keyspace.getKeyspace());
    }
}
