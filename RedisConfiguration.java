package org.apache.dubbo.admin.registry.config.impl;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.dubbo.admin.common.util.Constants;
import org.apache.dubbo.admin.registry.config.GovernanceConfiguration;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.RemotingConstants;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.ArrayUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.RpcException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.dubbo.common.constants.CommonConstants.*;
import static org.apache.dubbo.registry.Constants.*;
import static org.apache.dubbo.registry.Constants.DEFAULT_SESSION_TIMEOUT;

/**
 * @program: dubbo-admin
 * @description:
 * @author: LiZhaofu
 * @create: 2020-05-08 11:03
 **/

public class RedisConfiguration implements GovernanceConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

    private static final int DEFAULT_REDIS_PORT = 6379;

    private final static String DEFAULT_ROOT = "dubbo";

    private static final String REDIS_MASTER_NAME_KEY = "master-name";

    private final Map<String, Pool<Jedis>> jedisPools = new ConcurrentHashMap<>();

    private int expirePeriod;
    private URL url;
    private String root;
    private boolean replicate;

    @Override
    public void init() {
        if (url == null) {
            throw new IllegalStateException("server url is null, cannot init");
        }
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setTestOnBorrow(url.getParameter("test.on.borrow", true));
        config.setTestOnReturn(url.getParameter("test.on.return", false));
        config.setTestWhileIdle(url.getParameter("test.while.idle", false));
        if (url.getParameter("max.idle", 0) > 0) {
            config.setMaxIdle(url.getParameter("max.idle", 0));
        }
        if (url.getParameter("min.idle", 0) > 0) {
            config.setMinIdle(url.getParameter("min.idle", 0));
        }
        if (url.getParameter("max.active", 0) > 0) {
            config.setMaxTotal(url.getParameter("max.active", 0));
        }
        if (url.getParameter("max.total", 0) > 0) {
            config.setMaxTotal(url.getParameter("max.total", 0));
        }
        if (url.getParameter("max.wait", url.getParameter("timeout", 0)) > 0) {
            config.setMaxWaitMillis(url.getParameter("max.wait", url.getParameter("timeout", 0)));
        }
        if (url.getParameter("num.tests.per.eviction.run", 0) > 0) {
            config.setNumTestsPerEvictionRun(url.getParameter("num.tests.per.eviction.run", 0));
        }
        if (url.getParameter("time.between.eviction.runs.millis", 0) > 0) {
            config.setTimeBetweenEvictionRunsMillis(url.getParameter("time.between.eviction.runs.millis", 0));
        }
        if (url.getParameter("min.evictable.idle.time.millis", 0) > 0) {
            config.setMinEvictableIdleTimeMillis(url.getParameter("min.evictable.idle.time.millis", 0));
        }

        String cluster = url.getParameter("cluster", "failover");
        if (!"failover".equals(cluster) && !"replicate".equals(cluster)) {
            throw new IllegalArgumentException("Unsupported redis cluster: " + cluster + ". The redis cluster only supported failover or replicate.");
        }
        replicate = "replicate".equals(cluster);

        List<String> addresses = new ArrayList<>();
        addresses.add(url.getAddress());
        String[] backups = url.getParameter(RemotingConstants.BACKUP_KEY, new String[0]);
        if (ArrayUtils.isNotEmpty(backups)) {
            addresses.addAll(Arrays.asList(backups));
        }
        //获得Redis主节点名称
        String masterName = url.getParameter(REDIS_MASTER_NAME_KEY);
        if (StringUtils.isEmpty(masterName)) {
            //单机版redis
            for (String address : addresses) {
                int i = address.indexOf(':');
                String host;
                int port;
                if (i > 0) {
                    host = address.substring(0, i);
                    port = Integer.parseInt(address.substring(i + 1));
                } else {
                    host = address;
                    port = DEFAULT_REDIS_PORT;
                }
                this.jedisPools.put(address, new JedisPool(config, host, port,
                        url.getParameter(TIMEOUT_KEY, DEFAULT_TIMEOUT), StringUtils.isEmpty(url.getPassword()) ? null : url.getPassword(),
                        url.getParameter("db.index", 0)));
            }
        } else {
            //哨兵版redis
            Set<String> sentinelSet = new HashSet<>(addresses);
            int index = url.getParameter("db.index", 0);
            int timeout = url.getParameter(TIMEOUT_KEY, DEFAULT_TIMEOUT);
            String password = StringUtils.isEmpty(url.getPassword()) ? null : url.getPassword();
            JedisSentinelPool pool = new JedisSentinelPool(masterName, sentinelSet, config, timeout, password, index);
            this.jedisPools.put(masterName, pool);
        }

//        this.reconnectPeriod = url.getParameter(REGISTRY_RECONNECT_PERIOD_KEY, DEFAULT_REGISTRY_RECONNECT_PERIOD);
        String group = url.getParameter(GROUP_KEY, DEFAULT_ROOT);
        if (!group.startsWith(PATH_SEPARATOR)) {
            group = PATH_SEPARATOR + group;
        }
        if (!group.endsWith(PATH_SEPARATOR)) {
            group = group + PATH_SEPARATOR;
        }
        this.root = group;

        this.expirePeriod = url.getParameter(SESSION_TIMEOUT_KEY, DEFAULT_SESSION_TIMEOUT);
    }

    @Override
    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public String setConfig(String key, String value) {
        return setConfig(null, key, value);
    }

    @Override
    public String getConfig(String key) {
        return getConfig(null, key);
    }

    @Override
    public boolean deleteConfig(String key) {
        return deleteConfig(null, key);
    }

    @Override
    public String setConfig(String group, String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("key or value cannot be null");
        }

        String expire = String.valueOf(System.currentTimeMillis() + expirePeriod);
        boolean success = false;
        RpcException exception = null;
        for (Map.Entry<String, Pool<Jedis>> entry : jedisPools.entrySet()) {
            Pool<Jedis> jedisPool = entry.getValue();
            try {
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.hset(key, value, expire);
                    jedis.publish(key, REGISTER);
                    success = true;
                    if (!replicate) {
                        break; //  If the server side has synchronized data, just write a single machine
                    }

                    return value;
                }
            } catch (Throwable t) {
                exception = new RpcException("Failed to register service to redis registry. registry: " + entry.getKey() + ", service: " + url + ", cause: " + t.getMessage(), t);
            }
        }
        if (exception != null) {
            if (success) {
                logger.warn(exception.getMessage(), exception);
            } else {
                throw exception;
            }
        }

        return null;
    }

    @Override
    public String getConfig(String group, String key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }

        String path = getNodePath(key, group);

        String value="";
        boolean success = false;
        RpcException exception = null;
        for (Map.Entry<String, Pool<Jedis>> entry : jedisPools.entrySet()) {
            Pool<Jedis> jedisPool = entry.getValue();
            try {
                try (Jedis jedis = jedisPool.getResource()) {
                    value = jedis.hget(key, path);
                    jedis.publish(key, REGISTER);
                    success = true;
                    if (!replicate) {
                        break; //  If the server side has synchronized data, just write a single machine
                    }

                    return value;
                }
            } catch (Throwable t) {
                exception = new RpcException("Failed to register service to redis registry. registry: " + entry.getKey() + ", service: " + url + ", cause: " + t.getMessage(), t);
            }
        }
        if (exception != null) {
            if (success) {
                logger.warn(exception.getMessage(), exception);
            } else {
                throw exception;
            }
        }
        return null;
    }

    @Override
    public boolean deleteConfig(String group, String key) {

        String path = getNodePath(key, group);

        RpcException exception = null;
        boolean success = false;
        for (Map.Entry<String, Pool<Jedis>> entry : jedisPools.entrySet()) {
            Pool<Jedis> jedisPool = entry.getValue();
            try {
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.hdel(key, path);
                    jedis.publish(key, UNREGISTER);
                    success = true;
                    if (!replicate) {
                        break; //  If the server side has synchronized data, just write a single machine
                    }
                }
            } catch (Throwable t) {
                exception = new RpcException("Failed to unregister service to redis registry. registry: " + entry.getKey() + ", service: " + url + ", cause: " + t.getMessage(), t);
            }
        }
        if (exception != null) {
            if (success) {
                logger.warn(exception.getMessage(), exception);
            } else {
                throw exception;
            }
        }

        return true;
    }

    @Override
    public String getPath(String key) {
        return getNodePath(key, null);
    }

    @Override
    public String getPath(String group, String key) {
        return getNodePath(key, group);
    }

    private String getNodePath(String path, String group) {
        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }
        return toRootDir(group) + path;
    }

    private String toRootDir(String group) {
        if (group != null) {
            if (!group.startsWith(Constants.PATH_SEPARATOR)) {
                root = Constants.PATH_SEPARATOR + group;
            } else {
                root = group;
            }
        }
        if (root.equals(Constants.PATH_SEPARATOR)) {
            return root;
        }
        return root + Constants.PATH_SEPARATOR;
    }
}
