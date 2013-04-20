/**
 * <p>文件名:	ValidateRule.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.validation.annotation;

/**
 * <p>
 * ValidateRule
 * </p>
 * <p>
 * 验证规则注解；
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
 *          <td>2013-4-10下午7:23:38</td>
 *          </tr>
 *          </table>
 */
public @interface ValidateRule {

	/**
	 * @return 验证器名称
	 */
	String value();

	/**
	 * @return 验证器参数集合
	 */
	String[] params() default {};

	/**
	 * @return 错误提示信息模板
	 */
	String message() default "";

}
