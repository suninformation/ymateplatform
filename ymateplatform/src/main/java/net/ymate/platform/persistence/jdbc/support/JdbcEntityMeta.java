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

import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.base.dialect.IDialect;
import net.ymate.platform.persistence.support.EntityMeta;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * JdbcEntityMeta
 * </p>
 * <p>
 * JDBC实体模型元数据描述类；
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
 *          <td>2011-6-16下午09:41:36</td>
 *          </tr>
 *          </table>
 */
public class JdbcEntityMeta extends EntityMeta {

	/**
	 * 构造器
	 *
	 * @param entityClass 实体类对象
	 */
	public JdbcEntityMeta(Class<?> entityClass) {
		super(entityClass, JDBC.TABLE_PREFIX);
	}

	/**
	 * 构造器
	 * @param entityClass 实体类对象
	 * @param simple 是否为简单的用于结果集与实现类映射，若为true则此entityClass对象不能用于数据库操作
	 */
	public JdbcEntityMeta(Class<?> entityClass, boolean simple) {
		super(entityClass, JDBC.TABLE_PREFIX, simple);
	}

	/**
	 * @param dialect 指定数据库方言
	 * @return 构建根据主键检索数据库SQL
	 */
	public String createSelectByPkSql(IDialect dialect) {
		return createSelectByPkSql(dialect, null, null);
	}

	public String createSelectByPkSql(IDialect dialect, String[] fieldFilter, String[] pkFieldFilter) {
		String _sql = "select #FIELDS from #TABLENAME where #PK";
		_sql = _sql.replaceAll("#FIELDS", __doGenerateFieldsFormatStr(dialect, fieldFilter != null && fieldFilter.length > 0 ? Arrays.asList(fieldFilter) : this.getColumnNames()));
		if (dialect.getDialectName().equals("Oracle")) {
			_sql = _sql.replaceAll("#TABLENAME", this.getTableName());
		} else {
			_sql = _sql.replaceAll("#TABLENAME", dialect.wapperQuotedIdent(this.getTableName()));
		}
		_sql = _sql.replaceAll("#PK", __doGeneratePrimaryKeyFormatStr(dialect, pkFieldFilter != null && pkFieldFilter.length > 0 ? Arrays.asList(pkFieldFilter) : this.getPrimaryKeys()));
		return _sql;
	}

	/**
	 * @return 构建检索数据库全部记录的SQL
	 */
	public String createSelectAllSql(IDialect dialect) {
		return createSelectAllSql(dialect, null);
	}

	public String createSelectAllSql(IDialect dialect, String[] fieldFilter) {
		String _sql = "select #FIELDS from #TABLENAME ";
		_sql = _sql.replaceAll("#FIELDS", __doGenerateFieldsFormatStr(dialect, fieldFilter != null && fieldFilter.length > 0 ? Arrays.asList(fieldFilter) : this.getColumnNames()));
		if (dialect.getDialectName().equals("Oracle")) {
			_sql = _sql.replaceAll("#TABLENAME", this.getTableName());
		} else {
			_sql = _sql.replaceAll("#TABLENAME", dialect.wapperQuotedIdent(this.getTableName()));
		}
		return _sql;
	}

	/**
	 * @return 构建数据库记录插入的SQL
	 */
	public String createInsertSql(IDialect dialect) {
		if (this.hasAutoIncrementColumn()) {
			// 剔除自动生成的主键字段
			List<String> _fieldFilter = new ArrayList<String>();
			for (String _columnName : this.getColumnNames()) {
				// 剔除自动生成的主键字段, 如果指定的序列需要将序列添加到字段集合中
                if (this.isAutoIncrementColumn(_columnName)) {
                    String _seqName = this.getColumnMap().get(_columnName).getSequenceName();
                    if (StringUtils.isBlank(_seqName)) {
                        continue;
                    }
                }
                _fieldFilter.add(_columnName);
			}
			return createInsertSql(dialect, _fieldFilter.toArray(new String[_fieldFilter.size()]));
		}
		return createInsertSql(dialect, null);
	}

	public String createInsertSql(IDialect dialect, String[] fieldFilter) {
		String sql = "insert into #TABLENAME (#FIELDS) values (#VALUES)";
		if (dialect.getDialectName().equals("Oracle")) {
			sql = sql.replaceAll("#TABLENAME", this.getTableName());
		} else {
			sql = sql.replaceAll("#TABLENAME", dialect.wapperQuotedIdent(this.getTableName()));
		}
		sql = sql.replaceAll("#FIELDS", __doGenerateFieldsFormatStr(dialect, fieldFilter != null && fieldFilter.length > 0 ? Arrays.asList(fieldFilter) : this.getColumnNames()));
		sql = sql.replaceAll("#VALUES",__doGenerateFieldsValueFormatStr(dialect, fieldFilter != null && fieldFilter.length > 0 ? Arrays.asList(fieldFilter) : this.getColumnNames()));
		return sql;
	}

