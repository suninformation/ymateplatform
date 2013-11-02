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
package net.ymate.platform.validation;


/**
 * <p>
 * IValidateContext
 * </p>
 * <p>
 * 验证器上下文接口对象；
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
 *          <td>2013-4-13上午11:28:22</td>
 *          </tr>
 *          </table>
 */
public interface IValidateContext {

	/**
	 * @return 字段名称
	 */
	public String getFieldName();

	/**
	 * @return 字段值对象
	 */
	public Object getFieldValue();

	/**
	 * @return 验证器参数集合
	 */
	public String[] getParams();

	/**
	 * @return 获取字段值对象
	 */
	public Object getFieldValue(String fieldName);

	/**
	 * @return 是否资源国际化
	 */
	public boolean isI18n();

	/**
	 * @return 错误提示信息模板
	 */
	public String getMessage();

}
