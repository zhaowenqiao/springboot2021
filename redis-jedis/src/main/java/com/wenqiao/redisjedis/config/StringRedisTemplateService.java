package com.wenqiao.redisjedis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class StringRedisTemplateService {

    //12个小时(43200秒)
    public static final int HALF_DAY = 43200;
    //一个小时随机数
    public static final int RAND_ONE_HOUR = new Random().nextInt(3600);

    public static final int RAND_HALF_DAY = HALF_DAY + RAND_ONE_HOUR;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public StringRedisTemplateService() {

    }

    public StringRedisTemplateService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    /**
     * redis服务订阅与发布
     */
    public String deserializeValue(byte[] bodyByte){

        return (String) stringRedisTemplate.getValueSerializer().deserialize(bodyByte);

    }

    /**
     * redis服务订阅与发布
     */
    public String deserializeString(byte[] topicByte){

        return  stringRedisTemplate.getStringSerializer().deserialize(topicByte);

    }


    /**
     * 设置 String 类型 key-value
     * @param key
     * @param value
     */
    public void set(String key,String value){

        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void convertAndSend(String channel,String message){

        stringRedisTemplate.convertAndSend(channel,message);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean exists(String key){
        return stringRedisTemplate.hasKey(key);
    }


    /**
     * 获取 String 类型 key-value
     * @param key
     * @return
     */
    public String get(String key){
        return  stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 设置 String 类型 key-value 并添加过期时间 (秒单位)
     * @param key
     * @param value
     * @param time 过期时间,秒单位
     */
    public void setForTimeS(String key,String value,long time){
        stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
     * @param key
     * @param value
     * @param time 过期时间,分钟单位
     */
    public void setForTimeMIN(String key,String value,long time){
        stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }


    /**
     * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
     * @param key
     * @param value
     * @param time 过期时间,分钟单位
     */
    public void setForTimeCustom(String key,String value,long time,TimeUnit type){
        stringRedisTemplate.opsForValue().set(key, value, time, type);
    }

    /**
     * 如果 key 存在则覆盖,并返回旧值.
     * 如果不存在,返回null 并添加
     * @param key
     * @param value
     * @return
     */
    public String getAndSet(String key,String value){
        return  stringRedisTemplate.opsForValue().getAndSet(key, value);
    }


    /**
     * 批量添加 key-value (重复的键会覆盖)
     * @param keyAndValue
     */
    public void batchSet(Map<String,String> keyAndValue){
        stringRedisTemplate.opsForValue().multiSet(keyAndValue);
    }

    /**
     * 批量添加 key-value 只有在键不存在时,才添加
     * map 中只要有一个key存在,则全部不添加
     * @param keyAndValue
     */
    public void batchSetIfAbsent(Map<String,String> keyAndValue){
        stringRedisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是长整型 ,将报错
     * @param key
     * @param number
     */
    public Long increment(String key,long number){
        return stringRedisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是 纯数字 ,将报错
     * @param key
     * @param number
     */
    public Double increment(String key,double number){
        return stringRedisTemplate.opsForValue().increment(key, number);
    }


    /**
     * 给一个指定的 key 值附加过期时间
     * @param key
     * @param time
     * @param type
     * @return
     */
    public boolean expire(String key, long time, TimeUnit type){
        return stringRedisTemplate.boundValueOps(key).expire(time, type);
    }

    /**
     * 移除指定key 的过期时间
     * @param key
     * @return
     */
    public boolean persist(String key){
        return stringRedisTemplate.boundValueOps(key).persist();
    }


    /**
     * 获取指定key 的过期时间
     * @param key
     * @return
     */
    public Long getExpire(String key){
        return stringRedisTemplate.boundValueOps(key).getExpire();
    }

    /**
     * 修改 key
     * @param key
     * @return
     */
    public void rename(String key,String newKey){
        stringRedisTemplate.boundValueOps(key).rename(newKey);
    }

    /**
     * 删除 key-value
     * @param key
     * @return
     */
    public void delete(String key){
        stringRedisTemplate.delete(key);
    }

    //hash操作

    /**
     * 添加 Hash 键值对
     * @param key
     * @param hashKey
     * @param value
     */
    public void put(String key, String hashKey, String value){
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 批量添加 hash 的 键值对
     * 有则覆盖,没有则添加
     * @param key
     * @param map
     */
    public void putAll(String key,Map<String,String> map){
        stringRedisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 添加 hash 键值对. 不存在的时候才添加
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    public boolean putIfAbsent(String key, String hashKey, String value){
        return stringRedisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }


    /**
     * 删除指定 hash 的 HashKey
     * @param key
     * @param hashKeys
     * @return 删除成功的 数量
     */
    public Long delete(String key, String ...hashKeys){
        return stringRedisTemplate.opsForHash().delete(key, hashKeys);
    }


    /**
     * 给指定 hash 的 hashkey 做增减操作
     * @param key
     * @param hashKey
     * @param number
     * @return
     */
    public Long increment(String key, String hashKey,long number){
        return stringRedisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 给指定 hash 的 hashkey 做增减操作
     * @param key
     * @param hashKey
     * @param number
     * @return
     */
    public Double increment(String key, String hashKey,Double number){
        return stringRedisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 获取指定 key 下的 hashkey
     * @param key
     * @param hashKey
     * @return
     */
    public String getHashKey(String key,String hashKey){
        return (String)stringRedisTemplate.opsForHash().get(key, hashKey);
    }


    /**
     * 获取 key 下的 所有  hashkey 和 value
     * @param key
     * @return
     */
    public Map<Object,Object> getHashEntries(String key){
        return stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * 验证指定 key 下 有没有指定的 hashkey
     * @param key
     * @param hashKey
     * @return
     */
    public boolean hashKey(String key,String hashKey){
        return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取 key 下的 所有 hashkey 字段名
     * @param key
     * @return
     */
    public Set<Object> hashKeys(String key){
        return stringRedisTemplate.opsForHash().keys(key);
    }


    /**
     * 获取指定 hash 下面的 键值对 数量
     * @param key
     * @return
     */
    public Long hashSize(String key){
        return stringRedisTemplate.opsForHash().size(key);
    }

    //List 操作

    /**
     * 指定 list 从左入栈
     * @param key
     * @return 当前队列的长度
     */
    public Long leftPush(String key, String value){
        return stringRedisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 指定 list 从左出栈
     * 如果列表没有元素,会堵塞到列表一直有元素或者超时为止
     * @param key
     * @return 出栈的值
     */
    public String leftPop(String key){
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从左边依次入栈
     * 导入顺序按照 Collection 顺序
     * 如: a b c => c b a
     * @param key
     * @param values
     * @return
     */
    public Long leftPushAll(String key,Collection<String> values){
        return stringRedisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 指定 list 从右入栈
     * @param key
     * @return 当前队列的长度
     */
    public Long rightPush(String key,String value){
        return stringRedisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 指定 list 从右出栈
     * 如果列表没有元素,会堵塞到列表一直有元素或者超时为止
     * @param key
     * @return 出栈的值
     */
    public String rightPop(String key){
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    /**
     * 从右边依次入栈
     * 导入顺序按照 Collection 顺序
     * 如: a b c => a b c
     * @param key
     * @param values
     * @return
     */
    public Long rightPushAll(String key,Collection<String> values){
        return stringRedisTemplate.opsForList().rightPushAll(key, values);
    }


    /**
     * 根据下标获取值
     * @param key
     * @param index
     * @return
     */
    public String popIndex(String key,long index){
        return stringRedisTemplate.opsForList().index(key, index);
    }


    /**
     * 获取列表指定长度
     * @param key
     * @param index
     * @return
     */
    public Long listSize(String key,long index){
        return stringRedisTemplate.opsForList().size(key);
    }


    /**
     * 获取列表 指定范围内的所有值
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> listRange(String key, long start, long end){
        return stringRedisTemplate.opsForList().range(key, start, end);
    }


    /**
     * 删除 key 中 值为 value 的 count 个数.
     * @param key
     * @param count
     * @param value
     * @return 成功删除的个数
     */
    public Long listRemove(String key,long count,String value){
        return stringRedisTemplate.opsForList().remove(key, count, value);
    }


    /**
     * 删除 列表 [start,end] 以外的所有元素
     * @param key
     * @param start
     * @param end
     */
    public void listTrim(String key,long start,long end){
        stringRedisTemplate.opsForList().trim(key, start, end);

    }

    /**
     * 将 key 右出栈,并左入栈到 key2
     *
     * @param key 右出栈的列表
     * @param key2 左入栈的列表
     * @return 操作的值
     */
    public String rightPopAndLeftPush(String key,String key2){
        return stringRedisTemplate.opsForList().rightPopAndLeftPush(key, key2);

    }

    //set 操作  无序不重复集合

    /**
     * 添加 set 元素
     * @param key
     * @param values
     * @return
     */
    public Long add(String key ,String ...values){
        return stringRedisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取两个集合的差集
     * @param key
     * @return
     */
    public Set<String> difference(String key , String otherkey){
        return stringRedisTemplate.opsForSet().difference(key, otherkey);
    }


    /**
     * 获取 key 和 集合  collections 中的 key 集合的差集
     * @param key
     * @return
     */
    public Set<String> difference(String key , Collection<String> otherKeys){
        return stringRedisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * 将  key 与 otherkey 的差集 ,添加到新的 newKey 集合中
     * @param key
     * @param otherkey
     * @param newKey
     * @return 返回差集的数量
     */
    public Long differenceAndStore(String key ,String otherkey,String newKey){
        return stringRedisTemplate.opsForSet().differenceAndStore(key, otherkey, newKey);
    }

    /**
     * 将 key 和 集合  collections 中的 key 集合的差集 添加到  newkey 集合中
     * @param key
     * @param otherKeys
     * @param newKey
     * @return 返回差集的数量
     */
    public Long differenceAndStore(String key,Collection<String> otherKeys,String newKey){
        return stringRedisTemplate.opsForSet().differenceAndStore(newKey, otherKeys, newKey);
    }

    /**
     * 删除一个或多个集合中的指定值
     * @param key
     * @param values
     * @return 成功删除数量
     */
    public Long remove(String key,Object ...values){
        return stringRedisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 随机移除一个元素,并返回出来
     * @param key
     * @return
     */
    public String randomSetPop(String key){
        return stringRedisTemplate.opsForSet().pop(key);
    }

    /**
     * 随机获取一个元素
     * @param key
     * @return
     */
    public String randomSet(String key){
        return stringRedisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 随机获取指定数量的元素,同一个元素可能会选中两次
     * @param key
     * @param count
     * @return
     */
    public List<String> randomSet(String key, long count){
        return stringRedisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取指定数量的元素,去重(同一个元素只能选择两一次)
     * @param key
     * @param count
     * @return
     */
    public Set<String> randomSetDistinct(String key, long count){
        return stringRedisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * 将 key 中的 value 转入到 destKey 中
     * @param key
     * @param value
     * @param destKey
     * @return 返回成功与否
     */
    public boolean moveSet(String key,String value,String destKey){
        return stringRedisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 无序集合的大小
     * @param key
     * @return
     */
    public Long setSize(String key){
        return stringRedisTemplate.opsForSet().size(key);
    }

    /**
     * 判断 set 集合中 是否有 value
     * @param key
     * @param value
     * @return
     */
    public boolean isMember(String key,Object value){
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 返回 key 和 othere 的并集
     * @param key
     * @param otherKey
     * @return
     */
    public Set<String> unionSet(String key,String otherKey){
        return stringRedisTemplate.opsForSet().union(key, otherKey);
    }

    /**
     * 返回 key 和 otherKeys 的并集
     * @param key
     * @param otherKeys key 的集合
     * @return
     */
    public Set<String> unionSet(String key,Collection<String> otherKeys){
        return stringRedisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * 将 key 与 otherKey 的并集,保存到 destKey 中
     * @param key
     * @param otherKey
     * @param destKey
     * @return destKey 数量
     */
    public Long unionAndStoreSet(String key, String otherKey,String destKey){
        return stringRedisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * 将 key 与 otherKey 的并集,保存到 destKey 中
     * @param key
     * @param otherKeys
     * @param destKey
     * @return destKey 数量
     */
    public Long unionAndStoreSet(String key, Collection<String> otherKeys, String destKey){
        return stringRedisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 返回集合中所有元素
     * @param key
     * @return
     */
    public Set<String> members(String key){
        return stringRedisTemplate.opsForSet().members(key);
    }

    //Zset 根据 socre 排序   不重复 每个元素附加一个 socre  double类型的属性(double 可以重复)

    /**
     * 添加 ZSet 元素
     * @param key
     * @param value
     * @param score
     */
    public boolean add(String key,String value,double score){
        return stringRedisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 批量添加 Zset <br>
     *         Set<TypedTuple<Object>> tuples = new HashSet<>();<br>
     *         TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>("zset-5",9.6);<br>
     *         tuples.add(objectTypedTuple1);
     * @param key
     * @param tuples
     * @return
     */
    public Long batchAddZset(String key,Set<ZSetOperations.TypedTuple<String>> tuples){
        return stringRedisTemplate.opsForZSet().add(key, tuples);
    }

    /**
     * Zset 删除一个或多个元素
     * @param key
     * @param values
     * @return
     */
    public Long removeZset(String key,String ...values){
        return stringRedisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 对指定的 zset 的 value 值 , socre 属性做增减操作
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Double incrementScore(String key,String value,double score){
        return stringRedisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 获取 key 中指定 value 的排名(从0开始,从小到大排序)
     * @param key
     * @param value
     * @return
     */
    public Long rank(String key,String value){
        return stringRedisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 获取 key 中指定 value 的排名(从0开始,从大到小排序)
     * @param key
     * @param value
     * @return
     */
    public Long reverseRank(String key,String value){
        return stringRedisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从小到大,带上分数)
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> rangeWithScores(String key, long start, long end){
        return stringRedisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从小到大,只有列名)
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> range(String key, long start, long end){
        return stringRedisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,只有列名)
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<String> rangeByScore(String key, double min, double max){
        return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,集合带分数)
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> rangeByScoreWithScores(String key, double min, double max){
        return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,不带分数的集合)
     * @param key
     * @param min
     * @param max
     * @param offset 从指定下标开始
     * @param count 输出指定元素数量
     * @return
     */
    public Set<String> rangeByScore(String key, double min, double max,long offset,long count){
        return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    /**
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,带分数的集合)
     * @param key
     * @param min
     * @param max
     * @param offset 从指定下标开始
     * @param count 输出指定元素数量
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> rangeByScoreWithScores(String key, double min, double max, long offset, long count){
        return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从大到小,只有列名)
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> reverseRange(String key,long start,long end){
        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从大到小,带上分数)
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScores(String key, long start, long end){
        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合不带分数)
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<String> reverseRangeByScore(String key,double min,double max){
        return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合带分数)
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> reverseRangeByScoreWithScores(String key, double min, double max){
        return stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,不带分数的集合)
     * @param key
     * @param min
     * @param max
     * @param offset 从指定下标开始
     * @param count 输出指定元素数量
     * @return
     */
    public Set<String> reverseRangeByScore(String key,double min,double max,long offset,long count){
        return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,带分数的集合)
     * @param key
     * @param min
     * @param max
     * @param offset 从指定下标开始
     * @param count 输出指定元素数量
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> reverseRangeByScoreWithScores(String key, double min, double max, long offset, long count){
        return stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * 返回指定分数区间 [min,max] 的元素个数
     * @param key
     * @param min
     * @param max
     * @return
     */
    public long countZSet(String key,double min,double max){
        return stringRedisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 返回 zset 集合数量
     * @param key
     * @return
     */
    public long sizeZset(String key){
        return stringRedisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取指定成员的 score 值
     * @param key
     * @param value
     * @return
     */
    public Double score(String key,Object value){
        return stringRedisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 删除指定索引位置的成员,其中成员分数按( 从小到大 )
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long removeRange(String key,long start ,long end){
        return stringRedisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 删除指定 分数范围 内的成员 [main,max],其中成员分数按( 从小到大 )
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long removeRangeByScore(String key,double min ,double max){
        return stringRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     *  key 和 other 两个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long unionAndStoreZset(String key,String otherKey,String destKey){
        return stringRedisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     *  key 和 otherKeys 多个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long unionAndStoreZset(String key,Collection<String> otherKeys,String destKey){
        return stringRedisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     *  key 和 otherKey 两个集合的交集,保存在 destKey 集合中
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long intersectAndStore(String key,String otherKey,String destKey){
        return stringRedisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     *  key 和 otherKeys 多个集合的交集,保存在 destKey 集合中
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long intersectAndStore(String key,Collection<String> otherKeys,String destKey){
        return stringRedisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }
}
