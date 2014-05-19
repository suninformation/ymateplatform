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
package net.ymate.platform.persistence.jdbc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.persistence.base.ConnectionException;
import net.ymate.platform.persistence.base.OperatorException;
import net.ymate.platform.persistence.jdbc.base.dialect.IDialect;
import net.ymate.platform.persistence.jdbc.base.dialect.impl.MySqlDialect;
import net.ymate.platform.persistence.jdbc.base.dialect.impl.OracleDialect;
import net.ymate.platform.persistence.jdbc.base.dialect.impl.SQLServer2005Dialect;
import net.ymate.platform.persistence.jdbc.support.C3p0DataSourceAdapter;
import net.ymate.platform.persistence.jdbc.support.DbcpDataSourceAdapter;
import net.ymate.platform.persistence.jdbc.support.DefaultConnectionHolder;
import net.ymate.platform.persistence.jdbc.support.DefaultDataSourceAdapter;
import net.ymate.platform.persistence.jdbc.support.DefaultSession;
import net.ymate.platform.persistence.jdbc.support.JdbcDataSourceCfgMeta;
import net.ymate.platform.persistence.jdbc.support.JndiDataSourceAdapter;
import net.ymate.platform.persistence.jdbc.transaction.ITransaction;
import net.ymate.platform.persistence.jdbc.transaction.Trans;
import net.ymate.platform.persistence.jdbc.transaction.support.DefaultTransactionProxyHandler;
import net.ymate.platform.persistence.support.RepositoryBeanFactory;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * JDBC
 * </p>
 * <p>
 * 数据库连接管理类；
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
 *          <td>2011-9-10下午11:45:25</td>
 *          </tr>
 *          </table>
 */
public class JDBC {

	/**
	 * 是否日志输出执行的SQL语句
	 */
	public static boolean isShowSQL;

	/**
	 * 数据库表前缀，默认为""
	 */
	public static String TABLE_PREFIX;

	/**
	 * 默认数据源名称
	 */
	public static String DATASOURCE_DEFAULT_NAME;

	protected static Map<String, IDataSourceAdapter> __DATASOURCE_CACHE = new ConcurrentHashMap<String, IDataSourceAdapter>();

	protected static Map<String, String> __DEFAULT_ADAPTER_NAMES = new HashMap<String, String>();

	protected static RepositoryBeanFactory __REPOSTORY_BEAN_FACTORY;

	protected static Map<String, Class<? extends IDialect>> __DIALECT = new HashMap<String, Class<? extends IDialect>>();

	static {
		__DIALECT.put("oracle", OracleDialect.class);
		__DIALECT.put("mysql", MySqlDialect.class);
		__DIALECT.put("microsoft sql server", SQLServer2005Dialect.class);
		//  { "default", "c3p0", "dbcp", "jndi" };
		__DEFAULT_ADAPTER_NAMES.put("default", DefaultDataSourceAdapter.class.getName());
		__DEFAULT_ADAPTER_NAMES.put("c3p0", C3p0DataSourceAdapter.class.getName());
		__DEFAULT_ADAPTER_NAMES.put("dbcp", DbcpDataSourceAdapter.class.getName());
		__DEFAULT_ADAPTER_NAMES.put("jndi", JndiDataSourceAdapter.class.getName());
	}

	/**
	 * 数据源配置信息
	 */
	public static boolean isInited;

	protected static IJdbcConfig __config;

	/**
	 * 初始化JDBC框架管理器
	 * 
	 * @param config
	 */
	public static void initialize(IJdbcConfig config) {
		if (!isInited) {
			__config = config;
			DATASOURCE_DEFAULT_NAME = StringUtils.defaultIfEmpty(config.getDefaultDataSourceName(), "default");
			TABLE_PREFIX = StringUtils.trimToEmpty(config.getTablePrefix());
			isShowSQL = config.isShowSql();
			for (JdbcDataSourceCfgMeta _cfgMeta : config.getDataSourceCfgMetas()) {
				String _adapterClassName = StringUtils.defaultIfEmpty(_cfgMeta.getAdapterClass(), "default");
				if (__DEFAULT_ADAPTER_NAMES.containsKey(_adapterClassName.toLowerCase())) {
					_adapterClassName = __DEFAULT_ADAPTER_NAMES.get(_adapterClassName.toLowerCase());
				}
				IDataSourceAdapter _adapter = ClassUtils.impl(_adapterClassName, IDataSourceAdapter.class, JDBC.class);
				_adapter.initialize(_cfgMeta);
				__DATASOURCE_CACHE.put(_cfgMeta.getName(), _adapter);
			}
			__REPOSTORY_BEAN_FACTORY = new RepositoryBeanFactory(config.getRepositoryPackages());
			isInited = true;
		}
	}

	/**
	 * @return 获取JDBC持久化框架初始化配置对象
	 */
	public static IJdbcConfig getConfig() {
		return __config;
	}

