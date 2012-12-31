/**
 * <p>文件名:	PluginInstanceException.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Plugin</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.plugin;

/**
 * 插件实例化异常; 
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
 *          <td>2010-2-8下午03:19:58</td>
 *          </tr>
 *          </table>
 */
public class PluginInstanceException extends PluginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3999038479396745379L;

	/**
	 * 构造器
	 */
	public PluginInstanceException() {
		super();
	}

	/**
	 * 构造器
	 * @param cause
	 */
	public PluginInstanceException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造器
	 * @param message
	 * @param cause
	 */
	public PluginInstanceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造器
	 * @param message
	 */
	public PluginInstanceException(String message) {
		super(message);
	}

}
