package com.zl.util;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
获取memcache工具方法
/
public class MemcachedUtils {

	private static MemCachedClient  memCachedClient;

	static {
		
		//获取服务器地址
		String[] servers = ReadConfigUtils.getInstance().getProperty("memcached.servers").split(",");
		
		memCachedClient = new MemCachedClient();
		
		// 设置服务器权重
        //Integer[] weights = {3, 2};

        // 创建一个Socked连接池实例
        SockIOPool pool = SockIOPool.getInstance();
        
        // 向连接池设置服务器和权重
        pool.setServers(servers);
        //pool.setWeights(weights);

        // set some TCP settings
        // disable nagle
        // set the read timeout to 3 secs
        // and don't set a connect timeout
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setSocketConnectTO(0);
        
        // initialize the connection pool
        pool.initialize();
	}
	

	public static synchronized MemCachedClient getInstance() {
		return memCachedClient;
	}
}
