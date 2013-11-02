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
package net.ymate.platform.mvc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * AbstractView
 * </p>
 * <p>
 * MVC视图接口抽象实现类；
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
 *          <td>2012-12-20下午6:56:56</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractView implements IView {

	protected final Map<String, Object> attributes;

	/**
	 * 构造器
	 */
	public AbstractView() {
		this.attributes = new HashMap<String, Object>();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.view.IView#addAttribute(java.lang.String, java.lang.Object)
	 */
	public void addAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.view.IView#getAttribute(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key) {
		return (T) this.attributes.get(key);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.view.IView#getAttributes()
	 */
	public Map<String, Object> getAttributes() {
		return Collections.unmodifiableMap(this.attributes);
	}

}
