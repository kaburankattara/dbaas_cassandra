CREATE DATABASE dbaas;
USE dbaas;

使用中データベース確認
SELECT database();

データベース指定でのアクセス
mysql -u root dbaas -h 52.192.7.169 -p


drop table server_by_user;

create table server_by_user (
  user_id VARCHAR(20) default '' not null
  , server_name VARCHAR(20) default '' not null
  , unique server_by_user_index (user_id, server_name)
) ;


$2a$08$V36opBTB3.OYPrxWLo4Xsu1ijXG3d3kPUVeT.BjQqo1guUMLL7U7y