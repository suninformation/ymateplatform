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

import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.operator.OperatorException;

/**
 * <p>
 * ICompositeQuery
 * </p>
 * <p>
 * 组合查询接口定义类；
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
 *          <td>2011-6-23上午09:01:46</td>
 *          </tr>
 *          </table>
 */
public interface ICompositeQuery<T> {

	/**
	 * @return 执行组合查询
	 */
	public List<T> doQuery() throws OperatorException;

	public List<T> doQuery(IConnectionHolder conn) throws OperatorException;

	/**
	 * @param whereStr SQL条件字符串
	 * @param values SQL参数
	 * @return 执行组合查询
	 */
	public List<T> doQuery(String whereStr, Object[] values) throws OperatorException;

	public List<T> doQuery(String whereStr, Object[] values, IConnectionHolder conn) throws OperatorException;

	/**
	 * @param whereStr SQL条件字符串
	 * @param values SQL参数
	 * @param pageSize 分页大小
	 * @param currentPage 查询页号
	 * @return 执行组合查询（采用分页方式）
	 */
	public PageResultSet<T> doQuery(String whereStr, Object[] values, int pageSize, int currentPage) throws OperatorException;

	public PageResultSet<T> doQuery(String whereStr, Object[] values, int pageSize, int currentPage, IConnectionHolder conn) throws OperatorException;

	/**
	 * @param whereStr SQL条件字符串
	 * @param values SQL参数
	 * @param pageSize 分页大小
	 * @param currentPage 查询页号
	 * @param allowRecordCount 是否执行总记录数查询
	 * @return 执行组合查询（采用分页方式）
	 */
	public PageResultSet<T> doQuery(String whereStr, Object[] values, int pageSize, int currentPage, boolean allowRecordCount) throws OperatorException;

	public PageResultSet<T> doQuery(String whereStr, Object[] values, int pageSize, int currentPage, boolean allowRecordCount, IConnectionHolder conn) throws OperatorException;

	/**
	 * 设置当前使用的数据库连接对象
	 * 
	 * @param conn
	 * @return
	 */
	public ICompositeQuery<T> setConnection(IConnectionHolder conn);

	/**
	 * @return 获取当前使用的数据库连接对象
	 */
	public IConnectionHolder getConnection();

	/**
	 * @return 获取WHERE条件查询时与已存在的条件关系连接运算符号，如：AND或OR等，若为NULL则默认采用 "AND"
	 */
	public String getQueryWhereConditionType();

	/**
	 * 设置WHERE条件查询时与已存在的条件关系连接运算符号，如：AND或OR等，若为NULL则默认采用 "AND"
	 * 
	 * @param conditionType
	 * @return
	 */
	public ICompositeQuery<T> setQueryWhereConditionType(String conditionType);

}
