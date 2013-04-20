/**
 * <p>文件名:	IMvcConfig.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc;

import java.util.Locale;
import java.util.Map;

import net.ymate.platform.plugin.IPluginExtraParser;



/**
 * <p>
 * IMvcConfig
 * </p>
 * <p>
 * MVC框架初始化配置接口；
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
 *          <td>2012-12-6下午7:07:35</td>
 *          </tr>
 *          </table>
 */
public interface IMvcConfig {

	/**
	 * @return 返回MVC框架事件处理器接口实现类
	 */
	public IEventHandler getEventHandlerClassImpl();

	/**
	 * @return 返回MVC框架异常错误处理器接口实现类
	 */
	public IErrorHandler getErrorHandlerClassImpl();

	/**
	 * @return 返回语言区域设置
	 */
	public Locale getLocale();

	/**
	 * @return 是否开启国际化支持，默认false
	 */
	public boolean isI18n();

	/**
	 * @return 返回默认编码字符集
	 */
	public String getCharsetEncoding();

	/**
	 * @return 返回扩展参数设置映射
	 */
	public Map<String, String> getExtendParams();

	/**
	 * @return 返回插件主配置文件附加内容分析器
	 */
	public IPluginExtraParser getPluginExtraParser();

	/**
	 * @return 返回插件主目录路径
	 */
	public String getPluginHome();

	/**
	 * @return 返回自动扫描的控制器包名称集合
	 */
	public String[] getControllerPackages();

}
