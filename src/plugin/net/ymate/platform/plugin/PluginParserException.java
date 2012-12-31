/**
 * <p>文件名:	PluginParserException.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.plugin;

/**
 * <p>
 * PluginParserException
 * </p>
 * <p>
 * 插件配置文件分析时异常；
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
 *          <td>2012-11-29下午10:37:01</td>
 *          </tr>
 *          </table>
 */
public class PluginParserException extends PluginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4890969690805659547L;

	/**
	 * 构造器
	 */
	public PluginParserException() {
		super();
	}

	/**
	 * 构造器
	 * @param cause
	 */
	public PluginParserException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造器
	 * @param message
	 * @param cause
	 */
	public PluginParserException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造器
	 * @param message
	 */
	public PluginParserException(String message) {
		super(message);
	}

}
