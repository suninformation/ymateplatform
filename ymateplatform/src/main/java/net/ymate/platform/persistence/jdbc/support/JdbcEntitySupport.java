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

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.ClassUtils.ClassBeanWrapper;
import net.ymate.platform.persistence.base.OperatorException;
import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.base.AbstractAccessorCfgEvent;
import net.ymate.platform.persistence.jdbc.base.AccessorEventContext;
import net.ymate.platform.persistence.jdbc.base.SqlBatchParameter;
import net.ymate.platform.persistence.jdbc.base.SqlParameter;
import net.ymate.platform.persistence.jdbc.base.dialect.impl.OracleDialect;
import net.ymate.platform.persistence.jdbc.operator.AbstractResultSetHandler;
import net.ymate.platform.persistence.jdbc.operator.IQueryOperator;
import net.ymate.platform.persistence.jdbc.operator.IUpdateBatchOperator;
import net.ymate.platform.persistence.jdbc.operator.IUpdateOperator;
import net.ymate.platform.persistence.jdbc.operator.impl.ArrayResultSetHandler;
import net.ymate.platform.persistence.jdbc.operator.impl.QueryOperator;
import net.ymate.platform.persistence.jdbc.operator.impl.UpdateBatchOperator;
import net.ymate.platform.persistence.jdbc.operator.impl.UpdateOperator;
import net.ymate.platform.persistence.jdbc.query.PageQuery;
import net.ymate.platform.persistence.support.EntityMeta;
import net.ymate.platform.persistence.support.ISessionEvent;
import net.ymate.platform.persistence.support.PageResultSet;
import net.ymate.platform.persistence.support.SessionEventObject;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * <p>
 * JdbcEntitySupport
 * </p>
 * <p>
 * DAO 实体操作支持类；
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
 *          <td>2011-10-7下午01:25:17</td>
 *          </tr>
 *          </table>
 */
public class JdbcEntitySupport {

	/**
	 * 实体模型元数据描述类缓存
	 */
	private static Map<String, JdbcEntityMeta> __cacheEntityMetas = new ConcurrentHashMap<String, JdbcEntityMeta>();

	private IConnectionHolder __conn;

	/**
	 * 构造器
	 * @param conn
	 */
	public JdbcEntitySupport(IConnectionHolder conn) {
		this.__conn = conn;
	}

	/**
	 * @return 获取数据库连接对象
	 */
	public IConnectionHolder getConnection() {
		return this.__conn;
	}

	public JdbcEntitySupport setConnection(IConnectionHolder conn) {
		this.__conn = conn;
		return this;
	}

	/**
	 * @param <T>
	 * @param entityClass
	 * @return 获取实体模型元数据描述类，若缓存中不存在则创建它
	 */
	public synchronized <T> JdbcEntityMeta getEntityMeta(Class<T> entityClass) {
		JdbcEntityMeta _returnValue = __cacheEntityMetas.get(entityClass.getName());
		if (_returnValue == null) {
			_returnValue = new JdbcEntityMeta(entityClass);
			__cacheEntityMetas.put(entityClass.getName(), _returnValue);
		}
		return _returnValue;
	}

	public <T> T selectById(Class<T> entityClass, Object id) throws OperatorException {
		return selectById(entityClass, id, null);
	}

	public <T> T selectById(Class<T> entityClass, Object id, String[] fieldFilter) throws OperatorException {
		JdbcEntityMeta _meta = this.getEntityMeta(entityClass);
		IQueryOperator<Object[]> _opt = new QueryOperator<Object[]>(new ArrayResultSetHandler());
		List<String> _pkFieldFilter = new ArrayList<String>();
		for (String _pkField : _meta.getPrimaryKeys()) {
			if (_meta.isCompositeKey()) {
				Object _pkFieldValue = ClassUtils.wrapper(id).getValue(_meta.getClassAttributeMap().get(_pkField));
				// 仅处理主键值不为NULL的字段
				if (_pkFieldValue != null) {
					_opt.addParameter(_pkFieldValue);
                    if (!_pkFieldFilter.contains(_pkField)) {
                        _pkFieldFilter.add(_pkField);
                    }
				}
			} else {
				_opt.addParameter(id);
				break;
			}
		}
		T _returnValue = null;
		ResultSetHelper _helper = null;
		try {
			_opt.setSql(_meta.createSelectByPkSql(__conn.getDialect(), fieldFilter, _pkFieldFilter.toArray(new String[_pkFieldFilter.size()])));
			_opt.execute(this.getConnection());
			if (_opt.isResultSetAvailable()) {
				// 处理结果集->对象
				_helper = ResultSetHelper.bind(_opt.getResultSet());
				_returnValue = __doRenderToEntity(entityClass, _meta, _helper);
			}
			return _returnValue;
		} finally {
			if (_helper != null) {
				_helper.clearAll();
				_helper = null;
			}
			_opt = null;
		}
	}

