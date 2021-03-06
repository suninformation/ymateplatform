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
package net.ymate.platform.mvc.web.filter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import net.ymate.platform.commons.beans.annotation.Bean;
import net.ymate.platform.mvc.filter.IFilter;
import net.ymate.platform.mvc.support.RequestMeta;
import net.ymate.platform.mvc.view.IView;
import net.ymate.platform.mvc.web.WebMVC;
import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.support.HttpMethod;
import net.ymate.platform.mvc.web.support.HttpRequestMeta;
import net.ymate.platform.mvc.web.view.impl.HttpStatusView;

/**
 * <p>
 * RequestMethodFilter
 * </p>
 * <p>
 * 控制器请求方法拦截器；
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
 *          <td>2012-12-20下午1:56:22</td>
 *          </tr>
 *          </table>
 */
@Bean
public class RequestMethodFilter implements IFilter {

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.filter.IFilter#doFilter(net.ymate.platform.mvc.support.RequestMeta, java.lang.String)
	 */
	public IView doFilter(RequestMeta meta, String params) throws Exception {
		HttpRequestMeta _httpMeta = (HttpRequestMeta) meta;
		boolean _flag = !WebMVC.getConfig().isRestfulModel() && !_httpMeta.allowHttpMethod(HttpMethod.valueOf(WebContext.getRequest().getMethod()));
		if (!_flag) {
			String _pValue = null;
			for (String _pName : _httpMeta.getAllowHttpHeaders().keySet()) {
				if (_pName.startsWith("!")) {
					_pValue = WebContext.getRequest().getHeader(_pName.substring(1));
					if (/* StringUtils.isBlank(_pValue) || */StringUtils.equalsIgnoreCase(_pValue, _httpMeta.getAllowHttpHeaders().get(_pName))) {
						_flag = true;
						break;
					}
				} else {
					_pValue = WebContext.getRequest().getHeader(_pName);
					if (/* StringUtils.isBlank(_pValue) || */!StringUtils.equalsIgnoreCase(_pValue, _httpMeta.getAllowHttpHeaders().get(_pName))) {
						_flag = true;
						break;
					}
				}
			}
		}
		if (!_flag) {
			String _pValue = null;
			for (String _pName : _httpMeta.getAllowHttpParams().keySet()) {
				if (_pName.startsWith("!")) {
					_pValue = WebContext.getRequest().getParameter(_pName.substring(1));
					if (/* StringUtils.isBlank(_pValue) || */StringUtils.equalsIgnoreCase(_pValue, _httpMeta.getAllowHttpParams().get(_pName))) {
						_flag = true;
						break;
					}
				} else {
					_pValue = WebContext.getRequest().getParameter(_pName);
					if (/* StringUtils.isBlank(_pValue) || */!StringUtils.equalsIgnoreCase(_pValue, _httpMeta.getAllowHttpParams().get(_pName))) {
						_flag = true;
						break;
					}
				}
			}
		}
		return _flag ? new HttpStatusView(HttpServletResponse.SC_METHOD_NOT_ALLOWED) : null;
	}

}
