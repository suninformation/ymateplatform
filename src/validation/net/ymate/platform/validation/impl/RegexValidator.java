/**
 * <p>文件名:	RegexValidator.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.validation.impl;

import org.apache.commons.lang.StringUtils;

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
						return doMessageResult(context, getResultMessageI18nStr(context), getDefaultResultMessageStr(context));
					}
				}
			} else {
				throw new ValidationException("不支持非字符串类型的正则表达式参数验证");
			}
		} else {
			throw new IllegalArgumentException("未指定正则表达式参数");
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
						return doMessageResult(context, getResultMessageI18nStr(context), getDefaultResultMessageStr(context));
					}
				}
			} else {
				throw new ValidationException("不支持非字符串类型的正则表达式参数验证");
			}
		} else {
			throw new IllegalArgumentException("未指定正则表达式参数");
		}
		return onValidateNull(context);
	}

	/**
	 * @param context 验证器上下文对象
	 * @return 获取回应消息字符串，非i18n环境请设置为null
	 */
	protected String getResultMessageI18nStr(IValidateContext context) {
		return context.isI18n() ? "ymp.validation.regex" : null;
	}

	/**
	 * @param context 验证器上下文对象
	 * @return 获取默认回应消息字符串
	 */
	protected String getDefaultResultMessageStr(IValidateContext context) {
		return "不是有效数据类型";
	}

}