	/**
	 * @return 构建数据库记录更新的SQL
	 */
	public String createUpdateByPkSql(IDialect dialect) {
		return createUpdateByPkSql(dialect, null, null);
	}

	public String createUpdateByPkSql(IDialect dialect, String[] fieldFilter, String[] pkFieldFilter) {
		String _sql = "update #TABLENAME set #FIELDS where #PK";
		_sql = _sql.replaceAll("#FIELDS", __doGenerateFieldsValueUpdateFormatStr(dialect, fieldFilter != null && fieldFilter.length > 0 ? Arrays.asList(fieldFilter) : this.getColumnNames()));
		if (dialect.getDialectName().equals("Oracle")) {
			_sql = _sql.replaceAll("#TABLENAME", this.getTableName());
		} else {
			_sql = _sql.replaceAll("#TABLENAME", dialect.wapperQuotedIdent(this.getTableName()));
		}
		_sql = _sql.replaceAll("#PK", __doGeneratePrimaryKeyFormatStr(dialect, pkFieldFilter != null && pkFieldFilter.length > 0 ? Arrays.asList(pkFieldFilter) : this.getPrimaryKeys()));
		return _sql;
	}

	/**
	 * @return 构建数据库记录删除的SQL
	 */
	public String createDeleteByPkSql(IDialect dialect) {
		return createDeleteByPkSql(dialect, null);
	}

	public String createDeleteByPkSql(IDialect dialect, String[] pkFieldFilter) {
		String sql = "delete from #TABLENAME where #PK";
		if (dialect.getDialectName().equals("Oracle")) {
			sql = sql.replaceAll("#TABLENAME", this.getTableName());
		} else {
			sql = sql.replaceAll("#TABLENAME", dialect.wapperQuotedIdent(this.getTableName()));
		}
		sql = sql.replaceAll("#PK", __doGeneratePrimaryKeyFormatStr(dialect, pkFieldFilter != null && pkFieldFilter.length > 0 ? Arrays.asList(pkFieldFilter) : this.getPrimaryKeys()));
		return sql;
	}

	private String __doGeneratePrimaryKeyFormatStr(IDialect dialect, List<String> primaryKeyNames) {
		StringBuilder _pkStr = new StringBuilder();
		for (String pkName : primaryKeyNames) {
			if (_pkStr.length() > 0) {
				_pkStr.append(" and ");
			}
			_pkStr.append(dialect.wapperQuotedIdent(pkName)).append("=?");
		}
		return _pkStr.toString();
	}

	private String __doGenerateFieldsFormatStr(IDialect dialect, List<String> fields) {
		StringBuilder _fieldsSB = new StringBuilder();
		for (String _field : fields) {
			_fieldsSB.append(dialect.wapperQuotedIdent(_field)).append(",");
		}
		return _fieldsSB.substring(0, _fieldsSB.length() - 1);
	}

	private String __doGenerateFieldsValueFormatStr(IDialect dialect, List<String> fields) {
		StringBuilder _returnValue = new StringBuilder();
		for (int i = 0; i < fields.size(); i++) {
			String _columnName = fields.get(i);
			if (this.isAutoIncrementColumn(_columnName)) {
				String _seqName = this.getColumnMap().get(_columnName).getSequenceName();
				if (StringUtils.isNotBlank(_seqName)) {
					// 如果指定了序列值,需要将序列值拼装到SQL对应的位置
					_returnValue.append(dialect.getSequenceNextValSql(_seqName));
				}
			} else {
				_returnValue.append("?");
			}
			if (i < fields.size() - 1) {
				_returnValue.append(",");
			}
		}
		return _returnValue.toString();
	}

	private String __doGenerateFieldsValueUpdateFormatStr(IDialect dialect, List<String> fields) {
		StringBuilder _returnValue = new StringBuilder();
		for (String _field : fields) {
			if (this.getPrimaryKeys().contains(_field)) {
				continue;
			}
			_returnValue.append(dialect.wapperQuotedIdent(_field)).append("=?");
			_returnValue.append(",");
		}
		return _returnValue.length() >= 1 ? _returnValue.substring(0, _returnValue.length() - 1) : _returnValue.toString();
	}

	/**
	 * 处理字段名称，使其符合JavaBean属性串格式<br/>
	 * 例如：字段名称为"user_name"，其处理结果为"UserName"<br/>
	 * @param fieldName 字段名称
	 * @return 符合JavaBean属性格式串
	 */
	public static String buildFieldNameToClassAttribute(String fieldName) {
		String[] _words = StringUtils.split(fieldName, '_');
		if (_words != null) {
			if ( _words.length > 1) {
				StringBuilder _returnBuilder = new StringBuilder();
				for (String _word : _words) {
					_returnBuilder.append(StringUtils.capitalize(_word.toLowerCase()));
				}
				return _returnBuilder.toString();
			} else {
				return StringUtils.capitalize(_words[0].toLowerCase());
			}
		}
		return null;
	}

}
