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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.persistence.jdbc.IEntity;
import net.ymate.platform.persistence.jdbc.IEntityPK;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.annotation.Column;
import net.ymate.platform.persistence.jdbc.annotation.Id;
import net.ymate.platform.persistence.jdbc.annotation.PK;
import net.ymate.platform.persistence.jdbc.annotation.Table;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * EntityMeta
 * </p>
 * <p>
 * 实体模型元数据描述类；
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
public class EntityMeta {

	private String __tableName;
	private Class<?> __primaryKeyClass;
	private List<String> __primaryKeys = new ArrayList<String>();
	private List<String> __autoIncrementColumns = new ArrayList<String>();
	private List<ColumnInfo> __columns = new ArrayList<ColumnInfo>();
	//
	private boolean __isCompositeKey;
	//
	private boolean __isSimple;
	//

	/**
	 * 数据表字段名称集合
	 */
	private List<String> __fieldSet = new ArrayList<String>();

	/**
	 * 数据表字段与实体Bean属性对应关系
	 */
	private Map<String, String> __classAttributeMap = new HashMap<String, String>();

	/**
	 * 构造器
	 * 
	 * @param entityClass 实体类对象
	 */
	public EntityMeta(Class<?> entityClass) {
		this(entityClass, false);
	}

	/**
	 * 构造器
	 * @param entityClass 实体类对象
	 * @param simple 是否为简单的用于结果集与实现类映射，若为true则此entityClass对象不能用于数据库操作
	 */
	public EntityMeta(Class<?> entityClass, boolean simple) {
		if (simple) {
			this.__isSimple = true;
			this.__init(entityClass);
		} else {
			// 判断当前实体对象是否合法
			if (!ClassUtils.isInterfaceOf(entityClass, IEntity.class)) {
				throw new RuntimeException("类 " + entityClass.getName() + " 未实现 IEntity 接口，请检查");
			}
			Table t = entityClass.getAnnotation(Table.class);
			if (null != t) {
				this.__tableName = JDBC.TABLE_PREFIX + t.name();
				this.__init(entityClass);
			} else {
				throw new RuntimeException("类 " + entityClass.getName() + " 未指定 @Table 注解，请检查");
			}
		}
	}

	/**
	 * 初始化
	 * 
	 * TODO 尚未对默认值进行判断和处理，若注解中为提供name等属性值则应该默认采用属性名称和类名称；
	 * <p>注意：在DAO实体支持类中需要判断具体字段是否为自增长类型，若是则在插入时请赋值NULL；</p>
	 */
	private void __init(Class<?> entityClass) {
		// @Column
		for (Field _f : ClassUtils.getFields(entityClass, true)) {
			Column _c = _f.getAnnotation(Column.class);
			if (_c != null) {
				this.getColumns().add(new ColumnInfo(_c.name(), _f.getName(), _c.isAutoIncrement(), _c.sequenceName()));
				this.getColumnNames().add(_c.name());
				this.getClassAttributeMap().put(_c.name(), buildFieldNameToClassAttribute(_c.name()));
				if (_c.isAutoIncrement()) {
					this.__autoIncrementColumns.add(_c.name());
				}
			}
		}
		if (!this.__isSimple) {
			// @Id
			List<PairObject<Field, Id>> _results = ClassUtils.getFieldAnnotations(entityClass, Id.class, true);
			if (_results.size() > 0) {
				Field _idF = _results.get(0).getKey();
				this.__primaryKeyClass = _idF.getType();
				PK _pk = _idF.getType().getAnnotation(PK.class);
				if (_pk != null) {
					// 判断当前实体的主键对象是否合法
					if (!ClassUtils.isInterfaceOf(_idF.getType(), IEntityPK.class)) {
						throw new RuntimeException("类 " + entityClass.getName() + " 的复合主键对象未实现 IEntityPK 接口，请检查");
					}
					this.__isCompositeKey = true;
					for (Field _pkF : ClassUtils.getFields(_idF.getType(), true)) {
						// PK @Column
						Column _pkC = _pkF.getAnnotation(Column.class);
						if (_pkC != null) {
							this.getColumns().add(new ColumnInfo(_pkC.name(), _pkF.getName(), _pkC.isAutoIncrement(), _pkC.sequenceName()));
							this.getColumnNames().add(_pkC.name());
							this.getClassAttributeMap().put(_pkC.name(), buildFieldNameToClassAttribute(_pkC.name()));
							this.getPrimaryKeys().add(_pkC.name());
							if (_pkC.isAutoIncrement()) {
								this.__autoIncrementColumns.add(_pkC.name());
							}
						}
					}
				} else {
					Column _idC = _idF.getAnnotation(Column.class);
					if (_idC != null) {
						this.getPrimaryKeys().add(_idC.name());
					}
				}
			}
			//
			if (this.getPrimaryKeys().isEmpty()) {
				throw new RuntimeException("类 " + entityClass.getName() + " 未指定 @Id 注解，请检查");
			}
		}
	}

