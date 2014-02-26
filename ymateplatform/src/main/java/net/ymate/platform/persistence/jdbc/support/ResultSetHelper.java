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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.ClassUtils.ClassBeanWrapper;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.persistence.base.OperatorException;

/**
 * <p>
 * ResultSetHelper
 * </p>
 * <p>
 * 数据结果集处理助手类，用于帮助开发人员便捷的读取数据内容；
 * 注：此助手类仅支持结果集类型为 List&lt;Map&lt;String, Object&gt;&gt; 或 List&lt;Object[]&gt; 的数据；
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
 *          <td>2010-10-10上午10:59:40</td>
 *          </tr>
 *          </table>
 */
public class ResultSetHelper {

	/**
	 * 简单实体模型元数据描述类缓存
	 */
	private static Map<String, JdbcEntityMeta> __cacheSimpleEntityMetas = new ConcurrentHashMap<String, JdbcEntityMeta>();

	/**
	 * @param <T> 实体类型
	 * @param entityClass 实体类对象
	 * @return 获取简单实体模型元数据描述类，若缓存中不存在则创建它
	 */
	public <T> JdbcEntityMeta getSimpleEntityMeta(Class<T> entityClass) {
		JdbcEntityMeta _returnValue = __cacheSimpleEntityMetas.get(entityClass.getName());
		if (_returnValue == null) {
			_returnValue = new JdbcEntityMeta(entityClass, true);
			__cacheSimpleEntityMetas.put(entityClass.getName(), _returnValue);
		}
		return _returnValue;
	}

	/**
	 * 数据结果集
	 */
	private List<?> __dataSet;

	private boolean __isObjectArray = false;

	private int __rowCount;

	private int __colCount;

	private int __position = 0;

	private boolean __clearFlag = false;

	private String __columnNames[] = null;

	/**
	 * @param dataSet 结果数据集合
	 * @return 绑定结果集数据（若参数为空，则返回null）
	 */
	public static ResultSetHelper bind(List<?> dataSet) {
		if (dataSet != null && !dataSet.isEmpty()) {
			Object _value = dataSet.get(0);
			if (_value instanceof Map) {
				return new ResultSetHelper(dataSet, false);
			} else if (_value instanceof Object[]) {
				return new ResultSetHelper(dataSet, true);
			}
		}
		return null;
	}


	/**
	 * 构造器
	 * 
	 * @param resultSet 结果数据集合
	 * @param isObjectArray 集合中数据是否为数组类型
	 */
	private ResultSetHelper(List<?> resultSet, boolean isObjectArray) {
		this.__dataSet = resultSet;
		this.__isObjectArray = isObjectArray;
		if (this.__dataSet == null) {
			this.__colCount = 0;
			this.__rowCount = 0;
		} else {
			this.__rowCount = this.__dataSet.size();
			if (this.__rowCount > 0) {
				if (this.__isObjectArray) {
					this.__colCount = ((Object[]) this. __dataSet.get(0)).length;
				} else {
					this.__colCount = ((Map<?, ?>) this. __dataSet.get(0)).size();
				}
			}
		}
	}

	/**
	 * 按列名顺序获取字段值
	 * 
	 * @param index 索引
	 * @return 字段值
	 */
	public Object getFieldValue(int index) {
		return this.__getFieldVauleImp(index);
	}

	@SuppressWarnings("unchecked")
	private Object __getFieldVauleImp(int index) {
		Object _returnValue = null;
		if (!this.__dataSet.isEmpty()) {
			if (this.__position >= 0 && this.__position < this.__rowCount) {
				if (index >= 0 && index < this.__colCount) {
					if (this.__isObjectArray) {
						Object[] _obj = (Object[]) this.__dataSet.get(this.__position);
						Object[] _object = (Object[]) _obj[index];
						_returnValue = _object[1];
					} else {
						Map<String, Object> map = (Map<String, Object>) this.__dataSet.get(this.__position);
						Iterator<Object> it = map.values().iterator();
						int i = 0;
						while (it.hasNext()) {
							_returnValue = it.next();
							if (index == i) {
								break;
							} else {
								_returnValue = null;
							}
							i++;
						}
					}
				}
			}
		}
		return _returnValue;
	}