	public <T> List<T> selectByCond(Class<T> entityClass, String whereStr, Object[] values) throws OperatorException {
		return selectByCond(entityClass, whereStr, values, null);
	}

	public <T> List<T> selectByCond(Class<T> entityClass, String whereStr, Object[] values, String[] fieldFilter) throws OperatorException {
		JdbcEntityMeta _meta = this.getEntityMeta(entityClass);
		IQueryOperator<Object[]> _opt = new QueryOperator<Object[]>(new ArrayResultSetHandler());
		String _sql = _meta.createSelectAllSql(__conn.getDialect(), fieldFilter);
		// 拼装SQL
		if (StringUtils.isNotBlank(whereStr)) {
			if (!StringUtils.trim(whereStr).toLowerCase().startsWith("where")) {
				_sql += " where " + whereStr;
			} else {
				_sql += " " + whereStr;
			}
			// 添加参数值
			if (values != null && values.length > 0) {
				int i = _sql.indexOf("?");
				if (i > 0) {
					for (int j = 0; j < values.length; j++) {
						_opt.addParameter(values[j]);
					}
				}
			}
		}
		List<T> _returnValue = new ArrayList<T>();
		ResultSetHelper _helper = null;
		try {
			_opt.setSql(_sql);
			_opt.execute(this.getConnection());
			if (_opt.isResultSetAvailable()) {
				// 处理结果集->对象
				_helper = ResultSetHelper.bind(_opt.getResultSet());
				for (int _idx = 0; _idx < _helper.getRowCount(); _idx++) {
					_helper.move(_idx);
					_returnValue.add(__doRenderToEntity(entityClass, _meta, _helper));
				}
			}
			return _returnValue;
		} finally {
			if (_helper != null) {
				_helper.clearAll();
				_helper = null;
			}
			_opt = null;
		}
	}

	public <T> PageResultSet<T> selectByCondWithPage(Class<T> entityClass, String whereStr, Object values[], int pageSize, int currentPage) throws OperatorException {
		return selectByCondWithPage(entityClass, whereStr, values, pageSize, currentPage, null, true);
	}

	public <T> PageResultSet<T> selectByCondWithPage(Class<T> entityClass, String whereStr, Object values[], int pageSize, int currentPage, String[] fieldFilter) throws OperatorException {
		return selectByCondWithPage(entityClass, whereStr, values, pageSize, currentPage, fieldFilter, true);
	}

	public <T> PageResultSet<T> selectByCondWithPage(Class<T> entityClass, String whereStr, Object values[], int pageSize, int currentPage, boolean allowRecordCount) throws OperatorException {
		return selectByCondWithPage(entityClass, whereStr, values, pageSize, currentPage, null, allowRecordCount);
	}

	public <T> PageResultSet<T> selectByCondWithPage(final Class<T> entityClass, String whereStr, Object values[], int pageSize, int currentPage, final String[] fieldFilter, boolean allowRecordCount) throws OperatorException {
		final JdbcEntityMeta _meta = this.getEntityMeta(entityClass);
		PageQuery<T> _page = new PageQuery<T>(new AbstractResultSetHandler<T>() {
			public void processRowData(ResultSet rs, List<T> result) throws OperatorException, SQLException {
				result.add(__doRenderToEntity(entityClass, _meta, rs, fieldFilter));
			}
		}, currentPage, pageSize);
		String _sql = _meta.createSelectAllSql(__conn.getDialect(), fieldFilter);
		// 拼装SQL
		if (StringUtils.isNotBlank(whereStr)) {
			if (!StringUtils.trim(whereStr).toLowerCase().startsWith("where")) {
				_sql += " where " + whereStr;
			} else {
				_sql += " " + whereStr;
			}
			// 添加参数值
			if (values != null && values.length > 0) {
				int i = _sql.indexOf("?");
				if (i > 0) {
					for (int j = 0; j < values.length; j++) {
						_page.addParameter(values[j]);
					}
				}
			}
		}
		try {
			_page.setSql(_sql);
			_page.setAllowRecordCount(allowRecordCount);
			return _page.execute(this.getConnection());
		} finally {
			_page = null;
		}
	}