	/**
	 * @return the __isSimple
	 */
	public boolean isSimple() {
		return __isSimple;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return this.__tableName;
	}

	/**
	 * @return the primaryKeys
	 */
	public List<String> getPrimaryKeys() {
		return __primaryKeys;
	}

	/**
	 * @param columnName 字段名称
	 * @return 判断字段 columnName 的取值是否为自增长
	 */
	public boolean isAutoIncrementColumn(String columnName) {
		return this.__autoIncrementColumns.contains(columnName);
	}

	/**
	 * @return 判断是否存在自动生成的键
	 */
	public boolean hasAutoIncrementColumn() {
		return this.__autoIncrementColumns.size() > 0;
	}

	/**
	 * @return the columns
	 */
	public List<ColumnInfo> getColumns() {
		return __columns;
	}

	/**
	 * @return 通过@Column注解，获取当前模型中所有表字段与类成员名称对应关系
	 */
	public List<String> getColumnNames() {
		return this.__fieldSet;
	}

	/**
	 * @return 数据表字段与实体Bean属性对应关系
	 */
	public Map<String, String> getClassAttributeMap() {
		return this.__classAttributeMap;
	}

	/**
	 * @return 判断当前实体是否为联合主键
	 */
	public boolean isCompositeKey() {
		return this.__isCompositeKey;
	}

	/**
	 * @return 获取主键对象类型
	 */
	public Class<?> getPrimaryKeyClass() {
		return this.__primaryKeyClass;
	}

	/**
	 * @return 构建根据主键检索数据库SQL
	 */
	public String createSelectByPkSql() {
		return createSelectByPkSql(null, null);
	}

	public String createSelectByPkSql(String[] fieldFilter, String[] pkFieldFilter) {
		String _sql = "select #FIELDS from #TABLENAME where #PK";
		_sql = _sql.replaceAll("#FIELDS", __doGenerateFieldsFormatStr(fieldFilter != null && fieldFilter.length > 0 ? Arrays.asList(fieldFilter) : this.getColumnNames()));
		_sql = _sql.replaceAll("#TABLENAME", this.getTableName());
		_sql = _sql.replaceAll("#PK", __doGeneratePrimaryKeyFormatStr(pkFieldFilter != null && pkFieldFilter.length > 0 ? Arrays.asList(pkFieldFilter) : this.getPrimaryKeys()));
		return _sql;
	}

	/**
	 * @return 构建检索数据库全部记录的SQL
	 */
	public String createSelectAllSql() {
		return createSelectAllSql(null);
	}

	public String createSelectAllSql(String[] fieldFilter) {
		String _sql = "select #FIELDS from #TABLENAME ";
		_sql = _sql.replaceAll("#FIELDS", __doGenerateFieldsFormatStr(fieldFilter != null && fieldFilter.length > 0 ? Arrays.asList(fieldFilter) : this.getColumnNames()));
		_sql = _sql.replaceAll("#TABLENAME", this.getTableName());
		return _sql;
	}

	/**
	 * @return 构建数据库记录插入的SQL
	 */
	public String createInsertSql() {
		if (this.hasAutoIncrementColumn()) {
			// 剔除自动生成的主键字段
			List<String> _fieldFilter = new ArrayList<String>();
			for (String _columnName : this.getColumnNames()) {
				// 剔除自动生成的主键字段
				if (this.isAutoIncrementColumn(_columnName)) {
					continue;
				}
				_fieldFilter.add(_columnName);
			}
			return createInsertSql(_fieldFilter.toArray(new String[_fieldFilter.size()]));
		}
		return createInsertSql(null);
	}

