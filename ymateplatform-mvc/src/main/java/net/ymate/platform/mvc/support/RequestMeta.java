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
package net.ymate.platform.mvc.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.mvc.annotation.RequestMapping;
import net.ymate.platform.mvc.annotation.RequestMetodHandler;
import net.ymate.platform.mvc.filter.IFilter;
import net.ymate.platform.mvc.filter.annotation.Filter;
import net.ymate.platform.mvc.filter.annotation.FilterClean;
import net.ymate.platform.mvc.filter.annotation.FilterGroup;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * RequestMeta
 * </p>
 * <p>
 * MVC请求元数据描述对象；
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
 *          <td>2012-12-10下午10:59:14</td>
 *          </tr>
 *          </table>
 */
public class RequestMeta {

	protected final Object target;
	protected final String mapping;
	protected final IRequestMethodHandler handler;
	protected final Method method;
	protected final String[] methodParamNames;
	protected final List<PairObject<Class<IFilter>, String>> interceptors;
	protected final Class<?>[] parameterTypes;
	
	/**
	 * 构造器
	 * 
	 * @param target 目标控制器对象
	 * @param rootMapping 控制器根请求映射
	 * @param method 方法
	 * @param interceptors 控制器根拦截器集合
	 */
	@SuppressWarnings("unchecked")
	public RequestMeta(Object target, String rootMapping, Method method, List<PairObject<Class<IFilter>, String>> interceptors) {
		this.target = target;
		this.mapping = this.buildRequestMapping(rootMapping, method);
		this.method = method;
		RequestMetodHandler _handlerAnno = method.getAnnotation(RequestMetodHandler.class);
		if (_handlerAnno != null && _handlerAnno.value() != null) {
			this.handler = ClassUtils.impl(_handlerAnno.value(), IRequestMethodHandler.class);
		} else {
			this.handler = null;
		}
		this.methodParamNames = ClassUtils.getMethodParamNames(method);
		this.parameterTypes = method.getParameterTypes();
		// 处理控制器方法对象声明的拦截器
		boolean hasInterceptorClean = method.isAnnotationPresent(FilterClean.class);
		List<PairObject<Class<IFilter>, String>> _interceptors = hasInterceptorClean ? new ArrayList<PairObject<Class<IFilter>, String>>() : new ArrayList<PairObject<Class<IFilter>, String>>(interceptors);
		FilterGroup _interceptorGroup = method.getAnnotation(FilterGroup.class);
		if (_interceptorGroup != null && _interceptorGroup.value() != null && _interceptorGroup.value().length > 0) {
			Filter[] _interceptorArray = _interceptorGroup.value();
			if (_interceptorArray != null && _interceptorArray.length > 0) {
				for (Filter _interceptor : _interceptorArray) {
					_interceptors.add(new PairObject<Class<IFilter>, String>((Class<IFilter>) _interceptor.value(), _interceptor.params()));
				}
			}
		} else {
			Filter _interceptor = method.getAnnotation(Filter.class);
			if (_interceptor != null) {
				_interceptors.add(new PairObject<Class<IFilter>, String>((Class<IFilter>) _interceptor.value(), _interceptor.params()));
			}
		}
		this.interceptors = _interceptors;
	}

	/**
	 * 若RequestMapping的value为空，则应取方法名称作为值，根与RequestMapping.value之间的分隔符需要通过getMappingSeparator方法获取
	 * 
	 * @param rootMapping 控制器根请求映射
	 * @param method 方法对象
	 * @return 拼装与当前方法匹配的请求映射
	 */
	protected String buildRequestMapping(String rootMapping, Method method) {
		StringBuilder _mappingSB = new StringBuilder(this.checkMappingSeparator(rootMapping));
		String _mapping = method.getAnnotation(RequestMapping.class).value();
		if (StringUtils.isBlank(_mapping)) {
			_mappingSB.append(this.getMappingSeparator()).append(method.getName());
		} else {
			_mappingSB.append(this.checkMappingSeparator(_mapping));
		}
		return _mappingSB.toString();
	}

	/**
	 * @param mapping 原始Maping字符串
	 * @return 检查Mapping分隔符并返回校正后结果
	 */
	protected String checkMappingSeparator(String mapping) {
		if (StringUtils.isBlank(mapping)) {
			return "";
		}
		if (!mapping.startsWith(this.getMappingSeparator())) {
			mapping = this.getMappingSeparator() + mapping;
		}
		if (mapping.endsWith(this.getMappingSeparator())) {
			mapping = mapping.substring(0, mapping.length() - 1);
		}
		return mapping;
	}

	/**
	 * @return 返回RequestMapping分隔符
	 */
	protected String getMappingSeparator() {
		return ".";
    }

	/**
	 * @return 返回请求映射
	 */
	public String getRequestMapping() {
		return mapping;
	}

	/**
	 * @return 返回控制器请求方法参数处理程序
	 */
	public IRequestMethodHandler getRequestMethodHandler() {
		return handler;
	}

	/**
	 * @return 返回方法对象
	 */
	public Method getMethod() {
		return method;
	}

	public String[] getMethodParamNames() {
		return methodParamNames;
	}

	/**
	 * @return 返回方法对象的参数类型集合
	 */
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	/**
	 * @return 返回拦截器集合
	 */
	public List<PairObject<Class<IFilter>, String>> getFilters() {
		return Collections.unmodifiableList(interceptors);
	}

	/**
	 * @return 返回目标控制器对象
	 */
	public Object getTarget() {
		return target;
	}

}