	public<T> T insert(final T entity, ISessionEvent event) throws OperatorException {
		if (entity == null) {
			throw new OperatorException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.entity_class_need_anno_id"));
		}
		//
		if (event != null) {
			event.onInsertBefore(SessionEventObject.createInsertEvent(entity));
		}
		//
		final JdbcEntityMeta _meta = this.getEntityMeta(entity.getClass());
		Map<String, AttributeInfo>  _entityMap = __doRenderEntityToMap(_meta, entity);
		IUpdateOperator _update = new UpdateOperator(_meta.createInsertSql(__conn.getDialect()));
		if (_meta.hasAutoIncrementColumn()) {
            if (__conn.getDialect() instanceof OracleDialect) {
                final String[] _ids = _meta.getPrimaryKeys().toArray(new String[_meta.getPrimaryKeys().size()]);
                _update.setAccessorCfgEvent(new EntitryAccessorCfgEvent(_meta, entity) {
                    @Override
                    public PreparedStatement getPreparedStatement(Connection conn, String sql) throws SQLException {
                        return conn.prepareStatement(sql, _ids);
                    }
                });
            } else {
                _update.setAccessorCfgEvent(new EntitryAccessorCfgEvent(_meta, entity));
            }
		}
		for (String _columnName : _meta.getColumnNames()) {
			// 剔除自动生成的主键字段
			if (_meta.hasAutoIncrementColumn() && _meta.isAutoIncrementColumn(_columnName)) {
				continue;
			}
			this.__addUpdateParam(_entityMap.get(_columnName), _update);
		}
		try {
			_update.execute(this.getConnection());
			//
			if (event != null) {
				event.onInsertAfter(SessionEventObject.createInsertEvent(entity));
			}
			//
			return entity;
		} finally {
			_entityMap.clear();
			_update = null;
		}
	}

	public<T> List<T> insertBatch(final List<T> entityList, ISessionEvent event) throws OperatorException {
		if (entityList == null || entityList.isEmpty()) {
			throw new OperatorException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.entity_list_null"));
		}
		//
		if (event != null) {
			event.onInsertBefore(SessionEventObject.createInsertBatchEvent(entityList.get(0).getClass(), entityList));
		}
		//
		final JdbcEntityMeta _meta = this.getEntityMeta(entityList.get(0).getClass());
		IUpdateBatchOperator _update = new UpdateBatchOperator(_meta.createInsertSql(__conn.getDialect()));
		if (_meta.hasAutoIncrementColumn()) {
            if (__conn.getDialect() instanceof OracleDialect) {
				final String[] _ids = _meta.getPrimaryKeys().toArray(new String[_meta.getPrimaryKeys().size()]);
                _update.setAccessorCfgEvent(new EntitryAccessorCfgEvent(_meta, entityList) {
                    @Override
                    public PreparedStatement getPreparedStatement(Connection conn, String sql) throws SQLException {
                        return conn.prepareStatement(sql, _ids);
                    }
                });whtwht
            } else {
                _update.setAccessorCfgEvent(new EntitryAccessorCfgEvent(_meta, entityList));
            }
		}
		Map<String, AttributeInfo>  _entityMap = null;
		for (T _entity : entityList) {
			SqlBatchParameter _batchParam = new SqlBatchParameter();
			_entityMap = __doRenderEntityToMap(_meta, _entity);
			for (String _columnName : _meta.getColumnNames()) {
                // 剔除自动生成的主键字段
                if (_meta.hasAutoIncrementColumn() && _meta.isAutoIncrementColumn(_columnName)) {
                    continue;
                }
				this.__addBatchParam(_batchParam, _entityMap.get(_columnName));
			}
			_update.addBatchParameter(_batchParam);
			_entityMap.clear();
		}
		try {
			_update.execute(this.getConnection());
			//
			if (event != null) {
				event.onInsertAfter(SessionEventObject.createInsertBatchEvent(entityList.get(0).getClass(), entityList));
			}
			//
			return entityList;
		} finally {
			_update = null;
			_entityMap = null;
		}
	}

	public <T> T update(T entity, ISessionEvent event) throws OperatorException {
		return update(entity, null, false, event);
	}

