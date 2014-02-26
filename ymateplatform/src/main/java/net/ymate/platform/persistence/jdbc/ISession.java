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
package net.ymate.platform.persistence.jdbc;

import java.util.List;

import net.ymate.platform.persistence.base.OperatorException;
import net.ymate.platform.persistence.jdbc.operator.IResultSetHandler;
import net.ymate.platform.persistence.support.PageResultSet;


/**
 * <p>
 * ISession
 * </p>
 * <p>
 * JDBC 数据库操作会话接口定义类；
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
 *          <td>2011-9-21下午01:45:36</td>
 *          </tr>
 *          </table>
 */
public interface ISession {

	/**
	 * @return 获取会话对象唯一标识ID
	 */
	public String getId();

	/**
	 * @return 获取数据连接对象
	 */
	public IConnectionHolder getConnection();

	/**
	 * 设置会话事件处理器
	 * 
	 * @param event 事件处理器接口
	 * @return 会话对象
	 */
	public ISession setSessionEvent(ISessionEvent event);

	/**
	 * 关闭/释放会话
	 */
	public void close();

	/**
	 * @param <T> 指定结果集数据类型
	 * @param sql SQL语句
	 * @param handler 结果集数据处理器
	 * @param params SQL参数集合
	 * @return 执行SQL查询，返回全部结果数据
	 * @throws OperatorException
	 */
	public <T> List<T> findAll(String sql, IResultSetHandler<T> handler, Object[] params) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @return 根据实体执行SQL查询，返回全部结果数据
	 * @throws OperatorException
	 */
	public <T> List<T> findAll(Class<T> entity) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param cond 查询条件
	 * @param params 条件参数
	 * @return 根据实体执行SQL查询，返回全部结果数据
	 * @throws OperatorException
	 */
	public <T> List<T> findAll(Class<T> entity, String cond, Object[] params) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param cond 查询条件
	 * @param fieldFilter 显示字段过滤集合
	 * @param params 条件参数
	 * @return 根据实体执行SQL查询，返回全部结果数据
	 * @throws OperatorException
	 */
	public <T> List<T> findAll(Class<T> entity, String cond, String[] fieldFilter, Object[] params) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param sql SQL语句
	 * @param handler 结果集数据处理器
	 * @param pageSize 分页大小
	 * @param page 页号
	 * @param params SQL参数集合
	 * @return 执行SQL分页查询（执行总记录数统计）
	 * @throws OperatorException
	 */
	public <T> PageResultSet<T> findAll(String sql, IResultSetHandler<T> handler, int pageSize, int page, Object[] params) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param sql SQL语句
	 * @param handler 结果集数据处理器
	 * @param pageSize 分页大小
	 * @param page 页号
	 * @param count 是否执行总记录数统计
	 * @param params SQL参数集合
	 * @return 执行SQL分页查询
	 * @throws OperatorException
	 */
	public <T> PageResultSet<T> findAll(String sql, IResultSetHandler<T> handler, int pageSize, int page, boolean count, Object[] params) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param cond 查询条件
	 * @param fieldFilter 显示字段过滤集合
	 * @param pageSize 分页大小
	 * @param page 页号
	 * @param params 条件参数
	 * @return 根据实体执行SQL分页查询
	 * @throws OperatorException
	 */
	public <T> PageResultSet<T> findAll(Class<T> entity, String cond, String[] fieldFilter, int pageSize, int page, Object[] params) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param cond 查询条件
	 * @param fieldFilter 显示字段过滤集合
	 * @param pageSize 分页大小
	 * @param page 页号
	 * @param count 是否执行总记录数统计
	 * @param params 条件参数
	 * @return 根据实体执行SQL分页查询
	 * @throws OperatorException
	 */
	public <T> PageResultSet<T> findAll(Class<T> entity, String cond, String[] fieldFilter, int pageSize, int page, boolean count, Object[] params) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param sql SQL语句
	 * @param handler 结果集数据处理器
	 * @param params SQL参数集合
	 * @return 执行SQL查询，返回结果集中第一条数据
	 * @throws OperatorException
	 */
	public <T> T findFirst(String sql, IResultSetHandler<T> handler, Object[] params) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param cond 查询条件
	 * @param fieldFilter 显示字段过滤集合
	 * @param params 条件参数
	 * @return 根据实体执行SQL查询，返回结果集中第一条数据
	 * @throws OperatorException
	 */
	public <T>T findFirst(Class<T> entity, String cond, String[] fieldFilter, Object[] params) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param id 记录Id
	 * @return 通过ID查找指定的实体对象
	 * @throws OperatorException
	 */
	public <T> T find(Class<T> entity, Object id) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param id 记录Id
	 * @param fieldFilter 显示字段过滤集合
	 * @return 通过ID查找指定的实体对象的指定字段
	 * @throws OperatorException
	 */
	public <T> T find(Class<T> entity, Object id, String[] fieldFilter) throws OperatorException;

