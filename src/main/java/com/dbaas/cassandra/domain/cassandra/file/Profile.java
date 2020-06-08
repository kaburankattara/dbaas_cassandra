package com.dbaas.cassandra.domain.cassandra.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Profile {

	private boolean isInstalledJava;
	private boolean isInstalledCassandra;
	
	/**
	 * 設定ファイルマネージャーを生成
	 * @return
	 */
	public static Profile createManager() {
		return new Profile();
	}
	
	public Profile() {
		
	}

	public void createProfileForInstallCassandra() {
		// Cassandraインストールを設定に取り入れる
		setInstalleadCassandra();
		
		// profileを作成
		createProfile();
	}

	public void createProfile() {
		// profileのバッファを生成
		String profileBuffer = createBufferForProfile();
		
		// 生成したprofileのバッファをもとにファイルを生成する
        try (BufferedWriter in = new BufferedWriter(new FileWriter(new File("/tmp/profile")))){
        	in.append(profileBuffer);
        } catch (Exception e){ }
	}
	
    public void fileDelete() {
        File file = new File("/Users/Shared/java/java.txt");
 
        //deleteメソッドを使用してファイルを削除する
        file.delete();
    }
	
	private String createBufferForProfile() {
		StringBuffer sb = new StringBuffer();
		sb.append("# /etc/profile").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("# System wide environment and startup programs, for login setup").append(System.getProperty("line.separator"));
		sb.append("# Functions and aliases go in /etc/bashrc").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("# It's NOT a good idea to change this file unless you know what you").append(System.getProperty("line.separator"));
		sb.append("# are doing. It's much better to create a custom.sh shell script in").append(System.getProperty("line.separator"));
		sb.append("# /etc/profile.d/ to make custom changes to your environment, as this").append(System.getProperty("line.separator"));
		sb.append("# will prevent the need for merging in future updates.").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("pathmunge () {").append(System.getProperty("line.separator"));
		sb.append("    case \":${PATH}:\" in").append(System.getProperty("line.separator"));
		sb.append("        *:\"$1\":*)").append(System.getProperty("line.separator"));
		sb.append("            ;;").append(System.getProperty("line.separator"));
		sb.append("        *)").append(System.getProperty("line.separator"));
		sb.append("            if [ \"$2\" = \"after\" ] ; then").append(System.getProperty("line.separator"));
		sb.append("                PATH=$PATH:$1").append(System.getProperty("line.separator"));
		sb.append("            else").append(System.getProperty("line.separator"));
		sb.append("                PATH=$1:$PATH").append(System.getProperty("line.separator"));
		sb.append("            fi").append(System.getProperty("line.separator"));
		sb.append("    esac").append(System.getProperty("line.separator"));
		sb.append("}").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("if [ -x /usr/bin/id ]; then").append(System.getProperty("line.separator"));
		sb.append("    if [ -z \"$EUID\" ]; then").append(System.getProperty("line.separator"));
		sb.append("        # ksh workaround").append(System.getProperty("line.separator"));
		sb.append("        EUID=`/usr/bin/id -u`").append(System.getProperty("line.separator"));
		sb.append("        UID=`/usr/bin/id -ru`").append(System.getProperty("line.separator"));
		sb.append("    fi").append(System.getProperty("line.separator"));
		sb.append("    USER=\"`/usr/bin/id -un`\"").append(System.getProperty("line.separator"));
		sb.append("    LOGNAME=$USER").append(System.getProperty("line.separator"));
		sb.append("    MAIL=\"/var/spool/mail/$USER\"").append(System.getProperty("line.separator"));
		sb.append("fi").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("# Path manipulation").append(System.getProperty("line.separator"));
		sb.append("if [ \"$EUID\" = \"0\" ]; then").append(System.getProperty("line.separator"));
		sb.append("    pathmunge /usr/sbin").append(System.getProperty("line.separator"));
		sb.append("    pathmunge /usr/local/sbin").append(System.getProperty("line.separator"));
		sb.append("else").append(System.getProperty("line.separator"));
		sb.append("    pathmunge /usr/local/sbin after").append(System.getProperty("line.separator"));
		sb.append("    pathmunge /usr/sbin after").append(System.getProperty("line.separator"));
		sb.append("fi").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("HOSTNAME=`/usr/bin/hostname 2>/dev/null`").append(System.getProperty("line.separator"));
		sb.append("HISTSIZE=1000").append(System.getProperty("line.separator"));
		sb.append("if [ \"$HISTCONTROL\" = \"ignorespace\" ] ; then").append(System.getProperty("line.separator"));
		sb.append("    export HISTCONTROL=ignoreboth").append(System.getProperty("line.separator"));
		sb.append("else").append(System.getProperty("line.separator"));
		sb.append("    export HISTCONTROL=ignoredups").append(System.getProperty("line.separator"));
		sb.append("fi").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("export PATH USER LOGNAME MAIL HOSTNAME HISTSIZE HISTCONTROL").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("# By default, we want umask to get set. This sets it for login shell").append(System.getProperty("line.separator"));
		sb.append("# Current threshold for system reserved uid/gids is 200").append(System.getProperty("line.separator"));
		sb.append("# You could check uidgid reservation validity in").append(System.getProperty("line.separator"));
		sb.append("# /usr/share/doc/setup-*/uidgid file").append(System.getProperty("line.separator"));
		sb.append("if [ $UID -gt 199 ] && [ \"`/usr/bin/id -gn`\" = \"`/usr/bin/id -un`\" ]; then").append(System.getProperty("line.separator"));
		sb.append("    umask 002").append(System.getProperty("line.separator"));
		sb.append("else").append(System.getProperty("line.separator"));
		sb.append("    umask 022").append(System.getProperty("line.separator"));
		sb.append("fi").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("for i in /etc/profile.d/*.sh /etc/profile.d/sh.local ; do").append(System.getProperty("line.separator"));
		sb.append("    if [ -r \"$i\" ]; then").append(System.getProperty("line.separator"));
		sb.append("        if [ \"${-#*i}\" != \"$-\" ]; then ").append(System.getProperty("line.separator"));
		sb.append("            . \"$i\"").append(System.getProperty("line.separator"));
		sb.append("        else").append(System.getProperty("line.separator"));
		sb.append("            . \"$i\" >/dev/null").append(System.getProperty("line.separator"));
		sb.append("        fi").append(System.getProperty("line.separator"));
		sb.append("    fi").append(System.getProperty("line.separator"));
		sb.append("done").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		sb.append("unset i").append(System.getProperty("line.separator"));
		sb.append("unset -f pathmunge").append(System.getProperty("line.separator"));
		sb.append("").append(System.getProperty("line.separator"));
		
		// JavaとCassandraをインストールした場合、Pathを通す
		if (isInstalleadCassandra()) {
			sb.append("#ここから編集した内容").append(System.getProperty("line.separator"));
			sb.append("PATH=$PATH:$HOME/.local/bin:$HOME/bin").append(System.getProperty("line.separator"));
			sb.append("").append(System.getProperty("line.separator"));
			sb.append("export PATH").append(System.getProperty("line.separator"));
			sb.append("").append(System.getProperty("line.separator"));
			sb.append("export JAVA_HOME=/opt/java").append(System.getProperty("line.separator"));
			sb.append("export JRE_HOME=~/opt/java/jre").append(System.getProperty("line.separator"));
			sb.append("export PATH=$PATH:/opt/java/bin:/opt/java/jre/bin").append(System.getProperty("line.separator"));
			sb.append("").append(System.getProperty("line.separator"));
			sb.append("export CASSANDRA_HOME=/opt/cassandra/").append(System.getProperty("line.separator"));
			sb.append("export PATH=$PATH:$CASSANDRA_HOME/bin").append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
	public void setInstalleadCassandra() {
		isInstalledJava = true;
		isInstalledCassandra = true;
	}

	public boolean isInstalleadCassandra() {
		return isInstalledJava && isInstalledCassandra;
	}
}