	public <T> T update(T entity, String[] fieldFilter, boolean isExcluded, ISessionEvent event) throws OperatorException {
		//
		if (event != null) {
			event.onUpdateBefore(SessionEventObject.createUpdateEvent(entity, fieldFilter));
		}
		//
		JdbcEntityMeta _meta = this.getEntityMeta(entity.getClass());
		Map<String, AttributeInfo>  _entityMap = __doRenderEntityToMap(_meta, entity);
		IUpdateOperator _update = new UpdateOperator();

        List<String> _fieldFilter = null;
		//
		if (fieldFilter != null && fieldFilter.length > 0) {
            if (isExcluded) {
                _fieldFilter = new ArrayList<String>();
                List<String> _excludedField = Arrays.asList(fieldFilter);
                for (String _columnName : _meta.getColumnNames()) {
                    if (_meta.getPrimaryKeys().contains(_columnName) || _excludedField.contains(_columnName)) {
                        continue;
                    }
                    _fieldFilter.add(_columnName);
                    this.__addUpdateParam(_entityMap.get(_columnName), _update);
                }
            } else {
                for (String _columnName : fieldFilter) {
                    if (_meta.getPrimaryKeys().contains(_columnName)) {
                        continue;
                    }
                    this.__addUpdateParam(_entityMap.get(_columnName), _update);
                }
            }
		} else {
			for (String _columnName : _meta.getColumnNames()) {
				if (_meta.getPrimaryKeys().contains(_columnName)) {
					continue;
				}
				this.__addUpdateParam(_entityMap.get(_columnName), _update);
			}
		}
		//
		List<String> _pkFieldFilter = new ArrayList<String>();
		ClassBeanWrapper<T> _wrapperEntity = ClassUtils.wrapper(entity);
		ClassBeanWrapper<?> _wrapperId = null;
		for (String _pkField : _meta.getPrimaryKeys()) {
			if (_meta.isCompositeKey()) {
				if (_wrapperId == null) {
					_wrapperId = ClassUtils.wrapper(_wrapperEntity.getValue("id"));
				}
				Object _pkFieldValue = _wrapperId.getValue( _meta.getClassAttributeMap().get(_pkField));
				// 仅处理主键值不为NULL的字段
				if (_pkFieldValue != null) {
					_update.addParameter(_pkFieldValue);
                    if (!_pkFieldFilter.contains(_pkField)) {
                        _pkFieldFilter.add(_pkField);
                    }
				}
			} else {
				_update.addParameter(_wrapperEntity.getValue("id"));
				break;
			}
		}
		try {
			_update.setSql(_meta.createUpdateByPkSql(__conn.getDialect(), _fieldFilter != null ? _fieldFilter.toArray(new String[0]) : fieldFilter, _pkFieldFilter.toArray(new String[_pkFieldFilter.size()])));
			_update.execute(this.getConnection());
			//
			if (event != null) {
				event.onUpdateAfter(SessionEventObject.createUpdateEvent(entity, fieldFilter));
			}
			//
			return entity;
		} finally {
			_entityMap.clear();
			_entityMap = null;
			_update = null;
            _pkFieldFilter.clear();
            _pkFieldFilter = null;
        }
	}

	public <T> List<T> updateBatch(List<T> entityList, ISessionEvent event) throws OperatorException {
		return updateBatch(entityList, null, false, event);
	}

	public <T> List<T> updateBatch(List<T> entityList, String[] fieldFilter, boolean isExcluded, ISessionEvent event) throws OperatorException {
		if (entityList == null || entityList.isEmpty()) {
			throw new OperatorException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.entity_list_null"));
		}
		//
		if (event != null) {
			event.onUpdateBefore(SessionEventObject.createUpdateBatchEvent(entityList.get(0).getClass(), entityList, fieldFilter));
		}
		//
		JdbcEntityMeta _meta = this.getEntityMeta(entityList.get(0).getClass());
		IUpdateBatchOperator _update = new UpdateBatchOperator();
		//
		Map<String, AttributeInfo>  _entityMap = null;
		List<String> _pkFieldFilter = new ArrayList<String>();

        List<String> _fieldFilter = null;

		for (T entity : entityList) {
			SqlBatchParameter _batchParam = new SqlBatchParameter();
			_entityMap = __doRenderEntityToMap(_meta, entity);
			if (fieldFilter != null && fieldFilter.length > 0) {
                if (isExcluded) {
                    _fieldFilter = new ArrayList<String>();
                    List<String> _excludedField = Arrays.asList(fieldFilter);
                    for (String _columnName : _meta.getColumnNames()) {
                        if (_meta.getPrimaryKeys().contains(_columnName) || _excludedField.contains(_columnName)) {
                            continue;
                        }
                        _fieldFilter.add(_columnName);
                        this.__addBatchParam(_batchParam, _entityMap.get(_columnName));
                    }
                } else {
                    for (String _columnName : fieldFilter) {
                        if (_meta.getPrimaryKeys().contains(_columnName)) {
                            continue;
                        }
                        this.__addBatchParam(_batchParam, _entityMap.get(_columnName));
                    }
                }
			} else {
				for (String _columnName : _meta.getColumnNames()) {
					if (_meta.getPrimaryKeys().contains(_columnName)) {
						continue;
					}
					this.__addBatchParam(_batchParam, _entityMap.get(_columnName));
				}
			}
			//
			ClassBeanWrapper<T> _wrapperEntity = ClassUtils.wrapper(entity);
			ClassBeanWrapper<?> _wrapperId = null;
			for (String _pkField : _meta.getPrimaryKeys()) {
				if (_meta.isCompositeKey()) {
					if (_wrapperId == null) {
						_wrapperId = ClassUtils.wrapper(_wrapperEntity.getValue("id"));
					}
					Object _pkFieldValue = _wrapperId.getValue( _meta.getClassAttributeMap().get(_pkField));
					// 仅处理主键值不为NULL的字段
					if (_pkFieldValue != null) {
						_batchParam.addParameter(_pkFieldValue);
                        if (!_pkFieldFilter.contains(_pkField)) {
                            _pkFieldFilter.add(_pkField);
                        }
					}
				} else {
					_batchParam.addParameter(_wrapperEntity.getValue("id"));
					break;
				}
			}
			_update.addBatchParameter(_batchParam);
			_entityMap.clear();
		}
		try {
			_update.setSql(_meta.createUpdateByPkSql(__conn.getDialect(), _fieldFilter != null ? _fieldFilter.toArray(new String[0]) : fieldFilter, _pkFieldFilter.toArray(new String[_pkFieldFilter.size()])));
			_update.execute(this.getConnection());
			//
			if (event != null) {
				event.onUpdateAfter(SessionEventObject.createUpdateBatchEvent(entityList.get(0).getClass(), entityList, fieldFilter));
			}
			//
			return entityList;
		} finally {
			_pkFieldFilter.clear();
			_pkFieldFilter = null;
			_update = null;
            if (_entityMap != null) {
                _entityMap.clear();
			    _entityMap = null;
            }
		}
	}

