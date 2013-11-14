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
package net.ymate.platform.plugin;


/**
 * <p>
 * IPluginConfig
 * </p>
 * <p>
 * 插件初始化配置接口；
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
 *          <td>2012-11-30下午6:35:14</td>
 *          </tr>
 *          </table>
 */
public interface IPluginConfig {

	/**
	 * @return 返回插件工厂接口实现类对象
	 */
	public IPluginFactory getPluginFactoryClassImpl();

	/**
	 * @return 返回插件主配置文件分析器接口实现类对象
	 */
	public IPluginParser getPluginParserClassImpl();

	/**
	 * @return 返回插件附加配置分析器接口实现类对象
	 */
	public IPluginExtraParser getPluginExtraParserClassImpl();

	/**
	 * @return 返回插件存放路径
	 */
	public String getPluginHomePath();

	/**
	 * @return 返回插件主配置文件名称，若不提供则默认采用：ymate_plugin.xml
	 */
	public String getPluginManifestFile();

}