	public String createInsertSql(String[] fieldFilter) {
		String sql = "insert into #TABLENAME (#FIELDS) values (#VALUES)";
		sql = sql.replaceAll("#TABLENAME", this.getTableName());
		sql = sql.replaceAll("#FIELDS", __doGenerateFieldsFormatStr(fieldFilter != null && fieldFilter.length > 0 ? Arrays.asList(fieldFilter) : this.getColumnNames()));
		sql = sql.replaceAll("#VALUES",__doGenerateFieldsValueFormatStr(fieldFilter != null && fieldFilter.length > 0 ? Arrays.asList(fieldFilter) : this.getColumnNames()));
		return sql;
	}

	/**
	 * @return 构建数据库记录更新的SQL
	 */
	public String createUpdateByPkSql() {
		return createUpdateByPkSql(null, null);
	}

	public String createUpdateByPkSql(String[] fieldFilter, String[] pkFieldFilter) {
		String _sql = "update #TABLENAME set #FIELDS where #PK";
		_sql = _sql.replaceAll("#FIELDS", __doGenerateFieldsValueUpdateFormatStr(fieldFilter != null && fieldFilter.length > 0 ? Arrays.asList(fieldFilter) : this.getColumnNames()));
		_sql = _sql.replaceAll("#TABLENAME", this.getTableName());
		_sql = _sql.replaceAll("#PK", __doGeneratePrimaryKeyFormatStr(pkFieldFilter != null && pkFieldFilter.length > 0 ? Arrays.asList(pkFieldFilter) : this.getPrimaryKeys()));
		return _sql;
	}

	/**
	 * @return 构建数据库记录删除的SQL
	 */
	public String createDeleteByPkSql() {
		return createDeleteByPkSql(null);
	}

	public String createDeleteByPkSql(String[] pkFieldFilter) {
		String sql = "delete from #TABLENAME where #PK";
		sql = sql.replaceAll("#TABLENAME", this.getTableName());
		sql = sql.replaceAll("#PK", __doGeneratePrimaryKeyFormatStr(pkFieldFilter != null && pkFieldFilter.length > 0 ? Arrays.asList(pkFieldFilter) : this.getPrimaryKeys()));
		return sql;
	}

	private String __doGeneratePrimaryKeyFormatStr(List<String> primaryKeyNames) {
		StringBuilder _pkStr = new StringBuilder();
		for (String pkName : primaryKeyNames) {
			if (_pkStr.length() > 0) {
				_pkStr.append(" and ");
			}
			_pkStr.append(pkName).append("=?");
		}
		return _pkStr.toString();
	}

	private String __doGenerateFieldsFormatStr(List<String> fields) {
		String _returnValue = fields.toString();
		return _returnValue.substring(1, _returnValue.length() - 1);
	}

	private String __doGenerateFieldsValueFormatStr(List<String> fields) {
		StringBuilder _returnValue = new StringBuilder();
		for (int i = 0; i < fields.size(); i++) {
			_returnValue.append("?");
			if (i < fields.size() - 1) {
				_returnValue.append(",");
			}
		}
		return _returnValue.toString();
	}

	private String __doGenerateFieldsValueUpdateFormatStr(List<String> fields) {
		StringBuilder _returnValue = new StringBuilder();
		for (String _field : fields) {
			if (this.getPrimaryKeys().contains(_field)) {
				continue;
			}
			_returnValue.append(_field).append("=?");
			_returnValue.append(",");
		}
		return _returnValue.substring(0, _returnValue.length() - 1);
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

	/**
	 * <p>
	 * ColumnInfo
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
	 *          <td>2011-10-8下午11:19:57</td>
	 *          </tr>
	 *          </table>
	 */
	public class ColumnInfo {
	
		private String columnName;
		private String fieldName;
		private boolean autoIncrement;
		private String sequenceName;

		/**
		 * 构造器
		 */
		public ColumnInfo() {
		}

		/**
		 * 构造器
		 * @param columnName
		 * @param fieldName
		 * @param autoIncrement
		 * @param sequenceName
		 */
		public ColumnInfo(String columnName, String fieldName, boolean autoIncrement, String sequenceName) {
			this.columnName = columnName;
			this.fieldName = fieldName;
			this.autoIncrement = autoIncrement;
			this.sequenceName = sequenceName;
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public boolean isAutoIncrement() {
			return autoIncrement;
		}

		public void setAutoIncrement(boolean autoIncrement) {
			this.autoIncrement = autoIncrement;
		}

		public String getSequenceName() {
			return sequenceName;
		}

		public void setSequenceName(String sequenceName) {
			this.sequenceName = sequenceName;
		}

	}

}
