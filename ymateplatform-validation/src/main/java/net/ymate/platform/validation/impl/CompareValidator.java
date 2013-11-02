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

import java.util.Map;

import net.ymate.platform.validation.AbstractValidator;
import net.ymate.platform.validation.IValidateContext;
import net.ymate.platform.validation.ValidationException;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * CompareValidator
 * </p>
 * <p>
 * 参数值比较验证；
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
 *          <td>2013-4-17下午9:57:16</td>
 *          </tr>
 *          </table>
 */
public class CompareValidator extends AbstractValidator {

	public static final String NAME = "compare";

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
					Map<String, String> _params = getParamMaps(context);
					String _cond = StringUtils.defaultIfEmpty(_params.get("cond"), "equals");
					String _field = _params.get("field");
					if (_field == null || context.getFieldValue(_field) == null) {
						throw new IllegalArgumentException("未指定验证器参数或参数无效");
					}
					if (_cond.equals("equals")) {
						if (!_value.equals(context.getFieldValue(_field))) {
							return doMessageResult(context, getResultMessageI18nStr(context), getDefaultResultMessageStr(context));
						}
					} else {
						throw new ValidationException("不支持的验证条件运算符");
					}
				}
			} else {
				throw new ValidationException("不支持非字符串类型的参数比较验证");
			}
		} else {
			throw new IllegalArgumentException("未指定验证器参数或参数无效");
		}
		return onValidateNull(context);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.validation.AbstractValidator#onValidateArray(net.ymate.platform.validation.IValidateContext, java.lang.Class)
	 */
	@Override
	protected String onValidateArray(IValidateContext context, Class<?> arrayClassType) {
		throw new ValidationException("不支持非字符串类型的参数比较验证");
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.validation.impl.RegexValidator#getResultMessageI18nStr(net.ymate.platform.validation.IValidateContext)
	 */
	protected String getResultMessageI18nStr(IValidateContext context) {
		return context.isI18n() ? "ymp.validation.compare" : null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.validation.impl.RegexValidator#getDefaultResultMessageStr(net.ymate.platform.validation.IValidateContext)
	 */
	protected String getDefaultResultMessageStr(IValidateContext context) {
		return "两参数值不匹配";
	}

}
