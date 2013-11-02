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
package net.ymate.platform.commons.beans.impl;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.ymate.platform.commons.beans.AbstractBeanMetaLoader;
import net.ymate.platform.commons.beans.IBeanMeta;
import net.ymate.platform.commons.beans.annotation.Bean;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.RuntimeUtils;

/**
 * <p>
 * AnnotationBeanMetaLoader
 * </p>
 * <p>
 * 基于Annotation特性的对象元描述加载器接口实现类；
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
 *          <td>2012-12-15下午4:37:03</td>
 *          </tr>
 *          </table>
 */
public class AnnotationBeanMetaLoader<T extends Annotation> extends AbstractBeanMetaLoader {

	protected Class<T> annotationClass;

	/**
	 * 构造器，加载指定的类
	 * 
	 * @param annotationClass 限定的注解类型
	 * @param clazz 目标类对象
	 */
	public AnnotationBeanMetaLoader(Class<T> annotationClass, Class<?> clazz) {
		this.annotationClass = annotationClass;
		this.beanMetaList = new ArrayList<IBeanMeta>();
		IBeanMeta _meta = parserBeanMeta(clazz);
		if (_meta != null) {
			this.beanMetaList.add(_meta);
		}
	}

	/**
	 * 构造器，加载指定包下的类
	 * 
	 * @param annotationClass 限定的注解类型
	 * @param packageNames 目标包名称集合
	 */
	public AnnotationBeanMetaLoader(Class<T> annotationClass, String...packageNames) {
		this.annotationClass = annotationClass;
		if (packageNames != null) {
			Collection<Class<T>>  _results = ClassUtils.findClassByClazz(annotationClass, Arrays.asList(packageNames), getClass());
			for (Class<T> _c : _results) {
				IBeanMeta _meta = parserBeanMeta(_c);
				if (_meta != null) {
					this.beanMetaList.add(_meta);
				}
			}
		}
	}

	/**
	 * @param clazz 目标类对象
	 * @return 解析限定的注解类型，其返回值将做为Id值
	 */
	protected String getAnnotationValue(T clazz) {
		if (clazz instanceof Bean) {
			return ((Bean) clazz).value();
		}
		return "";
	}

	/**
	 * @return 返回基于Annotation特性的对象元描述实例
	 */
	protected AnnotationBeanMeta getAnnotationBeanMeta() {
		return new AnnotationBeanMeta();
	}

	/**
	 * @param clazz 目标类对象
	 * @return  分析目标类对象并返回对象元描述
	 */
	protected IBeanMeta parserBeanMeta(Class<?> clazz) {
		T _bean = clazz.getAnnotation(annotationClass);
		if (_bean == null) {
			return null;
		}
		AnnotationBeanMeta _meta = this.getAnnotationBeanMeta();
		_meta.setId(getAnnotationValue(_bean));
		_meta.setClassName(clazz.getName());
		//
		String[] names = ClassUtils.getInterfaceNames(clazz);
		_meta.setInterfaceNames(names);
		//
		try {
			_meta.setObject(clazz.newInstance());
		} catch (Throwable e) {
			throw new Error("[错误]分析IBeanMeta时发生异常", RuntimeUtils.unwrapThrow(e));
		}
		return _meta;
	}

}
