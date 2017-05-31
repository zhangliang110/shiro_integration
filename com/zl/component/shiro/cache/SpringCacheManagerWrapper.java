package com.meixun.b2b.mall.component.shiro.cache;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.CollectionUtils;

/**
 * 包装SimpleMapCache cache抽象
 * <p>Version: 1.0
 */
public class SpringCacheManagerWrapper implements CacheManager {

	
	private MemcacheManager cacheManager;
    

    /**
     * 设置memcacheMananger
     *
     * @param cacheManager
     */
    public void setCacheManager(MemcacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Cache<Object, Object> getCache(String name) throws CacheException {
       Cache<Object, Object> cache = cacheManager.getCache(name);
       if (cache == null) {
    	    cacheManager.createCache(name, new SimpleMapCache(name, new HashMap<>()));
    	    cache = cacheManager.getCache(name);
       }
       return cache;
    }

    @SuppressWarnings("serial")  
    static class SimpleMapCache implements Cache<Object, Object>, Serializable {  
      
        private final Map<Object, Object> attributes;  
        private final String name;  
      
        public SimpleMapCache(String name, Map<Object, Object> backingMap) {  
            if (name == null)  
                throw new IllegalArgumentException("Cache name cannot be null.");  
            if (backingMap == null) {  
                throw new IllegalArgumentException("Backing map cannot be null.");  
            } else {  
                this.name = name;  
                attributes = backingMap;  
            }  
        }  
      
        public Object get(Object key) throws CacheException {  
            return attributes.get(key);  
        }  
      
        public Object put(Object key, Object value) throws CacheException {  
            return attributes.put(key, value);  
        }  
      
        public Object remove(Object key) throws CacheException {  
            return attributes.remove(key);  
        }  
      
        public void clear() throws CacheException {  
            attributes.clear();  
        }  
      
        public int size() {  
            return attributes.size();  
        }  
      
        public Set<Object> keys() {  
            Set<Object> keys = attributes.keySet();  
            if (!keys.isEmpty())  
                return Collections.unmodifiableSet(keys);  
            else  
                return Collections.emptySet();  
        }  
      
        public Collection<Object> values() {  
            Collection<Object> values = attributes.values();  
            if (!CollectionUtils.isEmpty(values))  
                return Collections.unmodifiableCollection(values);  
            else  
                return Collections.emptySet();  
        }  
      
        public String toString() {  
            return (new StringBuilder("SimpleMapCache '")).append(name).append("' (").append(attributes.size()).append(  
                    " entries)").toString();  
        }  
      
    }  
}
