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

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ymate.platform.mvc.web.context.IWebRequestContext;
import net.ymate.platform.mvc.web.support.DispatchHelper;

/**
 * <p>
 * DispatcherServlet
 * </p>
 * <p>
 * WebMVC请求分发调度器；
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
 *          <td>2013年8月18日下午7:04:30</td>
 *          </tr>
 *          </table>
 */
public class DispatcherServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5707999783426434200L;

	private DispatchHelper __dispHelper;
	private ServletContext __servletContext;

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		__servletContext = config.getServletContext();
		__dispHelper = new DispatchHelper(config);
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置默认编码和内容类型
		request.setCharacterEncoding(WebMVC.getConfig().getCharsetEncoding());
		response.setCharacterEncoding(WebMVC.getConfig().getCharsetEncoding());
		response.setContentType("text/html;charset=" + WebMVC.getConfig().getCharsetEncoding());
		//
		IWebRequestContext _context = __dispHelper.bindRequestContext((HttpServletRequest) request);
		__dispHelper.doRequestProcess(_context, __servletContext, request, response);
	}

}
