drop table t_keyspace_regist_plan;

create table t_keyspace_regist_plan (
  user_id VARCHAR(20) default '' not null
  , keyspace VARCHAR(20) default '' not null
  , unique t_keyspace_regist_plan_index (user_id, keyspace)
) ;