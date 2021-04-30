package com.dbaas.cassandra.domain.cassandra.keyspace;

import com.dbaas.cassandra.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class Keyspaces {

    public static Keyspaces createEmptyInstance() {
        return new Keyspaces();
    }

    public static Keyspaces createInstance(List<String> keyspaceList) {
        return new Keyspaces(keyspaceList);
    }

    public Keyspaces() {
        this.keyspaceList = new ArrayList<Keyspace>();
    }

    public Keyspaces(List<String> keyspaceListArg) {
        List<Keyspace> keyspaceList = new ArrayList<Keyspace>();
        for (String keyspaceArg : keyspaceListArg) {
            keyspaceList.add(Keyspace.createInstance(keyspaceArg));
        }

        this.keyspaceList = keyspaceList;
    }

    private List<Keyspace> keyspaceList;

    public List<Keyspace> getKeyspaceList() {
        return keyspaceList;
    }

    public void setKeyspaceList(List<Keyspace> keyspaceList) {
        this.keyspaceList = keyspaceList;
    }

    public boolean hasKeyspace(Keyspace keyspaceArg) {

        if (ObjectUtils.isEmpty(keyspaceList)) {
            return false;
        }

        for (Keyspace keyspace : keyspaceList) {
            if (keyspace.isEquals(keyspaceArg)) {
                return true;
            }
        }

        return false;
    }
}
