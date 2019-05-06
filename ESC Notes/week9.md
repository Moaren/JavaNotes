# Concurrency: Performance
## Option: use Synchronized Collections
- Vector，Hashtable,Collections.synchronizedXxx
- Problem: list过大或者处理过于复杂不高效（vector被认为是一种过时的数据结构）
## Concurrent Collections
- Java 5.0 improves on the synchronized collection by providing several concurrent collection classes.
- Replacing synchronized collections with concurrent collections can offer dramatic scalability improvement with little risk.
### ConcurrentHashMap
- 重要:concurrentHashMap不能作为client lock
### CopyOnWriteArrayList
- It is a concurrent replacement for a synchronized list that offers better concurrency in some common situations.
- A new copy of the collection is created and published every time it is modified. 
- All write operations are protected by the same lock and read operations are not protected.
