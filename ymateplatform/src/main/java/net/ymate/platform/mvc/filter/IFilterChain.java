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
package net.ymate.platform.mvc.filter;

import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.mvc.support.RequestMeta;
import net.ymate.platform.mvc.view.IView;

/**
 * <p>
 * IFilterChain
 * </p>
 * <p>
 * 拦截器执行链接口；
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
 *          <td>2012-12-17下午9:20:41</td>
 *          </tr>
 *          </table>
 */
public interface IFilterChain {

	/**
	 * 添加过滤器
	 * 
	 * @param filter 过滤器对象
	 */
	public void add(PairObject<IFilter, String> filter);

	/**
	 * 添加过滤器
	 * 
	 * @param index 索引位置
	 * @param filter 过滤器对象
	 */
	public void add(int index, PairObject<IFilter, String> filter);

	/**
	 * 执行拦截器链
	 * 
	 * @param meta 请求元数据描述对象
	 * @return 返回拦截器链执行结果
	 * @throws Exception 根据具体拦截器实现，可能抛出任何异常
	 */
	public IView doChain(RequestMeta meta) throws Exception;

}
