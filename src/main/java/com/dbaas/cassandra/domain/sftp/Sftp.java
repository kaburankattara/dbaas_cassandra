package com.dbaas.cassandra.domain.sftp;

import com.dbaas.cassandra.domain.jsch.Jsch;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.jcraft.jsch.*;

import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.*;

public class Sftp {

    public static Sftp createInstance() {
        return new Sftp();
    }

    private Sftp() {
    }

    private final Jsch jsch = Jsch.createInstance();

    /**
     * サーバにファイルを作成する
     *
     * @param fileName ファイル名
     * @param fileStringDetail ファイル内容
     * @return 実行結果
     */
    public String execCreateFile(Instance instance, String fileName, String fileStringDetail) {
        // StringBuilderでコマンドを生成
        StringBuilder sb = new StringBuilder();
        sb.append(COMMAND_SUDO)
                .append(String.format(COMMAND_TOUCH, fileName))
                .append(" | ")
                .append(COMMAND_SUDO)
                .append(" echo ")
                .append("\"" + fileStringDetail + "\"")
                .append(" | sudo tee ")
                .append(fileName);

        // コマンド実行
        Session session = null;
        ChannelExec channel = null;
        try {
            session = jsch.connectSessionToRetryCount(instance);
            channel = (ChannelExec) session.openChannel(CHANNEL_TYPE_EXEC);
            channel.setCommand(sb.toString());
            channel.connect();
            System.out.println("exec-command:" + sb.toString());
            return jsch.getCommandResult(channel);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        } finally {
            jsch.disconnect(session, channel);
        }
        return null;
    }

    /**
     * ファイルアップロード
     *
     * @param sourcePath
     *            アップロード対象ファイルのパス<br>
     *            アプリ実行環境上の絶対パスを指定
     * @param destPath
     *            アップ先のパス
     */
    public void putFile(Instance instance, final String sourcePath, final String destPath) {
        Session session = null;
        ChannelSftp channel = null;

        try {
            session = jsch.connectSessionToRetryCount(instance);
            channel = jsch.connectChannelSftp(session);
            channel.put(sourcePath, destPath);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            jsch.disconnect(session, channel);
        }
    }
}
