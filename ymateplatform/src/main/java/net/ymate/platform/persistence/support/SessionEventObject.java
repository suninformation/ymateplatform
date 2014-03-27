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
package net.ymate.platform.persistence.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;

/**
 * <p>
 * SessionEventObject
 * </p>
 * <p>
 * 会话事件封装类；
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
 *          <td>2014年3月12日下午4:17:32</td>
 *          </tr>
 *          </table>
 */
public class SessionEventObject extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1388612883614401105L;

	/**
	 * <p>
	 * EventType
	 * </p>
	 * <p>
	 * 事件类型；
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
	 *          <td>2014年3月12日下午9:52:19</td>
	 *          </tr>
	 *          </table>
	 */
	public enum EventType {
		INSERT, INSERT_BATCH, UPDATE, UPDATE_BATCH, REMOVE, REMOVE_BATCH
	}

	private Class<?> __sourceClass;

	private EventType __eventType;

	private List<Object> __extraParams = new ArrayList<Object>();

	/**
	 * 构造器
	 * 
	 * @param source
	 */
	public SessionEventObject(Object source) {
		super(source);
	}

	public Class<?> getSourceClass() {
		return __sourceClass;
	}

	public SessionEventObject setSourceClass(Class<?> sourceClass) {
		this.__sourceClass = sourceClass;
		return this;
	}

	public EventType getEventType() {
		return __eventType;
	}

	public SessionEventObject setEventType(EventType eventType) {
		this.__eventType = eventType;
		return this;
	}

	public SessionEventObject addExtraParam(Object param) {
		this.__extraParams.add(param);
		return this;
	}

	public List<Object> getExtraParams() {
		return Collections.unmodifiableList(this.__extraParams);
	}

	public static SessionEventObject createRemoveEvent(Class<?> entityClass, Object id) {
		return new SessionEventObject(id).setEventType(EventType.REMOVE).setSourceClass(entityClass);
	}

	public static SessionEventObject createRemoveBatchEvent(Class<?> entityClass, List<? extends Object> ids) {
		return new SessionEventObject(ids).setEventType(EventType.REMOVE_BATCH).setSourceClass(entityClass);
	}

	public static SessionEventObject createUpdateEvent(Object entity, String[] filter) {
		if (filter == null) {
			filter = new String[0];
		}
		return new SessionEventObject(entity).setEventType(EventType.UPDATE).setSourceClass(entity.getClass()).addExtraParam(filter);
	}

	public static SessionEventObject createUpdateBatchEvent(Class<?> entityClass, List<? extends Object> entities, String[] filter) {
		if (filter == null) {
			filter = new String[0];
		}
		return new SessionEventObject(entities).setEventType(EventType.UPDATE_BATCH).setSourceClass(entityClass).addExtraParam(filter);
	}

	public static SessionEventObject createInsertEvent(Object entity) {
		return new SessionEventObject(entity).setEventType(EventType.INSERT).setSourceClass(entity.getClass());
	}

	public static SessionEventObject createInsertBatchEvent(Class<?> entityClass, List<? extends Object> entities) {
		return new SessionEventObject(entities).setEventType(EventType.INSERT_BATCH).setSourceClass(entityClass);
	}

}
