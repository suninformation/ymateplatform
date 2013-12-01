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
package net.ymate.platform.mvc.web.support;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.ymate.platform.mvc.web.IUploadFileWrapper;
import net.ymate.platform.mvc.web.IWebMultipartHandler;
import net.ymate.platform.mvc.web.WebMVC;
import net.ymate.platform.mvc.web.annotation.FileUpload;
import net.ymate.platform.mvc.web.impl.DefaultWebMultipartHandler;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * MultipartRequestWrapper
 * </p>
 * <p>
 * 类型为"multipart/form-data"表单请求包装类；
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
 *          <td>2011-8-5上午10:19:47</td>
 *          </tr>
 *          </table>
 */
public class MultipartRequestWrapper extends HttpServletRequestWrapper {

	private IWebMultipartHandler __handler;

	/**
	 * 构造器
	 * @param request 原始请求对象
	 * @param upload 文件上传注解对象
	 * @throws Exception 
	 */
	public MultipartRequestWrapper(HttpServletRequest request, FileUpload upload) throws Exception {
		super(request);
		__handler = WebMVC.getConfig().getMultipartHandlerClassImpl();
		if (__handler == null) {
			__handler = new DefaultWebMultipartHandler();
		}
		__handler.doHandler(upload);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
	 */
	public String getParameter(String name) {
		String _returnStr = super.getParameter(name);
		if (StringUtils.isBlank(_returnStr)) {
			String[] params = __handler.getFieldMap().get(name);
			_returnStr = (params == null ? null : params[0]);
		}
		return _returnStr;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String name) {
		String[] _returnStr = super.getParameterValues(name);
		if (_returnStr == null || _returnStr.length == 0) {
			_returnStr = __handler.getFieldMap().get(name);
		}
		return _returnStr;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getParameterMap()
	 */
	public Map<String, String[]> getParameterMap() {
		@SuppressWarnings("unchecked")
		Map<String, String[]> _returnMap = new HashMap<String, String[]>(super.getParameterMap());
		_returnMap.putAll(__handler.getFieldMap());
		return Collections.unmodifiableMap(_returnMap);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getParameterNames()
	 */
	public Enumeration<String> getParameterNames() {
		Enumeration<String> names = new Enumeration<String>() {
			private Iterator<String> it = getParameterMap().keySet().iterator();

			public boolean hasMoreElements() {
				return it.hasNext();
			}

			public String nextElement() {
				return it.next();
			}
		};
		return names;
	}

	/**
	 * @param name 文件字段名称
	 * @return 获取上传的文件
	 */
	public IUploadFileWrapper getFile(String name) {
		IUploadFileWrapper[] files = __handler.getFileMap().get(name);
		return files == null ? null : files[0];
	}

	/**
	 * @param name 文件字段名称
	 * @return 获取上传的文数组
	 */
	public IUploadFileWrapper[] getFiles(String name) {
		return __handler.getFileMap().get(name);
	}

}
