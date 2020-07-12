drop table t_keyspace_manager;

create table t_keyspace_manager (
  user_id VARCHAR(20) default '' not null
  , keyspace VARCHAR(20) default '' not null
  , unique t_keyspace_manager_index (user_id, keyspace)
) ;