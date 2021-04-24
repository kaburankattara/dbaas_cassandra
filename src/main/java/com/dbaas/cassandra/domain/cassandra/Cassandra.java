package com.dbaas.cassandra.domain.cassandra;

import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_MV;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_RM;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_SOURCE;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_STATUS_SUCCESS;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.FILE_CASSANDRA;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.FILE_JAVA;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.FILE_PROFILE;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.PATH_EC2_USER_HOME;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.PATH_ETC;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.PATH_OPT;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.PATH_TMP;

import com.dbaas.cassandra.domain.cassandra.file.CassandraRepo;
import com.dbaas.cassandra.domain.cassandra.file.CassandraYaml;
import com.dbaas.cassandra.domain.cassandra.file.Profile;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.dbaas.cassandra.domain.sftp.Sftp;
import com.dbaas.cassandra.domain.ssh.Ssh;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.shared.applicationProperties.ApplicationProperties;

public class Cassandra {

	Ssh ssh = Ssh.createInstance();

	Sftp sftp = Sftp.createInstance();

	ApplicationProperties ap = ApplicationProperties.createInstance();

	/**
	 * インスタンス生成
	 * 
	 * @return cassandra
	 */
	public static Cassandra createInstance() {
		return new Cassandra();
	}

	/**
	 * コンストラクタ
	 */
	private Cassandra() {
	}

	/**
	 * casssandraをインストールする
	 *
	 * @param instance
	 * @return 実効結果
	 */
	public String unInstallCassandra(Instance instance) {
		String optJava = PATH_OPT + FILE_JAVA;
		String optCassandra = PATH_OPT + FILE_CASSANDRA;
		String homeProfile = PATH_EC2_USER_HOME + FILE_PROFILE;
		String etcProfile = PATH_ETC + FILE_PROFILE;
		String tmpProfile = PATH_TMP + FILE_PROFILE;
		Profile profile = Profile.createManager();

		// javaとCassandraを削除
		ssh.execSudo(instance, COMMAND_RM, optJava);
		ssh.execSudo(instance, COMMAND_RM, optCassandra);

		// profileを最新化
		// 最新のprofile情報を取得し、サーバにprofileを配置
		profile.createProfile();
		sftp.putFile(instance, tmpProfile, homeProfile);

		// 今あるprofileを削除し新しいprofileを配置
		ssh.execSudo(instance, COMMAND_RM, etcProfile);
		ssh.execSudo(instance, COMMAND_MV, homeProfile, etcProfile);
		
		// profileをサーバに配置し、pathを通す
		ssh.exec(instance, COMMAND_SOURCE, etcProfile);
		ssh.execSudo(instance, COMMAND_SOURCE, etcProfile);
		return COMMAND_STATUS_SUCCESS;
	}

	/**
	 * casssandraをインストールする
	 *
	 * @param instance
	 * @return 実効結果
	 */
	public String installCassandra(Instance instance) {
		// javaとcassandraのインストール
		ssh.exec(instance, "sudo amazon-linux-extras enable corretto8");
		ssh.exec(instance, "sudo yum -y install java-1.8.0-amazon-corretto");
		CassandraRepo repo = CassandraRepo.createManager();
		repo.create();
		sftp.execCreateFile(instance, repo.getFileName(), repo.getDetail());
		ssh.exec(instance, "sudo yum -y install cassandra");
		
		
//		// シェルをサーバに格納し、javaとcassandraをセットアップ
//		InstallCassandraSh installCassandraSh = InstallCassandraSh.createManager();
//		installCassandraSh.create();
//		String homeInstallCassandraSh = PATH_EC2_USER_HOME + FILE_INSTALL_CASSANDRA_SH;
//		String tmpInstallCassandraSh = PATH_TMP + FILE_INSTALL_CASSANDRA_SH;
//		putFile(tmpInstallCassandraSh, homeInstallCassandraSh);
//		exec(COMMAND_CHMOD, CHMOD_OCTAL_777, homeInstallCassandraSh);
//		exec(COMMAND_EXEC_SHELL_INSTALLCASSANDRA, "");
//		
//		// profileを最新化
//		Profile profile = Profile.createManager();
//		String homeProfile = PATH_EC2_USER_HOME + FILE_PROFILE;
//		String etcProfile = PATH_ETC + FILE_PROFILE;
//		String tmpProfile = PATH_TMP + FILE_PROFILE;
//
//		// 最新のprofile情報を取得し、サーバにprofileを配置
//		profile.createProfileForInstallCassandra();
//		putFile(tmpProfile, homeProfile);
//
//		// 今あるprofileを削除し新しいprofileを配置
//		execSudo(COMMAND_RM, etcProfile);
//		execSudo(COMMAND_MV, homeProfile, etcProfile);
//		
//		// profileをサーバに配置し、pathを通す
//		exec(COMMAND_SOURCE, etcProfile);
//		execSudo(COMMAND_SOURCE, etcProfile);
		return COMMAND_STATUS_SUCCESS;
	}

