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
package net.ymate.platform.persistence.jdbc.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.commons.util.ExpressionUtils;
import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.operator.IQueryOperator;
import net.ymate.platform.persistence.jdbc.operator.IResultSetHandler;
import net.ymate.platform.persistence.jdbc.operator.OperatorException;
import net.ymate.platform.persistence.jdbc.operator.impl.QueryOperator;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * AbstractCompositeQuery
 * </p>
 * <p>
 * 组合查询接口抽象实现类，与模型（Model）相似，由开发人员通过此接口定义具体查询条件；
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
 *          <td>2011-6-23上午09:09:23</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractCompositeQuery<T> implements ICompositeQuery<T> {

	/**
	 * 组合查询SQL缓存器（组合查询SQL语句由具体ICompositeQuery接口实现类提供）
	 */
	private static Map<String, String> __QUERY_CACHE = new ConcurrentHashMap<String, String>();
	
	private String __queryStr;

	private String __whereConditionType;

	private IResultSetHandler<T> __handler;

	/**
	 * 当前使用的数据库连接对象
	 */
	private IConnectionHolder __currentConnection;

	private List<QueryCondition> __conditions = new ArrayList<QueryCondition>();

	private boolean __isInited;

	/**
	 * 构造器
	 * 
	 * @param handler
	 */
	public AbstractCompositeQuery(IResultSetHandler<T> handler) {
		if (handler == null) {
			throw new CompositeQueryException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.result_null"));
		}
		this.__handler = handler;
		String _cacheKey = this.getClass().getName();
		if (__QUERY_CACHE.containsKey(_cacheKey)) {
			this.__queryStr = __QUERY_CACHE.get(_cacheKey);
		} else {
			this.__queryStr = this.initCompositeQuery().buildSql();
			if (StringUtils.isBlank(this.__queryStr)) {
				throw new CompositeQueryException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.composite_query_impl_null"));
			}
			__QUERY_CACHE.put(_cacheKey, this.__queryStr);
		}
	}

	/**
	 * 构造器
	 * 
	 * @param handler
	 * @param conn
	 */
	public AbstractCompositeQuery(IResultSetHandler<T> handler, IConnectionHolder conn) {
		this(handler);
		this.setConnection(conn);
	}

	/**
	 * 初始化组合查询对象
	 * 
	 * @return
	 */
	protected AbstractCompositeQuery<T> initCompositeQuery() {
		if (!__isInited) {
			this.__doInitCompositeQuery();
			this.__isInited = true;
		}
		return this;
	}

	/**
	 * 组合查询对象初始化过程（由子类实现）
	 */
	public abstract void __doInitCompositeQuery();

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeQuery#doQuery()
	 */
	public List<T> doQuery() throws OperatorException {
		return doQuery(null, null);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeQuery#doQuery(net.ymate.platform.persistence.jdbc.IConnectionHolder)
	 */
	public List<T> doQuery(IConnectionHolder conn) throws OperatorException {
		return setConnection(conn).doQuery(null, null);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeQuery#doQuery(java.lang.String, java.lang.Object[])
	 */
	public List<T> doQuery(String whereStr, Object[] values) throws OperatorException {
		if (StringUtils.isNotBlank(whereStr)) {
			if (this.__queryStr.toLowerCase().contains("where")) {
				this.__queryStr += (" " + (StringUtils.isNotBlank(this.getQueryWhereConditionType()) ? this.getQueryWhereConditionType() : "AND") + " " + whereStr);
			} else {
				this.__queryStr += (" " + whereStr);
			}
		}
		IQueryOperator<T> _opt = new QueryOperator<T>(this.__queryStr, this.__handler, this.getConnection());
		if (values != null && values.length > 0) {
			for (Object param : values) {
				_opt.addParameter(param);
			}
		}
		_opt.execute();
		return _opt.getResultSet();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeQuery#doQuery(java.lang.String, java.lang.Object[], net.ymate.platform.persistence.jdbc.IConnectionHolder)
	 */
	public List<T> doQuery(String whereStr, Object[] values, IConnectionHolder conn) throws OperatorException {
		return setConnection(conn).doQuery(whereStr, values);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeQuery#doQuery(java.lang.String, java.lang.Object[], int, int)
	 */
	public PageResultSet<T> doQuery(String whereStr, Object[] values, int pageSize, int currentPage) throws OperatorException {
		return doQuery(whereStr, values, pageSize, currentPage, true);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeQuery#doQuery(java.lang.String, java.lang.Object[], int, int, net.ymate.platform.persistence.jdbc.IConnectionHolder)
	 */
	public PageResultSet<T> doQuery(String whereStr, Object[] values, int pageSize, int currentPage, IConnectionHolder conn) throws OperatorException {
		return setConnection(conn).doQuery(whereStr, values, pageSize, currentPage);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeQuery#doQuery(java.lang.String, java.lang.Object[], int, int, boolean)
	 */
	public PageResultSet<T> doQuery(String whereStr, Object[] values, int pageSize, int currentPage, boolean allowRecordCount) throws OperatorException {
		if (StringUtils.isNotBlank(whereStr)) {
			if (!StringUtils.trim(whereStr).toLowerCase().startsWith("where")) {
				this.__queryStr += " where " + whereStr;
			} else {
				this.__queryStr += " " + whereStr;
			}
		}
		PageQuery<T> _p = new PageQuery<T>(this.__queryStr, this.__handler, this.getConnection(), currentPage, pageSize);
		_p.setAllowRecordCount(allowRecordCount);
		if (values != null && values.length > 0) {
			for (Object param : values) {
				_p.addParameter(param);
			}
		}
		return _p.execute();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeQuery#doQuery(java.lang.String, java.lang.Object[], int, int, boolean, net.ymate.platform.persistence.jdbc.IConnectionHolder)
	 */
	public PageResultSet<T> doQuery(String whereStr, Object[] values, int pageSize, int currentPage, boolean allowRecordCount, IConnectionHolder conn) throws OperatorException {
		return setConnection(conn).doQuery(whereStr, values, pageSize, currentPage, allowRecordCount);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeQuery#setConnection(net.ymate.platform.persistence.jdbc.IConnectionHolder)
	 */
	public ICompositeQuery<T> setConnection(IConnectionHolder conn) {
		this.__currentConnection = conn;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeQuery#getConnection()
	 */
	public IConnectionHolder getConnection() {
		return this.__currentConnection;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeQuery#getQueryWhereConditionType()
	 */
	public String getQueryWhereConditionType() {
		return __whereConditionType;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeQuery#setQueryWhereConditionType(java.lang.String)
	 */
	public ICompositeQuery<T> setQueryWhereConditionType(String conditionType) {
		this.__whereConditionType = conditionType;
		return this;
	}

	/**
	 * 添加查询条件
	 * 
	 * @param condition 查询条件接口实现类对象
	 * @return
	 */
	protected AbstractCompositeQuery<T> addCondition(QueryCondition condition) {
		this.__conditions.add(condition);
		return this;
	}

	/**
	 * 创建查询条件对象
	 * 
	 * @param conditionName 数据表名称或SQL子查询语句
	 * @return
	 */
	protected QueryCondition createCondition(String conditionName) {
		return new QueryCondition(conditionName);
	}

	/**
	 * 创建查询条件对象
	 * 
	 * @param condition 数据表名称或SQL子查询语句
	 * @param conditionAlias 别名
	 * @return
	 */
	protected QueryCondition createCondition(String conditionName, String conditionAlias) {
		return new QueryCondition(conditionName, conditionAlias);
	}

	/**
	 * 创建查询字段对象
	 * 
	 * @param columnName 字段名称
	 * @return
	 */
	protected QueryColumn createColumn(String columnName) {
		return new QueryColumn(columnName);
	}

	/**
	 * 创建查询字段对象
	 * 
	 * @param columnName 字段名称
	 * @param columnAlias 字段别名
	 * @return
	 */
	protected QueryColumn createColumn(String columnName, String columnAlias) {
		return new QueryColumn(columnName, columnAlias);
	}

	/**
	 * @return 生成组合查询SQL语句
	 */
	protected String buildSql() {
		if (this.__isInited) {
			StringBuilder _fieldSB = new StringBuilder();
			StringBuilder _fromSB = new StringBuilder();
			StringBuilder _joinSB = new StringBuilder();
			StringBuilder _whereSB = new StringBuilder();
			// 上一个条件对象的JOIN类型
			JoinType _preJoinType = null;
			String _perWhereConditionType = null;
			for (QueryCondition _cond : this.__conditions) {
				// 处理显示字段
				boolean _isSubQuery = __isSubQuery(_cond.getConditionName());
				boolean _isAliasNotNull = StringUtils.isNotBlank(_cond.getConditionAlias());
				if (_isSubQuery && !_isAliasNotNull) {
					throw new CompositeQueryException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.alias_null"));
				}
				String _tName = _isAliasNotNull ? _cond.getConditionAlias() : _cond.getConditionName();
				if (StringUtils.isBlank(_tName)) {
					throw new CompositeQueryException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.table_name_null"));
				}
				for (QueryColumn _column : _cond.getColumns()) {
					if (_fieldSB.length() > 0) {
						_fieldSB.append(", ");
					}
					// 此处用于判断是否为"*"字符，若为"*"则仅处理第一个参数且不处理别名
					if (_column.getColumnName().trim().equals("*")) {
						_fieldSB.append(_tName).append(".* ");
						break;
					} else {
						_fieldSB.append(_tName).append(".").append(_column.getColumnName());
						if (StringUtils.isNotBlank(_column.getColumnAlias())) {
							_fieldSB.append(" AS ").append(_column.getColumnAlias());
						}
					}
				}
				// 处理条件对象时，第一个条件对象的onCondition参数将被忽略，请在执行此组合查询时手工添加此部份；
				if (_preJoinType != null) {
					_joinSB.append(__parseJoinOn(_preJoinType, _cond, _isSubQuery, _isAliasNotNull));
				} else {
					if (_fromSB.length() > 0) {
						_fromSB.append(", ");
					}
					_fromSB.append(_cond.getConditionName());
					if (_isAliasNotNull) {
						_fromSB.append(" ").append(_cond.getConditionAlias());
					}
					_fromSB.append(" ");
					if (StringUtils.isNotBlank(_cond.getOnCondition())) {
						if (_whereSB.length() > 0) {
							if (StringUtils.isNotBlank(_perWhereConditionType)) {
								_whereSB.append(" ").append(_perWhereConditionType).append(" ");
							} else {
								_whereSB.append(" AND ");
							}
						}
						_whereSB.append(" ").append(_cond.getOnCondition()).append(" ");
					}
				}
				_preJoinType = _cond.getJoinType();
				_perWhereConditionType = _cond.getWhereConditionType();
			}
			String _resultSql = ExpressionUtils.bind("SELECT ${fields} FROM ${froms} ${joins} ${wheres}")
				.set("fields", _fieldSB.toString())
				.set("froms", _fromSB.toString())
				.set("joins", _joinSB.toString())
				.set("wheres", (_whereSB.length() > 0 ? "WHERE " + _whereSB.toString() : "")).getResult();
			return _resultSql;
		}
		throw new CompositeQueryException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.composite_query_error"));
	}

	/**
	 * 判断condStr是否为子查询SQL串
	 * 
	 * @param condStr
	 * @return
	 */
	private boolean __isSubQuery(String condStr) {
		return (condStr.toLowerCase().contains("select"));
	}

	/**
	 * 处理JOIN
	 * 
	 * @param joinType
	 * @param condition
	 * @param isSubQuery
	 * @param isAliasNotNull
	 * @return
	 */
	private String __parseJoinOn(JoinType joinType, QueryCondition condition, boolean isSubQuery, boolean isAliasNotNull) {
		StringBuilder _joinSB = new StringBuilder();
		switch (joinType) {
		case LEFT_JOIN :
			_joinSB.append(" LEFT JOIN ");
			break;
		case RIGHT_JOIN :
			_joinSB.append(" RIGHT JOIN ");
			break;
		case INNER_JOIN :
		default :
			_joinSB.append(" JOIN ");
		}
		if (isSubQuery) {
			_joinSB.append("(").append(condition.getConditionName()).append(")");
		} else {
			_joinSB.append(condition.getConditionName());
		}
		if (isAliasNotNull) {
			_joinSB.append(" ").append(condition.getConditionAlias());
		}
		_joinSB.append(" ");
		if (StringUtils.isNotBlank(condition.getOnCondition())) {
			_joinSB.append(" ON ").append("(").append(condition.getOnCondition()).append(")");
		} else {
			throw new CompositeQueryException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.need_on_cond"));
		}
		_joinSB.append(" ");
		return _joinSB.toString();
	}

	/**
	 * <p>
	 * QueryColumn
	 * </p>
	 * <p>
	 * 查询字段类；
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
	 *          <td>2011-6-21上午10:44:02</td>
	 *          </tr>
	 *          </table>
	 */
	public static class QueryColumn {

		private String __columnName;

		private String __columnAlias;

		/**
		 * 构造器
		 * @param columnName
		 */
		public QueryColumn(String columnName) {
			this.__columnName = columnName;
		}

		/**
		 * 构造器
		 * @param columnName
		 * @param columnAlias
		 */
		public QueryColumn(String columnName, String columnAlias) {
			this.__columnName = columnName;
			this.__columnAlias = columnAlias;
		}

		/**
		 * @return 获取字段别名
		 */
		public String getColumnAlias() {
			return __columnAlias;
		}

		public QueryColumn setColumnAlias(String columnAlias) {
			this.__columnAlias = columnAlias;
			return this;
		}

		/**
		 * @return 获取字段名称
		 */
		public String getColumnName() {
			return __columnName;
		}

		public QueryColumn setColumnName(String columnName) {
			this.__columnName = columnName;
			return this;
		}

	}

	/**
	 * <p>
	 * QueryCondition
	 * </p>
	 * <p>
	 * 查询条件类；
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
	 *          <td>2011-6-21上午10:50:34</td>
	 *          </tr>
	 *          </table>
	 */
	public static class QueryCondition {

		private String __conditionName;
		private String __conditionAlias;
		private List<QueryColumn> __columnList = new ArrayList<QueryColumn>();
		private String __condition;
		private JoinType __joinType;
		private String __whereConditionType;

		/**
		 * 构造器
		 * 
		 * @param conditionName
		 */
		public QueryCondition(String conditionName) {
			this.__conditionName = conditionName;
		}

		/**
		 * 构造器
		 * 
		 * @param conditionName
		 * @param conditionAlias
		 */
		public QueryCondition(String conditionName, String conditionAlias) {
			this.__conditionName = conditionName;
			this.__conditionAlias = conditionAlias;
		}

		/**
		 * @return 获取查询字段列集合
		 */
		public List<QueryColumn> getColumns() {
			return __columnList;
		}

		/**
		 * 添加查询字段
		 * 
		 * @param column
		 * @return
		 */
		public QueryCondition addColumn(QueryColumn column) {
			this.__columnList.add(column);
			return this;
		}

		/**
		 * 添加查询字段
		 * 
		 * @param columnName
		 * @return
		 */
		public QueryCondition addColumn(String columnName) {
			this.__columnList.add(new QueryColumn(columnName));
			return this;
		}

		/**
		 * 添加查询字段
		 * 
		 * @param columnName
		 * @param columnAlias
		 * @return
		 */
		public QueryCondition addColumn(String columnName, String columnAlias) {
			this.__columnList.add(new QueryColumn(columnName, columnAlias));
			return this;
		}

		/**
		 * @return 获取数据表上下文关联JOIN查询类型（仅对下一个组合查询条件对象生效），分别为：LEFT、INNER和RIGHT，默认INNER
		 */
		public JoinType getJoinType() {
			return __joinType;
		}

		/**
		 * 设置数据表上下文关联JOIN查询类型（仅对下一个组合查询条件对象生效）
		 * 
		 * @param joinType
		 * @return
		 */
		public QueryCondition setJoinType(JoinType joinType) {
			this.__joinType = joinType;
			return this;
		}

		/**
		 * @return 获取组合查询条件对象间的关系连接运算符号（仅对下一个组合查询条件对象生效），如：AND或OR等，若为NULL则默认采用 "AND"
		 */
		public String getWhereConditionType() {
			return __whereConditionType;
		}

		/**
		 * 设置组合查询条件对象间的关系连接运算符号（仅对下一个组合查询条件对象生效），如：AND或OR等，若为NULL则默认采用 "AND"
		 * 
		 * @param conditionType
		 * @return
		 */
		public QueryCondition setWhereConditionType(String conditionType) {
			this.__whereConditionType = conditionType;
			return this;
		}

		/**
		 * @return 获取数据表上下文关联查询条件
		 */
		public String getOnCondition() {
			return __condition;
		}

		/**
		 * 设置数据表上下文关联查询条件，若JoinType为空，则此值将成为WHERE条件字符串
		 * 
		 * @param condition
		 * @return
		 */
		public QueryCondition setOnCondition(String condition) {
			this.__condition = condition;
			return this;
		}

		/**
		 * @return 获取别名
		 */
		public String getConditionAlias() {
			return __conditionAlias;
		}

		/**
		 * 设置别名
		 * 
		 * @param conditionAlias
		 * @return
		 */
		public QueryCondition setConditionAlias(String conditionAlias) {
			this.__conditionAlias = conditionAlias;
			return this;
		}

		/**
		 * @return 获取数据表名称或SQL子查询语句
		 */
		public String getConditionName() {
			return __conditionName;
		}

		/**
		 * 设置数据表名称或SQL子查询语句
		 * 
		 * @param conditionName
		 * @return
		 */
		public QueryCondition setConditionName(String conditionName) {
			this.__conditionName = conditionName;
			return this;
		}

	}

	/**
	 * <p>
	 * JoinType
	 * </p>
	 * <p>
	 * 查询联接类型枚举；
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
	 *          <td>2011-6-21上午11:00:11</td>
	 *          </tr>
	 *          </table>
	 */
	public static enum JoinType {

		INNER_JOIN("INNER"),
		LEFT_JOIN("LEFT JOIN"),
		RIGHT_JOIN("RIGHT JOIN");
		
		private String __joinType;
		
		private JoinType(String type) {
			this.__joinType = type;
		}

		public String getJoinType() {
			return this.__joinType;
		}

		public void setJoinType(String type) {
			this.__joinType = type;
		}

	}

}
