package com.dbaas.cassandra.domain.cassandra.file;

import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.FILE_EXEC_CASSANDRA_SH;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.PATH_EC2_USER_HOME;

import java.io.File;

public class ExecCqlSh {
	
	private String fileName;
	
	private String detail;

	/**
	 * 設定ファイルマネージャーを生成
	 * @return
	 */
	public static ExecCqlSh createManager() {
		return new ExecCqlSh();
	}
	
	public ExecCqlSh() {
		this.fileName = PATH_EC2_USER_HOME + FILE_EXEC_CASSANDRA_SH;
	}

	public void create() {
		// ファイル内容を生成
		createBuffer();
	}
	
    public void delete() {
        File file = new File(fileName);
 
        //deleteメソッドを使用してファイルを削除する
        file.delete();
    }
	
    public String getDetail() {
       return detail;
    }
	
    public String getFileName() {
       return fileName;
    }
	
	private void createBuffer() {
		StringBuilder sb = new StringBuilder();
		sb.append("#!/bin/bash\n");
		sb.append("\n");
		sb.append("#cassandraの実行\n");
		sb.append("touch aaa | cassandra -p /opt/cassandra/pidfile.pid & > aaa\n");
		sb.append("");
		sb.append("exit 0\n");
		detail = sb.toString();
	}
}
