package com.dbaas.cassandra.domain.kbn;

import static com.dbaas.cassandra.utils.BeanUtils.copyCrossTypeProperties;

import com.dbaas.cassandra.domain.table.kbn.KbnEntity;

public class Kbn extends KbnEntity {
	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * デフォルトコンストラクタ
	 */
	public Kbn() {
	}

	/**
	 * デフォルトコンストラクタ
	 */
	public Kbn(KbnEntity entity) {
		copyCrossTypeProperties(entity, this);
	}
}
