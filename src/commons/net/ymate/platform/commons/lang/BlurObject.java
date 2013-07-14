/**
 * <p>文件名:	BlurObject.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Commons</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.lang;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ymate.platform.commons.util.RuntimeUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * BlurObject
 * </p>
 * <p>
 * 模糊对象，不进行强制判断，只要能转换就转换，虽然使用时可能会出现类型不匹配的问题，但是会将类型装换可能带来的错误尽量缩小在一个类里面；
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
 *          <td>2010-4-16下午11:51:39</td>
 *          </tr>
 *          </table>
 */
public class BlurObject implements Serializable, Cloneable {

	private static final Log _LOG = LogFactory.getLog(BlurObject.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4141840934670622411L;

	/** 对象 */
	public static final int OBJECT = 1;

	/** 模糊对象 */
	public static final int BLUR_OBJECT = 2;

	/** 整数 */
	public static final int INT = 3;

	/** 长整数 */
	public static final int LONG = 4;

	/** 串 */
	public static final int STRING = 5;

	/** 浮点 */
	public static final int FLOAT = 6;

	/** 双精度 */
	public static final int DOUBLE = 7;

	/** 布尔 */
	public static final int BOOLEAN = 8;

	/** 映射 */
	public static final int MAP = 9;

	/** 列表 */
	public static final int LIST = 10;

	/** 集合 */
	public static final int SET = 11;

	/** 当前存储对象值 */
	private Object attr;

	/** 当前存储对象类型 */
	private int attrType;

	/** 如果是随意对象，那么需要提供类对象 */
	private Class<?> attrClass;

	/**
	 * 构造器
	 * 
	 * @param o
	 */
	public BlurObject(Object o) {
		attr = o;
		attrType = OBJECT;
		if (o != null) {
			attrClass = o.getClass();
		}
	}

	/**
	 * 构造器
	 * 
	 * @param m
	 */
	public BlurObject(Map<?, ?> m) {
		attr = m;
		attrType = MAP;
		attrClass = Map.class;
	}

	/**
	 * 构造器
	 * 
	 * @param s
	 */
	public BlurObject(Set<?> s) {
		attr = s;
		attrType = SET;
		attrClass = Set.class;
	}

	/**
	 * 构造器
	 * 
	 * @param l
	 */
	public BlurObject(List<?> l) {
		attr = l;
		attrType = LIST;
		attrClass = List.class;
	}

	/**
	 * 构造器
	 * 
	 * @param bo
	 */
	public BlurObject(BlurObject bo) {
		attr = bo;
		attrType = BLUR_OBJECT;
		if (bo != null) {
			attrClass = BlurObject.class;
		}
	}

	/**
	 * 构造器
	 * 
	 * @param i
	 */
	public BlurObject(int i) {
		attr = i;
		attrType = INT;
		attrClass = Integer.class;
	}

	/**
	 * 构造器
	 * 
	 * @param s
	 */
	public BlurObject(String s) {
		attr = s;
		attrType = STRING;
		attrClass = String.class;
	}

	/**
	 * 构造器
	 * 
	 * @param f
	 */
	public BlurObject(float f) {
		attr = f;
		attrType = FLOAT;
		attrClass = Float.class;
	}

	/**
	 * 构造器
	 * 
	 * @param d
	 */
	public BlurObject(double d) {
		attr = d;
		attrType = DOUBLE;
		attrClass = Double.class;
	}

	/**
	 * 构造器
	 * 
	 * @param l
	 */
	public BlurObject(long l) {
		attr = l;
		attrType = LONG;
		attrClass = Long.class;
	}

	/**
	 * 构造器
	 * 
	 * @param o
	 * @param c
	 */
	public BlurObject(Object o, Class<?> c) {
		attr = o;
		attrType = OBJECT;
		attrClass = c;
	}

	/**
	 * 构造器
	 * 
	 * @param b
	 */
	public BlurObject(boolean b) {
		attr = b;
		attrType = BOOLEAN;
		attrClass = Boolean.class;
	}

	/**
	 * 获取数据类型
	 * 
	 * @return
	 */
	public int getObjectType() {
		return attrType;
	}

	/**
	 * 输出对象
	 * 
	 * @return
	 */
	public Object toObjectValue() {
		return attr;
	}

	/**
	 * 输出模糊对象
	 * 
	 * @return
	 */
	public BlurObject toBlurObjectValue() {
		if (attrType == BLUR_OBJECT) {
			return (BlurObject) attr;
		}
		return new BlurObject(attr);
	}

	/**
	 * 输出为映射
	 * 
	 * @return
	 */
	public Map<?, ?> toMapValue() {
		if (attrType == MAP) {
			return (Map<?, ?>) attr;
		}
		return null;
	}

	/**
	 * 输出为列表
	 * 
	 * @return
	 */
	public List<?> toListValue() {
		if (attrType == LIST) {
			return (List<?>) attr;
		}
		return null;
	}

	/**
	 * 输出为集合
	 * 
	 * @return
	 */
	public Set<?> toSetValue() {
		if (attrType == SET) {
			return (Set<?>) attr;
		}
		return null;
	}

	/**
	 * 输出布尔值，如果当前类型非布尔值，那么尝试转换
	 * 
	 * @return
	 */
	public boolean toBooleanValue() {
		if (attr == null) {
			return false;
		}
		if (attrType == INT) {
			return ((Integer) attr) > 0;
		}
		if (attrType == LONG) {
			return ((Long) attr) > 0;
		}
		if (attrType == FLOAT) {
			return ((Float) attr) > 0;
		}
		if (attrType == DOUBLE) {
			return ((Double) attr) > 0;
		}
		if (attrType == STRING) {
			return "true".equalsIgnoreCase(attr.toString()) || "on".equalsIgnoreCase(attr.toString()) || "1".equalsIgnoreCase(attr.toString());
		}
		if (attrType == OBJECT) {
			if (attr instanceof Boolean) {
				return (Boolean) attr;
			}
			if (attr instanceof String) {
				String _value = (String) attr;
				if (_value.equalsIgnoreCase("true") || _value.equalsIgnoreCase("on") || _value.equals("1")) {
					return true;
				} else if (_value.equalsIgnoreCase("false") || _value.equalsIgnoreCase("off") || _value.equals("0")) {
					return false;
				}
			}
			if (attr instanceof Number) {
				Number _value = (Number) attr;
                return _value.intValue() > 0;
			}
		}
        return attrType == BLUR_OBJECT && ((BlurObject) attr).toBooleanValue();
    }

	/**
	 * 输出整数
	 * 
	 * @return
	 */
	public int toIntValue() {
		if (attr == null) {
			return 0;
		}
		if (attrType == INT) {
			return (Integer) attr;
		}
		if (attrType == LONG) {
			return ((Long) attr).intValue();
		}
		if (attrType == FLOAT) {
			return ((Float) attr).intValue();
		}
		if (attrType == DOUBLE) {
			return ((Double) attr).intValue();
		}
		if (attrType == STRING || attrType == OBJECT) {
			int ret = 0;
			try {
				ret = new Double(Double.parseDouble(attr.toString())).intValue();
			} catch (NumberFormatException e) {
				ret = 0;
			}
			return ret;
		}
		if (attrType == BLUR_OBJECT) {
			return ((BlurObject) attr).toIntValue();
		}
		return 0;
	}

	/**
	 * 输出串
	 * 
	 * @return
	 */
	public String toStringValue() {
		if (attr == null) {
			return null;
		}
		if (attrType == STRING || attrType == OBJECT) {
			return attr.toString();
		}
		if (attrType == BLUR_OBJECT) {
			return attr.toString();
		}
		if (attrType == INT) {
			return attr + "";
		}
		if (attrType == LONG) {
			return attr + "";
		}
		if (attrType == FLOAT) {
			return attr + "";
		}
		if (attrType == DOUBLE) {
			return attr + "";
		}
		if (attrType == BOOLEAN) {
			return attr + "";
		}
		return "";
	}

	/**
	 * 输出浮点数
	 * 
	 * @return
	 */
	public float toFloatValue() {
		if (attr == null) {
			return 0.0f;
		}
		if (attrType == INT) {
			return ((Integer) attr).floatValue();
		}
		if (attrType == LONG) {
			return ((Long) attr).floatValue();
		}
		if (attrType == FLOAT) {
			return (Float) attr;
		}
		if (attrType == DOUBLE) {
			return ((Double) attr).floatValue();
		}
		if (attrType == STRING || attrType == OBJECT) {
			float ret;
			try {
				ret = new Double(Double.parseDouble(attr.toString())).floatValue();
			} catch (NumberFormatException e) {
				ret = 0.0f;
			}
			return ret;
		}
		if (attrType == BLUR_OBJECT) {
			return ((BlurObject) attr).toFloatValue();
		}
		return 0.0f;
	}

	/**
	 * 输出双精度
	 * 
	 * @return
	 */
	public double toDoubleValue() {
		if (attr == null) {
			return 0.0;
		}
		if (attrType == INT) {
			return ((Integer) attr).doubleValue();
		}
		if (attrType == LONG) {
			return ((Long) attr).doubleValue();
		}
		if (attrType == FLOAT) {
			return ((Float) attr).doubleValue();
		}
		if (attrType == DOUBLE) {
			return (Double) attr;
		}
		if (attrType == STRING || attrType == OBJECT) {
			double ret;
			try {
				ret = Double.parseDouble(attr.toString());
			} catch (NumberFormatException e) {
				ret = 0.0;
			}
			return ret;
		}
		if (attrType == BLUR_OBJECT) {
			return ((BlurObject) attr).toDoubleValue();
		}
		return 0.0;
	}

	/**
	 * 输出长整形
	 * 
	 * @return
	 */
	public long toLongValue() {
		if (attr == null) {
			return 0l;
		}
		if (attrType == INT) {
			return ((Integer) attr).longValue();
		}
		if (attrType == LONG) {
			return (Long) attr;
		}
		if (attrType == FLOAT) {
			return ((Float) attr).longValue();
		}
		if (attrType == DOUBLE) {
			return ((Double) attr).longValue();
		}
		if (attrType == STRING || attrType == OBJECT) {
			long ret = 0l;
			try {
				ret = new Double(Double.parseDouble(attr.toString())).longValue();
			} catch (NumberFormatException e) {
				ret = 0l;
			}
			return ret;
		}
		if (attrType == BLUR_OBJECT) {
			return ((BlurObject) attr).toLongValue();
		}
		return 0l;
	}

	/**
	 * 输出指定类的对象
	 * 
	 * @param clazz 指定类
	 * @return 如果对象不能转换成指定类返回null，指定类是null，返回null。
	 */
	public Object toObjectValue(Class<?> clazz) {
		Object object = null;
		if (clazz.equals(String.class)) {
			object = this.toStringValue();
		} else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
			object = this.toDoubleValue();
		} else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
			object = this.toFloatValue();
		} else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
			object = this.toIntValue();
		} else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
			object = this.toLongValue();
		} else if (clazz.equals(List.class)) {
			object = this.toListValue();
		} else if (clazz.equals(Map.class)) {
			object = this.toMapValue();
		} else if (clazz.equals(Set.class)) {
			object = this.toSetValue();
		}
		if (object == null) {
			try {
				object = clazz.cast(attr);
			} catch (ClassCastException e) {
				_LOG.warn("", RuntimeUtils.unwrapThrow(e));
			}
		}
		return object;
	}

	/**
	 * 获得对象类
	 * 
	 * @return
	 */
	public Class<?> getObjectClass() {
		if (attrClass != null) {
			return attrClass;
		} else {
			if (attr != null) {
				return attr.getClass();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attr == null) ? 0 : attr.hashCode());
		result = prime * result + ((attrClass == null) ? 0 : attrClass.hashCode());
		result = prime * result + attrType;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BlurObject other = (BlurObject) obj;
		if (this.attr == null) {
			if (other.attr != null) {
				return false;
			}
		} else if (!this.attr.equals(other.attr)) {
			return false;
		}
		if (attrClass == null) {
			if (other.attrClass != null) {
				return false;
			}
		} else if (!attrClass.equals(other.attrClass)) {
			return false;
		}
        return attrType == other.attrType;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (attr != null) {
			return attr.toString();
		}
		return "";
	}
}
