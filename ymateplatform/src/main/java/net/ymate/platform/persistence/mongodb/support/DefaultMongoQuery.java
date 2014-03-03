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
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.persistence.base.OperatorException;
import net.ymate.platform.persistence.mongodb.IMongoClientHolder;
import net.ymate.platform.persistence.mongodb.IMongoQuery;
import net.ymate.platform.persistence.mongodb.IMongoResultSetHandler;
import net.ymate.platform.persistence.mongodb.MongoDB.OPT;
import net.ymate.platform.persistence.mongodb.MongoDB.OrderBy;
import net.ymate.platform.persistence.support.PageResultSet;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * <p>
 * DefaultMongoQuery
 * </p>
 * <p>
 * MongoDB复杂条件查询接口默认实现类；
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
 *          <td>2014年2月7日下午3:32:20</td>
 *          </tr>
 *          </table>
 */
public class DefaultMongoQuery<T> implements IMongoQuery<T> {

	private IMongoClientHolder __clientHolder;

	private IMongoResultSetHandler<T> __handler;

	private PageResultSet<T> __resultSet;

	private String __collectionName;

	private DBCollection __collection;

	private DBObject __condition = new BasicDBObject();

	private DBObject __orderBy;

	private DBObject __customFields;

	/**
	 * 是否已执行过
	 */
	private boolean __isExecuted;

