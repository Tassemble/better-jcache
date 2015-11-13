package org.betterbench.cache.aop;

public interface CacheConstant {
	public static final String SEPARATOR = "/";
	
	public static final String expiraton = "expiration";
	public static final String namespace = "namespace";
	
	
	//namespace verson key
	public static final String NAMESPACE_VERSION_KEY = "-ns_ver_key-";
	public static final String assignedKey = "assignedKey";
	
	public static final int DEFAULT_CACHE_TIME = 24 * 3600; 
	public static final int MAX_KEY_CACHE_TIME = 24 * 3600 * 10;

	public static final String INIT_VERSION_NUM = new String("0"); 
	
	
	
	//undefine_namespace
	public static final String UNDEFINE_NAMESPACE = "ud_ns";
	
	public static final String UNDEFINE_ASSIGNED_KEY = "ud_key";
	
}
