/**
 * <p>文件名:	ConfigurationInitializeException.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Configuration</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.configuration;

/**
 * <p>
 * ConfigurationInitializeException
 * </p>
 * <p>
 * 配置体系初始化异常类；
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
 *          <td>2010-4-17上午10:40:24</td>
 *          </tr>
 *          </table>
 */
public class ConfigurationInitializeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4447432295151630483L;

	/**
	 * 构造器
	 */
	public ConfigurationInitializeException() {
		super();
	}

	/**
	 * 构造器
	 * @param message
	 */
	public ConfigurationInitializeException(String message) {
		super(message);
	}

	/**
	 * 构造器
	 * @param cause
	 */
	public ConfigurationInitializeException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造器
	 * @param message
	 * @param cause
	 */
	public ConfigurationInitializeException(String message, Throwable cause) {
		super(message, cause);
	}

}
