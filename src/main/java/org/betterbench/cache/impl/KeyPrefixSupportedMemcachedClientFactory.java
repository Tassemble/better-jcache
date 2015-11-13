package org.betterbench.cache.impl;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import org.slf4j.Logger;

public class KeyPrefixSupportedMemcachedClientFactory extends MemcachedClientFactoryBean {
    Logger logger = org.slf4j.LoggerFactory.getLogger(KeyPrefixSupportedMemcachedClientFactory.class);
    private final ConnectionFactoryBuilder connectionFactoryBuilder =
            new ConnectionFactoryBuilder();
    private String servers;
    private String namespace;
    static MemcachedClient memcachedClient;

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    private void init() {
        synchronized (KeyPrefixSupportedMemcachedClientFactory.class) {
            if (memcachedClient == null) {
                try {
                    logger.info("init memcachedClient with servers:" + servers);
                    super.setServers(servers);
                    memcachedClient = (MemcachedClient) super.getObject();
                } catch (Exception e) {
                    logger.error("init memcachedClient failed, detail:" + e.getMessage(), e);
                }
            }
        }
        if (memcachedClient == null) {
            throw new RuntimeException("delegator is null,check MemcachedClient");
        }
        if (namespace == null) {
            throw new RuntimeException("null namespace is not supported");
        }
    }

    @Override
    public Object getObject() throws Exception {
        init();
        KeyPrefixSupportedMemcachedClient instance = new KeyPrefixSupportedMemcachedClient(connectionFactoryBuilder.build(),
                AddrUtil.getAddresses(servers));
        instance.setNamespace(namespace);
        instance.setDelegator(memcachedClient);
        instance.shutdown();//instance中的连接池资源可以释放掉，实际工作的是delegator
        return instance;
    }

}
