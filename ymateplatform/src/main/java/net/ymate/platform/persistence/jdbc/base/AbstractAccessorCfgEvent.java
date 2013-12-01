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
package net.ymate.platform.persistence.jdbc.base;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p>
 * AbstractAccessorCfgEvent
 * </p>
 * <p>
 * 访问器配置事件处理接口抽象实现类；
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
 *          <td>2011-8-30下午03:51:50</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractAccessorCfgEvent implements IAccessorCfgEvent {

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent#afterStatementExecution(net.ymate.platform.persistence.jdbc.base.AccessorEventContext)
	 */
	public void afterStatementExecution(AccessorEventContext context) throws SQLException {
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent#beforeStatementExecution(net.ymate.platform.persistence.jdbc.base.AccessorEventContext)
	 */
	public void beforeStatementExecution(AccessorEventContext context) throws SQLException {
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent#getCallableStatement(java.sql.Connection, java.lang.String)
	 */
	public CallableStatement getCallableStatement(Connection conn, String sql) throws SQLException {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent#getFetchDirection()
	 */
	public int getFetchDirection() {
		return ResultSet.FETCH_FORWARD;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent#getFetchSize()
	 */
	public int getFetchSize() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent#getMaxFieldSize()
	 */
	public int getMaxFieldSize() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent#getMaxRows()
	 */
	public int getMaxRows() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent#getPreparedStatement(java.sql.Connection, java.lang.String)
	 */
	public PreparedStatement getPreparedStatement(Connection conn, String sql) throws SQLException {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent#getQueryTimeout()
	 */
	public int getQueryTimeout() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent#getStatement(java.sql.Connection)
	 */
	public Statement getStatement(Connection conn) throws SQLException {
		return null;
	}

}
