package org.betterbench.cache.aop.dto;

public class AnnotationDataDto {

    private String  namespace       = "";
    private int     expiration      = 0;
    private String  assignedKey     = "";

    private String  key;

    /**
     * 是否使用共享的cache空间，默认使用产品独立的空间
     */
    private boolean shareCacheSpace = false;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public String getAssignedKey() {
        return assignedKey;
    }

    public void setAssignedKey(String assignedKey) {
        this.assignedKey = assignedKey;
    }

    public boolean isShareCacheSpace() {
        return shareCacheSpace;
    }

    public void setShareCacheSpace(boolean shareCacheSpace) {
        this.shareCacheSpace = shareCacheSpace;
    }

}
