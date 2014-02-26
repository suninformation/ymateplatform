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
import java.util.List;

import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.persistence.base.ConnectionException;
import net.ymate.platform.persistence.base.OperatorException;
import net.ymate.platform.persistence.mongodb.IMongoClientHolder;
import net.ymate.platform.persistence.mongodb.IMongoQuery;
import net.ymate.platform.persistence.mongodb.IMongoResultSetHandler;
import net.ymate.platform.persistence.mongodb.IMongoSession;
import net.ymate.platform.persistence.mongodb.IMongoSessionEvent;
import net.ymate.platform.persistence.mongodb.MongoDB;
import net.ymate.platform.persistence.mongodb.MongoDB.OrderBy;
import net.ymate.platform.persistence.support.PageResultSet;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

/**
 * <p>
 * DefaultMongoSession
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
 *          <td>2014年2月6日下午3:15:40</td>
 *          </tr>
 *          </table>
 */
public class DefaultMongoSession implements IMongoSession {

	private String __id;

	private IMongoClientHolder __clientHolder;

	private IMongoSessionEvent __sessionEvent;

	/**
	 * 构造器
	 * 
	 * @param clientHolder
	 */
	public DefaultMongoSession(IMongoClientHolder clientHolder) {
		this.__id = UUIDUtils.uuid();
		this.__clientHolder = clientHolder;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#getId()
	 */
	public String getId() {
		return __id;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#getClientHolder()
	 */
	public IMongoClientHolder getClientHolder() {
		return __clientHolder;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#setSessionEvent(net.ymate.platform.persistence.mongodb.IMongoSessionEvent)
	 */
	public IMongoSession setSessionEvent(IMongoSessionEvent event) {
		this.__sessionEvent = event;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#close()
	 */
	public void close() {
		__clientHolder.release();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#findAll(java.lang.Class)
	 */
	public <T> List<T> findAll(Class<T> entity) throws OperatorException {
		DBCursor _cursor = __clientHolder
				.getDB()
				.getCollection(MongoEntitySupport.getEntityName(entity))
				.find(new BasicDBObject());
        return MongoEntitySupport.randerToEntities(entity, _cursor);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#findAll(java.lang.Class, net.ymate.platform.persistence.mongodb.MongoDB.OrderBy)
	 */
	public <T> List<T> findAll(Class<T> entity, OrderBy orderBy) throws OperatorException {
		DBCursor _cursor = __clientHolder
				.getDB()
				.getCollection(MongoEntitySupport.getEntityName(entity))
				.find(new BasicDBObject()).sort(orderBy.toDBObject());
        return MongoEntitySupport.randerToEntities(entity, _cursor);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#findAll(java.lang.Class, int, int)
	 */
	public <T> PageResultSet<T> findAll(final Class<T> entity, int pageSize, int page) throws OperatorException {
		try {
			IMongoQuery<T> _query = MongoDB.createQuery(__clientHolder, MongoEntitySupport.getEntityName(entity), new IMongoResultSetHandler<T>() {
	
				public T handle(DBObject object) throws OperatorException {
					return MongoEntitySupport.randerToEntity(entity, object);
				}
				
			}).executeQuery(page, pageSize);
	        return _query.getResultSet();
		} catch (ConnectionException e) {
			throw new OperatorException(RuntimeUtils.unwrapThrow(e));
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#findAll(java.lang.Class, net.ymate.platform.persistence.mongodb.MongoDB.OrderBy, int, int)
	 */
	public <T> PageResultSet<T> findAll(final Class<T> entity, OrderBy orderBy, int pageSize, int page) throws OperatorException {
		try {
			IMongoQuery<T> _query = MongoDB.createQuery(__clientHolder, MongoEntitySupport.getEntityName(entity), new IMongoResultSetHandler<T>() {
	
				public T handle(DBObject object) throws OperatorException {
					return MongoEntitySupport.randerToEntity(entity, object);
				}
				
			}).sort(orderBy).executeQuery(page, pageSize);
	        return _query.getResultSet();
		} catch (ConnectionException e) {
			throw new OperatorException(RuntimeUtils.unwrapThrow(e));
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#findFirst(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	public <T> T findFirst(Class<T> entity, String key, Object value) throws OperatorException {
		DBObject _query = new BasicDBObject(key, value);
        DBObject _result = __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entity)).findOne(_query);
        return MongoEntitySupport.randerToEntity(entity, _result);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#find(java.lang.Class, java.lang.String)
	 */
	public <T> T find(Class<T> entity, String id) throws OperatorException {
		DBObject _obj = new BasicDBObject();
        _obj.put(MongoDB.OPT.ID, new ObjectId(id));
        DBObject _result = __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entity)).findOne(_obj);
		return MongoEntitySupport.randerToEntity(entity, _result);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#getAmount(java.lang.Class)
	 */
	public <T> long getAmount(Class<T> entity) throws OperatorException {
		return __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entity)).count();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#getAmount(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	public <T> long getAmount(Class<T> entity, String key, Object value) throws OperatorException {
		return __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entity)).count(new BasicDBObject(key, value));
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#distinct(java.lang.Class, java.lang.String)
	 */
	public <T> List<?> distinct(Class<T> entity, String key) throws OperatorException {
		return __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entity)).distinct(key);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#exists(java.lang.Class, java.lang.String)
	 */
	public <T> boolean exists(Class<T> entity, String id) throws OperatorException {
		DBObject _obj = new BasicDBObject();
        _obj.put(MongoDB.OPT.ID, new ObjectId(id));
        return __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entity)).findOne(_obj) != null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#exists(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	public <T> boolean exists(Class<T> entity, String key, Object value) throws OperatorException {
		DBObject _obj = new BasicDBObject(key, value);
        return __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entity)).findOne(_obj) != null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#update(java.lang.Object)
	 */
	public <T> WriteResult update(T entity) throws OperatorException {
		DBObject _value = MongoEntitySupport.randerToDBObject(entity);
		DBObject _cond = new BasicDBObject(MongoDB.OPT.ID, _value.removeField(MongoDB.OPT.ID));
		DBObject _set = new BasicDBObject(MongoDB.OPT.SET, _value);
		return __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entity.getClass())).update(_cond, _set);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#update(java.lang.Object, java.lang.String[])
	 */
	public <T> WriteResult update(T entity, String[] fieldFilter) throws OperatorException {
		DBObject _value = MongoEntitySupport.randerToDBObject(entity);
		DBObject _cond = new BasicDBObject(MongoDB.OPT.ID, _value.removeField(MongoDB.OPT.ID));
		DBObject _set = null;
		if (fieldFilter != null && fieldFilter.length > 0) {
			DBObject _filterValue = new BasicDBObject();
			for (String _field : fieldFilter) {
				if (_field.equals(MongoDB.OPT.ID)) {
					continue;
				}
				_filterValue.put(_field, _value.get(_field));
			}
			_set = new BasicDBObject(MongoDB.OPT.SET, _filterValue.keySet().isEmpty() ? _value : _filterValue);
		} else {
			_set = new BasicDBObject(MongoDB.OPT.SET, _value);
		}
		return __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entity.getClass())).update(_cond, _set);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#updateAll(java.util.List)
	 */
	public <T> WriteResult[] updateAll(List<T> entities) throws OperatorException {
		List<WriteResult> _results = new ArrayList<WriteResult>(entities.size());
		for (T _entity : entities) {
			_results.add(update(_entity));
		}
		return _results.toArray(new WriteResult[0]);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#updateAll(java.util.List, java.lang.String[])
	 */
	public <T> WriteResult[] updateAll(List<T> entities, String[] fieldFilter) throws OperatorException {
		List<WriteResult> _results = new ArrayList<WriteResult>(entities.size());
		for (T _entity : entities) {
			_results.add(update(_entity, fieldFilter));
		}
		return _results.toArray(new WriteResult[0]);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#insert(java.lang.Object)
	 */
	public <T> WriteResult insert(T entity) throws OperatorException {
		DBObject _obj = MongoEntitySupport.randerToDBObject(entity);
        WriteResult _result = __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entity.getClass())).insert(_obj);
        String _id = _obj.get(MongoDB.OPT.ID).toString();
        ClassUtils.wrapper(entity).setValue("id", _id);
        // Events
        if (__sessionEvent != null) {
        	// ----
        }
        return _result;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#insertAll(java.util.List)
	 */
	public <T> WriteResult insertAll(List<T> entities) throws OperatorException {
		List<DBObject> _objList = new ArrayList<DBObject>();
        for(T _entity : entities){
        	_objList.add(MongoEntitySupport.randerToDBObject(_entity));
        }
        WriteResult _result = __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entities.get(0).getClass())).insert(_objList);
		for (int _idx = 0; _idx < _objList.size(); _idx++) {
            String _id = _objList.get(_idx).get(MongoDB.OPT.ID).toString();
            ClassUtils.wrapper(_objList.get(_idx)).setValue("id", _id);
        }
        return _result;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#delete(java.lang.Object)
	 */
	public <T> WriteResult delete(T entity) throws OperatorException {
		DBObject _obj = new BasicDBObject(MongoDB.OPT.ID, ClassUtils.wrapper(entity).getValue("id"));
        return __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entity.getClass())).remove(_obj);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#delete(java.lang.Class, java.lang.Object)
	 */
	public <T> WriteResult delete(Class<T> entityClass, Object id) throws OperatorException {
		DBObject _obj = new BasicDBObject(MongoDB.OPT.ID, id);
        return __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entityClass)).remove(_obj);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#deleteAll(java.util.List)
	 */
	public <T> WriteResult deleteAll(List<T> entities) throws OperatorException {
		List<Object> _ids = new ArrayList<Object>();
		for (T _entity : entities) {
			_ids.add(ClassUtils.wrapper(_entity).getValue("id"));
		}
		return deleteAll(entities.get(0).getClass(), _ids);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#deleteAll(java.lang.Class, java.util.List)
	 */
	public <T> WriteResult deleteAll(Class<T> entityClass, List<Object> ids) throws OperatorException {
		DBObject _in = new BasicDBObject(MongoDB.OPT.IN, ids);
		return __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entityClass)).remove(new BasicDBObject(MongoDB.OPT.ID, _in));
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.mongodb.IMongoSession#deleteAll(java.lang.Class, java.lang.Object[])
	 */
	public <T> WriteResult deleteAll(Class<T> entityClass, Object[] ids) throws OperatorException {
		DBObject _in = new BasicDBObject(MongoDB.OPT.IN, ids);
		return __clientHolder.getDB().getCollection(MongoEntitySupport.getEntityName(entityClass)).remove(new BasicDBObject(MongoDB.OPT.ID, _in));
	}

}
