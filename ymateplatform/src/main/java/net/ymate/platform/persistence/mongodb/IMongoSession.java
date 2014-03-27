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
package net.ymate.platform.persistence.mongodb;

import java.util.List;

import net.ymate.platform.persistence.base.OperatorException;
import net.ymate.platform.persistence.mongodb.MongoDB.OrderBy;
import net.ymate.platform.persistence.support.ISessionEvent;
import net.ymate.platform.persistence.support.PageResultSet;

import com.mongodb.WriteResult;

/**
 * <p>
 * IMongoSession
 * </p>
 * <p>
 * 
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
 *          <td>2014年2月6日下午2:40:10</td>
 *          </tr>
 *          </table>
 */
public interface IMongoSession {

	/**
	 * @return 获取会话对象唯一标识ID
	 */
	public String getId();

	/**
	 * @return 获取数据库对象
	 */
	public IMongoClientHolder getClientHolder();

	/**
	 * 设置会话事件处理器
	 * 
	 * @param event 事件处理器接口
	 * @return 会话对象
	 */
	public IMongoSession setSessionEvent(ISessionEvent event);

	/**
	 * 关闭/释放会话
	 */
	public void close();

//	/**
//	 * 
//	 * @param entity
//	 * @throws OperatorException
//	 */
//	public <T> void drop(Class<T> entity) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @return 根据实体执行查询，返回全部结果数据
	 * @throws OperatorException
	 */
	public <T> List<T> findAll(Class<T> entity) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param orderBy 排序条件
	 * @return 根据实体执行查询，返回全部结果数据
	 * @throws OperatorException
	 */
	public <T> List<T> findAll(Class<T> entity, OrderBy orderBy) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param pageSize 分页大小
	 * @param page 页号
	 * @return 根据实体执行分页查询
	 * @throws OperatorException
	 */
	public <T> PageResultSet<T> findAll(Class<T> entity, int pageSize, int page) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param orderBy 排序条件
	 * @param pageSize 分页大小
	 * @param page 页号
	 * @return 根据实体执行分页查询
	 * @throws OperatorException
	 */
	public <T> PageResultSet<T> findAll(Class<T> entity, OrderBy orderBy, int pageSize, int page) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param key 属性名称
	 * @param value 属性值
	 * @return 根据指定key条件执行查询，返回结果集中第一条数据
	 * @throws OperatorException
	 */
	public <T> T findFirst(Class<T> entity, String key, Object value) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param id 记录Id
	 * @return 通过ID查找指定的实体对象
	 * @throws OperatorException
	 */
	public <T> T find(Class<T> entity, String id) throws OperatorException;

	/**
	 * @param <T> 指定实体类型
	 * @param entityClass 实体类对象
	 * @return 计算查询结果总记录数量
	 * @throws OperatorException
	 */
	public <T> long getAmount(Class<T> entity) throws OperatorException;

	/**
	 * @param <T> 指定实体类型
	 * @param entityClass 实体类对象
	 * @param key 属性名称
	 * @param value 属性值
	 * @return 计算查询结果总记录数量
	 * @throws OperatorException
	 */
	public <T> long getAmount(Class<T> entity, String key, Object value) throws OperatorException;

	public <T> List<?> distinct(Class<T> entity, String key) throws OperatorException;

	public <T> boolean exists(Class<T> entity, String id) throws OperatorException;

	public <T> boolean exists(Class<T> entity, String key, Object value) throws OperatorException;


	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @return 根据实体执行更新，返回更新后的实体对象
	 * @throws OperatorException
	 */
	public <T> WriteResult update(T entity) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @param fieldFilter 更新字段过滤集合
	 * @return 根据实体执行更新，返回更新后的实体对象
	 * @throws OperatorException
	 */
	public <T> WriteResult update(T entity, String[] fieldFilter) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entities 实体对象集合
	 * @return 根据实体执行批量更新，返回更新后的实体对象集合
	 * @throws OperatorException
	 */
	public <T> WriteResult[] updateAll(List<T> entities) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entities 实体对象集合
	 * @param fieldFilter 更新字段过滤集合
	 * @return 根据实体执行批量更新，返回更新后的实体对象集合
	 * @throws OperatorException
	 */
	public <T> WriteResult[] updateAll(List<T> entities, String[] fieldFilter) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @return 根据实体执行记录插入，返回插入后的实体对象
	 * @throws OperatorException
	 */
	public <T> WriteResult insert(T entity) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entities 实体对象集合
	 * @return 根据实体执行记录批量插入，返回插入后的实体对象集合
	 * @throws OperatorException
	 */
	public <T> WriteResult insertAll(List<T> entities) throws OperatorException;

//	/**
//	 * @param <T> 指定结果集数据类型
//	 * @param entity 实体对象
//	 * @return 根据实体执行记录插入，若已存在则更新
//	 * @throws OperatorException
//	 */
//	public <T> WriteResult saveOrUpdate(T entity) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entity 实体对象
	 * @return 根据实体执行记录删除，返回删除后的实体对象
	 * @throws OperatorException
	 */
	public <T> WriteResult delete(T entity) throws OperatorException;

	public <T> WriteResult delete(Class<T> entityClass, Object id) throws OperatorException;

	/**
	 * @param <T> 指定结果集数据类型
	 * @param entities 实体对象集合
	 * @return 根据实体执行记录批量删除，返回删除后的实体对象集合
	 * @throws OperatorException
	 */
	public <T> WriteResult deleteAll(List<T> entities) throws OperatorException;

	public <T> WriteResult deleteAll(Class<T> entityClass, List<Object> ids) throws OperatorException;

	public <T> WriteResult deleteAll(Class<T> entityClass, Object[] ids) throws OperatorException;

}
