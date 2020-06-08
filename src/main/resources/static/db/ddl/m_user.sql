CREATE DATABASE dbaas;
USE dbaas;

使用中データベース確認
SELECT database();

データベース指定でのアクセス
mysql -u root dbaas -h 52.192.7.169 -p


drop table m_user;

create table m_user (
  user_id VARCHAR(20) default '' not null PRIMARY KEY
  , user_name VARCHAR(12) default '' not null
  , password VARCHAR(80) default '' not null
) ;


$2a$08$V36opBTB3.OYPrxWLo4Xsu1ijXG3d3kPUVeT.BjQqo1guUMLL7U7y