	/**
	 * 按照字段名获取字段值
	 * 
	 * @param columnName 字段名称
	 * @return 字段值
	 */
	public Object getFieldValue(String columnName) {
		return this.__getFieldValueImp(columnName);
	}

	@SuppressWarnings("unchecked")
	private Object __getFieldValueImp(String columnName) {
		Object _returnValue = null;
		if (!this.__dataSet.isEmpty()) {
			if (this.__position >= 0 && this.__position < this.__rowCount) {
				if (this.__isObjectArray) {
					Object[] _obj = (Object[]) this.__dataSet.get(this.__position);
					for (int i = 0; i < this.getColumnNames().length; i++) {
						if (this.__columnNames[i].equalsIgnoreCase(columnName)) {
							Object[] _object = (Object[]) _obj[i];
							_returnValue = _object[1];
							break;
						}
					}
				} else {
					Map<String, Object> map = (Map<String, Object>) this.__dataSet.get(this.__position);
					_returnValue = map.get(columnName);
					if (_returnValue == null) {
						for (int i = 0; i < this.getColumnNames().length; i++) {
							if (this.__columnNames[i].equalsIgnoreCase(columnName)) {
								_returnValue = map.get(this.__columnNames[i]);
								break;
							}
						}
					}
				}
			}
		}
		return _returnValue;
	}

	/**
	 * 获取结果集的列名
	 * 
	 * @return String[] 字段名称集合
	 */
	@SuppressWarnings("unchecked")
	public String[] getColumnNames() {
		if (this.__columnNames != null) {
			return this.__columnNames;
		}
		if (this.__rowCount > 0) {
			if (this.__isObjectArray) {
				Object[] _obj = (Object[]) this.__dataSet.get(0);
				this.__columnNames = new String[_obj.length];
				for (int i = 0; i < _obj.length; i++) {
					Object[] _columnObj = (Object[]) _obj[i];
					this.__columnNames[i] =(String) _columnObj[0];
				}
			} else {
				Map<String, Object> map = (Map<String, Object>) this.__dataSet.get(0);
				Iterator<String> it = map.keySet().iterator();
				this.__columnNames = new String[map.keySet().size()];
				int i = 0;
				while (it.hasNext()) {
					this.__columnNames[i] = it.next();
					i++;
				}
			}
			return this.__columnNames;
		} else {
			return null;
		}
	}

	/**
	 * 清除结果集 本方法为可选方法
	 */
	public void clearAll() {
		if (this.__dataSet != null) {
			this.__dataSet.clear();
			this.__dataSet = null;
		}
		this.__columnNames = null;
		this.__clearFlag = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		if (!this.__clearFlag) {
			clearAll();
		}
		super.finalize();
	}

	// ---------------------------- Data Iterator Methods

	/**
	 * 结果集指针移动到首位
	 */
	public void first() {
		this.__position = 0;
	}

	/**
	 * 结果集指针移动到最后一位
	 */
	public void last() {
		this.__position = this.__rowCount - 1;
	}

	/**
	 * 结果集指针移动到下一位
	 */
	public void next() {
		if (this.__position >= 0 && this.__position < this.__rowCount - 1) {
			this.__position = this.__position + 1;
		}
	}

	/**
	 * 结果集指针移动到前一位
	 */
	public void prev() {
		if (this.__position > 0 && this.__position < this.__rowCount) {
			this.__position--;
		}
	}

	/**
	 * 结果集指针移动到指定的位置
	 * 
	 * @param i
	 */
	public void move(int i) {
		if (i >= 0 && i < this.__rowCount) {
			this.__position = i;
		}
	}

