package com.dbaas.cassandra.domain.jsch.bean;

import com.dbaas.cassandra.shared.applicationProperties.ApplicationProperties;
import com.jcraft.jsch.SocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketFactoryWithTimeout implements SocketFactory {
    public Socket createSocket(String host, int port) throws IOException {
        ApplicationProperties ap = ApplicationProperties.createInstance();

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), ap.getSocketTimeout());

        return socket;
    }

    public InputStream getInputStream(Socket socket) throws IOException {
        return socket.getInputStream();
    }

    public OutputStream getOutputStream(Socket socket) throws IOException {
        return socket.getOutputStream();
    }

}
