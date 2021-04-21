package com.dbaas.cassandra.shared.applicationProperties;

import com.dbaas.cassandra.shared.exception.SystemException;

import java.util.ResourceBundle;

import static com.dbaas.cassandra.consts.SysConsts.HALF_DOT;
import static com.dbaas.cassandra.consts.SysConsts.HALF_SLASH;
import static com.dbaas.cassandra.utils.NumberUtils.isInt;
import static com.dbaas.cassandra.utils.NumberUtils.toInt;
import static com.dbaas.cassandra.utils.StringUtils.*;

public class ApplicationProperties {

    public static ApplicationProperties createInstance() {
        return new ApplicationProperties();
    }

    private ApplicationProperties() {
    }

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("application");

    private static final String BUSINESS_PARAM = "business.param";

    public String getAmiId() {
        return getProperty("amiId");
    }

    public String getIdentityKeyFileName() {
        return getProperty("identityKeyFileName");
    }

    public String getCqlInstallDir() {
        String cqlInstallDir = getProperty("cqlInstallDir");

        // 末文字が半角スラッシュの場合、そのまま返す
        if (isEquals(getEndChar(cqlInstallDir), HALF_SLASH)) {
            return cqlInstallDir;
        }

        // 末文字が半角スラッシュ出ない場合、半角スラッシュを末文字にして返す
        return cqlInstallDir + HALF_SLASH;
    }

    public String getRemoteServerUser() {
        return getProperty("remoteServerUser");
    }

    public int getSshPort() {
        return getPropertyByInt("sshPort");
    }

    public int getConnectSessionRetryCount() {
        return getPropertyByInt("jsch.connectSessionRetryCount");
    }

    public int getSleepTime() {
        return getPropertyByInt("utils.sleepTime");
    }

    private String getProperty(String key) {
        return replaceNullToBlank(resourceBundle.getString(BUSINESS_PARAM + HALF_DOT + key));
    }

    private int getPropertyByInt(String key) {
        String property = getProperty(key);
        if (isEmpty(property) || !isInt(property)) {
            throw new SystemException();
        }
        return toInt(property);
    }

}
