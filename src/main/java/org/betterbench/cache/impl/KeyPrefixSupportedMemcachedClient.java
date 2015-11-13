package org.betterbench.cache.impl;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.GetFuture;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.transcoders.Transcoder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class KeyPrefixSupportedMemcachedClient extends MemcachedClient {

    public KeyPrefixSupportedMemcachedClient(ConnectionFactory cf,
                                             List<InetSocketAddress> addrs) throws IOException {
        super(cf, addrs);
    }

    private MemcachedClient delegator;
    private String namespace;

    private void init() {
        if (delegator == null) {
            throw new RuntimeException("delegator is null,check MemcachedClient");
        }
        if (namespace == null) {
            throw new RuntimeException("null namespace is not supported");
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public MemcachedClient getDelegator() {
        return delegator;
    }

    public void setDelegator(MemcachedClient delegator) {
        this.delegator = delegator;
    }

    public Object get(String key) {
        return delegator.get(namespace + key);
    }

    public OperationFuture<Boolean> set(String key, int exp, Object o) {
        return delegator.set(namespace + key, exp, o);
    }

    public OperationFuture<Boolean> delete(String key) {
        return delegator.delete(namespace + key);
    }

    public GetFuture<Object> asyncGet(final String key) {
        return delegator.asyncGet(namespace + key);
    }

    public long incr(String key, long by) {
        return delegator.incr(namespace + key, by);
    }

    public long incr(String key, int by) {
        return delegator.incr(namespace + key, by);
    }

    public long incr(String key, int by, long def) {
        return delegator.incr(namespace + key, by, def);
    }

    public long incr(String key, long by, long def, int exp) {
        return delegator.incr(namespace + key, by, def, exp);
    }

    public long incr(String key, int by, long def, int exp) {
        return delegator.incr(namespace + key, by, def, exp);
    }

    public long incr(String key, long by, long def) {
        return delegator.incr(namespace + key, by, def);
    }

    public <T> OperationFuture<Boolean> add(String key, int exp, T o,
                                            Transcoder<T> tc) {
        return delegator.add(namespace + key, exp, o, tc);
    }

    public OperationFuture<Boolean> add(String key, int exp, Object o) {
        return delegator.add(namespace + key, exp, o);
    }
}
