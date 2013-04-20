/**
 * <p>文件名:	ValidationException.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.validation;

/**
 * <p>
 * ValidationException
 * </p>
 * <p>
 * 验证异常类；
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
 *          <td>2013-4-8下午2:12:21</td>
 *          </tr>
 *          </table>
 */
public class ValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -249494536766598217L;

	/**
	 * 构造器
	 */
	public ValidationException() {
		super();
	}

	/**
	 * 构造器
	 * @param message
	 */
	public ValidationException(String message) {
		super(message);
	}

	/**
	 * 构造器
	 * @param cause
	 */
	public ValidationException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造器
	 * @param message
	 * @param cause
	 */
	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
