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
package net.ymate.platform.persistence.jdbc.operator.impl;

import java.sql.SQLException;
import java.util.List;

import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.base.impl.GenericAccessor;
import net.ymate.platform.persistence.jdbc.operator.AbstractOperator;
import net.ymate.platform.persistence.jdbc.operator.IQueryOperator;
import net.ymate.platform.persistence.jdbc.operator.IResultSetHandler;
import net.ymate.platform.persistence.jdbc.operator.OperatorException;

/**
 * <p>
 * QueryOperator
 * </p>
 * <p>
 * 数据库查询操作器接口实现类；
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
 *          <td>2011-9-23上午09:32:37</td>
 *          </tr>
 *          </table>
 */
public class QueryOperator<T> extends AbstractOperator implements IQueryOperator<T> {

	private int __maxRow = 0;

	private IResultSetHandler<T> __handler;

	/**
	 * 构造器
	 * 
	 * @param handler 结果集数据处理对象
	 */
	public QueryOperator(IResultSetHandler<T> handler) {
		this.__handler = handler;
	}

	/**
	 * 构造器
	 * 
	 * @param sql SQL 语句
	 * @param handler 结果集数据处理对象
	 */
	public QueryOperator(String sql, IResultSetHandler<T> handler) {
		this.setSql(sql);
		this.__handler = handler;
	}

	/**
	 * 构造器
	 * 
	 * @param sql SQL 语句
	 * @param handler 结果集数据处理对象
	 * @param conn 数据库连接对象
	 */
	public QueryOperator(String sql, IResultSetHandler<T> handler, IConnectionHolder conn) {
		this.setSql(sql);
		this.setConnection(conn);
		this.__handler = handler;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.AbstractOperator#__execute()
	 */
	protected int __execute() throws OperatorException, SQLException {
		this.doQuery(new GenericAccessor(this.getSql(), this.getParameters()) , this.getConnection(), this.__handler, this.__maxRow);
		return this.__handler.getResultDataSet().size();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.AbstractOperator#__parametersToString()
	 */
	protected String __parametersToString() {
		return this.getParameters().toString();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IQueryOperator#getResultSet()
	 */
	public List<T> getResultSet() {
		if (!this.isExecuted()) {
			throw new Error("当前查询操作器尚未被执行过，请先执行\"execute\"方法");
		}
		return this.__handler.getResultDataSet();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IQueryOperator#setResultSetHandler(net.ymate.platform.persistence.jdbc.operator.IResultSetHandler)
	 */
	public void setResultSetHandler(IResultSetHandler<T> handler) {
		this.__handler = handler;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IQueryOperator#isResultSetAvailable()
	 */
	public boolean isResultSetAvailable() {
		if (!this.isExecuted()) {
			throw new Error("当前查询操作器尚未被执行过，请先执行\"execute\"方法");
		}
		if (__handler.getRowCount() > 0) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IQueryOperator#setMaxRow(int)
	 */
	public void setMaxRow(int maxRow) {
		this.__maxRow = maxRow;
	}

}
