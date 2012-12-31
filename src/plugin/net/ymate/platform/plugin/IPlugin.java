/**
 * <p>文件名:	IPlugin.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Plugin</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.plugin;

/**
 * <p>
 * IPlugin
 * </p>
 * <p>
 * 插件启动器接口类; 任何插件的启动器类都必须实现该接口;
 * </p>
 * <p>注：请采用继承 AbstractPluginImpl类实现插件开发</p>
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
 *          <td>2011-10-17下午04:44:06</td>
 *          </tr>
 *          </table>
 */
public interface IPlugin {

	/**
	 * 初始化插件, 重写此方法时需执行super()调用
	 * 
	 * @param context
	 * @throws PluginException 插件运行时异常
	 */
	public void doInit(PluginContext context) throws PluginException;

	/**
	 * @return 插件配置信息元数据描述对象
	 */
	public PluginMeta getPluginMeta();

	/**
	 * 启动插件
	 * 
	 * @throws PluginException 插件运行时异常
	 */
	public void doStart() throws PluginException;

	/**
	 * 停止插件
	 * 
	 * @throws PluginException 插件运行时异常
	 */
	public void doStop() throws PluginException;

	/**
	 * 销毁插件, 同时调用doStop()接口方法, 重写此方法时需执行super()调用
	 * 
	 * @throws PluginException 插件运行时异常
	 */
	public void destroy() throws PluginException;

}
