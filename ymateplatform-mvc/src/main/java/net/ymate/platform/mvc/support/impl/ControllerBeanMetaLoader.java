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
package net.ymate.platform.mvc.support.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.ymate.platform.commons.beans.IBeanMeta;
import net.ymate.platform.commons.beans.impl.AnnotationBeanMetaLoader;
import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.mvc.annotation.Controller;
import net.ymate.platform.mvc.annotation.RequestMapping;
import net.ymate.platform.mvc.filter.IFilter;
import net.ymate.platform.mvc.filter.annotation.Filter;
import net.ymate.platform.mvc.filter.annotation.FilterGroup;
import net.ymate.platform.mvc.support.RequestMeta;

/**
 * <p>
 * ControllerBeanMetaLoader
 * </p>
 * <p>
 * MVC框架控制器对象元描述加载器接口实现类；
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
 *          <td>2012-12-15下午7:19:23</td>
 *          </tr>
 *          </table>
 */
public class ControllerBeanMetaLoader extends AnnotationBeanMetaLoader<Controller> {

	/**
	 * 构造器
	 * 
	 * @param clazz 目标控制器类对象
	 */
	public ControllerBeanMetaLoader(Class<?> clazz) {
		super(Controller.class, clazz);
	}

	/**
	 * 构造器
	 * 
	 * @param packageNames 目标控制器包名称集合
	 */
	public ControllerBeanMetaLoader(String... packageNames) {
		super(Controller.class, packageNames);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.impl.AnnotationBeanMetaLoader#getAnnotationValue(java.lang.annotation.Annotation)
	 */
	protected String getAnnotationValue(Controller clazz) {
		return clazz.value();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.impl.AnnotationBeanMetaLoader#getAnnotationBeanMeta()
	 */
	protected ControllerBeanMeta getAnnotationBeanMeta() {
		return new ControllerBeanMeta();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.impl.AnnotationBeanMetaLoader#parserBeanMeta(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	protected IBeanMeta parserBeanMeta(Class<?> c) {
		ControllerBeanMeta _meta = (ControllerBeanMeta) super.parserBeanMeta(c);
		// 获取控制器rootMapping值
		String _rootMapping = null;
		RequestMapping _requestMapping = c.getAnnotation(RequestMapping.class);
		if (_requestMapping != null) {
			_rootMapping = _requestMapping.value();
		}
		// 处理控制器类对象声明的拦截器
		List<PairObject<Class<IFilter>, String>> _iterceptors = new ArrayList<PairObject<Class<IFilter>, String>>();
		FilterGroup _interceptorGroup = c.getAnnotation(FilterGroup.class);
		if (_interceptorGroup != null && _interceptorGroup.value() != null && _interceptorGroup.value().length > 0) {
			Filter[] _interceptorArray = _interceptorGroup.value();
			if (_interceptorArray != null && _interceptorArray.length > 0) {
				for (Filter _interceptor : _interceptorArray) {
					_iterceptors.add(new PairObject<Class<IFilter>, String>((Class<IFilter>) _interceptor.value(), _interceptor.params()));
				}
			}
		} else {
			Filter _interceptor = c.getAnnotation(Filter.class);
			if (_interceptor != null) {
				_iterceptors.add(new PairObject<Class<IFilter>, String>((Class<IFilter>) _interceptor.value(), _interceptor.params()));
			}
		}
		_meta.setRequestMetas(this.parserRequestMetas(_meta.getObject(), _rootMapping, c, _iterceptors));
		return _meta;
	}

	/**
	 * @param target 控制器对象
	 * @param rootMapping 控制器根请求映射
	 * @param method 方法
	 * @param iterceptors 控制器根拦截器集合
	 * @return 返回MVC请求元数据描述对象实例
	 */
	protected RequestMeta getRequestMeta(Object target, String rootMapping, Method method, List<PairObject<Class<IFilter>, String>> iterceptors) {
		return new RequestMeta(target, rootMapping, method, iterceptors);
	}

	/**
	 * @param target 控制器对象
	 * @param rootMapping 控制器根根请求映射
	 * @param clazz 控制器对象类型
	 * @param iterceptors 控制器根拦截器集合
	 * @return 分析控制器对象并返回MVC请求元数据描述对象集合
	 */
	protected List<RequestMeta> parserRequestMetas(Object target, String rootMapping, Class<?> clazz, List<PairObject<Class<IFilter>, String>> iterceptors) {
		List<RequestMeta> _metas = new ArrayList<RequestMeta>();
		Method[] _methods = clazz.getMethods();
		for (Method _method : _methods) {
			if (_method.isAnnotationPresent(RequestMapping.class)) {
				RequestMeta _meta = this.getRequestMeta(target, rootMapping, _method, iterceptors);
				_metas.add(_meta);
			}
		}
		return _metas;
	}

}