	public <T> List<T> deleteBatch(List<T> entityList, ISessionEvent event) throws OperatorException {
		if (entityList == null || entityList.isEmpty()) {
			throw new OperatorException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.entity_list_null"));
		}
		List<Object> _ids = new ArrayList<Object>();
		ClassBeanWrapper<T> _wrapperEntity = null;
		for (T _entity : entityList) {
			_wrapperEntity = ClassUtils.wrapper(_entity);
			_ids.add(_wrapperEntity.getValue("id"));
		}
		this.deleteBatchByIds(entityList.get(0).getClass(), _ids, event);
		return entityList;
	}

	public <T> int[] deleteBatchByIds(Class<T> entityClass, List<Object> ids, ISessionEvent event) throws OperatorException {
		if (ids == null || ids.size() == 0) {
			throw new OperatorException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.pk_list_null"));
		}
		//
		if (event != null) {
			event.onRemoveBefore(SessionEventObject.createRemoveBatchEvent(entityClass, ids));
		}
		//
		JdbcEntityMeta _meta = this.getEntityMeta(entityClass);
		IUpdateBatchOperator _update = new UpdateBatchOperator();
		List<String> _pkFieldFilter = new ArrayList<String>();
		for (Object _idObj : ids) {
			SqlBatchParameter _batchParam = new SqlBatchParameter();
			ClassBeanWrapper<?> _wrapperId = null;
			for (String _pkField : _meta.getPrimaryKeys()) {
				if (_meta.isCompositeKey()) {
                    if (_wrapperId == null) {
                        _wrapperId = ClassUtils.wrapper(_idObj);
                    }
					Object _pkFieldValue = _wrapperId.getValue( _meta.getClassAttributeMap().get(_pkField));
					// 仅处理主键值不为NULL的字段
					if (_pkFieldValue != null) {
						_batchParam.addParameter(_pkFieldValue);
                        if (!_pkFieldFilter.contains(_pkField)) {
                            _pkFieldFilter.add(_pkField);
                        }
					}
				} else {
					_batchParam.addParameter(_idObj);
					break;
				}
			}
			_update.addBatchParameter(_batchParam);
		}
		try {
			_update.setSql(_meta.createDeleteByPkSql(__conn.getDialect(), _pkFieldFilter.toArray(new String[_pkFieldFilter.size()])));
			_update.execute(this.getConnection());
			//
			if (event != null) {
				event.onRemoveAfter(SessionEventObject.createRemoveBatchEvent(entityClass, ids));
			}
			//
			return _update.getBatchEffectCounts();
		} finally {
			_update = null;
			_pkFieldFilter.clear();
			_pkFieldFilter = null;
		}
	}

	public <T> T delete(T entity, ISessionEvent event) throws OperatorException {
		if (entity == null) {
			throw new OperatorException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.entity_null"));
		}
		this.deleteById(entity.getClass(), ClassUtils.wrapper(entity).getValue("id"), event);
		return entity;
	}

