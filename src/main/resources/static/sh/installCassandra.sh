#!/bin/bash

#javaをインストール
wget https://corretto.aws/downloads/latest/amazon-corretto-8-x64-linux-jdk.tar.gz

tar -xzvf /home/ec2-user/amazon-corretto-8-x64-linux-jdk.tar.gz
sudo mv /home/ec2-user/amazon-corretto-8.242.08.1-linux-x64 /opt/java

rm -rf amazon-corretto-8-x64-linux-jdk.tar.gz

#Casssandraをインストール
wget http://archive.apache.org/dist/cassandra/3.11.6/apache-cassandra-3.11.6-bin.tar.gz

tar -xzvf /home/ec2-user/apache-cassandra-3.11.6-bin.tar.gz
sudo mv /home/ec2-user/apache-cassandra-3.11.6 /opt/cassandra

rm -rf apache-cassandra-3.11.6-bin.tar.gz

exit 0



