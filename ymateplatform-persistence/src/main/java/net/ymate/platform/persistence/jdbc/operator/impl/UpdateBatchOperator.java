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
import java.util.ArrayList;
import java.util.List;

import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.base.SqlBatchParameter;
import net.ymate.platform.persistence.jdbc.base.SqlParameter;
import net.ymate.platform.persistence.jdbc.base.impl.BatchAccessor;
import net.ymate.platform.persistence.jdbc.operator.AbstractOperator;
import net.ymate.platform.persistence.jdbc.operator.IUpdateBatchOperator;

/**
 * <p>
 * UpdateBatchOperator
 * </p>
 * <p>
 * 数据库批量更新操作器实现类；
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
 *          <td>2011-9-23下午01:15:43</td>
 *          </tr>
 *          </table>
 */
public class UpdateBatchOperator extends AbstractOperator implements IUpdateBatchOperator {

	/**
	 * 批更新时使用的SQL参数集合
	 */
	private List<List<SqlParameter>> __batchParameters = new ArrayList<List<SqlParameter>>();
	
	/**
	 * 批更新操作影响的记录行数
	 */
	private int[] __batchEffectCounts;

	/**
	 * 构造器
	 */
	public UpdateBatchOperator() {
	}

	/**
	 * 构造器
	 * 
	 * @param sql SQL 语句
	 */
	public UpdateBatchOperator(String sql) {
		this.setSql(sql);
	}

	/**
	 * 构造器
	 * 
	 * @param sql SQL 语句
	 * @param conn 数据库连接对象
	 */
	public UpdateBatchOperator(String sql, IConnectionHolder conn) {
		this.setSql(sql);
		this.setConnection(conn);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.AbstractOperator#__execute()
	 */
	protected int __execute() throws SQLException {
		this.__batchEffectCounts = this.doBatchUpdate(new BatchAccessor(this.getSql(), this.__batchParameters) , this.getConnection());
		return this.__batchEffectCounts.length;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.AbstractOperator#__parametersToString()
	 */
	protected String __parametersToString() {
		return this.getBatchParameters().toString();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IUpdateBatchOperator#addBatchParameter(net.ymate.platform.persistence.jdbc.base.SqlBatchParameter)
	 */
	public void addBatchParameter(SqlBatchParameter parameter) {
		this.__batchParameters.add(parameter.getSqlParameterSet());
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IUpdateBatchOperator#getBatchEffectCounts()
	 */
	public int[] getBatchEffectCounts() {
		return this.__batchEffectCounts;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IUpdateBatchOperator#getBatchParameters()
	 */
	public List<List<SqlParameter>> getBatchParameters() {
		return this.__batchParameters;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.AbstractOperator#addParameter(java.lang.Object)
	 */
	public void addParameter(Object parameter) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.AbstractOperator#addParameter(net.ymate.platform.persistence.jdbc.base.SqlParameter)
	 */
	public void addParameter(SqlParameter parameter) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.AbstractOperator#getParameters()
	 */
	public List<SqlParameter> getParameters() {
		throw new UnsupportedOperationException();
	}

}
