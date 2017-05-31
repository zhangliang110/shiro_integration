package com.meixun.b2b.mall.component.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.whalin.MemCached.MemCachedClient;

@Component
public class MemcacheManagerImpl implements MemcacheManager {

	@Autowired
	private MemCachedClient memcachedClient;

	@Override
	public void createCache(String name, Cache<Object, Object> cache) throws CacheException {
		try {
			memcachedClient.add(name,cache);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Cache<Object, Object> getCache(String name) throws CacheException {
		try {
			return (Cache<Object, Object>) memcachedClient.get(name);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public void removeCache(String name) throws CacheException {
		try {
			memcachedClient.delete(name);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public void updateCahce(String name, Cache<Object, Object> cache) throws CacheException {
		try {
			memcachedClient.replace(name, cache);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public void destroy() throws CacheException {
		try {
			memcachedClient.flushAll();
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}
}