	public <T> int deleteById(Class<T> entityClass, Object id, ISessionEvent event) throws OperatorException {
		if (id == null) {
			throw new OperatorException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.pk_null"));
		}
		//
		if (event != null) {
			event.onRemoveBefore(SessionEventObject.createRemoveEvent(entityClass, id));
		}
		//
		JdbcEntityMeta _meta = this.getEntityMeta(entityClass);
		IUpdateOperator _update = new UpdateOperator();
		List<String> _pkFieldFilter = new ArrayList<String>();
		ClassBeanWrapper<?> _wrapperId = null;
		for (String _pkField : _meta.getPrimaryKeys()) {
			if (_meta.isCompositeKey()) {
				if (_wrapperId == null) {
					_wrapperId = ClassUtils.wrapper(id);
				}
				Object _pkFieldValue = _wrapperId.getValue( _meta.getClassAttributeMap().get(_pkField));
				// 仅处理主键值不为NULL的字段
				if (_pkFieldValue != null) {
					_update.addParameter(_pkFieldValue);
                    if (!_pkFieldFilter.contains(_pkField)) {
                        _pkFieldFilter.add(_pkField);
                    }
				}
			} else {
				_update.addParameter(id);
				break;
			}
		}
		try {
			_update.setSql(_meta.createDeleteByPkSql(__conn.getDialect(), _pkFieldFilter.toArray(new String[_pkFieldFilter.size()])));
			_update.execute(this.getConnection());
			//
			if (event != null) {
				event.onRemoveAfter(SessionEventObject.createRemoveEvent(entityClass, id));
			}
			//
			return _update.getEffectCounts();
		} finally {
			_update = null;
            _pkFieldFilter.clear();
            _pkFieldFilter = null;
        }
	}

	public <T> long getAmount(Class<T> entityClass, String whereStr, Object[] params) throws OperatorException {
		JdbcEntityMeta _meta = this.getEntityMeta(entityClass);
		IQueryOperator<Object[]> _query = new QueryOperator<Object[]>(new ArrayResultSetHandler());
		String _sql = "";
		// 拼装SQL
		if (StringUtils.isNotBlank(whereStr)) {
			if (!StringUtils.trim(whereStr).toLowerCase().startsWith("where")) {
				_sql += " where " + whereStr;
			} else {
				_sql += " " + whereStr;
			}
		}
		SQLHelper.create(
				_query,
				"SELECT count(1) FROM " + __conn.getDialect().wapperQuotedIdent(_meta.getTableName()) + " ${whereStr}").replace("whereStr", _sql)
				.addParameters(params).bindSQL();
		ResultSetHelper _rs = null;
		try {
			_query.execute(this.getConnection());
			_rs = ResultSetHelper.bind(_query.getResultSet());
			return _rs != null ? _rs.getAsLong(0) : -1;
		} finally {
			if (_rs != null) {
				_rs.clearAll();
				_rs = null;
			}
			_query = null;
		}
	}

	// =-=================================

	/**
	 * @param <T> 实体类型
	 * @param entityClass 实体类对象
	 * @param meta 实体模型元数据描述
	 * @param recordSet 结果集
	 * @return 将数据库记录映射到实体对象
	 * @throws OperatorException
	 */
	private <T> T __doRenderToEntity(Class<T> entityClass, JdbcEntityMeta meta, ResultSetHelper recordSet) throws OperatorException {
		ClassBeanWrapper<T> _returnValue = ClassUtils.wrapper(entityClass);
		if (_returnValue == null) {
			throw new OperatorException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.entity_init_exception", entityClass.getName()));
		}
		ClassBeanWrapper<?> _wrapperId = null;
		for (String key : meta.getColumnNames()) {
			Object value = recordSet.getFieldValue(key);
			if (value == null) {
				continue;
			}
			String _classAttr = meta.getClassAttributeMap().get(key);
			if (meta.isCompositeKey() && meta.getPrimaryKeys().contains(key)) {
                if (_wrapperId == null) {
                    _wrapperId = ClassUtils.wrapper(meta.getPrimaryKeyClass());
                    if (_wrapperId == null) {
                        throw new OperatorException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.pk_init_exception", meta.getPrimaryKeyClass().getName()));
                    }
                    _returnValue.setValue("id", _wrapperId.getTarget());
                }
				_wrapperId.setValue(_classAttr, new BlurObject(value).toObjectValue(_wrapperId.getFieldType(_classAttr)));
			} else {
				_returnValue.setValue(_classAttr, new BlurObject(value).toObjectValue(_returnValue.getFieldType(_classAttr)));
			}
		}
		return _returnValue.getTarget();
	}

