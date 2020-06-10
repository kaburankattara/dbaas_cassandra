package com.dbaas.cassandra.domain.cassandra.file;

public class CassandraRepo {
	
	private String fileName;
	
	private String detail;

	/**
	 * 設定ファイルマネージャーを生成
	 * @return
	 */
	public static CassandraRepo createManager() {
		return new CassandraRepo();
	}
	
	public CassandraRepo() {
		this.fileName = "/etc/yum.repos.d/cassandra.repo";
	}

	public void create() {
		// ファイル内容を生成
		createBuffer();
	}
	
    public String getDetail() {
       return detail;
    }
	
    public String getFileName() {
       return fileName;
    }
	
	private void createBuffer() {
		StringBuilder sb = new StringBuilder();
		sb.append("[cassandra]").append(System.getProperty("line.separator"));
		sb.append("name=Apache Cassandra").append(System.getProperty("line.separator"));
		sb.append("baseurl=https://www.apache.org/dist/cassandra/redhat/311x/").append(System.getProperty("line.separator"));
		sb.append("gpgcheck=1").append(System.getProperty("line.separator"));
		sb.append("repo_gpgcheck=1").append(System.getProperty("line.separator"));
		sb.append("gpgkey=https://www.apache.org/dist/cassandra/KEYS").append(System.getProperty("line.separator"));
		detail = sb.toString();
	}
}
