package com.omg.framework.common.utils.cache;

public class LruCacheFactory extends AbstractCacheFactory {
    private LruCacheFactory() {
    }

    protected Cache createCache(int maxSize) {
        return new LruCache(maxSize);
    }

    public static CacheFactory getInstance() {
        return LruCacheFactory.LruCacheFactoryHolder.instance;
    }

    static class LruCacheFactoryHolder {
        static final CacheFactory instance = new LruCacheFactory();

        LruCacheFactoryHolder() {
        }
    }
}