	/**
	 * 销毁数据库连接管理器
	 */
	public static void destroy() {
		if (isInited) {
			for (IDataSourceAdapter _adapter : __DATASOURCE_CACHE.values()) {
				_adapter.destroy();
			}
			__DATASOURCE_CACHE.clear();
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
	 * @param clazz 存储器接口类
	 * @return 返回存储器实例动态代理对象，支持声明式事务处理，若不存在则返回null
	 */
	public static <T> T getProxyBean(Class<T> clazz) {
		T _target = getBean(clazz);
		if (_target != null) {
			return new DefaultTransactionProxyHandler().bind(_target);
		}
		return null;
	}

	/**
	 * @param clazz 存储器实例对象
	 * @return 注册一个存储器实例对象，并返回注册是否成功
	 */
	public static boolean registerBean(Class<?> clazz) {
		return __REPOSTORY_BEAN_FACTORY.add(clazz) != null;
	}

	/**
	 * 注册数据库IDialect接口实现类
	 * 
	 * @param dialectName 方言名称(将被toLowerCase方法转换小写字符)，用于与数据库ProductName匹配
	 * @param clazz IDialect接口实现类
	 */
	public static void registerDialectClass(String dialectName, Class<? extends IDialect> clazz) {
		__DIALECT.put(dialectName.toLowerCase(), clazz);
	}

	public static void registerDataSourceAdapterClass(String adapterName, Class<? extends IDataSourceAdapter> clazz) {
		__DEFAULT_ADAPTER_NAMES.put(adapterName, clazz.getName());
	}

	/**
	 * @param dialectName 方言名称(将被toLowerCase方法转换小写字符)
	 * @return 获取数据库IDialect接口实现类
	 */
	public static Class<? extends IDialect> getDialectClass(String dialectName) {
		return __DIALECT.get(dialectName.toLowerCase());
	}

	/**
	 * @return 采用默认数据源构建会话对象
	 * @throws ConnectionException
	 */
	public static ISession openSession() throws ConnectionException {
		return new DefaultSession(getConnectionHolder());
	}

	/**
	 * @param dsName 数据源名称
	 * @return 采用由dsName指定的数据源构建会话对象
	 * @throws ConnectionException
	 */
	public static ISession openSession(String dsName) throws ConnectionException {
		return new DefaultSession(getConnectionHolder(dsName));
	}

	/**
	 * 创建会话对象并回调会话执行器接口，保证会话被安全关闭
	 * 
	 * @param executor 会话执行器接口
	 * @throws ConnectionException
	 * @throws OperatorException
	 */
	public static <T> T openSession(ISessionExecutor<T> executor) throws ConnectionException, OperatorException {
		ISession _session = openSession();
		try {
			return executor.execute(_session);
		} finally {
			_session.close();
		}
	}

	/**
	 * 创建会话对象并回调会话执行器接口，保证会话被安全关闭
	 * 
	 * @param dsName 数据源名称
	 * @param executor 会话执行器接口
	 * @return
	 * @throws ConnectionException
	 * @throws OperatorException
	 */
	public static <T> T openSession(String dsName, ISessionExecutor<T> executor) throws ConnectionException, OperatorException {
		ISession _session = openSession(dsName);
		try {
			return executor.execute(_session);
		} finally {
			_session.close();
		}
	}

	/**
	 * @return 获取默认数据源连接持有者对象
	 * @throws ConnectionException
	 */
	public static IConnectionHolder getConnectionHolder() throws ConnectionException {
		return getConnectionHolder(DATASOURCE_DEFAULT_NAME);
	}

	/**
	 * @param dsName 数据源名称
	 * @return 获取指定数据源的连接持有者对象
	 * @throws ConnectionException
	 */
	public static IConnectionHolder getConnectionHolder(String dsName) throws ConnectionException {
		IConnectionHolder _returnValue = null;
		ITransaction _trans = Trans.get();
		if (_trans != null) {
			_returnValue = _trans.getConnectionHolder(dsName);
			if (_returnValue == null) {
				_returnValue = new DefaultConnectionHolder(dsName, __DATASOURCE_CACHE.get(dsName));
				_trans.registerTransactionConnectionHolder(_returnValue);
			}
		} else {
			_returnValue = new DefaultConnectionHolder(dsName, __DATASOURCE_CACHE.get(dsName));
		}
		return _returnValue;
	}

	/**
	 * 关闭数据源的连接持有者
	 * 
	 * @param conn 数据源的连接持有者对象
	 * @throws ConnectionException
	 */
	public static void release(IConnectionHolder conn) throws ConnectionException {
		// 同时需要判断当前连接是否参与事务，若存在事务则不进行关闭操作
		if (conn != null && Trans.get() == null) {
			conn.release();
			conn = null;
		}
	}

}
