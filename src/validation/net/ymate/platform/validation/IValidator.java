/**
 * <p>文件名:	IValidator.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.validation;

/**
 * <p>
 * IValidator
 * </p>
 * <p>
 * 验证器接口定义；
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
 *          <td>2013-4-7下午4:46:33</td>
 *          </tr>
 *          </table>
 */
public interface IValidator {

	/**
	 * @return 获取验证器名称
	 */
	public String getName();

	/**
	 * @param context 验证器上下文对象
	 * @return 执行并返回验证结果，若无错误则返回null
	 */
	public String validate(IValidateContext context);

}
