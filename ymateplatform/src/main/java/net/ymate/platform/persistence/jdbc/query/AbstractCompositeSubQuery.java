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

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.commons.util.ExpressionUtils;
import net.ymate.platform.persistence.jdbc.query.AbstractCompositeQuery.JoinType;
import net.ymate.platform.persistence.jdbc.query.AbstractCompositeQuery.QueryColumn;
import net.ymate.platform.persistence.jdbc.query.AbstractCompositeQuery.QueryCondition;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * AbstractCompositeSubQuery
 * </p>
 * <p>
 * 组合子查询接口抽象实现类；
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
 *          <td>2011-10-31上午01:56:54</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractCompositeSubQuery implements ICompositeSubQuery {

	private String __whereConditionType;

	private List<QueryCondition> __conditions = new ArrayList<QueryCondition>();

	/**
	 * 构造器
	 */
	public AbstractCompositeSubQuery() {
		this.__doInitCompositeSubQuery();
	}

	/**
	 * 组合子查询对象初始化过程（由子类实现）
	 */
	public abstract void __doInitCompositeSubQuery();

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeSubQuery#getQueryWhereConditionType()
	 */
	public String getQueryWhereConditionType() {
		return __whereConditionType;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.query.ICompositeSubQuery#setQueryWhereConditionType(java.lang.String)
	 */
	public ICompositeSubQuery setQueryWhereConditionType(String conditionType) {
		this.__whereConditionType = conditionType;
		return this;
	}

	/**
	 * 添加查询条件
	 * 
	 * @param condition 查询条件接口实现类对象
	 * @return
	 */
	protected AbstractCompositeSubQuery addCondition(QueryCondition condition) {
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
	public String buildSql() {
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

}
