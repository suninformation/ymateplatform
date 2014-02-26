/*
 * Copyright 2007-2107 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.platform.persistence.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.persistence.base.ConnectionException;
import net.ymate.platform.persistence.mongodb.support.DefaultMongoClientHolder;
import net.ymate.platform.persistence.mongodb.support.DefaultMongoQuery;
import net.ymate.platform.persistence.mongodb.support.DefaultMongoResultSetHandler;
import net.ymate.platform.persistence.mongodb.support.DefaultMongoSession;
import net.ymate.platform.persistence.support.DataSourceCfgMeta;
import net.ymate.platform.persistence.support.RepositoryBeanFactory;

import org.apache.commons.lang.StringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

/**
 * <p>
 * MongoDB
 * </p>
 * <p>
 * 
 * </p>
 * 
 * @author 刘镇(suninformation@163.com)
 * @version 0.0.0
 *          <table style="border:1px solid gray;">
 *          <tr>
 *          <th width="100px">版本号</th><th width="100px">动作</th><th
 *          width="100px">修改人</th><th width="100px">修改时间</th>
 *          </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *          <tr>
 *          <td>0.0.0</td>
 *          <td>创建类</td>
 *          <td>刘镇</td>
 *          <td>2014年1月27日下午3:54:34</td>
 *          </tr>
 *          </table>
 */
public class MongoDB {

	public static final String DEFAULT_MONGODB_HOST = "localhost";

	public static final int DEFAULT_MONGODB_PORT = 27017;

	public static String DATASOURCE_DEFAULT_NAME;

	public static String COLLECTION_PREFIX;

	public static boolean isInited;

	protected static IMongoConfig __config;

	protected static Map<String, MongoClient> __DATASOURCE_CACHE = new ConcurrentHashMap<String, MongoClient>();

	/**
	 * 数据源名称与配置对象映射
	 */
	protected static Map<String, DataSourceCfgMeta> __DATASOURCE_CFG_METAS = new HashMap<String, DataSourceCfgMeta>();

	protected static RepositoryBeanFactory __REPOSTORY_BEAN_FACTORY;

	/**
	 * 初始化MongoDB框架管理器
	 * 
	 * @param config
	 */
	public static void initialize(IMongoConfig config) throws ConnectionException {
		if (!isInited) {
			__config = config;
			DATASOURCE_DEFAULT_NAME = StringUtils.defaultIfEmpty(config.getDefaultDataSourceName(), "default");
			COLLECTION_PREFIX = StringUtils.trimToEmpty(config.getCollectionPrefix());
			//
			try {
				for (DataSourceCfgMeta _cfgMeta : config.getDataSourceCfgMetas()) {
					List<ServerAddress> _addrs = new ArrayList<ServerAddress>();
					String[] _hosts = StringUtils.split(_cfgMeta.getConnectionUrl(), "|");
					if (_hosts == null || _hosts.length == 0) {
						_addrs.add(new ServerAddress(DEFAULT_MONGODB_HOST, DEFAULT_MONGODB_PORT));
					} else {
						for (String _host : _hosts) {
							String[] _tmpAddr = StringUtils.split(_host, ":");
							if (_tmpAddr.length == 1) {
								_addrs.add(new ServerAddress(_tmpAddr[0], DEFAULT_MONGODB_PORT));
							} else {
								_addrs.add(new ServerAddress(_tmpAddr[0], Integer.valueOf(_tmpAddr[1])));
							}
						}
					}
					//
					MongoClientOptions.Builder _builder = new MongoClientOptions.Builder();
					// TODO 分析Meta参数并赋值
					_builder.connectionsPerHost(200);
					MongoClient _client = new MongoClient(_addrs, _builder.build());
					// TODO 读写分离相关配置
					// _client.setReadPreference(...);
					//
					__DATASOURCE_CACHE.put(_cfgMeta.getName(), _client);
					__DATASOURCE_CFG_METAS.put(_cfgMeta.getName(), _cfgMeta);
				}
				//
				__REPOSTORY_BEAN_FACTORY = new RepositoryBeanFactory(config.getRepositoryPackages());
				isInited = true;
			} catch (Exception e) {
				throw new ConnectionException(RuntimeUtils.unwrapThrow(e));
			}
		}
	}

