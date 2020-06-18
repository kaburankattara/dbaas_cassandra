package com.dbaas.cassandra.domain.kbn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.dbaas.cassandra.domain.table.kbn.KbnEntity;

public class Kbns extends KbnEntity {
	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Kbn> kbnList;

	/**
	 * デフォルトコンストラクタ
	 */
	public Kbns(List<Kbn> kbnList) {
		if (ObjectUtils.isEmpty(kbnList)) {
			this.kbnList = new ArrayList<Kbn>();
		}
		this.kbnList = kbnList;
	}
	
	public List<Kbn> getKbnList() {
		return kbnList;
	}
	
	public void setKbnList(List<Kbn> kbnList) {
		this.kbnList = kbnList;
	}
	
	public static List<Kbn> toKbnList(List<KbnEntity> kbnEntityList) {
		List<Kbn> kbnList = new ArrayList<Kbn>();

		if (ObjectUtils.isEmpty(kbnEntityList)) {
			return kbnList;
		}
		
		for (KbnEntity entity : kbnEntityList) {
			kbnList.add(new Kbn(entity));
		}
		return kbnList;		
	}
	
}
