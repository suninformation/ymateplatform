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
package net.ymate.platform.persistence.support;

import net.ymate.platform.commons.beans.impl.AnnotationBeanMetaLoader;
import net.ymate.platform.persistence.support.annotation.Repository;

/**
 * <p>
 * RepositoryBeanMetaLoader
 * </p>
 * <p>
 * 
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
 *          <td>2012-12-27下午11:58:35</td>
 *          </tr>
 *          </table>
 */
public class RepositoryBeanMetaLoader extends AnnotationBeanMetaLoader<Repository> {

	/**
	 * 构造器
	 * 
	 * @param clazz 目标存储器类对象
	 */
	public RepositoryBeanMetaLoader(Class<?> clazz) {
		super(Repository.class, clazz);
	}

	/**
	 * 构造器
	 * 
	 * @param packageNames 目标存储器包名称集合
	 */
	public RepositoryBeanMetaLoader(String... packageNames) {
		super(Repository.class, packageNames);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.impl.AnnotationBeanMetaLoader#getAnnotationValue(java.lang.annotation.Annotation)
	 */
	protected String getAnnotationValue(Repository clazz) {
		return clazz.value();
	}

}
