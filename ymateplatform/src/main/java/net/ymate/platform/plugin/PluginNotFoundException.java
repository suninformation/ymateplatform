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
 * 不存在的插件异常
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
 *          <td>2010-2-8下午03:30:55</td>
 *          </tr>
 *          </table>
 */
public class PluginNotFoundException extends PluginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4882773801673945366L;

	/**
	 * 构造器
	 */
	public PluginNotFoundException() {
		super();
	}

	/**
	 * 构造器
	 * @param cause
	 */
	public PluginNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造器
	 * @param message
	 * @param cause
	 */
	public PluginNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造器
	 * @param message
	 */
	public PluginNotFoundException(String message) {
		super(message);
	}

}
