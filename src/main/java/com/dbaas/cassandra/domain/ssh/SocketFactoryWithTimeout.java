package com.dbaas.cassandra.domain.ssh;

import com.dbaas.cassandra.domain.jsch.Jsch;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.shared.applicationProperties.ApplicationProperties;
import com.dbaas.cassandra.shared.exception.SystemException;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.dbaas.cassandra.consts.SysConsts.EMPTY;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.*;

public class SocketFactoryWithTimeout implements SocketFactory {

    public static SocketFactoryWithTimeout createInstance(Instance instance) {
        ApplicationProperties ap = ApplicationProperties.createInstance();
        return new SocketFactoryWithTimeout(instance.getPublicIpAddress(), ap.getSshPort());
    }

    public SocketFactoryWithTimeout(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public SocketFactoryWithTimeout () {

    }

    private String host;

    private int port;

    public Socket createSocket(String host, int port) throws IOException,
            UnknownHostException
    {
        Socket socket = new Socket();
        int timeout = 2000;
        socket.connect(new InetSocketAddress(this.host, this.port), timeout);
        return socket;
    }

    public InputStream getInputStream(Socket socket) throws IOException
    {
        return socket.getInputStream();
    }

    public OutputStream getOutputStream(Socket socket) throws IOException
    {
        return socket.getOutputStream();
    }

}
