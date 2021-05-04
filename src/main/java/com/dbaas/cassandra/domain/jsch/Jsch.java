package com.dbaas.cassandra.domain.jsch;

import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.shared.applicationProperties.ApplicationProperties;
import com.dbaas.cassandra.shared.exception.SystemException;
import com.jcraft.jsch.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.*;
import static com.dbaas.cassandra.utils.ThreadUtils.sleep;

public class Jsch {

    public static Jsch createInstance() {
        return new Jsch();
    }

    private Jsch() {
    }

    ApplicationProperties ap = ApplicationProperties.createInstance();

    public Session connectSessionToRetryCount(Instance instance) {
        int execCount = 0;
        int retryCount = ap.getConnectSessionRetryCount();

        while (execCount < retryCount) {
            try {
                return connectSession(instance);
            } catch (Exception e) {
                sleep();
                execCount++;
                continue;
            }
        }
        throw new SystemException();
    }

    /**
     * Sessionを開始
     */
    public Session connectSession(Instance instance) {
        try {
            final JSch jsch = new JSch();
            // 鍵追加
            jsch.addIdentity(ap.getIdentityKeyFileName());
            // Session設定
            final Session session = jsch.getSession(ap.getRemoteServerUser(), instance.getPublicIpAddress(), ap.getSshPort());
            session.setUserInfo(new SessionUser());
            session.connect();

            return session;
        } catch (JSchException e) {
            throw new SystemException();
        }
    }

    /**
     * Session・Channelの終了
     *
     * @param session  開始されたSession情報
     * @param channels 開始されたChannel情報.複数指定可能
     */
    public void disconnect(final Session session, final Channel... channels) {
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

    public String getCommandResult(ChannelExec channel) {

        try (BufferedInputStream bin = new BufferedInputStream(channel.getInputStream())) {
//		try (BufferedInputStream bin = new BufferedInputStream(channel.getExtInputStream())) {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
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
        } finally {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
        }
        return "";
    }

    /**
     * SFTPのChannelを開始
     *
     * @param session
     * @return ChannelSftp
     */
    public ChannelSftp connectChannelSftp(final Session session) {
        try {
            final ChannelSftp channel = (ChannelSftp) session
                    .openChannel(CHANNEL_TYPE_SFTP);
            channel.connect();

            return channel;
        } catch (JSchException e) {
            throw new SystemException();
        }
    }

}