	/**
	 * @param sql SQL更新语句
	 * @param params SQL参数
	 * @return 执行SQL更新（如更新、插入和删除），返回此次更新影响的记录数
	 * @throws OperatorException
	 */
	public int executeForUpdate(String sql, Object[] params) throws OperatorException;

	/**
	 * @param sql SQL更新语句
	 * @param params SQL参数
	 * @return 执行SQL批量更新（如批更新、插入和删除），返回此次更新影响的记录数
	 * @throws OperatorException
	 */
	public int[] executeForUpdateAll(String sql, List<Object[]> params) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @return 根据实体执行SQL更新，返回更新后的实体对象
	 * @throws OperatorException
	 */
	public <T> T update(T entity) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param fieldFilter 更新字段过滤集合
	 * @return 根据实体执行SQL更新，返回更新后的实体对象
	 * @throws OperatorException
	 */
	public <T> T update(T entity, String[] fieldFilter) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entities 实体对象集合
	 * @return 根据实体执行SQL批量更新，返回更新后的实体对象集合
	 * @throws OperatorException
	 */
	public <T> List<T> updateAll(List<T> entities) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entities 实体对象集合
	 * @param fieldFilter 更新字段过滤集合
	 * @return 根据实体执行SQL批量更新，返回更新后的实体对象集合
	 * @throws OperatorException
	 */
	public <T> List<T> updateAll(List<T> entities, String[] fieldFilter) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @return 根据实体执行记录插入，返回插入后的实体对象
	 * @throws OperatorException
	 */
	public <T> T insert(T entity) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entities 实体对象集合
	 * @return 根据实体执行记录批量插入，返回插入后的实体对象集合
	 * @throws OperatorException
	 */
	public <T> List<T> insertAll(List<T> entities) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @return 根据实体执行记录删除，返回删除后的实体对象
	 * @throws OperatorException
	 */
	public <T> T delete(T entity) throws OperatorException;

	public <T> int delete(Class<T> entityClass, Object id) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entities 实体对象集合
	 * @return 根据实体执行记录批量删除，返回删除后的实体对象集合
	 * @throws OperatorException
	 */
	public <T> List<T> deleteAll(List<T> entities) throws OperatorException;

	public <T> int[] deleteAll(Class<T> entityClass, Object[] ids) throws OperatorException;

	/**
	 * @param <T> 指定实体类型
	 * @param entityClass 实体类对象
	 * @param whereStr 查询条件
	 * @param params 条件参数集合
	 * @return 计算查询结果总记录数量
	 * @throws OperatorException
	 */
	public <T> long getAmount(Class<T> entityClass, String whereStr, Object[] params) throws OperatorException;

	/**
	 * @param <T> 指定实体类型
	 * @param sql SQL语句
	 * @param params SQL参数集合
	 * @return 计算查询结果总记录数量
	 * @throws OperatorException
	 */
	public <T> long getAmount(String sql, Object[] params) throws OperatorException;

}
