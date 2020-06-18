drop table m_kbn;

create table m_kbn (
  type_cd VARCHAR(50) default '' not null
  , kbn VARCHAR(10) default '' not null
  , type_name VARCHAR(20) default '' not null
  , kbn_name VARCHAR(100) default '' not null
  , fuzui_moji_1 VARCHAR(40) default '' not null
  , fuzui_moji_2 VARCHAR(40) default '' not null
  , fuzui_moji_3 VARCHAR(40) default '' not null
  , fuzui_seisu_1 integer
  , fuzui_seisu_2 integer
  , fuzui_seisu_3 integer
  , hyojijun smallint default 0 not null
  , yuko_flg Boolean default true not null
  , unique m_kbn_index (type_cd, kbn)
) ;

INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'010', 'カラム種別', 'ASCII', 'ascii',1);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'020', 'カラム種別', 'bigint', 'bigint	', 2);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'030', 'カラム種別', 'blob', 'blob', 3);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'040', 'カラム種別', 'boolean', 'boolean', 4);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'050', 'カラム種別', 'counter', 'counter', 5);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'060', 'カラム種別', 'decimal', 'decimal', 6);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'070', 'カラム種別', 'double', 'double', 7);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'080', 'カラム種別', 'float', 'float', 8);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'090', 'カラム種別', 'int', 'int', 9);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'100', 'カラム種別', 'inet', 'inet', 10);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'110', 'カラム種別' , 'text', 'text', 11);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'120', 'カラム種別', 'timestamp', 'timestamp', 12);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'130', 'カラム種別', 'timeuuid', 'timeuuid', 13);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'140', 'カラム種別', 'varchar', 'varchar', 14);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'150', 'カラム種別', 'varint', 'varint', 15);
INSERT  INTO  m_kbn ( type_cd, kbn, type_name, 	kbn_name, fuzui_moji_1, hyojijun)  VALUES ('column_type',	'160', 'カラム種別', 'uuid', 'uuid', 16);



INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'ASCII', 'ascii',1);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'bigint', 'bigint	', 2);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'blob', 'blob', 3);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'boolean', 'boolean', 4);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'counter', 'counter', 5);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'decimal', 'decimal', 6);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'double', 'double', 7);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'float', 'float', 8);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'int', 'int', 9);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'inet', 'inet', 10);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別' , 'text', 'text', 11);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'timestamp', 'timestamp', 12);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'timeuuid', 'timeuuid', 13);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'varchar', 'varchar', 14);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'varint', 'varint', 15);
INSERT  INTO  m_kbn ( type_cd, type_name, kbn_name, kbn, hyojijun)  VALUES ('column_type', 'カラム種別', 'uuid', 'uuid', 16);

