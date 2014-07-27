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

import java.util.Arrays;
import java.util.List;

import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.persistence.base.OperatorException;
import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.ISession;
import net.ymate.platform.persistence.jdbc.base.SqlBatchParameter;
import net.ymate.platform.persistence.jdbc.operator.IQueryOperator;
import net.ymate.platform.persistence.jdbc.operator.IResultSetHandler;
import net.ymate.platform.persistence.jdbc.operator.IUpdateBatchOperator;
import net.ymate.platform.persistence.jdbc.operator.IUpdateOperator;
import net.ymate.platform.persistence.jdbc.operator.impl.ArrayResultSetHandler;
import net.ymate.platform.persistence.jdbc.operator.impl.QueryOperator;
import net.ymate.platform.persistence.jdbc.operator.impl.UpdateBatchOperator;
import net.ymate.platform.persistence.jdbc.operator.impl.UpdateOperator;
import net.ymate.platform.persistence.jdbc.query.PageQuery;
import net.ymate.platform.persistence.jdbc.transaction.Trans;
import net.ymate.platform.persistence.support.ISessionEvent;
import net.ymate.platform.persistence.support.PageResultSet;


/**
 * <p>
 * DefaultSession
 * </p>
 * <p>
 * 默认 JDBC 数据库操作会话接口实现类；
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
 *          <td>2011-9-27下午03:09:46</td>
 *          </tr>
 *          </table>
 */
public class DefaultSession implements ISession {

	private String __id;

	private IConnectionHolder __conn;

	private JdbcEntitySupport __entitySupport;

	private ISessionEvent __sessionEvent;

	/**
	 * 构造器
	 * @param conn
	 */
	public DefaultSession(IConnectionHolder conn) {
		this.__id = UUIDUtils.uuid();
		this.__conn = conn;
	}

