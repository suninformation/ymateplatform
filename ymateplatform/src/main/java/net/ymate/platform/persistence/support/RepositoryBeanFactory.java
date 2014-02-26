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

import java.util.List;

import net.ymate.platform.commons.beans.IBeanMeta;
import net.ymate.platform.commons.beans.IBeanMetaLoader;
import net.ymate.platform.commons.beans.impl.AnnotationBeanFactory;
import net.ymate.platform.persistence.support.annotation.Repository;

/**
 * <p>
 * RepositoryBeanFactory
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
 *          <td>2012-12-30上午1:35:07</td>
 *          </tr>
 *          </table>
 */
public class RepositoryBeanFactory extends AnnotationBeanFactory {

	public RepositoryBeanFactory(String...packageNames) {
		this.packageNames = packageNames;
		this.doLoadBeanMeta();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.impl.AnnotationBeanFactory#loadBeanMetas()
	 */
	protected List<IBeanMeta> loadBeanMetas() {
		return new RepositoryBeanMetaLoader(this.packageNames).loadBeanMetas();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.impl.AnnotationBeanFactory#add(java.lang.Class)
	 */
	public IBeanMeta add(Class<?> clazz) {
		if (clazz.isAnnotationPresent(Repository.class)) {
			IBeanMetaLoader _loader = new RepositoryBeanMetaLoader(clazz);
			for (IBeanMeta _meta : _loader.loadBeanMetas()) {
				if (!this.beanMap.containsKey(_meta.getClassName())) {
					this.beanMetaList.add(_meta);
					this.addBeanMeta(_meta);
					return _meta;
				}
			}
		}
		return null;
	}

}
