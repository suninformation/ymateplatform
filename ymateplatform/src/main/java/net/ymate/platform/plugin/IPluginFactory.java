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

import java.util.Collection;


/**
 * <p>
 * IPluginFactory
 * </p>
 * <p>
 * 插件工厂接口定义类，用于查找插件及插件资源和插件的初始化、启停动作；
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
 *          <td>2011-10-17下午04:49:03</td>
 *          </tr>
 *          </table>
 */
public interface IPluginFactory {

	/**
	 * @param pluginId
	 * @return 获取插件元数据描述对象，若不存在则返回NULL
	 */
	public PluginMeta getPluginMeta(String pluginId);

	public PluginMeta getPluginMeta(Class<?> clazz);

	/**
	 * @return 获取已加载插件集合
	 */
	public Collection<PluginMeta> getPluginMetas();

	/**
	 * @param pluginId
	 * @return 获取插件启动器类对象
	 * @throws PluginNotFoundException
	 * @throws PluginInstanceException
	 */
	public IPlugin getPlugin(String pluginId) throws PluginNotFoundException, PluginInstanceException;

	public <T> T getPlugin(Class<T> clazz) throws PluginNotFoundException, PluginInstanceException;

	/**
	 * @return 获取插件工厂通用类加载器
	 */
	public ClassLoader getPluginClassLoader();

	/**
	 * 插件工厂初始化
	 * 
	 * @param config 插件初始化配置接口
	 * @throws PluginException
	 */
	public void initialize(IPluginConfig config) throws PluginException;

	/**
	 * @return 返回插件工厂初始化配置对象
	 */
	public IPluginConfig getPluginConfig();

	/**
	 * @return 判断是否已初始化完成
	 */
	public boolean isInited();

	/**
	 * 销毁工厂
	 */
	public void destroy();

}