	/**
	 * @return 获取实体操作支持类对象
	 */
	private JdbcEntitySupport getEntitySupport() {
		if (__entitySupport == null) {
			__entitySupport = new JdbcEntitySupport(this.getConnection());
		}
		return __entitySupport;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#getId()
	 */
	public String getId() {
		return __id;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#getConnection()
	 */
	public IConnectionHolder getConnection() {
		return __conn;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#setSessionEvent(net.ymate.platform.persistence.support.ISessionEvent)
	 */
	public ISession setSessionEvent(ISessionEvent event) {
		this.__sessionEvent = event;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#close()
	 */
	public void close() {
		// 同时需要判断当前连接是否参与事务，若存在事务则不进行关闭操作
		if (__conn != null) {
			if (Trans.get() == null) {
				__conn.release();
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#delete(java.lang.Object)
	 */
	public <T> T delete(T entity) throws OperatorException {
		return this.getEntitySupport().delete(entity, __sessionEvent);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#delete(java.lang.Class, java.lang.Object)
	 */
	public <T> int delete(Class<T> entityClass, Object id) throws OperatorException {
		return this.getEntitySupport().deleteById(entityClass, id, __sessionEvent);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#deleteAll(java.util.List)
	 */
	public <T> List<T> deleteAll(List<T> entities) throws OperatorException {
		return this.getEntitySupport().deleteBatch(entities, __sessionEvent);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#deleteAll(java.lang.Class, java.lang.Object[])
	 */
	public <T> int[] deleteAll(Class<T> entityClass, Object[] ids) throws OperatorException {
		return this.getEntitySupport().deleteBatchByIds(entityClass, Arrays.asList(ids), __sessionEvent);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#executeForUpdate(java.lang.String, java.lang.Object[])
	 */
	public int executeForUpdate(String sql, Object[] params) throws OperatorException {
		IUpdateOperator _opt = new UpdateOperator(sql);
		if (params != null && params.length > 0) {
			if (sql.indexOf("?") > 0) {
				for (Object _param : params) {
					_opt.addParameter(_param);
				}
			}
		}
		_opt.execute(this.getConnection());
		return _opt.getEffectCounts();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#executeForUpdateAll(java.lang.String, java.util.List)
	 */
	public int[] executeForUpdateAll(String sql, List<Object[]> params) throws OperatorException {
		IUpdateBatchOperator _opt = new UpdateBatchOperator(sql);
		SqlBatchParameter _batchParam = null;
		if (params != null && params.size() > 0) {
			if (sql.indexOf("?") > 0) {
				for (Object[] _paramArr : params) {
					if (_paramArr != null && _paramArr.length > 0) {
						_batchParam = new SqlBatchParameter();
						for (Object _param : _paramArr) {
							_batchParam.addParameter(_param);
						}
						_opt.addBatchParameter(_batchParam);
					}
				}
			}
		}
		_opt.execute(this.getConnection());
		return _opt.getBatchEffectCounts();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#findAll(java.lang.Class, java.lang.String, java.lang.String[], int, int, java.lang.Object[])
	 */
	public <T> PageResultSet<T> findAll(Class<T> entity, String cond, String[] fieldFilter, int pageSize, int page, Object[] params) throws OperatorException {
		return this.getEntitySupport().selectByCondWithPage(entity, cond, params, pageSize, page, fieldFilter);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#findAll(java.lang.Class, java.lang.String, java.lang.String[], int, int, boolean, java.lang.Object[])
	 */
	public <T> PageResultSet<T> findAll(Class<T> entity, String cond, String[] fieldFilter, int pageSize, int page, boolean count, Object[] params) throws OperatorException {
		return this.getEntitySupport().selectByCondWithPage(entity, cond, params, pageSize, page, fieldFilter, count);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#findAll(java.lang.String, net.ymate.platform.persistence.jdbc.operator.IResultSetHandler, java.lang.Object[])
	 */
	public <T> List<T> findAll(String sql, IResultSetHandler<T> handler, Object[] params) throws OperatorException {
		IQueryOperator<T> _opt = new QueryOperator<T>(sql, handler);
		if (params != null && params.length > 0) {
			if (sql.indexOf("?") > 0) {
				for (Object _param : params) {
					_opt.addParameter(_param);
				}
			}
		}
		_opt.execute(this.getConnection());
		return _opt.getResultSet();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#findAll(java.lang.Class)
	 */
	public <T> List<T> findAll(Class<T> entity) throws OperatorException {
		return this.getEntitySupport().selectByCond(entity, null, null);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#findAll(java.lang.Class, java.lang.String, java.lang.Object[])
	 */
	public <T> List<T> findAll(Class<T> entity, String cond, Object[] params) throws OperatorException {
		return this.getEntitySupport().selectByCond(entity, cond, params);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#findAll(java.lang.Class, java.lang.String, java.lang.String[], java.lang.Object[])
	 */
	public <T> List<T> findAll(Class<T> entity, String cond, String[] fieldFilter, Object[] params) throws OperatorException {
		return this.getEntitySupport().selectByCond(entity, cond, params, fieldFilter);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#findAll(java.lang.String, net.ymate.platform.persistence.jdbc.operator.IResultSetHandler, int, int, java.lang.Object[])
	 */
	public <T> PageResultSet<T> findAll(String sql, IResultSetHandler<T> handler, int pageSize, int page, Object[] params) throws OperatorException {
		PageQuery<T> _opt = new PageQuery<T>(sql, handler, page, pageSize);
		if (params != null && params.length > 0) {
			if (sql.indexOf("?") > 0) {
				for (Object _param : params) {
					_opt.addParameter(_param);
				}
			}
		}
		return _opt.execute(this.getConnection());
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#findAll(java.lang.String, net.ymate.platform.persistence.jdbc.operator.IResultSetHandler, int, int, boolean, java.lang.Object[])
	 */
	public <T> PageResultSet<T> findAll(String sql, IResultSetHandler<T> handler, int pageSize, int page, boolean count, Object[] params) throws OperatorException {
		PageQuery<T> _opt = new PageQuery<T>(sql, handler, page, pageSize);
		_opt.setAllowRecordCount(count);
		if (params != null && params.length > 0) {
			if (sql.indexOf("?") > 0) {
				for (Object _param : params) {
					_opt.addParameter(_param);
				}
			}
		}
		return _opt.execute(this.getConnection());
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#findFirst(java.lang.String, net.ymate.platform.persistence.jdbc.operator.IResultSetHandler, java.lang.Object[])
	 */
	public <T> T findFirst(String sql, IResultSetHandler<T> handler, Object[] params) throws OperatorException {
		PageQuery<T> _opt = new PageQuery<T>(sql, handler, 1, 1);
		_opt.setAllowRecordCount(false);
		if (params != null && params.length > 0) {
			if (sql.indexOf("?") > 0) {
				for (Object _param : params) {
					_opt.addParameter(_param);
				
				}
			}
		}
		PageResultSet<T> _results = _opt.execute(this.getConnection());
		return _results.getResultSet().isEmpty() ? null : _results.getResultSet().get(0);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#findFirst(java.lang.Class, java.lang.String, java.lang.String[], java.lang.Object[])
	 */
	public <T> T findFirst(Class<T> entity, String cond, String[] fieldFilter, Object[] params) throws OperatorException {
		PageResultSet<T> _results = this.getEntitySupport().selectByCondWithPage(entity, cond, params, 1, 1, fieldFilter, false);
		return _results.getResultSet().isEmpty() ? null : _results.getResultSet().get(0);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#find(java.lang.Class, java.lang.Object)
	 */
	public <T> T find(Class<T> entity, Object id) throws OperatorException {
		return this.getEntitySupport().selectById(entity, id);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#find(java.lang.Class, java.lang.Object, java.lang.String[])
	 */
	public <T> T find(Class<T> entity, Object id, String[] fieldFilter) throws OperatorException {
		return this.getEntitySupport().selectById(entity, id, fieldFilter);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#insert(java.lang.Object)
	 */
	public <T> T insert(T entity) throws OperatorException {
		return this.getEntitySupport().insert(entity, __sessionEvent);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#insertAll(java.util.List)
	 */
	public <T> List<T> insertAll(List<T> entities) throws OperatorException {
		return this.getEntitySupport().insertBatch(entities, __sessionEvent);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#update(java.lang.Object)
	 */
	public <T> T update(T entity) throws OperatorException {
		return this.getEntitySupport().update(entity, __sessionEvent);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#update(java.lang.Object, java.lang.String[])
	 */
	public <T> T update(T entity, String[] fieldFilter) throws OperatorException {
		return this.getEntitySupport().update(entity, fieldFilter, false, __sessionEvent);
	}

    public <T> T update(T entity, String[] fieldFilter, boolean isExcluded) throws OperatorException {
        return this.getEntitySupport().update(entity, fieldFilter, isExcluded, __sessionEvent);
    }

    /* (non-Javadoc)
     * @see net.ymate.platform.persistence.jdbc.ISession#updateAll(java.util.List)
     */
	public <T> List<T> updateAll(List<T> entities) throws OperatorException {
		return this.getEntitySupport().updateBatch(entities, __sessionEvent);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#updateAll(java.util.List, java.lang.String[])
	 */
	public <T> List<T> updateAll(List<T> entities, String[] fieldFilter) throws OperatorException {
		return this.getEntitySupport().updateBatch(entities, fieldFilter, false, __sessionEvent);
	}

    public <T> List<T> updateAll(List<T> entities, String[] fieldFilter, boolean isExcluded) throws OperatorException {
        return this.getEntitySupport().updateBatch(entities, fieldFilter, isExcluded, __sessionEvent);
    }

    /* (non-Javadoc)
     * @see net.ymate.platform.persistence.jdbc.ISession#getAmount(java.lang.Class, java.lang.String, java.lang.Object[])
     */
	public <T> long getAmount(Class<T> entityClass, String whereStr, Object[] params) throws OperatorException {
		return this.getEntitySupport().getAmount(entityClass, whereStr, params);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.ISession#getAmount(java.lang.String, java.lang.Object[])
	 */
	public <T> long getAmount(String sql, Object[] params) throws OperatorException {
		IQueryOperator<Object[]> _query = new QueryOperator<Object[]>(new ArrayResultSetHandler());
		_query.setConnection(this.getConnection());
		if (params != null && params.length > 0) {
			if (sql.indexOf("?") > 0) {
				for (Object _param : params) {
					_query.addParameter(_param);
				}
			}
		}
		_query.setSql("select count(1) from (" + sql + ") c_t");
		_query.execute();
		return ResultSetHelper.bind(_query.getResultSet()).getAsLong(0);
	}

}
