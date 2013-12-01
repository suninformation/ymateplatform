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

import org.apache.commons.lang.StringUtils;

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.validation.AbstractValidator;
import net.ymate.platform.validation.IValidateContext;
import net.ymate.platform.validation.ValidationException;

/**
 * <p>
 * RegexValidator
 * </p>
 * <p>
 * 正则表达式验证；
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
 *          <td>2013-4-14上午1:20:13</td>
 *          </tr>
 *          </table>
 */
public class RegexValidator extends AbstractValidator {

	public static final String NAME = "regex";

	/* (non-Javadoc)
	 * @see net.ymate.platform.validation.IValidator#getName()
	 */
	public String getName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.validation.AbstractValidator#onValidate(net.ymate.platform.validation.IValidateContext)
	 */
	@Override
	protected String onValidate(IValidateContext context) {
		if (doParamsLengthCheck(context, 1)) {
			if (isString(context.getFieldValue().getClass())) {
				String _value = (String) context.getFieldValue();
				if (StringUtils.isNotBlank(_value)) {
					String _regex = getSingleParam(context);
					if (!_value.matches(_regex)) {
						return doMessageResult(context, getResultMessageI18nStr(context));
					}
				}
			} else {
				throw new ValidationException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.validation.unsupport_non_string_cond_op"));
			}
		} else {
			throw new IllegalArgumentException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.validation.validator_parameter_invalid"));
		}
		return onValidateNull(context);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.validation.AbstractValidator#onValidateArray(net.ymate.platform.validation.IValidateContext, java.lang.Class)
	 */
	@Override
	protected String onValidateArray(IValidateContext context, Class<?> arrayClassType) {
		if (doParamsLengthCheck(context, 1)) {
			String _regex = getSingleParam(context);
			if (isString(arrayClassType)) {
				for (String _value : ((String[]) context.getFieldValue())) {
					if (StringUtils.isNotBlank(_value) && !_value.matches(_regex)) {
						return doMessageResult(context, getResultMessageI18nStr(context));
					}
				}
			} else {
				throw new ValidationException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.validation.unsupport_non_string_cond_op"));
			}
		} else {
			throw new IllegalArgumentException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.validation.validator_parameter_invalid"));
		}
		return onValidateNull(context);
	}

	/**
	 * @param context 验证器上下文对象
	 * @return 获取回应消息字符串，非i18n环境请设置为null
	 */
	protected String getResultMessageI18nStr(IValidateContext context) {
		return "ymp.validation.regex";
	}

}
