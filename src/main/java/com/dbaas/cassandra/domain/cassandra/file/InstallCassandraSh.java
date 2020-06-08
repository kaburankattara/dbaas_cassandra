package com.dbaas.cassandra.domain.cassandra.file;

import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.FILE_INSTALL_CASSANDRA_SH;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.PATH_TMP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class InstallCassandraSh {
	
	private final String FILE_NAME = PATH_TMP + FILE_INSTALL_CASSANDRA_SH;

	/**
	 * 設定ファイルマネージャーを生成
	 * @return
	 */
	public static InstallCassandraSh createManager() {
		return new InstallCassandraSh();
	}
	
	public InstallCassandraSh() {
		
	}

	public void create() {
		// profileのバッファを生成
		String installCassandraShBuffer = createBuffer();
		
		// 生成したシェルのバッファをもとにファイルを生成する
        try (BufferedWriter in = new BufferedWriter(new FileWriter(new File(FILE_NAME)))){
        	in.append(installCassandraShBuffer);
        } catch (Exception e){ }
	}
	
    public void delete() {
        File file = new File(FILE_NAME);
 
        //deleteメソッドを使用してファイルを削除する
        file.delete();
    }
	
	private String createBuffer() {
		StringBuffer sb = new StringBuffer();
		sb.append("#!/bin/bash").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("#javaをインストール").append(System.getProperty("line.separator"));
		sb.append("wget https://corretto.aws/downloads/latest/amazon-corretto-8-x64-linux-jdk.tar.gz").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("mkdir /home/ec2-user/java && tar -xzvf /home/ec2-user/amazon-corretto-8-x64-linux-jdk.tar.gz -C /home/ec2-user/java --strip-components 1").append(System.getProperty("line.separator"));
		sb.append("sudo mv /home/ec2-user/java /opt/java").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("rm -rf amazon-corretto-8-x64-linux-jdk.tar.gz").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("#Casssandraをインストール").append(System.getProperty("line.separator"));
		sb.append("wget http://archive.apache.org/dist/cassandra/3.11.6/apache-cassandra-3.11.6-bin.tar.gz").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("tar -xzvf /home/ec2-user/apache-cassandra-3.11.6-bin.tar.gz").append(System.getProperty("line.separator"));
		sb.append("sudo mv /home/ec2-user/apache-cassandra-3.11.6 /opt/cassandra").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("rm -rf apache-cassandra-3.11.6-bin.tar.gz").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("exit 0").append(System.getProperty("line.separator"));
		return sb.toString();
	}
}
