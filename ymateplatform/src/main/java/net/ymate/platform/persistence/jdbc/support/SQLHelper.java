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

import net.ymate.platform.commons.util.ExpressionUtils;
import net.ymate.platform.persistence.jdbc.base.SqlParameter;
import net.ymate.platform.persistence.jdbc.operator.IOperator;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * SQLHelper
 * </p>
 * <p>
 * 拼装SQL语句的工具类；
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
 *          <td>2010-12-24下午10:02:14</td>
 *          </tr>
 *          </table>
 */
public class SQLHelper {

	private String __sqlStr;
	private IOperator __opt;

	/**
	 * 构造器
	 * 
	 * @param sql 目标SQL语句
	 */
	private SQLHelper(IOperator opt, String sql) {
		__opt = opt;
		__sqlStr = sql;
	}

	/**
	 * 创建SQLHelper对象实例
	 * @param sql 目标SQL语句
	 * @return SQLHelper对象实例本身
	 */
	public static SQLHelper create(IOperator opt, String sql) {
		return new SQLHelper(opt, sql);
	}

	/**
	 * 替换SQL串中占位符
	 * @param varName 占位符名称
	 * @param sqlStr 替换占位符的串
	 * @return SQLHelper对象实例本身
	 */
	public SQLHelper replace(String varName, String sqlStr) {
		this.__sqlStr = ExpressionUtils.bind(__sqlStr).set(varName, StringUtils.isNotBlank(sqlStr) ? sqlStr : " ").getResult();
		return this;
	}

	/**
	 * 清除SQL串中占位符（即使用" "替换占位符）
	 * @param varName 占位符名称
	 * @return SQLHelper对象实例本身
	 */
	public SQLHelper remove(String varName) {
		this.__sqlStr = ExpressionUtils.bind(__sqlStr).set(varName, " ").getResult();
		return this;
	}

	/**
	 * 添加SQL参数，若参数数组中值为null则不予处理
	 * @param values 参数集合
	 * @return SQLHelper对象实例本身
	 */
	public SQLHelper addParameters(Object[] values) {
		if (values != null && values.length > 0) {
			for (Object _value : values) {
				if (_value != null)
					this.addParameter(_value);
			}
		}
		return this;
	}

	/**
	 * 添加SQL参数
	 *
	 * @param values 参数集合
	 * @return SQLHelper对象实例本身
	 */
	public SQLHelper addParameters(SqlParameter[] values) {
		if (values != null && values.length > 0) {
			for (SqlParameter _value : values) {
				this.addParameter(_value);
			}
		}
		return this;
	}

	/**
	 * 添加SQL参数
	 * @param value 参数
	 * @return SQLHelper对象实例本身
	 */
	public SQLHelper addParameter(Object value) {
		this.__opt.addParameter(value);
		return this;
	}

	/**
	 * 添加SQL参数
	 * @param value 参数
	 * @return SQLHelper对象实例本身
	 */
	public SQLHelper addParameter(SqlParameter value) {
		this.__opt.addParameter(value);
		return this;
	}

	/**
	 * 添加SQL的IN参数
	 * @param varName 变量名称
	 * @param values 参数值数组
	 * @return 当前SQLHelper对象实例
	 */
	public SQLHelper IN(String varName, Object[] values) {
		StringBuilder _sb = new StringBuilder();
		if (values != null && values.length > 0) {
			for (Object _v : values) {
				if (_sb.length() > 0) {
					_sb.append(",");
				}
				_sb.append("?");
				this.__opt.addParameter(_v);
			}
		}
		String _sql = _sb.toString();
		this.__sqlStr = ExpressionUtils.bind(__sqlStr).set(varName, StringUtils.isNotBlank(_sql) ? _sql : " ").getResult();
		return this;
	}

	/**
	 * 绑定SQL语句到操作者对象
	 */
	public void bindSQL() {
		this.__opt.setSql(this.__sqlStr);
	}

}
