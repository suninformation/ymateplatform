/**
 * <p>文件名:	Validate.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Validate
 * </p>
 * <p>
 * 验证注解，用于声明一个类成员或一个方法参数对象允许进行数据有效性验证；
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
 *          <td>2013-4-7下午4:45:57</td>
 *          </tr>
 *          </table>
 */
@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {

	/**
	 * @return 验证规则配置集合
	 */
	ValidateRule[] value() default {};

	/**
	 * @return 自定义字段参数名称，若为空则默认采用参数变量名或成员属性名称
	 */
	String name() default "";

	/**
	 * @return 目标对象是否为JavaBean对象
	 */
	boolean isModel() default false;

}
