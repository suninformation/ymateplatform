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
package net.ymate.platform.configuration;

import net.ymate.platform.configuration.provider.IConfigurationProvider;

/**
 * <p>
 * ICfgConfig
 * </p>
 * <p>
 * 配置体系框架初始化配置接口；
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
 *          <td>2012-11-28下午9:04:52</td>
 *          </tr>
 *          </table>
 */
public interface ICfgConfig {

	public static String PROJECTS_FORLDER_NAME = "projects";

	public static String MODULES_FORLDER_NAME = "modules";

	/**
	 * @return 返回配置体系根路径
	 */
	public String getConfigHome();

	/**
	 * @return 返回项目名称
	 */
	public String getProjectName();

	/**
	 * @return 返回模块名称
	 */
	public String getModuleName();

	/**
	 * @return 返回配置提供者接口实现类对象
	 */
	public IConfigurationProvider getConfigurationProviderClassImpl();

}