	/**
	 * @return 获取MongoDB持久化框架初始化配置对象
	 */
	public static IMongoConfig getConfig() {
		return __config;
	}

	/**
	 * 销毁MongoDB连接管理器
	 */
	public static void destroy() {
		if (isInited) {
			for (MongoClient _client : __DATASOURCE_CACHE.values()) {
				_client.close();
			}
			__DATASOURCE_CACHE.clear();
			__DATASOURCE_CFG_METAS.clear();
			__REPOSTORY_BEAN_FACTORY = null;
			isInited = false;
		}
	}

	/**
	 * @param clazz 存储器接口类
	 * @return 返回存储器实例对象，若不存在则返回null
	 */
	public static <T> T getBean(Class<T> clazz) {
		return __REPOSTORY_BEAN_FACTORY.get(clazz);
	}

	/**
	 * @param clazz 存储器实例对象
	 * @return 注册一个存储器实例对象，并返回注册是否成功
	 */
	public static boolean registerBean(Class<?> clazz) {
		return __REPOSTORY_BEAN_FACTORY.add(clazz) != null;
	}

	/**
	 * @param dbName 数据库名称
	 * @return 采用默认数据源构建会话对象
	 * @throws ConnectionException
	 */
	public static IMongoSession openSession(String dbName) throws ConnectionException {
		return new DefaultMongoSession(getMongoClientHolder(dbName));
	}

	/**
	 * @param dsName 数据源名称
	 * @param dbName 数据库名称
	 * @return 采用由dsName指定的数据源构建会话对象
	 * @throws ConnectionException
	 */
	public static IMongoSession openSession(String dsName, String dbName) throws ConnectionException {
		return new DefaultMongoSession(getMongoClientHolder(dsName, dbName));
	}

	/**
	 * @param holder
	 * @return
	 * @throws ConnectionException
	 */
	public static IMongoSession openSession(IMongoClientHolder holder) throws ConnectionException {
		return new DefaultMongoSession(holder);
	}

	/**
	 * @param dbName 数据库名称
	 * @param collectionName 集合名称
	 * @return 创建并返回默认MongoDB复杂条件查询器
	 * @throws ConnectionException
	 */
	public static IMongoQuery<DBObject> createQuery(String dbName, String collectionName) throws ConnectionException {
		return new DefaultMongoQuery<DBObject>(getMongoClientHolder(dbName), new DefaultMongoResultSetHandler(), collectionName);
	}

	public static <T> IMongoQuery<T> createQuery(String dbName, String collectionName, IMongoResultSetHandler<T> handler) throws ConnectionException {
		return new DefaultMongoQuery<T>(getMongoClientHolder(dbName), handler, collectionName);
	}

	public static IMongoQuery<DBObject> createQuery(IMongoClientHolder holder, String collectionName) throws ConnectionException {
		return new DefaultMongoQuery<DBObject>(holder, new DefaultMongoResultSetHandler(), collectionName);
	}

	public static <T> IMongoQuery<T> createQuery(IMongoClientHolder holder, String collectionName, IMongoResultSetHandler<T> handler) throws ConnectionException {
		return new DefaultMongoQuery<T>(holder, handler, collectionName);
	}

	public static IMongoQuery<DBObject> createQuery(String dsName, String dbName, String collectionName) throws ConnectionException {
		return new DefaultMongoQuery<DBObject>(getMongoClientHolder(dsName, dbName), new DefaultMongoResultSetHandler(), collectionName);
	}

	public static <T> IMongoQuery<T> createQuery(String dsName, String dbName, String collectionName, IMongoResultSetHandler<T> handler) throws ConnectionException {
		return new DefaultMongoQuery<T>(getMongoClientHolder(dsName, dbName), handler, collectionName);
	}

	/**
	 * @param dbName 数据库名称
	 * @return 获取默认数据源连接持有者对象
	 * @throws ConnectionException
	 */
	public static IMongoClientHolder getMongoClientHolder(String dbName) throws ConnectionException {
		return getMongoClientHolder(DATASOURCE_DEFAULT_NAME, dbName);
	}

