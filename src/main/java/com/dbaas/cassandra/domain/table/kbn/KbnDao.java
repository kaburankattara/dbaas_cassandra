package com.dbaas.cassandra.domain.table.kbn;

import static com.dbaas.cassandra.domain.kbn.Kbns.toKbnList;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.kbn.Kbn;
import com.dbaas.cassandra.domain.kbn.Kbns;

@Service
@Transactional
public class KbnDao {
	
    private final KbnRepository repository;

    public KbnDao(KbnRepository repository){
        this.repository = repository;
    }
	
    /**
     * 主キー検索
     * 
     * @param key 主キー
     * @return 区分
     */
	public Kbn findById(KbnEntityKey key) {
		Optional<KbnEntity> OptionalKbn = repository.findById(key);
		if (isEmpty(OptionalKbn)) {
			return new Kbn();
		}
		return new Kbn(OptionalKbn.get());
	}
	
    /**
     * 種別CDで検索
     * 
     * @param typeCd 種別CD
     * @return 区分リスト
     */
	public Kbns findByTypeCd(String typeCd) {
		return new Kbns(toKbnList(repository.findByTypeCd(typeCd)));
	}
}
