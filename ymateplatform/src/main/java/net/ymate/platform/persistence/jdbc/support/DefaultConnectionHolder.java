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
package net.ymate.platform.persistence.jdbc.support;

import java.sql.Connection;
import java.sql.SQLException;

import net.ymate.platform.persistence.jdbc.ConnectionException;
import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.IDataSourceAdapter;
import net.ymate.platform.persistence.jdbc.base.dialect.IDialect;

/**
 * <p>
 * DefaultConnectionHolder
 * </p>
 * <p>
 * Connection对象持有者接口默认实现类；
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
 *          <td>2012-12-29下午4:44:08</td>
 *          </tr>
 *          </table>
 */
public class DefaultConnectionHolder implements IConnectionHolder {

	protected String dataSourceName;
	protected Connection connection;
	protected IDialect dialect;

	public DefaultConnectionHolder(String dataSourceName, IDataSourceAdapter adapter) throws ConnectionException {
		this.dataSourceName = dataSourceName;
		this.connection = adapter.getConnection();
		this.dialect = adapter.getDialect();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.IConnectionHolder#getDataSourceName()
	 */
	public String getDataSourceName() {
		return dataSourceName;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.IConnectionHolder#getConnection()
	 */
	public Connection getConnection() {
		return connection;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.IConnectionHolder#release()
	 */
	public void release() {
		try {
			if (this.connection != null && !this.connection.isClosed()) {
				this.connection.close();
			}
		} catch (SQLException e) {
			// ~~~
		} finally {
			this.connection = null;
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.IConnectionHolder#getDialect()
	 */
	public IDialect getDialect() {
		return dialect;
	}

}
