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
package net.ymate.platform.mvc.web.context.impl;

import javax.servlet.http.HttpServletRequest;

import net.ymate.platform.mvc.web.context.IWebRequestContext;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * WebRequestContext
 * </p>
 * <p>
 * WebMVC请求上下文封装类，分析请求路径，仅返回控制器请求映射；
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
 *          <td>2012-12-22上午2:17:10</td>
 *          </tr>
 *          </table>
 */
public class WebRequestContext implements IWebRequestContext {

	/**
	 * 原始URL
	 */
	private String url;

	/**
	 * 控制器请求映射
	 */
	private String requestMapping;

	private String prefix;

	/**
	 * 扩展名称
	 */
	private String suffix;

	/**
	 * 构造器
	 * @param request 请求对象
	 * @param prefix URL前缀
	 */
	public WebRequestContext(HttpServletRequest request, String prefix) {
		this.prefix = prefix;
		this.url = StringUtils.defaultIfEmpty(request.getPathInfo(), request.getServletPath());
		this.requestMapping = this.url;
		int _pos = 0;
		if (StringUtils.isNotBlank(prefix)) {
			this.requestMapping = StringUtils.substringAfter(this.requestMapping, this.prefix);
		}
		if (!this.requestMapping.endsWith("/")) {
			_pos = this.requestMapping.lastIndexOf('.');
			if (_pos < this.requestMapping.lastIndexOf('/')) {
				_pos = -1;
			}
		} else {
			// 请求映射字符串(注:必须以字符'/'开始且不以'/'结束)
			this.requestMapping = this.requestMapping.substring(0, this.requestMapping.length() -1);
		}
		if (_pos > 0) {
			this.suffix = this.requestMapping.substring(_pos + 1);
			this.requestMapping = this.requestMapping.substring(0, _pos);
		} else {
			this.suffix = "";
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.context.IRequestContext#getRequestMapping()
	 */
	public String getRequestMapping() {
		return requestMapping;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.context.IWebRequestContext#getUrl()
	 */
	public String getUrl() {
		return url;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.context.IWebRequestContext#getPrefix()
	 */
	public String getPrefix() {
		return prefix;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.context.IWebRequestContext#getSuffix()
	 */
	public String getSuffix() {
		return suffix;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder _tmpSB = new StringBuilder(128)
				.append("[url=").append(this.url)
				.append(", requestMapping=").append(this.requestMapping)
				.append(", prefix=").append(this.prefix)
				.append(", suffix=").append(this.suffix).append("]");
		return _tmpSB.toString();
	}

}
