package com.dbaas.cassandra.domain.ssh;

import com.dbaas.cassandra.domain.jsch.Jsch;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import static com.dbaas.cassandra.consts.SysConsts.EMPTY;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.*;

public class Ssh {

    public static Ssh createInstance() {
        return new Ssh();
    }

    private Ssh() {
    }

    private final Jsch jsch = Jsch.createInstance();

    /**
     * コマンドを実行する（インターフェース）
     *
     * @throws JSchException
     * @throws SftpException
     */
    public String exec(Instance instance, String command, Object... args) throws JSchException, SftpException {
        return execCommand(instance, EMPTY, command, args);
    }

    /**
     * スーパーユーザ権限でコマンドを実行する（インターフェース）
     *
     * @throws JSchException
     * @throws SftpException
     */
    public String execSudo(Instance instance, String command, Object... args) throws JSchException, SftpException {
        return execCommand(instance, COMMAND_SUDO, command, args);
    }

    /**
     * コマンドを実行する
     *
     * @throws JSchException
     * @throws SftpException
     */
    private String execCommand(Instance instance, String sudo, String command, Object... args) throws JSchException, SftpException {
        Session session = null;
        ChannelExec channel = null;

        try {
            session = jsch.connectSessionToRetryCount(instance);
            channel = (ChannelExec) session.openChannel(CHANNEL_TYPE_EXEC);
            channel.setCommand(String.format(sudo + command, args));
            channel.connect();
            System.out.println("exec-command:" + String.format(sudo + command, args));
            String commandResult = jsch.getCommandResult(channel, session);
            System.out.println("exec-result:" + commandResult);
            System.out.println("exec-statusCode:" + channel.getExitStatus());
            return commandResult;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        } finally {
            jsch.disconnect(session, channel);
        }
        return null;
    }

}