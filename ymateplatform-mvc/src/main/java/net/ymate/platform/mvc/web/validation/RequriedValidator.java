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
package net.ymate.platform.mvc.web.validation;

import net.ymate.platform.mvc.web.IUploadFileWrapper;
import net.ymate.platform.validation.IValidateContext;

/**
 * <p>
 * RequriedValidator
 * </p>
 * <p>
 * 基于Validation框架的RequriedValidator类重写以适应Web文件上传对象IUploadFileWrapper验证；
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
 *          <td>2013年11月1日下午9:35:29</td>
 *          </tr>
 *          </table>
 */
public class RequriedValidator extends net.ymate.platform.validation.impl.RequriedValidator {

	/**
	 * @param type
	 * @return 判断type指定的类型是否是上传文件包装器类型
	 */
	protected boolean isUploadFileWrapper(Class<?> type) {
		if (type.equals(IUploadFileWrapper.class)) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.validation.AbstractValidator#onValidateArray(net.ymate.platform.validation.IValidateContext, java.lang.Class)
	 */
	@Override
	protected String onValidateArray(IValidateContext context, Class<?> arrayClassType) {
		if (isUploadFileWrapper(arrayClassType) && ((IUploadFileWrapper[]) context.getFieldValue()).length > 0) {
			return VALIDATE_SUCCESS;
		}
		return super.onValidateArray(context, arrayClassType);
	}

}
