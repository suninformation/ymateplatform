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

import java.io.OutputStream;
import java.util.Map;


/**
 * <p>
 * IView
 * </p>
 * <p>
 * MVC视图接口；
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
 *          <td>2012-12-5下午3:11:32</td>
 *          </tr>
 *          </table>
 */
public interface IView {

	/**
	 * 添加视图属性
	 * 
	 * @param key 属性键
	 * @param value 属性值
	 */
	public void addAttribute(String key, Object value);

	/**
	 * 添加视图属性
	 * 
	 * @param key 属性键
	 * @param value 属性值
	 */
	public <T> T getAttribute(String key);

	/**
	 * @return 返回视图对象的属性映射
	 */
	public Map<String, Object> getAttributes();

	/**
     * 视图渲染动作
     * 
     * @throws Exception 抛出任何可能异常
     */
	public void render() throws Exception;

    /**
	 * @param output 视图渲染指定输出流
	 * @throws Exception 抛出任何可能异常
	 */
	public void render(OutputStream output) throws Exception;

}
