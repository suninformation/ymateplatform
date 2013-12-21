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

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import net.ymate.platform.commons.util.ResourceUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.persistence.jdbc.AbstractDataSourceAdapter;
import net.ymate.platform.persistence.jdbc.ConnectionException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

/**
 * <p>
 * DbcpDataSourceAdapter
 * </p>
 * <p>
 * 基于DBCP连接池的数据源适配器接口实现类；
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
 *          <td>2013年12月19日下午2:47:10</td>
 *          </tr>
 *          </table>
 */
public class DbcpDataSourceAdapter extends AbstractDataSourceAdapter {

	protected BasicDataSource __ds;

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.IDataSourceAdapter#destroy()
	 */
	public void destroy() {
		if (__ds != null) {
			try {
				__ds.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//
		super.destroy();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.IDataSourceAdapter#getConnection()
	 */
	public Connection getConnection() throws ConnectionException {
		try {
			return __ds.getConnection();
		} catch (SQLException e) {
			throw new ConnectionException(RuntimeUtils.unwrapThrow(e));
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.AbstractDataSourceAdapter#initialize(net.ymate.platform.persistence.jdbc.support.DataSourceCfgMeta)
	 */
	public void initialize(DataSourceCfgMeta cfgMeta) {
		super.initialize(cfgMeta);
		//
		try {
			Properties _props = new Properties();
			InputStream _in = ResourceUtils.getResourceAsStream("dbcp.properties", DbcpDataSourceAdapter.class);
			if (_in != null) {
				_props.load(_in);
			}
			//
			_props.put("driverClassName", cfgMeta.getDriverClass());
			_props.put("url", cfgMeta.getConnectionUrl());
			_props.put("username", cfgMeta.getUserName());
			_props.put("password", cfgMeta.getPassword());
			__ds = (BasicDataSource) BasicDataSourceFactory.createDataSource(_props);
		} catch (Exception e) {
			throw new Error(RuntimeUtils.unwrapThrow(e));
		}
	}

}