	private <T> T __doRenderToEntity(Class<T> entityClass, JdbcEntityMeta meta, ResultSet record, String[] fieldFilter) throws OperatorException, SQLException {
		ClassBeanWrapper<T> _returnValue = ClassUtils.wrapper(entityClass);
		if (_returnValue == null) {
			throw new OperatorException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.entity_init_exception", entityClass.getName()));
		}
		ClassBeanWrapper<?> _wrapperId = null;
		for (String key : (fieldFilter != null && fieldFilter.length > 0 ? Arrays.asList(fieldFilter) : meta.getColumnNames())) {
			Object value = record.getObject(key);
			if (value == null) {
				continue;
			}
			String _classAttr = meta.getClassAttributeMap().get(key);
			if (meta.isCompositeKey() && meta.getPrimaryKeys().contains(key)) {
                if (_wrapperId == null) {
                    _wrapperId = ClassUtils.wrapper(meta.getPrimaryKeyClass());
                    if (_wrapperId == null) {
                        throw new OperatorException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.pk_init_exception", meta.getPrimaryKeyClass().getName()));
                    }
                    _returnValue.setValue("id", _wrapperId.getTarget());
                }
				_wrapperId.setValue(_classAttr, new BlurObject(value).toObjectValue(_wrapperId.getFieldType(_classAttr)));
			} else {
				_returnValue.setValue(_classAttr, new BlurObject(value).toObjectValue(_returnValue.getFieldType(_classAttr)));
			}
		}
		return _returnValue.getTarget();
	}

	/**
	 * @param <T> 实体类型
	 * @param meta 实体模型元数据描述
	 * @param entity 实体对象
	 * @return 将实体对象映射到MAP对象
	 */
	private <T> Map<String, AttributeInfo> __doRenderEntityToMap(JdbcEntityMeta meta, T entity) {
		Map<String, AttributeInfo> _returnValue = new HashMap<String, AttributeInfo>();
		ClassBeanWrapper<?> _wrapperEntity = ClassUtils.wrapper(entity);
		ClassBeanWrapper<?> _wrapperId = null;
		for (String fn : meta.getColumnNames()) {
            String _attrFnName = meta.getClassAttributeMap().get(fn);
			if (meta.isCompositeKey() && meta.getPrimaryKeys().contains(fn)) {
                if (_wrapperId == null) {
                    _wrapperId = ClassUtils.wrapper(_wrapperEntity.getValue("id"));
                }
                Class<?> _attrType = _wrapperId.getFieldType(_attrFnName);
                Object _attrValue = _wrapperId.getValue(_attrFnName);
                // 如果实体属性值为null
                if (_attrValue == null) {
                    // 则尝试提取注解中的默认值
                    String _defValue = meta.getColumnMap().get(fn).getDefaultValue();
                    _attrValue = _defValue.equalsIgnoreCase("@NULL") ? null : _defValue;
                }
				_returnValue.put(fn, new AttributeInfo(_attrValue, _attrType));
			} else {
                Class<?> _attrType = _wrapperEntity.getFieldType(_attrFnName);
                Object _attrValue = _wrapperEntity.getValue(_attrFnName);
                // 如果实体属性值为null
                if (_attrValue == null) {
                    // 则尝试提取注解中的默认值
                    String _defValue = meta.getColumnMap().get(fn).getDefaultValue();
                    _attrValue = _defValue.equalsIgnoreCase("@NULL") ? null : _defValue;
                }
				_returnValue.put(fn, new AttributeInfo(_attrValue, _attrType));
			}
		}
		return _returnValue;
	}

	private void __addUpdateParam(AttributeInfo attributeInfo, IUpdateOperator update) {
		if (attributeInfo.getValue() != null) {
			update.addParameter(attributeInfo.getValue());
		} else {
			Class<?> type = attributeInfo.getType();
			if (type.equals(Integer.class)) {
				update.addParameter(new SqlParameter(java.sql.Types.INTEGER, null));
			} else if (type.equals(String.class)) {
				update.addParameter(new SqlParameter(java.sql.Types.VARCHAR, null));
			} else if (type.equals(Long.class)) {
				update.addParameter(new SqlParameter(java.sql.Types.BIGINT, null));
			} else if (type.equals(java.sql.Timestamp.class)) {
				update.addParameter(new SqlParameter(java.sql.Types.TIMESTAMP, null));
			} else if (type.equals(java.sql.Time.class)) {
				update.addParameter(new SqlParameter(java.sql.Types.TIME, null));
			} else if (type.equals(Double.class)) {
				update.addParameter(new SqlParameter(java.sql.Types.DOUBLE, null));
			} else if (type.equals(java.sql.Date.class)) {
				update.addParameter(new SqlParameter(java.sql.Types.DATE, null));
			} else {
				update.addParameter(new SqlParameter(java.sql.Types.NUMERIC, null));
			}
		}
	}

