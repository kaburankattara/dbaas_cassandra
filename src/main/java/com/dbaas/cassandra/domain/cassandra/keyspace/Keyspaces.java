package com.dbaas.cassandra.domain.cassandra.keyspace;

import com.dbaas.cassandra.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class Keyspaces {

    public static Keyspaces createEmptyInstance() {
        return new Keyspaces();
    }

    public static Keyspaces createInstance(List<Keyspace> keyspaceList) {
        return new Keyspaces(keyspaceList);
    }

    public static Keyspaces createInstanceByStringList(List<String> keyspaceListStr) {
        List<Keyspace> keyspaceList = new ArrayList<Keyspace>();
        for (String keyspaceStr :  keyspaceListStr) {
            keyspaceList.add(Keyspace.createInstance(keyspaceStr));
        }

        return new Keyspaces(keyspaceList);
    }

    public Keyspaces() {
    }

    public Keyspaces(List<Keyspace> keyspaceList) {
        this.keyspaceList = keyspaceList;
    }

    private List<Keyspace> keyspaceList = new ArrayList<Keyspace>();

    public List<Keyspace> getKeyspaceList() {
        return keyspaceList;
    }

    public List<String> toStringList() {
        if (isEmpty()) {
            return new ArrayList<String>();
        }

        List<String> resultList = new ArrayList<String>();
        for (Keyspace keyspace : keyspaceList) {
            resultList.add(keyspace.getKeyspace());
        }
        return resultList;
    }

    public void setKeyspaceList(List<Keyspace> keyspaceList) {
        this.keyspaceList = keyspaceList;
    }

    public boolean isEmpty() {
        return ObjectUtils.isEmpty(keyspaceList);
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

    public void addAll(Keyspaces keyspaces) {

        if (keyspaces.isEmpty()) {
            return;
        }

        this.keyspaceList.addAll(keyspaces.getKeyspaceList());
    }

}
