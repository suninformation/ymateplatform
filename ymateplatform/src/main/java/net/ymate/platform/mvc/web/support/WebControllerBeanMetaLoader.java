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

import java.lang.reflect.Method;
import java.util.List;

import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.mvc.filter.IFilter;
import net.ymate.platform.mvc.support.RequestMeta;
import net.ymate.platform.mvc.support.impl.ControllerBeanMetaLoader;

/**
 * <p>
 * WebControllerBeanMetaLoader
 * </p>
 * <p>
 * 基于Web应用的MVC框架控制器对象元描述加载器接口实现类；
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
 *          <td>2012-12-15下午11:58:42</td>
 *          </tr>
 *          </table>
 */
public class WebControllerBeanMetaLoader extends ControllerBeanMetaLoader {

	/**
	 * 构造器
	 * 
	 * @param clazz 目标控制器类对象
	 */
	public WebControllerBeanMetaLoader(Class<?> clazz) {
		super(clazz);
	}

	/**
	 * 构造器
	 * 
	 * @param packageNames 目标控制器包名称集合
	 */
	public WebControllerBeanMetaLoader(String... packageNames) {
		super(packageNames);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.support.impl.ControllerBeanMetaLoader#getRequestMeta(java.lang.Object, java.lang.String, java.lang.reflect.Method, java.util.List)
	 */
	protected RequestMeta getRequestMeta(Object target, String rootMapping, Method method, List<PairObject<Class<IFilter>, String>> iterceptors) {
		return new HttpRequestMeta(target, rootMapping, method, iterceptors);
	}

}