	// ---------------------------- Fill Record Data Auto
	
	/**
	 * 将当前行记录数据向 valueObject 对象进行填充，无需再使用getFieldValue方法逐一获取
	 * 
	 * @param valueObject 与记录对应的值对象（对象属性名词要和查询字段名称保持一致，大小写敏感）
	 */
	public void renderToObject(Object valueObject) throws OperatorException {
		if (valueObject == null) {
			throw new OperatorException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.value_obj_null"));
		}
		String[] cnames = this.getColumnNames();
		if (cnames != null) {
			ClassBeanWrapper<?> _wrapper = ClassUtils.wrapper(valueObject);
            for (String key : cnames) {
                Object value = this.getFieldValue(key);
                if (value == null) {
                    continue;
                }
                _wrapper.setValue(key, new BlurObject(value).toObjectValue(_wrapper.getFieldType(key)));
            }
		}
	}

	/**
	 * @param <T> 实体类型
	 * @param entityClass 实体类对象
	 * @return 将数据库记录映射到实体对象
	 * @throws OperatorException
	 */
	public <T> T renderToObject(Class<T> entityClass) throws OperatorException {
		T _returnValue;
		JdbcEntityMeta _meta;
		try {
			_returnValue = entityClass.newInstance();
			_meta = this.getSimpleEntityMeta(entityClass);
		} catch (Exception e) {
			throw new OperatorException(RuntimeUtils.unwrapThrow(e));
		}
		ClassBeanWrapper<?> _wrapper = ClassUtils.wrapper(_returnValue);
		for (String key : _meta.getColumnNames()) {
			Object value = this.getFieldValue(key);
			if (value == null) {
				continue;
			}
			String _classAttr = _meta.getClassAttributeMap().get(key);
			_wrapper.setValue(_classAttr, new BlurObject(value).toObjectValue(_wrapper.getFieldType(_classAttr)));
		}
		return _returnValue;
	}

	// ---------------------------- Get Value Methods

	public Time getAsTime(int i) {
		Object o = getFieldValue(i);
		if (o == null) {
			return null;
		}
		if (o instanceof Time) {
			return (Time) o;
		} else {
			return new Time(((Date) o).getTime());
		}
	}

	public Time getAsTime(String columnName) {
		Object o = getFieldValue(columnName);
		if (o == null) {
			return null;
		}
		if (o instanceof Time) {
			return (Time) o;
		}
		return new Time(((Date) o).getTime());
	}

	public Timestamp getAsTimestamp(int i) {
		Object o = getFieldValue(i);
		if (o == null) {
			return null;
		}
		if (o instanceof Timestamp) {
			return (Timestamp) o;
		}
		return new Timestamp(((Date) o).getTime());
	}

	public Timestamp getAsTimestamp(String columnName) {
		Object o = getFieldValue(columnName);
		if (o == null) {
			return null;
		}
		if (o instanceof Timestamp) {
			return (Timestamp) o;
		}
        return new Timestamp(((Date) o).getTime());
	}

	public Date getAsDate(int i) {
		Object o = getFieldValue(i);
		if (o == null) {
			return null;
		}
		if (o instanceof Date) {
			return (Date) o;
		}
        return new Date(((Timestamp) o).getTime());
	}

	public Date getAsDate(String columnName) {
		Object o = getFieldValue(columnName);
		if (o == null) {
			return null;
		}
		if (o instanceof Date) {
			return (Date) o;
		}
        return new Date(((Timestamp) o).getTime());
	}

	public Float getAsFloat(int i) {
		Object o = getFieldValue(i);
		if (o == null) {
			return null;
		}
		return new BlurObject(o).toFloatValue();
	}

	public Float getAsFloat(String columnName) {
		Object o = getFieldValue(columnName);
		if (o == null) {
			return null;
		}
		return new BlurObject(o).toFloatValue();
	}