	/**
	 * casssandra.yamlを設定する
	 * 
	 * @param user ユーザー
	 * @param instance EC2インスタンス
	 * @return 処理結果
	 */
	public String setCassandraYaml(LoginUser user, Instance instance) {
		// cassandra.yamlファイルを作成し設定する
		CassandraYaml yaml = CassandraYaml.createManager(user);
		yaml.create(user, instance);
		sftp.execCreateFile(instance, yaml.getFileName(), yaml.getDetail());
		return COMMAND_STATUS_SUCCESS;
	}

	/**
	 * cassandraのプロセスIDを取得
	 *
	 * @param instance
	 * @return 判定結果
	 */
	public String getProcessIdByCassandra(Instance instance) {
		// cassandraのプロセスIDを取得し起動済みか判定
		return ssh.exec(instance, "pgrep -f cassandra");
	}

//	/**
//	 * cassandraを実行する
//	 * 
//	 * @return 処理結果
//	 * @throws JSchException
//	 * @throws SftpException
//	 */
//	public String execCassandra() throws JSchException, SftpException {
//		ExecCassandraSh sh = ExecCassandraSh.createManager();
//		sh.create();
//		execCreateFile(sh.getFileName(), sh.getDetail());
//		exec(COMMAND_CHMOD, CHMOD_OCTAL_777, sh.getFileName());
//		exec(sh.getFileName());
//		
//		return COMMAND_STATUS_SUCCESS;
//	}

	/**
	 * cassandraを実行する
	 *
	 * @param instance
	 * @return 処理結果
	 */
	public String execCassandra(Instance instance) {
//		exec("sudo systemctl daemon-reload");
//		exec("sudo systemctl start cassandra");

//		execSudo(CassandraConsts.EXEC_CASSANDRA_BACK_GROUND);
//		execSudo("systemctl restart execCassandra");
		ssh.execSudo(instance, "systemctl restart cassandra");
		
//		execSudo(CassandraConsts.EXEC_CASSANDRA_BACK_GROUND);
//		exec(EXEC_CASSANDRA_FORE_GROUND);
		return COMMAND_STATUS_SUCCESS;
	}

	/**
	 * Cqlコマンドを実行
	 *
	 * @param instance
	 * @param cqlCommand
	 * @return 実効結果
	 */
	public String execCql(Instance instance, String cqlCommand) {
		return ssh.exec(instance, ap.getCqlInstallDir() + "cqlsh " + instance.getPublicIpAddress() + " -e \"" + cqlCommand + "\"");
//		return exec("cqlsh " + instance.getPublicIpAddress() + " -e \"" + cqlCommand + "\"  | tr -d '\\n'");
//		return exec("sudo source /etc/profile && cqlsh " + instance.getPublicIpAddress() + " -e \"" + cqlCommand + "\"");
//		return exec("/home/ec2-user/testCql.sh | touch aaa | printenv > aaa");
//		return exec("touch aaa | cassandra -v 2> aaa");
	}

}

