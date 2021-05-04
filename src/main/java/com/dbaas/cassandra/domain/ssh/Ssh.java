package com.dbaas.cassandra.domain.ssh;

import static com.dbaas.cassandra.consts.SysConsts.EMPTY;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.CHANNEL_TYPE_EXEC;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.COMMAND_SUDO;

import com.dbaas.cassandra.domain.jsch.Jsch;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.shared.exception.SystemException;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

public class Ssh {

    public static Ssh createInstance() {
        return new Ssh();
    }

    private Ssh() {
    }

    private final Jsch jsch = Jsch.createInstance();

    /**
     * コマンドを実行する
     *
     * @param instance
     * @param command
     * @param args
     * @return 実行結果
     */
    public String exec(Instance instance, String command, Object... args) {
        return execCommand(true, instance, EMPTY, command, args);
    }

    /**
     * スーパーユーザ権限でコマンドを実行する
     *
     * @param instance
     * @param command
     * @param args
     * @return 実行結果
     */
    public String execSudo(Instance instance, String command, Object... args) {
        return execCommand(true, instance, COMMAND_SUDO, command, args);
    }

    /**
     * コマンドを実行する（リトライ無し）
     *
     * @param instance
     * @param command
     * @param args
     * @return 実行結果
     */
    public String execNoRetry(Instance instance, String command, Object... args) {
        return execCommand(false, instance, EMPTY, command, args);
    }

    /**
     * スーパーユーザ権限でコマンドを実行する（リトライ無し）
     *
     * @param instance
     * @param command
     * @param args
     * @return 実行結果
     */
    public String execSudoNoRetry(Instance instance, String command, Object... args) {
        return execCommand(false, instance, COMMAND_SUDO, command, args);
    }

    /**
     * コマンドを実行する
     *
     * @param instance
     * @param sudo
     * @param command
     * @param args
     * @return 実行結果
     */
    private String execCommand(boolean isRetry, Instance instance, String sudo, String command, Object... args) {
        Session session = null;
        ChannelExec channel = null;

        try {
            session = isRetry ? jsch.connectSessionToRetryCount(instance) : jsch.connectSession(instance);
            channel = (ChannelExec) session.openChannel(CHANNEL_TYPE_EXEC);
            channel.setCommand(String.format(sudo + command, args));
            channel.connect();
            System.out.println("exec-command:" + String.format(sudo + command, args));
            String commandResult = jsch.getCommandResult(channel);
            System.out.println("exec-result:" + commandResult);
            System.out.println("exec-statusCode:" + channel.getExitStatus());
            return commandResult;
        } catch (Exception ex) {
            if (isRetry) {
                System.out.println(ex.toString());
                ex.printStackTrace();
                throw new SystemException();
            }
        } finally {
            jsch.disconnect(session, channel);
        }
        return null;
    }

}
