# jdbc_connection_pool_collection
1）compare_jcp
1.第一步对比两种连接池的速度,发现确实20个线程下只比查询Hikari连接池要快一点
2.查询与插入要混合进行进行测试，（待完成）