	/**
	 * 构造器
	 * 
	 * @param handler 结果集处理器
	 * @param collectionName 集合名称
	 */
	public DefaultMongoQuery(IMongoClientHolder clientHolder, IMongoResultSetHandler<T> handler, String collectionName) {
		this.__clientHolder = clientHolder;
		this.__handler = handler;
		this.__collectionName = collectionName;
		//
		this.__collection = __clientHolder.getDB().getCollection(__collectionName);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#getClientHolder()
	 */
	public IMongoClientHolder getClientHolder() {
		return __clientHolder;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#getCollection()
	 */
	public DBCollection getCollection() {
		return __collection;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#getCollectionName()
	 */
	public String getCollectionName() {
		return __collectionName;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#setResultSetHandler(net.ymate.platform.persistence.mongodb.IMongoResultSetHandler)
	 */
	public void setResultSetHandler(IMongoResultSetHandler<T> handler) {
		this.__handler = handler;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#isClientHolderAvailable()
	 */
	public boolean isClientHolderAvailable() {
		return (__clientHolder == null ? false : true) && (__clientHolder.getDB() == null ? false : true);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#isResultSetAvailable()
	 */
	public boolean isResultSetAvailable() {
		if (!this.isExecuted()) {
			throw new Error(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.need_execute"));
		}
		return (__resultSet != null && !__resultSet.getResultSet().isEmpty());
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#getResultSet()
	 */
	public PageResultSet<T> getResultSet() {
		if (!this.isExecuted()) {
			throw new Error(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.need_execute"));
		}
		return __resultSet;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#isExecuted()
	 */
	public boolean isExecuted() {
		return __isExecuted;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#executeQuery()
	 */
	public IMongoQuery<T> executeQuery() throws OperatorException {
		return executeQuery(0, 0);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#executeQuery(int, int)
	 */
	public IMongoQuery<T> executeQuery(int pageNumber, int pageSize) throws OperatorException {
		if (!__isExecuted) {
			List<T> _results = new ArrayList<T>();
			Long _recordCount = 0L;
			if (__orderBy != null || pageNumber != 0 || pageSize != 0) {
				DBCursor _cursor = __collection.find(this.__condition, __customFields);
				if (__orderBy != null) {
					_cursor.sort(__orderBy);
				}
				if (pageNumber > 0 && pageSize > 0) {
					_cursor.skip((pageNumber - 1) * pageSize).limit(pageSize);
					_recordCount = executeCount();
				}
				while (_cursor.hasNext()) {
					DBObject _obj = _cursor.next();
					_results.add(__handler.handle(_obj));
				}
			} else {
				DBObject _obj = __collection.findOne(this.__condition, __customFields);
				if (_obj != null) {
					_results.add(__handler.handle(_obj));
				}
			}
			__isExecuted = true;
			this.__resultSet = new PageResultSet<T>(_results, pageNumber, pageSize, _recordCount.intValue());
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#executeCount()
	 */
	public long executeCount() {
		return __collection.count(__condition);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#executeDistinct(java.lang.String)
	 */
	public List<?> executeDistinct(String key) {
		return __collection.distinct(key, __condition);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#toDBObject()
	 */
	public DBObject toDBObject() {
		return __condition;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#eq(java.lang.String, java.lang.Object)
	 */
	public IMongoQuery<T> eq(String key, Object value) {
		__doCondition(key, null, value);
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#notEq(java.lang.String, java.lang.Object)
	 */
	public IMongoQuery<T> notEq(String key, Object value) {
		__doCondition(key, OPT.NE, value);
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#or(net.ymate.platform.persistence.mongodb.IMongoQuery[])
	 */
	public IMongoQuery<T> or(IMongoQuery<?>... querys) {
		@SuppressWarnings("unchecked")
		List<DBObject> _querys = (List<DBObject>) __condition.get(OPT.AND);
		if (_querys == null) {
			_querys = new ArrayList<DBObject>();
			__condition.put(OPT.OR, _querys);
		}
		for (IMongoQuery<?> _query : querys) {
			_querys.add(_query.toDBObject());
		}
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#and(net.ymate.platform.persistence.mongodb.IMongoQuery[])
	 */
	public IMongoQuery<T> and(IMongoQuery<?>... querys) {
		@SuppressWarnings("unchecked")
		List<DBObject> _querys = (List<DBObject>) __condition.get(OPT.AND);
		if (_querys == null) {
			_querys = new ArrayList<DBObject>();
			__condition.put(OPT.AND, _querys);
		}
		for (IMongoQuery<?> _query : querys) {
			_querys.add(_query.toDBObject());
		}
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#greaterThan(java.lang.String, java.lang.Object)
	 */
	public IMongoQuery<T> greaterThan(String key, Object value) {
		__doCondition(key, OPT.GT, value);
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#greaterThanEq(java.lang.String, java.lang.Object)
	 */
	public IMongoQuery<T> greaterThanEq(String key, Object value) {
		__doCondition(key, OPT.GTE, value);
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#lessThan(java.lang.String, java.lang.Object)
	 */
	public IMongoQuery<T> lessThan(String key, Object value) {
		__doCondition(key, OPT.LT, value);
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#lessThanEq(java.lang.String, java.lang.Object)
	 */
	public IMongoQuery<T> lessThanEq(String key, Object value) {
		__doCondition(key, OPT.LTE, value);
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#in(java.lang.String, java.util.List)
	 */
	public IMongoQuery<T> in(String key, List<Object> values) {
		if (values == null) {
			values = Collections.emptyList();
		}
		return in(key, values.toArray());
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#in(java.lang.String, java.lang.Object[])
	 */
	public IMongoQuery<T> in(String key, Object... values) {
		__doCondition(key, OPT.IN, values);
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#notIn(java.lang.String, java.util.List)
	 */
	public IMongoQuery<T> notIn(String key, List<Object> values) {
		if (values == null) {
			values = Collections.emptyList();
		}
		return notIn(key, values.toArray());
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#notIn(java.lang.String, java.lang.Object[])
	 */
	public IMongoQuery<T> notIn(String key, Object... values) {
		__doCondition(key, OPT.NIN, values);
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#all(java.lang.String, java.util.List)
	 */
	public IMongoQuery<T> all(String key, List<Object> values) {
		if (values == null) {
			values = Collections.emptyList();
		}
		return all(key, values.toArray());
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#all(java.lang.String, java.lang.Object[])
	 */
	public IMongoQuery<T> all(String key, Object... values) {
		__doCondition(key, OPT.ALL, values);
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#size(java.lang.String, int)
	 */
	public IMongoQuery<T> size(String key, int value) {
		__doCondition(key, OPT.SIZE, value);
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#mod(java.lang.String, int, int)
	 */
	public IMongoQuery<T> mod(String key, int divisor, int remainder) {
		__doCondition(key, OPT.MOD, new int[] { divisor, remainder });
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#regex(java.lang.String, java.lang.String)
	 */
	public IMongoQuery<T> regex(String key, String regex) {
		__doCondition(key, null, Pattern.compile(regex));
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#where(java.lang.String)
	 */
	public IMongoQuery<T> where(String whereStr) {
		__doCondition(OPT.WHERE, null, whereStr);
		return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#exists(java.lang.String)
	 */
	public IMongoQuery<T> exists(String key) {
		__doCondition(key, OPT.EXISTS, Boolean.TRUE);
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#notExists(java.lang.String)
	 */
	public IMongoQuery<T> notExists(String key) {
		__doCondition(key, OPT.EXISTS, Boolean.FALSE);
        return this;
	}

	// ---------------------------

	/*　(non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#near(java.lang.String, double, double)
	 */
	public IMongoQuery<T> near(String key, double x, double y) {
		__doCondition(key, OPT.NEAR, new Double[] { x, y });
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#near(java.lang.String, double, double, double)
	 */
	public IMongoQuery<T> near(String key, double x, double y, double maxDistance) {
		__doCondition(key, OPT.NEAR, new Double[] { x, y, maxDistance });
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#withinCenter(java.lang.String, double, double, double)
	 */
	public IMongoQuery<T> withinCenter(String key, double x, double y, double radius) {
		__doCondition(key, OPT.WITHIN, new BasicDBObject(OPT.CENTER, new Object[] { new Double[] { x, y }, radius }));
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#withinBox(java.lang.String, double, double, double, double)
	 */
	public IMongoQuery<T> withinBox(String key, double x1, double y1, double x2, double y2) {
		__doCondition(key, OPT.WITHIN, new BasicDBObject(OPT.BOX, new Object[] { new Double[] { x1, y1 }, new Double[] { x2, y2 } }));
    	return this;
	}

	private void __doCondition(String key, String opt, Object value) {
		if (opt == null) {
			__condition.put(key, value);
			return;
		}
		DBObject _object = null;
		if (!(__condition.get(key) instanceof DBObject)) {
			_object = new BasicDBObject(opt, value);
			__condition.put(key, _object);
		} else {
			_object = (DBObject) __condition.get(key);
			_object.put(opt, value);
		}
	}

	// ---------------------------

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#slice(java.lang.String, long)
	 */
	public IMongoQuery<T> slice(String key, long number) {
		return __doSlice(key, new BasicDBObject(OPT.SLICE, number));
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#slice(java.lang.String, long, long)
	 */
	public IMongoQuery<T> slice(String key, long skip, long length) {
		return __doSlice(key, new BasicDBObject(OPT.SLICE, new Long[] { skip, length }));
	}

	private IMongoQuery<T> __doSlice(String key, DBObject object) {
        __customFields.put(key, object);
        return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#sort(net.ymate.platform.persistence.mongodb.MongoDB.OrderBy)
	 */
	public IMongoQuery<T> sort(OrderBy order) {
		__orderBy = order.toDBObject();
		return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#returnFields(java.lang.String[])
	 */
	public IMongoQuery<T> returnFields(String... keys) {
		for (String key : keys) {
			__doCustomFields(key, 1);
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoQuery#discardFields(java.lang.String[])
	 */
	public IMongoQuery<T> discardFields(String... keys) {
		for (String key : keys) {
			__doCustomFields(key, 0);
		}
		return this;
	}

	private IMongoQuery<T> __doCustomFields(String key, int value) {
		if (__customFields == null) {
			__customFields = new BasicDBObject();
		}
		if (__customFields.get(key) == null) {
			__customFields.put(key, value);
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		DBObject _tmp = new BasicDBObject();
		_tmp.put("ds", __clientHolder.getDataSourceName());
		_tmp.put("db", __clientHolder.getDB().getName());
		_tmp.put("collection", __collectionName);
		_tmp.put("condition", __condition);
		_tmp.put("order", __orderBy);
		_tmp.put("fields", __customFields);
		return _tmp.toString();
	}
}
