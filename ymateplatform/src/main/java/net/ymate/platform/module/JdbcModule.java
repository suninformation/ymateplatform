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
package net.ymate.platform.module;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.ymate.platform.base.AbstractModule;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.persistence.jdbc.IJdbcConfig;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.support.DataSourceCfgMeta;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * JdbcModule
 * </p>
 * <p>
 * JDBC持久层框架模块加载器接口实现类；
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
 *          <td>2012-12-29下午2:19:18</td>
 *          </tr>
 *          </table>
 */
public class JdbcModule extends AbstractModule {

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.AbstractModule#initialize(java.util.Map)
	 */
	public void initialize(final Map<String, String> moduleCfgs) throws Exception {
		final boolean _showSql = new BlurObject(moduleCfgs.get("base.show_sql")).toBooleanValue();
		final Set<DataSourceCfgMeta> _metas = new HashSet<DataSourceCfgMeta>();
		for (String _name : StringUtils.split(StringUtils.trimToEmpty(moduleCfgs.get("base.datasource_list")), "|")) {
			String _adaptorClass = moduleCfgs.get("datasource." + _name + ".adapter_class");
			String _driverClass = moduleCfgs.get("datasource." + _name + ".driver_class");
			String _connectionUrl = moduleCfgs.get("datasource." + _name + ".connection_url");
			String _userName = moduleCfgs.get("datasource." + _name + ".username");
			String _password = moduleCfgs.get("datasource." + _name + ".password");
			//
			Map<String, String> _params = new HashMap<String, String>();
			String _paramKey = "datasource." + _name + ".params.";
			for (String _cfgKey : moduleCfgs.keySet()) {
				if (_cfgKey.startsWith(_paramKey)) {
					_params.put(StringUtils.substringAfter(_cfgKey, _paramKey), moduleCfgs.get(_cfgKey));
				}
			}
			_metas.add(new DataSourceCfgMeta(_name, _adaptorClass, _driverClass, _connectionUrl, _userName, _password, _params));
		}
		JDBC.initialize(new IJdbcConfig() {
			
			public boolean isShowSql() {
				return _showSql;
			}
			
			public String getTablePrefix() {
				return moduleCfgs.get("base.table_prefix");
			}

			public String getDefaultDataSourceName() {
				return moduleCfgs.get("base.datasource_default");
			}
			
			public String[] getRepositoryPackages() {
				return StringUtils.split(moduleCfgs.get("base.repository_packages"), "|");
			}
			
			public Set<DataSourceCfgMeta> getDataSourceCfgMetas() {
				return _metas;
			}
		});
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.AbstractModule#destroy()
	 */
	public void destroy() throws Exception {
		JDBC.destroy();
	}

}
