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
 * ISessionEvent
 * </p>
 * <p>
 * 会话事件处理接口定义类；
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
 *          <td>2011-9-27下午03:46:08</td>
 *          </tr>
 *          </table>
 */
public interface ISessionEvent {

	/**
	 * 插入操用之前事件调用
	 * 
	 * @param event
	 */
	public void onInsertBefore(SessionEventObject event);

	/**
	 * 插入操作之后事件调用
	 * 
	 * @param event
	 */
	public void onInsertAfter(SessionEventObject event);

	/**
	 * 更新操作之前事件调用
	 * 
	 * @param event
	 */
	public void onUpdateBefore(SessionEventObject event);

	/**
	 * 更新操作之后事件调用
	 * 
	 * @param event
	 */
	public void onUpdateAfter(SessionEventObject event);

	/**
	 * 删除操作之前事件调用
	 * 
	 * @param event
	 */
	public void onRemoveBefore(SessionEventObject event);

	/**
	 * 删除操作之后事件调用
	 * 
	 * @param event
	 */
	public void onRemoveAfter(SessionEventObject event);

}
