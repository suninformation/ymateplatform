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
package net.ymate.platform.persistence.mongodb.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.ymate.platform.persistence.base.ConnectionException;
import net.ymate.platform.persistence.base.OperatorException;
import net.ymate.platform.persistence.mongodb.IMongoClientHolder;
import net.ymate.platform.persistence.mongodb.MongoDB;
import net.ymate.platform.persistence.mongodb.MongoDB.OrderBy;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand.OutputType;
import com.mongodb.MapReduceOutput;

/**
 * <p>
 * IMongoDBHelper
 * </p>
 * <p>
 * MongoDB助手类；
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
 *          <td>2014年2月27日下午8:40:54</td>
 *          </tr>
 *          </table>
 */
public class MongoDBHelper {

	private IMongoClientHolder __holder;

	protected Map<String, DBCollection> __collectionCaches = new ConcurrentHashMap<String, DBCollection>();

	/**
	 * 构造器
	 * 
	 * @param holder DB对象持有者
	 */
	private MongoDBHelper(IMongoClientHolder holder) {
		this.__holder = holder;
	}

	/**
	 * @param holder DB对象持有者
	 * @return 基于已存在的DB对象创建助手类对象
	 */
	public static MongoDBHelper bind(IMongoClientHolder holder) {
		return new MongoDBHelper(holder);
	}

	/**
	 * @param dsName 数据源名称
	 * @param dbName 数据库名称
	 * @return 创建并返回基于指定数据源的助手类对象
	 * @throws ConnectionException
	 */
	public static MongoDBHelper bind(String dsName, String dbName) throws ConnectionException {
		return new MongoDBHelper(MongoDB.getMongoClientHolder(dsName, dbName));
	}

	/**
	 * @param dbName 预绑定的数据库名称
	 * @return 创建并返回基于默认数据源助手类对象
	 * @throws ConnectionException
	 */
	public static MongoDBHelper bind(String dbName) throws ConnectionException {
		return new MongoDBHelper(MongoDB.getMongoClientHolder(dbName));
	}

	/**
	 * @return 获取数据库对象
	 */
	public IMongoClientHolder getClientHolder() {
		return __holder;
	}

	/**
	 * 开启数据库连接
	 */
	public void begin() {
		__holder.requestStart();
	}

	/**
	 * 保持数据库连接
	 */
	public void keep() {
		__holder.requestEnsureConnection();
	}

	/**
	 * 结束数据库连接
	 */
	public void end() {
		__holder.requestDone();
	}

	/**
	 * 释放当前助手类对象资源
	 */
	public void release() {
		__holder.release();
		__holder = null;
		//
		__collectionCaches.clear();
		__collectionCaches = null;
	}

	/**
	 * 删除默认数据源中的数据库
	 * 
	 * @param dbName 数据库名称
	 */
	public static void drapDatabase(String dbName) {
		MongoDB.getCachedMongoClient(MongoDB.DATASOURCE_DEFAULT_NAME).dropDatabase(dbName);
	}

	/**
	 * 删除指定数据源中的数据库
	 * 
	 * @param dsName 数据源名称
	 * @param dbName 数据库名称
	 */
	public static void drapDatabase(String dsName, String dbName) {
		MongoDB.getCachedMongoClient(dsName).dropDatabase(dbName);
	}

	/**
	 * 创建集合
	 * 
	 * @param name 集合名称
	 * @return 返回新创建的集合对象
	 */
	public DBCollection createCollection(String name) {
		return __holder.getDB().createCollection(name, new BasicDBObject());
	}

	/**
	 * 创建copped类型的集合
	 * 
	 * @param name 集合名称
	 * @param size
	 * @param max
	 * @return 返回新创建的集合对象
	 */
	public DBCollection createCollection(String name, long size, long max) {
		DBObject _options = new BasicDBObject();
		_options.put("copped", true);
		if (size > 0) {
			_options.put("size", size);
		}
		if (max > 0) {
			_options.put("max", max);
		}
		return __holder.getDB().createCollection(name, _options);
	}

	public DBCollection getCollection(String name) {
		DBCollection _collection = __collectionCaches.get(name);
		if (_collection == null) {
			_collection = __holder.getDB().getCollection(name);
			if (_collection != null) {
				__collectionCaches.put(name, _collection);
			}
		}
		return _collection;
	}

	public boolean isCollectionExists(String name) {
		return __collectionCaches.containsKey(name) || __holder.getDB().collectionExists(name);
	}

	/**
	 * 清空集合中的数据
	 * 
	 * @param name 集合名称
	 */
	public void clearCollection(String name) {
		__holder.getDB().getCollection(name).remove(new BasicDBObject(), __holder.getWriteConcern());
	}

	/**
	 * 删除集合
	 * 
	 * @param name 集合名称
	 */
	public void dropCollection(String name) {
		getCollection(name).drop();
		getCollection(name).dropIndexes();
	}

	public Iterable<DBObject> mapReduce(String collectionName, String map, String reduce, DBObject query) throws OperatorException {
		MapReduceOutput _output = getCollection(collectionName).mapReduce(map, reduce, null, OutputType.INLINE, query);
		CommandResult _result = _output.getCommandResult();
		if (!_result.ok()) {
			throw new OperatorException(_result.getErrorMessage());
		}
		return _output.results();
	}

	public Iterable<DBObject> mapReduce(String collectionName, String map, String reduce, String outputTarget, OutputType type, OrderBy order, DBObject query) throws OperatorException {
		return mapReduce(collectionName, map, reduce, outputTarget, type, order, 0, 0, query);
	}

	public Iterable<DBObject> mapReduce(String collectionName, String map, String reduce, String outputTarget, OutputType type, OrderBy order, int pageNumber, int pageSize, DBObject query) throws OperatorException {
		MapReduceOutput _output = getCollection(collectionName).mapReduce(map, reduce, outputTarget, type, query);
		CommandResult _result = _output.getCommandResult();
		if (!_result.ok()) {
			throw new OperatorException(_result.getErrorMessage());
		}
		DBCollection _collection = _output.getOutputCollection();
		DBCursor _cursor = null;
		if (order != null) {
			_cursor = _collection.find().sort(order.toDBObject());
		} else {
			_cursor = _collection.find();
		}
		if (pageNumber > 0 && pageSize > 0) {
			_cursor.skip((pageNumber - 1) * pageSize).limit(pageSize);
		}
		List<DBObject> _results = new ArrayList<DBObject>();
		for (Iterator<DBObject> _it = _cursor.iterator(); _it.hasNext();) {
			_results.add(_it.next());
		}
		return _results;
	}

	


}
