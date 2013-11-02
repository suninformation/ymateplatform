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
package net.ymate.platform.base;

import java.util.Properties;


/**
 * <p>
 * IModuleLoader
 * </p>
 * <p>
 * 模块加载器接口类；
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
 *          <td>2012-11-30下午7:01:22</td>
 *          </tr>
 *          </table>
 */
public interface IModuleLoader {

	/**
	 * 初始化模块加载器
	 * 
	 * @param configs 配置参数集合
	 * @throws Exception
	 */
	public void initialize(Properties configs) throws Exception;

	/**
	 * 销毁
	 * 
	 * @throws Exception
	 */
	public void destroy() throws Exception;

}
