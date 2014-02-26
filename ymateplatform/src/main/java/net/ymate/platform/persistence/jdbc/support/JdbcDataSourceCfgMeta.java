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

import java.util.Map;

import net.ymate.platform.persistence.support.DataSourceCfgMeta;

/**
 * <p>
 * JdbcDataSourceCfgMeta
 * </p>
 * <p>
 * JDBC数据源配置元描述对象；
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
public class JdbcDataSourceCfgMeta extends DataSourceCfgMeta {

	private String adapterClass;

	private String driverClass;

	public JdbcDataSourceCfgMeta(String name, String adapterClass, String driverClass, String connectionUrl, String userName, String password, Map<String, String> params) {
		super(name, connectionUrl, userName, password, params);
		this.adapterClass = adapterClass;
		this.driverClass = driverClass;
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

}
