package com.dbaas.cassandra.domain.cassandra;

import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.CHANNEL_TYPE_EXEC;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.CHANNEL_TYPE_SFTP;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.CHMOD_OCTAL_777;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_CHMOD;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_EXEC_SHELL_INSTALLCASSANDRA;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_MV;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_RM;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_SOURCE;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_STATUS_SUCCESS;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_SUDO;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_TOUCH;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.EXEC_CASSANDRA_FORE_GROUND;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.FILE_CASSANDRA;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.FILE_INSTALL_CASSANDRA_SH;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.FILE_JAVA;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.FILE_PROFILE;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.PATH_EC2_USER_HOME;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.PATH_ETC;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.PATH_OPT;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.PATH_TMP;
import static com.dbaas.cassandra.utils.StringUtils.isNotEmpty;
import static com.dbaas.cassandra.utils.ThreadUtils.sleep;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.file.CassandraYaml;
import com.dbaas.cassandra.domain.cassandra.file.InstallCassandraSh;
import com.dbaas.cassandra.domain.cassandra.file.Profile;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class Cassandra {

	/**
	 * ホスト名
	 */
	private String hostname;

	/**
	 * ポート番号
	 */
	private int port;

	/**
	 * ユーザID
	 */
	private String userId;

	/**
	 * 認証キーファイル名
	 */
	private String identityKeyFileName;

	/**
	 * インスタンス生成
	 * 
	 * @param hostname
	 * @param port
	 * @param userId
	 * @param identityKeyFileName
	 * @return
	 */
	public static Cassandra createInstance(String hostname, int port, String userId, String identityKeyFileName) {
		return new Cassandra(hostname, port, userId, identityKeyFileName);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param hostname
	 * @param port
	 * @param userId
	 * @param identityKeyFileName
	 */
	private Cassandra(String hostname, int port, String userId, String identityKeyFileName) {
		this.hostname = hostname;
		this.port = port;
		this.userId = userId;
		this.identityKeyFileName = identityKeyFileName;
	}

    /**
     * ファイルアップロード
     *
     * @param sourcePath
     *            アップロード対象ファイルのパス<br>
     *            アプリ実行環境上の絶対パスを指定
     * @param destPath
     *            アップ先のパス
     * @throws JSchException
     *             Session・Channelの設定/接続エラー時に発生
     * @throws SftpException
     *             sftp操作失敗時に発生
     */
	public void putFile(final String sourcePath, final String destPath) throws JSchException, SftpException {
        Session session = null;
        ChannelSftp channel = null;

		try {
			session = connectSessionToRetryCount();
            channel = connectChannelSftp(session);
            channel.put(sourcePath, destPath);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		} finally {
			disconnect(session, channel);
		}
    }

	/**
	 * casssandraをインストールする
	 * 
	 * @throws JSchException
	 * @throws SftpException
	 */
	public String unInstallCassandra() throws JSchException, SftpException {
		String optJava = PATH_OPT + FILE_JAVA;
		String optCassandra = PATH_OPT + FILE_CASSANDRA;
		String homeProfile = PATH_EC2_USER_HOME + FILE_PROFILE;
		String etcProfile = PATH_ETC + FILE_PROFILE;
		String tmpProfile = PATH_TMP + FILE_PROFILE;
		Profile profile = Profile.createManager();

		// javaとCassandraを削除
		execSudo(COMMAND_RM, optJava);
		execSudo(COMMAND_RM, optCassandra);

		// profileを最新化
		// 最新のprofile情報を取得し、サーバにprofileを配置
		profile.createProfile();
		putFile(tmpProfile, homeProfile);

		// 今あるprofileを削除し新しいprofileを配置
		execSudo(COMMAND_RM, etcProfile);
		execSudo(COMMAND_MV, homeProfile, etcProfile);
		
		// profileをサーバに配置し、pathを通す
		exec(COMMAND_SOURCE, etcProfile);
		execSudo(COMMAND_SOURCE, etcProfile);
		return COMMAND_STATUS_SUCCESS;
	}

	/**
	 * casssandraをインストールする
	 * 
	 * @throws JSchException
	 * @throws SftpException
	 */
	public String installCassandra() throws JSchException, SftpException {
		// シェルをサーバに格納し、javaとcassandraをセットアップ
		InstallCassandraSh installCassandraSh = InstallCassandraSh.createManager();
		installCassandraSh.create();
		String homeInstallCassandraSh = PATH_EC2_USER_HOME + FILE_INSTALL_CASSANDRA_SH;
		String tmpInstallCassandraSh = PATH_TMP + FILE_INSTALL_CASSANDRA_SH;
		putFile(tmpInstallCassandraSh, homeInstallCassandraSh);
		exec(COMMAND_CHMOD, CHMOD_OCTAL_777, homeInstallCassandraSh);
		exec(COMMAND_EXEC_SHELL_INSTALLCASSANDRA, "");
		
		// profileを最新化
		Profile profile = Profile.createManager();
		String homeProfile = PATH_EC2_USER_HOME + FILE_PROFILE;
		String etcProfile = PATH_ETC + FILE_PROFILE;
		String tmpProfile = PATH_TMP + FILE_PROFILE;

		// 最新のprofile情報を取得し、サーバにprofileを配置
		profile.createProfileForInstallCassandra();
		putFile(tmpProfile, homeProfile);

		// 今あるprofileを削除し新しいprofileを配置
		execSudo(COMMAND_RM, etcProfile);
		execSudo(COMMAND_MV, homeProfile, etcProfile);
		
		// profileをサーバに配置し、pathを通す
		exec(COMMAND_SOURCE, etcProfile);
		execSudo(COMMAND_SOURCE, etcProfile);
		return COMMAND_STATUS_SUCCESS;
	}

	/**
	 * casssandra.yamlを設定する
	 * 
	 * @param user ユーザー
	 * @param instance EC2インスタンス
	 * @return 処理結果
	 * @throws JSchException
	 * @throws SftpException
	 */
	public String setCassandraYaml(LoginUser user, Instance instance) throws JSchException, SftpException {
		// cassandra.yamlファイルを作成し設定する
		CassandraYaml yaml = CassandraYaml.createManager(user);
		yaml.create(user, instance);
		execCreateFile(yaml.getFileName(), yaml.getDetail());
		return COMMAND_STATUS_SUCCESS;
	}

	/**
	 * casssandraが起動済み判定
	 * 
	 * @return 判定結果
	 * @throws JSchException
	 * @throws SftpException
	 */
	public boolean isExecCassandra() throws JSchException, SftpException {
		// cassandraのプロセスIDを取得し起動済みか判定
		return isNotEmpty(exec("pgrep -f cassandra"));
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
	 * @return 処理結果
	 * @throws JSchException
	 * @throws SftpException
	 */
	public String execCassandra() throws JSchException, SftpException {
		exec(EXEC_CASSANDRA_FORE_GROUND);
		return COMMAND_STATUS_SUCCESS;
	}

	/**
	 * Cqlコマンドを実行
	 * 
	 * @return 判定結果
	 * @throws JSchException
	 * @throws SftpException
	 */
	public String execCql(Instance instance, String cqlCommand) throws JSchException, SftpException {
//		exec("touch aaa | printenv > aaa");
//		return "";

//		return exec("sudo source /etc/profile && cqlsh " + instance.getPublicIpAddress() + " -e \"" + cqlCommand + "\"");
//		return exec("/home/ec2-user/testCql.sh | touch aaa | printenv > aaa");
		return exec("touch aaa | cassandra -v 2> aaa");
	}
	
	/**
	 * コマンドを実行する（インターフェース）
	 * 
	 * @throws JSchException
	 * @throws SftpException
	 */
	private String exec(String command, Object... args) throws JSchException, SftpException {
		return execCommand("", command, args);
	}

	/**
	 * スーパーユーザ権限でコマンドを実行する（インターフェース）
	 * 
	 * @throws JSchException
	 * @throws SftpException
	 */
	private String execSudo(String command, Object... args) throws JSchException, SftpException {
		return execCommand(COMMAND_SUDO, command, args);
	}

	/**
	 * コマンドを実行する
	 * 
	 * @throws JSchException
	 * @throws SftpException
	 */
	private String execCommand(String sudo, String command, Object... args) throws JSchException, SftpException {
		Session session = null;
		ChannelExec channel = null;

		try {
			session = connectSessionToRetryCount();
			channel = (ChannelExec) session.openChannel(CHANNEL_TYPE_EXEC);
			channel.setCommand(String.format(sudo + command, args));
            channel.connect();
            System.out.println("exec-command:" + String.format(sudo + command, args));
            return getCommandResult(channel, session);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
		} finally {
			disconnect(session, channel);
		}
		return null;
	}
	
	/**
	 * サーバにファイルを作成する
	 * 
	 * @param fileName ファイル名
	 * @param fileStringDetail ファイル内容
	 * @return 実行結果
	 * @throws JSchException
	 * @throws SftpException
	 */
	public String execCreateFile(String fileName, String fileStringDetail) throws JSchException, SftpException {
		// StringBuilderでコマンドを生成
		StringBuilder sb = new StringBuilder();
		sb.append(COMMAND_SUDO)
			.append(String.format(COMMAND_TOUCH, fileName))
			.append(" | ")
			.append("echo ")
			.append("\"" + fileStringDetail + "\"")
			.append(" > ")
			.append(fileName);
		
		// コマンド実行
		Session session = null;
		ChannelExec channel = null;
		try {
			session = connectSessionToRetryCount();
			channel = (ChannelExec) session.openChannel(CHANNEL_TYPE_EXEC);
			channel.setCommand(sb.toString());
            channel.connect();
    		System.out.println("exec-command:" + sb.toString());
    		return getCommandResult(channel, session);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
		} finally {
			disconnect(session, channel);
		}
		return null;
	}
	
	public Session connectSessionToRetryCount() {
		int execCount = 0;
		int retryCount = 5;// TODO 可変定数化

		while (execCount <= retryCount) {
			try {
				return connectSession();
			} catch (Exception e) {
				sleep();
				execCount++;
				continue;
			}
		}
		throw new RuntimeException();
	}
	
	/**
	 * Sessionを開始
	 */
	private Session connectSession() throws JSchException, IOException {
		final JSch jsch = new JSch();
		// 鍵追加
		jsch.addIdentity(identityKeyFileName);
		// Session設定
		final Session session = jsch.getSession(userId, hostname, port);
		session.setUserInfo(new SessionUser());
		session.connect();

		return session;
	}

	/**
	 * Session・Channelの終了
	 *
	 * @param session  開始されたSession情報
	 * @param channels 開始されたChannel情報.複数指定可能
	 */
	private void disconnect(final Session session, final Channel... channels) {
		if (channels != null) {
			Arrays.stream(channels).forEach(c -> {
				if (c != null) {
					c.disconnect();
				}
			});
		}
		if (session != null) {
			session.disconnect();
		}
	}
	
	private String getCommandResult(ChannelExec channel, Session session) {
		BufferedInputStream bin = null;
		ByteArrayOutputStream bout = null;
		
		bout = new ByteArrayOutputStream();
		try {
			bin = new BufferedInputStream(channel.getInputStream());

			byte[] buf = new byte[1024];
			int length;
			while (true) {
				length = bin.read(buf);
				if (length == -1) {
					break;
				}
				bout.write(buf, 0, length);
			}
			// 標準出力
			return new String(bout.toByteArray(), StandardCharsets.UTF_8);

		} catch(IOException ex) {
			System.out.println(ex.toString());
		}finally {
			if (bin != null) {
				try {
					bin.close();
				} catch (IOException e) {
				}
			}
            if (channel != null) {
                try {
                    channel.disconnect();
                }
                catch (Exception e) {
                }
            }
            if (session != null) {
                try {
                    session.disconnect();
                }
                catch (Exception e) {
                }
            }
		}
		return new String(bout.toByteArray(), StandardCharsets.UTF_8);
	}

    /**
     * SFTPのChannelを開始
     *
     * @param session
     *            開始されたSession情報
     */
    private ChannelSftp connectChannelSftp(final Session session)
            throws JSchException {
        final ChannelSftp channel = (ChannelSftp) session
                .openChannel(CHANNEL_TYPE_SFTP);
        channel.connect();

        return channel;
    }
}
