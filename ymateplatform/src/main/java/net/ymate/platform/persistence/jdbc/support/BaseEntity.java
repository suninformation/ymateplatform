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
package net.ymate.platform.persistence.jdbc.support;

import java.util.List;

import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.persistence.base.ConnectionException;
import net.ymate.platform.persistence.base.IEntity;
import net.ymate.platform.persistence.base.OperatorException;
import net.ymate.platform.persistence.jdbc.ISession;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.support.PageResultSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * <p>
 * BaseEntity
 * </p>
 * <p>
 * 实体模型接口抽象实现类，并集成实体存储器接口；
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
 *          <td>2013-7-16下午5:22:15</td>
 *          </tr>
 *          </table>
 */
public abstract class BaseEntity<Entity extends IEntity<PK>, PK> implements IEntity<PK> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 824323403067675376L;

	private Class<Entity> __entityClass;
	private String __dsName;

	/**
	 * 构造器
	 */
	@SuppressWarnings("unchecked")
	public BaseEntity() {
		List<Class<?>> _c = ClassUtils.getParameterizedTypes(getClass());
		__entityClass = (Class<Entity>) _c.get(0);
	}

	public void setDataSourceName(String dsName) {
		this.__dsName = dsName;
	}

	/**
	 * @return 获取当前数据源名称，若为空则返回默认数据源名称
	 */
	protected String getDataSourceName() {
		return StringUtils.defaultIfEmpty(this.__dsName, JDBC.DATASOURCE_DEFAULT_NAME);
	}

	/**
	 * @return 获取实体对象类型
	 */
	protected Class<Entity> getEntityClass() {
		return this.__entityClass;
	}

	public Entity load(String... fieldFilter) throws OperatorException, ConnectionException {
		ISession _session = JDBC.openSession(this.getDataSourceName());
		try {
			return _session.find(this.getEntityClass(), this.getId(), fieldFilter);
		} finally {
			_session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public Entity save() throws OperatorException, ConnectionException {
		ISession _session = JDBC.openSession(getDataSourceName());
		try {
			return _session.insert((Entity) this);
		} finally {
			_session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public Entity update(String... fieldFilter) throws OperatorException, ConnectionException {
		return update(false, fieldFilter);
	}

    public Entity update(boolean isExcluded, String... fieldFilter) throws OperatorException, ConnectionException {
        ISession _session = JDBC.openSession(getDataSourceName());
        try {
            return _session.update((Entity) this, fieldFilter, isExcluded);
        } finally {
            _session.close();
        }
    }

	@SuppressWarnings("unchecked")
	public Entity delete() throws OperatorException, ConnectionException {
		ISession _session = JDBC.openSession(getDataSourceName());
		try {
			return _session.delete((Entity) this);
		} finally {
			_session.close();
		}
	}

	public List<Entity> findAll(String cond, Object[] params, String... fieldFilter) throws OperatorException, ConnectionException {
		ISession _session = JDBC.openSession(this.getDataSourceName());
		try {
			return _session.findAll(this.getEntityClass(), cond, fieldFilter, params);
		} finally {
			_session.close();
		}
	}

	public PageResultSet<Entity> findAll(String cond, Object[] params, int pageSize, int currentPage, boolean allowRecordCount, String... fieldFilter) throws OperatorException, ConnectionException {
		ISession _session = JDBC.openSession(this.getDataSourceName());
		try {
			return _session.findAll(this.getEntityClass(), cond, fieldFilter, pageSize, currentPage, allowRecordCount, params);
		} finally {
			_session.close();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
