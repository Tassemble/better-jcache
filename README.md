# better-jcache

better-jcache is java cache framework bases on memcached that can easy write/read cache with flexible ways, such as annotation. The main idea is to write java code more fast, more convenient, more productivity.

**features:**
* namespace is supported based on memcached.
* annotation is supported for cache or invalidate cache object by using AssignedCache or InvalidateAssignedCache separately.
* namepsace can be assigned in the runtime using @ValueAsPartialNamespace or @PropertyAsPartialNamespace.
* the consist of the key is flexible to assigned , you can specific one variable、multi variables、several properties from object or any of these three together.


Usage
---

In a Maven project include the dependency:
```
<dependency>
  <groupId>com.netease</groupId>
  <artifactId>better-jcache</artifactId>
  <version>(insert latest version)</version>
</dependency>
```
or Gradle example:
```
compile 'com.netease:better-jcache:0.1.+'
```


Then, you should set up your memcached environment. For example, you deploy it in your local machine.

You can just add the follow configs to your spring application context( for example applicationContext.xml).

```
<bean id="memcachedClient" class="com.netease.edu.cache.impl.KeyPrefixSupportedMemcachedClientFactory">
        <property name="servers" value="127.0.0.1:11211" />
        <property name="protocol" value="BINARY" />
        <property name="transcoder">
                <bean class="net.spy.memcached.transcoders.SerializingTranscoder">
                        <property name="compressionThreshold" value="16384" />
                </bean>
        </property>
        <property name="maxReconnectDelay" value="60" />
        <property name="opTimeout" value="10000" />
        <property name="timeoutExceptionThreshold" value="900" />
        <property name="hashAlg">
           <value type="net.spy.memcached.DefaultHashAlgorithm">KETAMA_HASH</value>
        </property>
        <property name="locatorType" value="CONSISTENT" />
        <property name="failureMode" value="Redistribute" />
        <property name="useNagleAlgorithm" value="false" />
</bean>
<bean class="org.netease.cache.aop.advice.CachedWithAssignedKeyAdvice" >
	<property name="memcachedClient" ref="memcachedClient" />
</bean>
<bean class="org.netease.cache.aop.advice.InvalidateCacheAfterUpdateAdvice" >
	<property name="memcachedClient" ref="memcachedClient" />
</bean>
```

after these configures, you can use @AssignedCache and @InvalidateAssignedCache in your public methods.

For example:
```
@AssignedCache(namespace = "common-kv")
public String obtain(@ValueAsCacheKey String name) {
	return commonLogic.obtain(name);
}
```
this means when the obtain method is called, the return value will be cached in memcached. Next time, the method is called again with the same name,  a value will get from memcached .

```
@InvalidateAssignedCache(namespace = "common-kv")
public String set(String name) {
	return commonLogic.set(name);
}
```
this means when the set method is called, all the cached value using namespace common-kv will invalidate.

there are more advance features you can try, like  annotation @ValueAsPartialNamespace @PropertyAsPartialNamespace, these two can get value from parameter, and treat them as part of namespace in the runtime. Just try it.

_And don't forget to change the property(servers) in the product environment._


FAQ
---

**Q: Why does annotation  not work?**

A: You may invoke method in self class. This will lead annotation fail to work, because it is base on dynamic proxy.





License
---

```
Copyright (C) 2015 NETEASE.CORP

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
