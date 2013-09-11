/**
 * <p>文件名:	CompositeQueryException.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMateSmartPlatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.query;

/**
 * <p>
 * CompositeQueryException
 * </p>
 * <p>
 * 组合查询异常类；
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
 *          <td>2011-6-23上午10:48:42</td>
 *          </tr>
 *          </table>
 */
public class CompositeQueryException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2659583082651603891L;

	/**
	 * 构造器
	 */
	public CompositeQueryException() {
		super();
	}

	/**
	 * 构造器
	 * @param message
	 */
	public CompositeQueryException(String message) {
		super(message);
	}

	/**
	 * 构造器
	 * @param cause
	 */
	public CompositeQueryException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造器
	 * @param message
	 * @param cause
	 */
	public CompositeQueryException(String message, Throwable cause) {
		super(message, cause);
	}

}
