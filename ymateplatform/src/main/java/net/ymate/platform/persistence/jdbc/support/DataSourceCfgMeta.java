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

import java.util.Collections;
import java.util.Map;

/**
 * <p>
 * DataSourceCfgMeta
 * </p>
 * <p>
 * 数据源配置元描述对象；
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
 *          <td>2012-12-29下午3:06:52</td>
 *          </tr>
 *          </table>
 */
public class DataSourceCfgMeta {

	private final String name;

	private final String adapterClass;

	private final String driverClass;

	private final String connectionUrl;

	private final String userName;

	private final String password;

	private final Map<String, String> params;

	public DataSourceCfgMeta(String name, String adapterClass, String driverClass, String connectionUrl, String userName, String password, Map<String, String> params) {
		this.name = name;
		this.adapterClass = adapterClass;
		this.driverClass = driverClass;
		this.connectionUrl = connectionUrl;
		this.userName = userName;
		this.password = password;
		this.params = Collections.unmodifiableMap(params);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the adapterClass
	 */
	public String getAdapterClass() {
		return adapterClass;
	}

	/**
	 * @return the driverClass
	 */
	public String getDriverClass() {
		return driverClass;
	}

	/**
	 * @return the connectionUrl
	 */
	public String getConnectionUrl() {
		return connectionUrl;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the params
	 */
	public Map<String, String> getParams() {
		return params;
	}

}
