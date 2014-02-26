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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.persistence.annotation.Entity;
import net.ymate.platform.persistence.annotation.Id;
import net.ymate.platform.persistence.annotation.PK;
import net.ymate.platform.persistence.annotation.Property;
import net.ymate.platform.persistence.base.IEntity;
import net.ymate.platform.persistence.base.IEntityPK;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * EntityMeta
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
 *          <td>2014年2月16日下午2:20:48</td>
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
	 * @param prefix 前缀
	 */
	public EntityMeta(Class<?> entityClass, String prefix) {
		this(entityClass, prefix, false);
	}

	/**
	 * 构造器
	 * @param entityClass 实体类对象
	 * @param prefix 前缀
	 * @param simple 是否为简单的用于结果集与实现类映射，若为true则此entityClass对象不能用于数据库操作
	 */
	public EntityMeta(Class<?> entityClass, String prefix, boolean simple) {
		if (simple) {
			this.__isSimple = true;
			this.__init(entityClass);
		} else {
			// 判断当前实体对象是否合法
			if (!ClassUtils.isInterfaceOf(entityClass, IEntity.class)) {
				throw new RuntimeException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.entity_class_need_impl", entityClass.getName()));
			}
			Entity t = entityClass.getAnnotation(Entity.class);
			if (null != t) {
				this.__tableName = StringUtils.defaultIfBlank(prefix, "") + t.name();
				this.__init(entityClass);
			} else {
				throw new RuntimeException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.entity_class_need_anno_table", entityClass.getName()));
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
			Property _c = _f.getAnnotation(Property.class);
			if (_c != null) {
				this.getColumns().add(new ColumnInfo(_c.name(), _f.getName(), _c.isAutoIncrement(), _c.sequenceName()));
				String _cName = StringUtils.defaultIfEmpty(_c.name(), _f.getName());
				this.getColumnNames().add(_cName);
				this.getClassAttributeMap().put(_cName, buildFieldNameToClassAttribute(_cName));
				if (_c.isAutoIncrement()) {
					this.__autoIncrementColumns.add(_cName);
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
						throw new RuntimeException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.entity_class_need_entitypk", entityClass.getName()));
					}
					this.__isCompositeKey = true;
					for (Field _pkF : ClassUtils.getFields(_idF.getType(), true)) {
						// PK @Column
						Property _pkC = _pkF.getAnnotation(Property.class);
						if (_pkC != null) {
							this.getColumns().add(new ColumnInfo(_pkC.name(), _pkF.getName(), _pkC.isAutoIncrement(), _pkC.sequenceName()));
							String _cName = StringUtils.defaultIfEmpty(_pkC.name(), _pkF.getName());
							this.getColumnNames().add(_cName);
							this.getClassAttributeMap().put(_cName, buildFieldNameToClassAttribute(_cName));
							this.getPrimaryKeys().add(_cName);
							if (_pkC.isAutoIncrement()) {
								this.__autoIncrementColumns.add(_cName);
							}
						}
					}
				} else {
					Property _idC = _idF.getAnnotation(Property.class);
					if (_idC != null) {
						this.getPrimaryKeys().add(StringUtils.defaultIfEmpty(_idC.name(), _idF.getName()));
					}
				}
			}
			//
			if (this.getPrimaryKeys().isEmpty()) {
				throw new RuntimeException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.entity_class_need_anno_id", entityClass.getName()));
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
