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
package net.ymate.platform.persistence.jdbc.base;

import net.ymate.platform.commons.lang.BlurObject;


/**
 * <p>
 * SqlParameter
 * </p>
 * <p>
 * SQL参数对象类；
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
 *          <td>2011-8-28上午01:51:52</td>
 *          </tr>
 *          </table>
 */
public class SqlParameter {

	/**
	 * 不指定具体参数类型，爱啥啥...
	 */
	public static int UNKNOW_TYPE = -1001;

	private int __type;

	private Object __value;

	/**
	 * 构造器
	 */
	public SqlParameter(Object value) {
		this(UNKNOW_TYPE, value);
	}

	/**
	 * 构造器
	 * 
	 * @param type java.sql.Types
	 * @param value
	 */
	public SqlParameter(int type, Object value) {
		this.__type = type;
		this.__value = value;
	}

	public int getType() {
		return this.__type;
	}

	public Object getValue() {
		return this.__value;
	}

	public String toString() {
		return new BlurObject(this.__value).toStringValue();
	}

}
