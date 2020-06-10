package com.dbaas.cassandra.domain.cassandra;

public class CassandraConsts {
	/** sudoコマンド */
	public static final String COMMAND_SUDO = "sudo ";
	/** rmコマンド */
	public static final String COMMAND_RM = "rm -rf %s";
	/** chmodコマンド */
	public static final String COMMAND_CHMOD = "chmod %s %s";
	/** touchコマンド */
	public static final String COMMAND_TOUCH = "touch %s";
	/** echoコマンド */
	public static final String COMMAND_ECHO = "echo %s";
	/** sourceコマンド */
	public static final String COMMAND_SOURCE = "source %s";
	/** mvコマンド */
	public static final String COMMAND_MV = "mv %s %s";
	/** cpコマンドのテンプレート文字列 */
	public static final String COMMAND_CHECK_RESULT_TIMESTAMP = "date -r resultFile";
	/** cpコマンドのテンプレート文字列 */
	public static final String COMMAND_WGET = "wget %s";
	/** profile */
	public static final String FILE_PROFILE = "/profile";
	/** java */
	public static final String FILE_JAVA = "/java";
	/** cassandra */
	public static final String FILE_CASSANDRA= "/cassandra";
	/** cassandra.yaml */
	public static final String FILE_CASSANDRA_YAML= "/cassandra.yaml";
	/** cassandraセットアップシェル */
	public static final String FILE_INSTALL_CASSANDRA_SH= "/installCassandra.sh";
	/** cassandra実行シェル */
	public static final String FILE_EXEC_CASSANDRA_SH= "/execCassandra.sh";
	/** パス opt */
	public static final String PATH_OPT = "/opt";
	/** パス etc */
	public static final String PATH_ETC = "/etc";
	/** パス tmp */
	public static final String PATH_TMP = "/tmp";
	/** パス conf */
	public static final String PATH_CONF = "/conf";
	/** パス ec2-userホーム **/
	public static final String PATH_EC2_USER_HOME = "/home/ec2-user";
	/** パス cassandraホーム */
	public static final String PATH_CASSANDRA_HOME = "/usr/local" + FILE_CASSANDRA;
	/** URL amazon corretto8  **/
	public static final String DOWNLOAD_URL_AMAZON_CORRETTO_8_X64_LINUX_LATEST = "https://corretto.aws/downloads/latest/amazon-corretto-8-x64-linux-jdk.tar.gz";
	/** URL cassandraインストールバッチ  **/
	public static final String COMMAND_EXEC_SHELL_INSTALLCASSANDRA = "./installCassandra.sh";
	/** ssh実行の接続タイプ */
	public static final String CHANNEL_TYPE_EXEC = "exec";
    /** Channel接続タイプ */
    public static final String CHANNEL_TYPE_SFTP = "sftp";
    /** コマンドステータス 成功 */
    public static final String COMMAND_STATUS_SUCCESS = "0";
    /** コマンドステータス  */
    public static final String COMMAND_STATUS_FAILURE = "1";
    /** chmodコマンドのオクタルモードの権限 777  */
    public static final String CHMOD_OCTAL_777 = "777";
    /** cassandra */
    private static final String CASSANDRA = "cassandra ";
    /** cassandraをバックグラウンド実行 */
    public static final String EXEC_CASSANDRA_BACK_GROUND = CASSANDRA + " -p " + PATH_CASSANDRA_HOME + "/pidfile.pid -R";
    /** cassandraをフォアグラウンド実行 */
    public static final String EXEC_CASSANDRA_FORE_GROUND = CASSANDRA + " -f ";
}
