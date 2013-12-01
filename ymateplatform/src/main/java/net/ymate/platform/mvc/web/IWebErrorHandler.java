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
package net.ymate.platform.mvc.web;

import net.ymate.platform.mvc.IErrorHandler;
import net.ymate.platform.mvc.view.IView;

/**
 * <p>
 * IWebErrorHandler
 * </p>
 * <p>
 * 基于Web应用的MVC框架异常错误处理器接口；
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
 *          <td>2012-12-9下午3:57:38</td>
 *          </tr>
 *          </table>
 */
public interface IWebErrorHandler extends IErrorHandler {

	/**
	 * 自定义处理URL请求过程
	 * 
	 * @param requestMapping URL请求路径
	 * @return 可用视图对象，若为空则采用系统默认
	 */
	public IView onConvention(String requestMapping);

}
