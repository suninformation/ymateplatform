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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.ClassUtils.ClassBeanWrapper;
import net.ymate.platform.persistence.mongodb.MongoDB;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * <p>
 * MongoEntitySupport
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
 *          <td>2014年2月16日下午5:54:50</td>
 *          </tr>
 *          </table>
 */
public class MongoEntitySupport {

	/**
	 * 实体模型元数据描述类缓存
	 */
	private static Map<Class<?>, MongoEntityMeta> __cacheEntityMetas = new ConcurrentHashMap<Class<?>, MongoEntityMeta>();

	public static MongoEntityMeta getEntityMeta(Class<?> entity) {
		MongoEntityMeta _meta = __cacheEntityMetas.get(entity);
		if (_meta == null) {
			_meta = new MongoEntityMeta(entity);
			__cacheEntityMetas.put(entity, _meta);
		}
		return _meta;
	}

	public static String getEntityName(Class<?> entity) {
		return getEntityMeta(entity).getTableName();
	}

	public static DBObject getEntityFields(Class<?> entity) {
		DBObject _keys = new BasicDBObject();
		for (String _c : getEntityMeta(entity).getColumnNames()) {
			_keys.put(_c, 1);
		}
		return _keys;
	}

	public static <T> T randerToEntity(Class<T> entity, DBObject object) {
		MongoEntityMeta _meta = getEntityMeta(entity);
		if (_meta.isCompositeKey()) {
			// Not Support...
			throw new UnsupportedOperationException("Do not support composite keys of entity bean.");
		}
		ClassBeanWrapper<T> _returnValue = ClassUtils.wrapper(entity);
		for (String key : _meta.getColumnNames()) {
			Object value = object.get(key);
			if (value == null) {
				continue;
			}
			String _classAttr = _meta.getClassAttributeMap().get(key);
			_returnValue.setValue(_classAttr, new BlurObject(value).toObjectValue(_returnValue.getFieldType(_classAttr)));
		}
		return _returnValue.getTarget();
	}

	public static <T> List<T> randerToEntities(Class<T> entity, DBCursor cursor) {
		List<T> _resultList = new ArrayList<T>();
		while (cursor.hasNext()) {
			DBObject _object = cursor.next();
			_resultList.add(randerToEntity(entity, _object));
		}
		cursor.close();
		return _resultList;
	}

	public static <T> List<T> randerToEntities(Class<T> entity, Collection<DBObject> objects) {
		List<T> _resultList = new ArrayList<T>();
        for (DBObject _object : objects) {
        	_resultList.add(randerToEntity(entity, _object));
        }
        return _resultList;
	}

	public static DBObject randerToDBObject(Object object) {
		MongoEntityMeta _meta = getEntityMeta(object.getClass());
		ClassBeanWrapper<?> _wrapper = ClassUtils.wrapper(object);
		DBObject _returnObj = new BasicDBObject();
		for (String _key : _meta.getColumnNames()) {
			Object _value = _wrapper.getValue(_meta.getClassAttributeMap().get(_key));
			if (_value == null) {
				_returnObj.put(_key, null);
				continue;
			}
			if (_key.equals(MongoDB.OPT.ID)) {
				_returnObj.put(_key, new ObjectId(_value.toString()));
			} else {
				_returnObj.put(_key, _value);
			}
		}
		return _returnObj;
	}

}
