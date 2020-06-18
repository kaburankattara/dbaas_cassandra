package com.dbaas.cassandra.domain.table.kbn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Data
@IdClass(KbnEntityKey.class)
@Table(name = "m_kbn")
@Entity
public class KbnEntity implements Serializable {
	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * デフォルトコンストラクタ
	 */
	public KbnEntity() {
	}

	@Id
	private String typeCd;

	@Id
	private String kbn;

	private String typeName;

	private String kbnName;

	@Column(name = "fuzui_moji_1")
	private String fuzuiMoji1;

	@Column(name = "fuzui_moji_2")
	private String fuzuiMoji2;

	@Column(name = "fuzui_moji_3")
	private String fuzuiMoji3;

	@Column(name = "fuzui_seisu_1")
	private Integer fuzuiSeisu1;

	@Column(name = "fuzui_seisu_2")
	private Integer fuzuiSeisu2;

	@Column(name = "fuzui_seisu_3")
	private Integer fuzuiSeisu3;

	private Short hyojijun;

	private Boolean yukoFlg;

	public String getTypeCd() {
		return typeCd;
	}

	public void setTypeCd(String typeCd) {
		this.typeCd = typeCd;
	}

	public String getKbn() {
		return kbn;
	}

	public void setKbn(String kbn) {
		this.kbn = kbn;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getKbnName() {
		return kbnName;
	}

	public void setKbnName(String kbnName) {
		this.kbnName = kbnName;
	}

	public String getFuzuiMoji1() {
		return fuzuiMoji1;
	}

	public void setFuzuiMoji1(String fuzuiMoji1) {
		this.fuzuiMoji1 = fuzuiMoji1;
	}

	public String getFuzuiMoji2() {
		return fuzuiMoji2;
	}

	public void setFuzuiMoji2(String fuzuiMoji2) {
		this.fuzuiMoji2 = fuzuiMoji2;
	}

	public String getFuzuiMoji3() {
		return fuzuiMoji3;
	}

	public void setFuzuiMoji3(String fuzuiMoji3) {
		this.fuzuiMoji3 = fuzuiMoji3;
	}

	public Integer getFuzuiSeisu1() {
		return fuzuiSeisu1;
	}

	public void setFuzuiSeisu1(Integer fuzuiSeisu1) {
		this.fuzuiSeisu1 = fuzuiSeisu1;
	}

	public Integer getFuzuiSeisu2() {
		return fuzuiSeisu2;
	}

	public void setFuzuiSeisu2(Integer fuzuiSeisu2) {
		this.fuzuiSeisu2 = fuzuiSeisu2;
	}

	public Integer getFuzuiSeisu3() {
		return fuzuiSeisu3;
	}

	public void setFuzuiSeisu3(Integer fuzuiSeisu3) {
		this.fuzuiSeisu3 = fuzuiSeisu3;
	}

	public Short getHyojijun() {
		return hyojijun;
	}

	public void setHyojijun(Short hyojijun) {
		this.hyojijun = hyojijun;
	}

	public Boolean getYukoFlg() {
		return yukoFlg;
	}

	public void setYukoFlg(Boolean yukoFlg) {
		this.yukoFlg = yukoFlg;
	}
}
