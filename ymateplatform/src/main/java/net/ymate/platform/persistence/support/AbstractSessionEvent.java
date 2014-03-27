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

/**
 * <p>
 * AbstractSessionEvent
 * </p>
 * <p>
 * 会话事件处理接口抽象实现类；
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
 *          <td>2014年3月27日下午3:16:09</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractSessionEvent implements ISessionEvent {

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.support.ISessionEvent#onInsertBefore(net.ymate.platform.persistence.support.SessionEventObject)
	 */
	public void onInsertBefore(SessionEventObject event) {
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.support.ISessionEvent#onInsertAfter(net.ymate.platform.persistence.support.SessionEventObject)
	 */
	public void onInsertAfter(SessionEventObject event) {
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.support.ISessionEvent#onUpdateBefore(net.ymate.platform.persistence.support.SessionEventObject)
	 */
	public void onUpdateBefore(SessionEventObject event) {
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.support.ISessionEvent#onUpdateAfter(net.ymate.platform.persistence.support.SessionEventObject)
	 */
	public void onUpdateAfter(SessionEventObject event) {
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.support.ISessionEvent#onRemoveBefore(net.ymate.platform.persistence.support.SessionEventObject)
	 */
	public void onRemoveBefore(SessionEventObject event) {
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.support.ISessionEvent#onRemoveAfter(net.ymate.platform.persistence.support.SessionEventObject)
	 */
	public void onRemoveAfter(SessionEventObject event) {
	}

}
