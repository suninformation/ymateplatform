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
package net.ymate.platform.persistence.jdbc.query;

import java.util.List;

import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.persistence.base.OperatorException;
import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.base.SqlParameter;
import net.ymate.platform.persistence.jdbc.operator.IQueryOperator;
import net.ymate.platform.persistence.jdbc.operator.IResultSetHandler;
import net.ymate.platform.persistence.jdbc.operator.impl.ArrayResultSetHandler;
import net.ymate.platform.persistence.jdbc.operator.impl.QueryOperator;
import net.ymate.platform.persistence.support.PageResultSet;

/**
 * <p>
 * PageQuery
 * </p>
 * <p>
 * 分页查询类；
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
 *          <td>2011-9-24下午08:33:40</td>
 *          </tr>
 *          </table>
 */
public class PageQuery<T> {

	/**
	 * 查询页号
	 */
	private int __pageNumber;

	/**
	 * 每页记录数
	 */
	private int __pageSize;

	/**
	 * 是否执行总记录数查询，默认true，若不执行则获取记录总数将返回-1
	 */
	private boolean __allowRecordCountFlag = true;

	private IQueryOperator<T> __queryOpt;

	/**
	 * 构造器
	 * 
	 * @param queryOpt 自定义查询操作者对象
	 * @param pageNumber 页号
	 * @param pageSize 分页大小
	 */
	public PageQuery(IQueryOperator<T> queryOpt, int pageNumber, int pageSize) {
		this.__queryOpt = queryOpt;
		this.__pageNumber = pageNumber;
		this.__pageSize = pageSize;
	}

	/**
	 * 构造器
	 * 
	 * @param handler 结果集处理器
	 * @param pageNumber 页号
	 * @param pageSize 分页大小
	 */
	public PageQuery(IResultSetHandler<T> handler, int pageNumber, int pageSize) {
		this.__queryOpt = new QueryOperator<T>(handler);
		this.__pageNumber = pageNumber;
		this.__pageSize = pageSize;
	}

	/**
	 * 构造器
	 * 
	 * @param sql SQL语句
	 * @param handler 结果集处理器
	 * @param pageNumber 页号
	 * @param pageSize 分页大小
	 */
	public PageQuery(String sql, IResultSetHandler<T> handler, int pageNumber, int pageSize) {
		this.__queryOpt = new QueryOperator<T>(sql, handler);
		this.__pageNumber = pageNumber;
		this.__pageSize = pageSize;
	}

	/**
	 * 构造器
	 * 
	 * @param sql SQL语句
	 * @param handler 结果集处理器
	 * @param conn 数据库连接持有对象
	 * @param pageNumber 页号
	 * @param pageSize 分页大小
	 */
	public PageQuery(String sql, IResultSetHandler<T> handler, IConnectionHolder conn, int pageNumber, int pageSize) {
		this.__queryOpt = new QueryOperator<T>(sql, handler, conn);
		this.__pageNumber = pageNumber;
		this.__pageSize = pageSize;
	}

    /**
     * @return 是否执行总记录数查询
     */
	public boolean isAllowRecordCount() {
		return __allowRecordCountFlag;
	}

	public PageQuery<T> setAllowRecordCount(boolean allowRecordCount) {
		this.__allowRecordCountFlag = allowRecordCount;
		return this;
	}

	public IConnectionHolder getConnection() {
		return this.__queryOpt.getConnection();
	}

	public PageQuery<T> setConnection(IConnectionHolder conn) {
		this.__queryOpt.setConnection(conn);
		return this;
	}

	public String getSql() {
		return this.__queryOpt.getSql();
	}

	public PageQuery<T> setSql(String sql) {
		this.__queryOpt.setSql(sql);
		return this;
	}

	public int getPageNumber() {
		return __pageNumber;
	}

	public PageQuery<T> setPageNumber(int pageNumber) {
		this.__pageNumber = pageNumber;
		return this;
	}

	public int getPageSize() {
		return __pageSize;
	}

	public PageQuery<T> setPageSize(int pageSize) {
		this.__pageSize = pageSize;
		return this;
	}

	public List<SqlParameter> getParameters() {
		return this.__queryOpt.getParameters();
	}

	public PageQuery<T> addParameter(SqlParameter parameter) {
		this.__queryOpt.addParameter(parameter);
		return this;
	}

	public PageQuery<T> addParameter(Object parameter) {
		this.__queryOpt.addParameter(parameter);
		return this;
	}

	public PageResultSet<T> execute() throws OperatorException {
		int _limit = ((this.__pageNumber - 1) * this.__pageSize);
		String _sourceSql = this.getSql();
		String _pageSql = this.getConnection().getDialect().getPaginationSql(_sourceSql, _limit, this.__pageSize);
		this.__queryOpt.setSql(_pageSql);
		this.__queryOpt.execute();
		//
		return new PageResultSet<T>(this.__queryOpt.getResultSet(), this.__pageNumber, this.__pageSize, this.__allowRecordCountFlag ? __doRecordCount(_sourceSql) : -1);
	}

	public PageResultSet<T> execute(IConnectionHolder conn) throws OperatorException {
		this.setConnection(conn);
		return this.execute();
	}

	/**
	 * @param sql 需要统计的SQL语句
	 * @return 执行SQL查询，计算记录总数
	 * @throws OperatorException
	 */
	protected int __doRecordCount(String sql) throws OperatorException {
		IQueryOperator<Object[]> _query = new QueryOperator<Object[]>(new ArrayResultSetHandler());
		_query.setConnection(this.getConnection());
		_query.getParameters().addAll(this.getParameters());
		_query.setSql("select count(1) from (" + __doRemoveOrderBy(sql) + ") c_t");
		_query.execute();
		Object[] _ct = (Object[]) _query.getResultSet().get(0)[0];
		return new BlurObject(_ct[1]).toIntValue();
	}

	/**
	 * @param sql 目标SQL语句
	 * @return 移除 SQL 中的 order by 子句
	 */
	protected String __doRemoveOrderBy(String sql) {
		int orderByIndex = sql.toLowerCase().lastIndexOf("order by");
		if (orderByIndex > 0) {
			return sql.substring(0, orderByIndex);
		}
		return sql;
	}

}
