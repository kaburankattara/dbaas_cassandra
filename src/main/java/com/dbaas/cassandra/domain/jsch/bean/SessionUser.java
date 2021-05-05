package com.dbaas.cassandra.domain.jsch.bean;

import com.jcraft.jsch.UserInfo;

/**
 * サーバ接続に使用するユーザ情報を保持するクラス
 */
public class SessionUser implements UserInfo {

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean promptPassword(String arg0) {
        return true;
    }

    @Override
    public boolean promptPassphrase(String arg0) {
        return true;
    }

    @Override
    public boolean promptYesNo(String arg0) {
        return true;
    }

    @Override
    public void showMessage(String arg0) {
    }

    @Override
    public String getPassphrase() {
        return null;
    }
}