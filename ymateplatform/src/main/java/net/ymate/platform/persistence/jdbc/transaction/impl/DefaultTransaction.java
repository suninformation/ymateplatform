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
package net.ymate.platform.persistence.jdbc.transaction.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.persistence.jdbc.ConnectionException;
import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.transaction.ITransaction;

/**
 * <p>
 * DefaultTransaction
 * </p>
 * <p>
 * 默认 JDBC 事务处理接口实现类；
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
 *          <td>2011-9-6下午04:43:45</td>
 *          </tr>
 *          </table>
 */
public class DefaultTransaction implements ITransaction {

	private List<TransactionInfo> __transList;

	private TransactionLevel __level;

	private String __id;

	/**
	 * 构造器
	 */
	public DefaultTransaction() {
		this.__id = UUIDUtils.uuid();
		this.__transList = new ArrayList<TransactionInfo>();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.transaction.ITransaction#close()
	 */
	public void close() throws SQLException {
		try {
			for (TransactionInfo p : __transList) {
				p.release();
			}
		} finally {
			// 清除数据源记录
			__transList.clear();
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.transaction.ITransaction#commit()
	 */
	public void commit() throws SQLException {
		for (TransactionInfo p : __transList) {
			// 提交事务
			p.__conn.getConnection().commit();
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.transaction.ITransaction#getConnectionHolder(java.lang.String)
	 */
	public IConnectionHolder getConnectionHolder(String dsName) {
		for (TransactionInfo p : __transList) {
			if (p.__dsName.equals(dsName)) {
				return p.__conn;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.transaction.ITransaction#registerTransactionConnectionHolder(net.ymate.platform.persistence.jdbc.IConnectionHolder)
	 */
	public void registerTransactionConnectionHolder(IConnectionHolder connectionHolder) throws ConnectionException {
		try {
			if (connectionHolder.getConnection().getAutoCommit()) {
				connectionHolder.getConnection().setAutoCommit(false);
			}
			__transList.add(new TransactionInfo(connectionHolder.getDataSourceName(), connectionHolder, getLevel()));
		} catch (SQLException e) {
			throw new ConnectionException(RuntimeUtils.unwrapThrow(e));
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.transaction.ITransaction#getId()
	 */
	public String getId() {
		return __id;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.transaction.ITransaction#rollback()
	 */
	public void rollback() throws SQLException {
		for (TransactionInfo p : __transList) {
			p.__conn.getConnection().rollback();
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.transaction.ITransaction#getLevel()
	 */
	public TransactionLevel getLevel() {
		return __level;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.transaction.ITransaction#setLevel(net.ymate.platform.persistence.jdbc.transaction.ITransaction.TransactionLevel)
	 */
	public void setLevel(TransactionLevel level) {
		if (level != null && (this.__level == null || this.__level.getLevel() <= 0)) {
			this.__level = level;
		}
	}

	/**
	 * <p>
	 * TransactionInfo
	 * </p>
	 * <p>
	 * 事务信息存储对象；
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
	 *          <td>2010-10-16下午03:50:01</td>
	 *          </tr>
	 *          </table>
	 */
	private static class TransactionInfo {

		String __dsName;
		IConnectionHolder __conn;
		int __level;

		/**
		 * 构造器
		 * @param dsName 数据源名称
		 * @param conn 数据库连接持有者对象
		 * @param level 事务级别
		 * @throws SQLException
		 */
		TransactionInfo(String dsName, IConnectionHolder conn, TransactionLevel level) throws SQLException {
			this.__dsName = dsName;
			this.__conn = conn;
			__level = conn.getConnection().getTransactionIsolation();
			if (level != null && __level != level.getLevel()) {
				conn.getConnection().setTransactionIsolation(level.getLevel());
			}
		}

		/**
		 * 释放数据源、连接资源
		 */
		void release() throws SQLException {
			try {
				if (this.__conn != null) {
					this.__conn.release();
				}
			} finally {
				this.__conn = null;
				this.__dsName = null;
			}
		}

	}

}
