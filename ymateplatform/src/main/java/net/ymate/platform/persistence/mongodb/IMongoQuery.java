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
import net.ymate.platform.persistence.support.PageResultSet;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * <p>
 * IMongoQuery
 * </p>
 * <p>
 * MongoDB复杂条件查询接口定义；
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
 *          <td>2014年2月6日下午5:42:40</td>
 *          </tr>
 *          </table>
 */
public interface IMongoQuery<T> {

	/**
	 * @return 获取当前使用的数据库连接对象
	 */
	public IMongoClientHolder getClientHolder();

	/**
	 * @return 获取当前使用的集合对象
	 */
	public DBCollection getCollection();

	/**
	 * @return 获取当前使用的集合名称
	 */
	public String getCollectionName();

	/**
	 * 设置结果集数据处理对象
	 * 
	 * @param handler
	 */
	public void setResultSetHandler(IMongoResultSetHandler<T> handler);

	/**
	 * @return 当前数据库连接是否可用
	 */
	public boolean isClientHolderAvailable();

	/**
	 * @return 当前结果集是否可用，即是否为空或元素数量为 0
	 */
	public boolean isResultSetAvailable();

	/**
	 * @return 获取当前的结果集，若当前操作者对象尚未被执行将抛出运行时异常
	 */
	public PageResultSet<T> getResultSet();

	/**
	 * @return 是否已执行过操作
	 */
	public boolean isExecuted();

	/**
	 * 执行查询操作
	 * 
	 * @return
	 * @throws OperatorException
	 */
	public IMongoQuery<T> executeQuery() throws OperatorException;

	/**
	 * 采用分页方式执行查询操作
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 * @throws OperatorException
	 */
	public IMongoQuery<T> executeQuery(int pageNumber, int pageSize) throws OperatorException;

	/**
	 * @return 返回符合查询条件的记录数
	 */
	public long executeCount();

	public List<?> executeDistinct(String key);

	/**
	 * @return 将查询条件转换为DBObject对象
	 */
	public DBObject toDBObject();

	//

	/**
	 * 条件：等于
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public IMongoQuery<T> eq(String key, Object value);

	/**
	 * 条件：不等于
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public IMongoQuery<T> notEq(String key, Object value);

	/**
	 * 条件：或
	 * 
	 * @param querys 子查询集合
	 * @return
	 */
	public IMongoQuery<T> or(IMongoQuery<?>...querys);

	/**
	 * 条件：与
	 * 
	 * @param querys 子查询集合
	 * @return
	 */
	public IMongoQuery<T> and(IMongoQuery<?>...querys);

	/**
	 * 条件：大于
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public IMongoQuery<T> greaterThan(String key, Object value);

	/**
	 * 条件：大于等于
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public IMongoQuery<T> greaterThanEq(String key, Object value);

	/**
	 * 条件：小于
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public IMongoQuery<T> lessThan(String key, Object value);

	/**
	 * 条件：小于等于
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public IMongoQuery<T> lessThanEq(String key, Object value);

	/**
	 * 条件：包含
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public IMongoQuery<T> in(String key, List<Object> values);

	/**
	 * 条件：包含
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public IMongoQuery<T> in(String key, Object...values);

	/**
	 * 条件：不包含
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public IMongoQuery<T> notIn(String key, List<Object> values);

	/**
	 * 条件：不包含
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public IMongoQuery<T> notIn(String key, Object...values);

	/**
	 * 条件：多元素匹配
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public IMongoQuery<T> all(String key, List<Object> values);

	/**
	 * 条件：多元素匹配
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public IMongoQuery<T> all(String key, Object...values);

	/**
	 * 条件：匹配数组长度
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public IMongoQuery<T> size(String key, int value);

	/**
	 * 条件：余数
	 * 
	 * @param key
	 * @param divisor
	 * @param remainder
	 * @return
	 */
	public IMongoQuery<T> mod(String key, int divisor, int remainder);

	/**
	 * 条件：正则表达式
	 * 
	 * @param key
	 * @param regex
	 * @return
	 */
	public IMongoQuery<T> regex(String key, String regex);

	/**
	 * 条件：where子句查询
	 * 
	 * @param whereStr
	 * @return
	 */
	public IMongoQuery<T> where(String whereStr);

	/**
	 * 条件：key属性存在
	 * 
	 * @param key
	 * @return
	 */
	public IMongoQuery<T> exists(String key);

	/**
	 * 条件：key属性不存在
	 * 
	 * @param key
	 * @return
	 */
	public IMongoQuery<T> notExists(String key);

	//

	/**
	 * 条件：查询点(x,y)附近
	 * 
	 * @param key
	 * @param x
	 * @param y
	 * @return
	 */
	public IMongoQuery<T> near(String key, double x, double y);

	/**
	 * 条件：查询点(x,y)附近
	 * 
	 * @param key
	 * @param x
	 * @param y
	 * @param maxDistance
	 * @return
	 */
	public IMongoQuery<T> near(String key, double x, double y, double maxDistance);

	/**
	 * 条件：查询圆形区域
	 * 
	 * @param key
	 * @param x
	 * @param y
	 * @param radius
	 * @return
	 */
	public IMongoQuery<T> withinCenter(String key, double x, double y, double radius);

	/**
	 * 条件：查询矩形区域
	 * 
	 * @param key
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public IMongoQuery<T> withinBox(String key, double x1, double y1, double x2, double y2);

	//

	public IMongoQuery<T> slice(String key, long number);

	public IMongoQuery<T> slice(String key, long skip, long length);

	/**
	 * 添加排序条件
	 * 
	 * @param order
	 * @return
	 */
	public IMongoQuery<T> sort(OrderBy order);

	/**
	 * 添加返回的属性键值
	 * 
	 * @param keys
	 * @return
	 */
	public IMongoQuery<T> returnFields(String...keys);

	/**
	 * 添加屏蔽的属性键值
	 * 
	 * @param keys
	 * @return
	 */
	public IMongoQuery<T> discardFields(String...keys);

}