	/**
	 * @param dsName 数据源名称
	 * @param dbName 数据库名称
	 * @return 获取指定数据源的连接持有者对象
	 * @throws ConnectionException
	 */
	public static IMongoClientHolder getMongoClientHolder(String dsName, String dbName) throws ConnectionException {
		try {
			DB _db = __DATASOURCE_CACHE.get(dsName).getDB(dbName);
			if (_db.isAuthenticated()) {
				DataSourceCfgMeta _meta = __DATASOURCE_CFG_METAS.get(dsName);
				_db.authenticate(_meta.getUserName(), _meta.getPassword().toCharArray());
			}
			return new DefaultMongoClientHolder(dsName, _db);
		} catch (Exception e) {
			throw new ConnectionException(RuntimeUtils.unwrapThrow(e));
		}
	}

	/**
	 * 关闭数据源的连接持有者
	 * 
	 * @param conn 数据源的连接持有者对象
	 * @throws ConnectionException
	 */
	public static void release(IMongoClientHolder conn) throws ConnectionException {
		if (conn != null) {
			conn.release();
			conn = null;
		}
	}

	/**
	 * <p>
	 * OPT
	 * </p>
	 * <p>
	 * MongoDB操作符常量
	 * </p>
	 * 
	 * @author 刘镇(suninformation@163.com)
	 * @version 0.0.0
	 *          <table style="border:1px solid gray;">
	 *          <tr>
	 *          <th width="100px">版本号</th><th width="100px">动作</th><th
	 *          width="100px">修改人</th><th width="100px">修改时间</th>
	 *          </tr>
	 *          <!-- 以 Table 方式书写修改历史 -->
	 *          <tr>
	 *          <td>0.0.0</td>
	 *          <td>创建类</td>
	 *          <td>刘镇</td>
	 *          <td>2014年2月6日下午5:50:26</td>
	 *          </tr>
	 *          </table>
	 */
	public class OPT {
	    
	    // 主键
	    public static final String ID = "_id";
	    
	    // 查询条件
	    public static final String GT = "$gt";
	    public static final String GTE = "$gte";
	    public static final String LT = "$lt";
	    public static final String LTE = "$lte";
	    public static final String NE = "$ne";
	    public static final String IN = "$in";
	    public static final String NIN = "$nin";
	    public static final String MOD = "$mod";
	    public static final String ALL = "$all";
	    public static final String SLICE = "$slice";
	    public static final String SIZE = "$size";
	    public static final String EXISTS = "$exists";
	    public static final String WHERE = "$where";
	    
	    //
	    public static final String AND = "$and";
	    public static final String OR = "$or";
	    
	    //
	    public static final String NEAR = "$near";
	    public static final String WITHIN = "$within";
	    public static final String CENTER = "$center";
	    public static final String BOX = "$box";
	    
	    //
	    public static final String SET = "$set";
	    public static final String UNSET = "$unset";
	    public static final String INC = "$inc";
	    public static final String MUL = "$mul";
	    public static final String PUSH = "$push";
	    public static final String PULL = "$pull";
	    public static final String EACH = "$each";
	    public static final String POP = "$pop";
	    public static final String MIN = "$min";
	    public static final String MAX = "$max";
	    public static final String BIT = "$bit";
	    
	    // 聚合类型
	    public static final String PROJECT = "$project";
	    public static final String MATCH = "$match";
	    public static final String LIMIT = "$limit";
	    public static final String SKIP = "$skip";
	    public static final String UNWIND = "$unwind";
	    public static final String GROUP = "$group";
	    public static final String SORT = "$sort";
	    
	}

	public static class OrderBy {
		private DBObject __orderBy;

		private OrderBy() {
			this.__orderBy = new BasicDBObject();
		}

		public static OrderBy create() {
			return new OrderBy();
		}

		public OrderBy desc(String key) {
			this.__orderBy.put(key, -1);
			return this;
		}

		public OrderBy asc(String key) {
			this.__orderBy.put(key, 1);
			return this;
		}

		public DBObject toDBObject() {
			return this.__orderBy;
		}
	}

}
