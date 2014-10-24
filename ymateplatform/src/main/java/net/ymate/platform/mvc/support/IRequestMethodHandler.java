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
package net.ymate.platform.mvc.support;

import java.lang.reflect.Method;
import java.util.Set;

import net.ymate.platform.validation.ValidateResult;

/**
 * <p>
 * IRequestMethodHandler
 * </p>
 * <p>
 * 控制器请求方法参数处理接口；
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
 *          <td>2013年9月13日下午6:27:57</td>
 *          </tr>
 *          </table>
 */
public interface IRequestMethodHandler {

    /**
     * @return 返回方法对象invoke时所需的参数集合
     * @throws Exception
     */
	public Object[] getMethodParams() throws Exception;

	/**
	 * @param targetMethod 目标控制器方法对象
	 * @param params 方法对象参数集合
	 * @return 执行参数的有效性验证
     * @throws Exception
	 */
	public Set<ValidateResult> doValidation(Method targetMethod, Object[] params) throws Exception;

}
