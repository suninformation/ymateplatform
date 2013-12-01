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
package net.ymate.platform.persistence.jdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import net.ymate.platform.persistence.jdbc.ConnectionException;
import net.ymate.platform.persistence.jdbc.IConnectionHolder;

/**
 * <p>
 * ITransaction
 * </p>
 * <p>
 * 事务处理接口定义类；
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
 *          <td>2011-9-6下午03:59:38</td>
 *          </tr>
 *          </table>
 */
public interface ITransaction {

	/**
	 * <p>
	 * TransactionLevel
	 * </p>
	 * <p>
	 * 事务级别枚举类对象；
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
	 *          <td>2010-12-22上午11:07:49</td>
	 *          </tr>
	 *          </table>
	 */
	public enum TransactionLevel {

		/**
		 * 不（使用）支持事务
		 */
		TRANSACTION_NONE(Connection.TRANSACTION_NONE),

		/**
		 * 在一个事务中进行查询时，允许读取提交前的数据，数据提交后，当前查询就可以读取到数据，update数据时候并不锁住表
		 */
		TRANSACTION_READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

		/**
		 * 俗称“脏读”（dirty read），在没有提交数据时能够读到已经更新的数据
		 */
		TRANSACTION_READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
		/**
		 * 在一个事务中进行查询时，不允许读取其他事务update的数据，允许读取到其他事务提交的新增数据
		 */
		TRANSACTION_REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

		/**
		 * 在一个事务中进行查询时，不允许任何对这个查询表的数据修改
		 */
		TRANSACTION_SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

		private int _level;

		/**
		 * 构造器
		 * @param level 事务级别
		 */
		private TransactionLevel(int level) {
			this._level = level;
		}

		/**
		 * @return the level
		 */
		public int getLevel() {
			return _level;
		}

		/**
		 * @param level the level to set
		 */
		public void setLevel(int level) {
			this._level = level;
		}

	}

	/**
	 * @return 获取事务级别
	 */
	public abstract TransactionLevel getLevel();

	/**
	 * 设置事务级别
	 * 
	 * @param level 事务级别
	 */
	public abstract void setLevel(TransactionLevel level);

	/**
	 * @return 获取事务Id
	 */
	public abstract String getId();

	/**
	 * 提交事务
	 */
	public abstract void commit() throws SQLException;

	/**
	 * 回滚事务
	 */
	public abstract void rollback() throws SQLException;

	/**
	 * @param dsName 数据源名称
	 * @return 获取数据库连接持有者对象
	 */
	public abstract IConnectionHolder getConnectionHolder(String dsName);

	/**
	 * 注册一个ConnectionHolder对象由事务管理
	 * 
	 * @param connectionHolder 数据库连接持有者对象
	 * @throws ConnectionException
	 */
	public abstract void registerTransactionConnectionHolder(IConnectionHolder connectionHolder) throws ConnectionException;

	/**
	 * 关闭事务（连接）
	 */
	public abstract void close() throws SQLException;

}
