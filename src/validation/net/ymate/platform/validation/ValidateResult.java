/**
 * <p>文件名:	ValidateResult.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.validation;

/**
 * <p>
 * ValidateResult
 * </p>
 * <p>
 * 验证器执行结果对象；
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
 *          <td>2013-4-12下午6:00:53</td>
 *          </tr>
 *          </table>
 */
public class ValidateResult {

	/**
	 * 字段名称
	 */
	private String fieldName;

	/**
	 * 错误信息
	 */
	private String message;

	/**
	 * 构造器
	 * @param fieldName
	 * @param message
	 */
	public ValidateResult(String fieldName, String message) {
		this.fieldName = fieldName;
		this.message = message;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "{ fieldName : '" + fieldName + "', message : '" + message + "' }";
	}

}