	public Double getAsDouble(int i) {
		Object o = getFieldValue(i);
		if (o == null) {
			return null;
		}
		return new BlurObject(o).toDoubleValue();
	}

	public Double getAsDouble(String columnName) {
		Object o = getFieldValue(columnName);
		if (o == null) {
			return null;
		}
		return new BlurObject(o).toDoubleValue();
	}

	public Byte getAsByte(int i) {
		Object o = getFieldValue(i);
		if (o == null) {
			return null;
		}
		if (o instanceof Byte) {
			return (Byte) o;
		} else if (o instanceof Integer) {
			return ((Integer) o).byteValue();
		} else {
			return ((BigDecimal) o).byteValue();
		}
	}

	public Byte getAsByte(String columnName) {
		Object o = getFieldValue(columnName);
		if (o == null) {
			return null;
		}
		if (o instanceof Byte) {
			return (Byte) o;
		} else if (o instanceof Integer) {
			return ((Integer) o).byteValue();
		} else {
			return ((BigDecimal) o).byteValue();
		}
	}

	public Short getAsShort(int i) {
		Object o = getFieldValue(i);
		if (o == null) {
			return null;
		}
		if (o instanceof Short) {
			return (Short) o;
		} else if (o instanceof Integer) {
			return ((Integer) o).shortValue();
		} else {
			return ((BigDecimal) o).shortValue();
		}
	}

	public Short getAsShort(String columnName) {
		Object o = getFieldValue(columnName);
		if (o == null) {
			return null;
		}
		if (o instanceof Short) {
			return (Short) o;
		} else if (o instanceof Integer) {
			return ((Integer) o).shortValue();
		} else {
			return ((BigDecimal) o).shortValue();
		}
	}

	public Long getAsLong(int i) {
		Object o = getFieldValue(i);
		if (o == null) {
			return null;
		}
		return new BlurObject(o).toLongValue();
	}

	public Long getAsLong(String columnName) {
		Object o = getFieldValue(columnName);
		if (o == null) {
			return null;
		}
		return new BlurObject(o).toLongValue();
	}

	public BigDecimal getAsBigDecimal(int i) {
		Object o = getFieldValue(i);
		if (o == null) {
			return null;
		}
		return (BigDecimal) o;
	}

	public BigDecimal getAsBigDecimal(String columnName) {
		Object o = getFieldValue(columnName);
		if (o == null) {
			return null;
		}
		return (BigDecimal) o;
	}

	public Integer getAsInteger(int i) {
		Object o = getFieldValue(i);
		if (o == null) {
			return null;
		}
		return new BlurObject(o).toIntValue();
	}

	public Integer getAsInteger(String columnName) {
		Object o = getFieldValue(columnName);
		if (o == null) {
			return null;
		}
		return new BlurObject(o).toIntValue();
	}

	public Character getAsChar(String columnName) {
		Object o = getFieldValue(columnName);
		if (o == null) {
			return null;
		}
		if (o instanceof Character) {
			return (Character) o;
		} else {
			return o.toString().charAt(0);
		}
	}

	public Character getAsChar(int index) {
		Object o = getFieldValue(index);
		if (o == null) {
			return null;
		}
		if (o instanceof Character) {
			return (Character) o;
		} else {
			return o.toString().charAt(0);
		}
	}

	public String getAsString(String columnName) {
		Object v = getFieldValue(columnName);
		if (v != null) {
			return v.toString();
		} else {
			return null;
		}
	}

	public String getAsString(int index) {
		Object v = getFieldValue(index);
		if (v != null) {
			return v.toString();
		} else {
			return null;
		}
	}

	/**
	 * @return 获取总记录行数
	 */
	public int getRowCount() {
		return __rowCount;
	}

	/**
	 * @return 获取每条记录的字段数
	 */
	public int getColumnCount() {
		return __colCount;
	}

	/**
	 * @return 获取当前所在记录行位置
	 */
	public int getCurrentPosition() {
		return this.__position;
	}

}
