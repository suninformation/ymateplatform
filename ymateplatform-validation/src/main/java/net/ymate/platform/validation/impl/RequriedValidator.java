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
package net.ymate.platform.validation.impl;

import net.ymate.platform.validation.AbstractValidator;
import net.ymate.platform.validation.IValidateContext;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * RequriedValidator
 * </p>
 * <p>
 * 必填项验证；
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
 *          <td>2013-4-13下午6:06:29</td>
 *          </tr>
 *          </table>
 */
public class RequriedValidator extends AbstractValidator {

	public static final String NAME = "requried";

	/* (non-Javadoc)
	 * @see net.ymate.platform.validation.IValidator#getName()
	 */
	public String getName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.validation.AbstractValidator#onValidateNull(net.ymate.platform.validation.IValidateContext)
	 */
	@Override
	protected String onValidateNull(IValidateContext context) {
		return doMessageResult(context, context.isI18n() ? "ymp.validation.required" : null, "参数不能为空");
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.validation.AbstractValidator#onValidate(net.ymate.platform.validation.IValidateContext)
	 */
	@Override
	protected String onValidate(IValidateContext context) {
		if (isString(context.getFieldValue().getClass()) && StringUtils.isNotBlank((String) context.getFieldValue())) {
			return VALIDATE_SUCCESS;
		}
		return onValidateNull(context);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.validation.AbstractValidator#onValidateArray(net.ymate.platform.validation.IValidateContext, java.lang.Class)
	 */
	@Override
	protected String onValidateArray(IValidateContext context, Class<?> arrayClassType) {
		if (isString(arrayClassType) && ((String[]) context.getFieldValue()).length > 0) {
			return VALIDATE_SUCCESS;
		}
		return onValidateNull(context);
	}

}
