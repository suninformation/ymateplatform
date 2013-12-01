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
package net.ymate.platform.commons.beans;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * AbstractBeanFactory
 * </p>
 * <p>
 * 对象工厂接口抽象实现类；
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
 *          <td>2012-12-15下午3:09:43</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractBeanFactory implements IBeanFactory {

	protected final Map<String, Object> beanMap;
	protected List<IBeanMeta> beanMetaList;
	
	/**
	 * 构造器
	 * <p>默认不自动加载对象元描述集合，需要子类中调用doLoadBeanMeta方法</p>
	 */
	public AbstractBeanFactory() {
		this.beanMap = new HashMap<String, Object>();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.IBeanFactory#get(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz) {
		return (T) beanMap.get(clazz.getName());
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.IBeanFactory#get(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String id) {
		return (T) beanMap.get(id);
	}

	/**
	 * 加载对象元描述集合
	 */
	protected void doLoadBeanMeta() {
		this.beanMetaList = this.loadBeanMetas();
		//
		for (IBeanMeta _meta : this.beanMetaList) {
			this.addBeanMeta(_meta);
		}
	}

	/**
	 * 添加对象元描述方法，用于子类实现自定义添加规则
	 * 
	 * @param beanMeta 目标元描述对象
	 */
	protected abstract void addBeanMeta(IBeanMeta beanMeta);

	/**
	 * 由子类实现具体加载过程
	 * 
	 * @return 加载的对象元描述集合
	 */
	protected abstract List<IBeanMeta> loadBeanMetas();

	/**
	 * 将目标元描述的对象存入工厂对象池中（注：未进行重复检测）
	 * 
	 * @param beanMeta 目标元描述对象
	 */
	protected void addBeanMetaToMap(IBeanMeta beanMeta) {
//		if (!beanMap.containsKey(beanMeta.getClassName())) {
			Object object = beanMeta.getObject();
			String id = beanMeta.getId();
			if (StringUtils.isNotEmpty(id)) {
				beanMap.put(id, object);
			}
			beanMap.put(beanMeta.getClassName(), object);
			String[] keys = beanMeta.getInterfaceNames();
			for (String k : keys) {
				// 排除JDK自带的接口和自己定接口列表
				if (/* k.startsWith("java") || */this.getExcludedClassNameSet().contains(k)) {
					continue;
				}
				beanMap.put(k, object);
			}
//		} else {
//			System.err.println("[警告]存在重复的键" + beanMeta.getClassName() + "，已忽略");
//		}
	}

	/**
	 * 用于子类自定义元描述对象存入工厂对象池时进行接口过滤，默认返回的是emptySet
	 * 
	 * @return 返回排除的接口类名称集合
	 */
	protected Set<String> getExcludedClassNameSet() {
		return Collections.emptySet();
	}

}
