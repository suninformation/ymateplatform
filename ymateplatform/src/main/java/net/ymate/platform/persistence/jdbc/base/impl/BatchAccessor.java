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
package net.ymate.platform.persistence.jdbc.base.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.ymate.platform.persistence.jdbc.base.AbstractAccessor;
import net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent;
import net.ymate.platform.persistence.jdbc.base.SqlParameter;

/**
 * <p>
 * BatchAccessor
 * </p>
 * <p>
 * 支持批量操作的数据库访问器实现类；
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
 *          <td>2011-8-31下午03:34:38</td>
 *          </tr>
 *          </table>
 */
public class BatchAccessor extends AbstractAccessor {

	private List<List<SqlParameter>> __parameters = new ArrayList<List<SqlParameter>>();

	/**
	 * 构造器
	 * 
	 * @param sql
	 * @param params
	 */
	public BatchAccessor(String sql, List<List<SqlParameter>> params) {
		this(sql, params, null);
	}

	/**
	 * 构造器
	 * 
	 * @param sql
	 * @param params
	 * @param eventObj
	 */
	public BatchAccessor(String sql, List<List<SqlParameter>> params, IAccessorCfgEvent eventObj) {
		this.setSqlStr(sql);
		this.setAccessorCfgEvent(eventObj);
		if (params != null && !params.isEmpty()) {
			this.__parameters.addAll(params);
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.AbstractAccessor#getCallableStatement(java.sql.Connection)
	 */
	public CallableStatement getCallableStatement(Connection conn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.AbstractAccessor#processSqlParams(java.sql.PreparedStatement)
	 */
	protected void processSqlParams(PreparedStatement statement) throws SQLException {
        for (List<SqlParameter> _params : this.__parameters) {
            this.doSetSqlParams(statement, _params);
            statement.addBatch();
        }
	}

}
