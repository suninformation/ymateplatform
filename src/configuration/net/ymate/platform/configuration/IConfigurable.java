/**
 * <p>文件名:	IConfigurable.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Configuration</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.configuration;


/**
 * <p>
 * IConfigurable
 * </p>
 * <p>
 * 配置能力接口类；实现此接口的具体类将具备赋予其配置对象的能力；
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
 *          <td>2011-8-27上午12:21:26</td>
 *          </tr>
 *          </table>
 */
public interface IConfigurable {

	/**
	 * 获取配置对象类实例；
	 * 
	 * @return 继承IConfiguration接口的具体类
	 */
	public IConfiguration getConfig();
	
	/**
	 * 设置配置对象类实例；
	 * 
	 * @param config 任何继承IConfiguration接口的具体类；
	 */
	public void setConfig(IConfiguration config);

}
