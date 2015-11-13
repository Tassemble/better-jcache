# better-jcache

better-jcache is flexible java cache framework bases on memcached that can easy write/read cache with flexible ways, such as annotation. The main idea is to write java code more fast, more convenient, more productivity.

**features:**
* namespace is supported based on memcached.
* annotation is supported for cache or invalidate cache object.
* namepsace can be assigned in the runtime using @ValueAsPartialNamespace or @PropertyAsPartialNamespace
* the consist of the key is flexible to assigned , you can specific one variable、multi variables、several properties from object or any of these three together.


Usage
---

coming soon.


Download
---

coming soon.



FAQ
---

**Q: Why does annotation  not work?**

A: You may invoke method in self class. This will lead annotation fail to work, because it is base on dynamic proxy.



License
---

```
Copyright 2013 Netease Corp.

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
