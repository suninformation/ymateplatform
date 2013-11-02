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
 * AbstractPlugin
 * </p>
 * <p>
 * 插件启动器接口抽象实现类;
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
 *          <td>2012-4-20下午5:30:30</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractPlugin implements IPlugin {

	private PluginContext __context;

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPlugin#doInit(net.ymate.platform.plugin.PluginContext)
	 */
	public void doInit(PluginContext context) throws PluginException {
		this.__context = context;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPlugin#getPluginMeta()
	 */
	public PluginMeta getPluginMeta() {
		return this.__context.getPluginMeta();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPlugin#destroy()
	 */
	public void destroy() throws PluginException {
		this.doStop();
	}

	/**
	 * @return the pluginContext
	 */
	protected PluginContext getPluginContext() {
		return __context;
	}

}