	private void __addBatchParam(SqlBatchParameter batchParam, AttributeInfo attributeInfo) {
		if (attributeInfo.getValue() != null) {
			batchParam.addParameter(attributeInfo.getValue());
		} else {
			Class<?> type = attributeInfo.getType();
			if (type.equals(Integer.class)) {
				batchParam.addParameter(new SqlParameter(java.sql.Types.INTEGER, null));
			} else if (type.equals(String.class)) {
				batchParam.addParameter(new SqlParameter(java.sql.Types.VARCHAR, null));
			} else if (type.equals(Long.class)) {
				batchParam.addParameter(new SqlParameter(java.sql.Types.BIGINT, null));
			} else if (type.equals(java.sql.Timestamp.class)) {
				batchParam.addParameter(new SqlParameter(java.sql.Types.TIMESTAMP, null));
			} else if (type.equals(java.sql.Time.class)) {
				batchParam.addParameter(new SqlParameter(java.sql.Types.TIME, null));
			} else if (type.equals(Double.class)) {
				batchParam.addParameter(new SqlParameter(java.sql.Types.DOUBLE, null));
			} else if (type.equals(java.sql.Date.class)) {
				batchParam.addParameter(new SqlParameter(java.sql.Types.DATE, null));
			} else {
				batchParam.addParameter(new SqlParameter(java.sql.Types.NUMERIC, null));
			}
		}
	}

	/**
	 * <p>
	 * AttributeInfo
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
	 *          <td>2010-10-14上午10:26:38</td>
	 *          </tr>
	 *          </table>
	 */
	private  class AttributeInfo {
		private Object value;
		private Class<?> type;

		public AttributeInfo(Object value, Class<?> type) {
			this.value = value;
			this.type = type;
		}

		public Object getValue() {
			return value;
		}

		public Class<?> getType() {
			return type;
		}
	}

	/**
	 * <p>
	 * EntitryAccessorCfgEvent
	 * </p>
	 * <p>
	 * 访问器配置事件处理接口EntitySupport实现类
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
	 *          <td>2012-12-28下午7:34:27</td>
	 *          </tr>
	 *          </table>
	 */
	private class EntitryAccessorCfgEvent extends AbstractAccessorCfgEvent {

		private List<?> __entities;
		private JdbcEntityMeta __meta;

		public EntitryAccessorCfgEvent(JdbcEntityMeta meta, Object...entity) {
			this.__meta = meta;
			this.__entities = Arrays.asList(entity);
		}

		public EntitryAccessorCfgEvent(JdbcEntityMeta meta, List<?> entities) {
			this.__meta = meta;
			this.__entities = entities;
		}

        public void afterStatementExecution(AccessorEventContext context) throws SQLException {
            if (__entities != null && __meta.hasAutoIncrementColumn()) {
                // 获取返回的自动生成主键集合, 建议每个数据表最多一个自动生成主键
                Object[] _genKeyValue = getConnection().getDialect().getGeneratedKey(context.getStatement());
                if (_genKeyValue != null && _genKeyValue.length > 0) {
                    for (int _idx = 0; _idx < this.__entities.size(); _idx++) {
                        for (String _autoFieldName : __meta.getPrimaryKeys()) {
                            EntityMeta.ColumnInfo _columnInfo = __meta.getColumnMap().get(_autoFieldName);
                            if (__meta.isCompositeKey()) {
                                ClassBeanWrapper<?> _wrapperEntity = ClassUtils.wrapper(this.__entities.get(_idx));
                                ClassBeanWrapper<?> _wrapperId = ClassUtils.wrapper(_wrapperEntity.getValue("id"));
                                // 若执行插入操作时已为自生成主键赋值则将不再自动填充
                                if (_wrapperId.getValue(_columnInfo.getFieldName()) == null) {
                                    _wrapperId.setValue(_columnInfo.getFieldName(), new BlurObject(_genKeyValue[_idx]).toObjectValue(_wrapperId.getFieldType(_columnInfo.getFieldName())));
                                }
                            } else {
                                ClassBeanWrapper<?> _wrapperEntity = ClassUtils.wrapper(this.__entities.get(_idx));
                                if (_wrapperEntity.getValue("id") == null) {
                                    _wrapperEntity.setValue("id", new BlurObject(_genKeyValue[_idx]).toObjectValue(_wrapperEntity.getFieldType(_columnInfo.getFieldName())));
                                }
                            }
                        }
                    }
                }
            }
		}

		public PreparedStatement getPreparedStatement(Connection conn, String sql) throws SQLException {
			return conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		}
	}

}
