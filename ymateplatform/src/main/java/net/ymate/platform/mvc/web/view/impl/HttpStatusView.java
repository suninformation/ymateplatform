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
package net.ymate.platform.mvc.web.view.impl;

import org.apache.commons.lang.StringUtils;

import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.view.AbstractWebView;

/**
 * <p>
 * HttpStatusView
 * </p>
 * <p>
 * HTTP返回码视图实现类；
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
 *          <td>2011-10-29上午02:26:25</td>
 *          </tr>
 *          </table>
 */
public class HttpStatusView extends AbstractWebView {

	/**
	 * HTTP返回码
	 */
	private final int status;
	private final String msg;
	private final boolean __error;

	/**
	 * 构造器
	 * @param status HTTP返回码
	 */
	public HttpStatusView(int status) {
		this.status = status;
		this.msg = null;
		this.__error = true;
	}

	/**
	 * 构造器
	 * @param status HTTP返回码
	 * @param useError 是否使用sendError方法
	 */
	public HttpStatusView(int status, boolean useError) {
		this.status = status;
		this.__error = useError;
		this.msg = null;
	}

	/**
	 * 构造器
	 * @param status HTTP返回码
	 * @param msg 错误提示信息
	 */
	public HttpStatusView(int status, String msg) {
		this.status = status;
		this.msg = msg;
		this.__error = false;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.AbstractWebView#renderView()
	 */
	protected void renderView() throws Exception {
		if (StringUtils.isNotBlank(msg)) {
			WebContext.getResponse().sendError(status, msg);
		} else {
			if (__error) {
				WebContext.getResponse().sendError(status);
			} else {
				WebContext.getResponse().setStatus(status);
			}
		}
	}

}
