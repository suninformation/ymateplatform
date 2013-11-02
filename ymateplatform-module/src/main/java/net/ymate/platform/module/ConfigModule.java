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

import java.util.Map;

import net.ymate.platform.base.AbstractModule;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.configuration.Cfgs;
import net.ymate.platform.configuration.ICfgConfig;
import net.ymate.platform.configuration.provider.IConfigurationProvider;
import net.ymate.platform.configuration.provider.impl.JConfigProvider;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * ConfigModule
 * </p>
 * <p>
 * 配置体系模块加载器接口实现类；
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
 *          <td>2012-12-23下午6:58:07</td>
 *          </tr>
 *          </table>
 */
public class ConfigModule extends AbstractModule {

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.AbstractModule#initialize(java.util.Map)
	 */
	public void initialize(final Map<String, String> moduleCfgs) throws Exception {
		Cfgs.initialize(new ICfgConfig() {

			private IConfigurationProvider __provider;

			public String getConfigHome() {
				String _configHomeF = moduleCfgs.get("config_home");
				if (StringUtils.isBlank(_configHomeF) || _configHomeF.equalsIgnoreCase("${root}")) {
					_configHomeF = RuntimeUtils.getRootPath();
				}
				return _configHomeF;
			}

			public String getProjectName() {
				return moduleCfgs.get("project_name");
			}

			public String getModuleName() {
				return moduleCfgs.get("module_name");
			}

			public IConfigurationProvider getConfigurationProviderClassImpl() {
				if (__provider == null) {
					__provider = ClassUtils.impl(StringUtils.defaultIfEmpty(moduleCfgs.get("provider_impl_class"), JConfigProvider.class.getName()), IConfigurationProvider.class, ConfigModule.class);
				}
				return __provider;
			}

		});
	}

}
