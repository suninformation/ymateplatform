/**
 * <p>文件名:	WebMVC.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web;

import net.ymate.platform.mvc.MVC;
import net.ymate.platform.mvc.web.impl.WebRequestProcessor;




/**
 * <p>
 * WebMVC
 * </p>
 * <p>
 * 基于Web应用的MVC框架核心管理器；
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
 *          <td>2012-12-7下午10:23:39</td>
 *          </tr>
 *          </table>
 */
public class WebMVC extends MVC {

	/**
	 * 初始化WebMVC管理器
	 * 
	 * @param config
	 */
	public static void initialize(IWebMvcConfig config) {
		__doInitialize(config, new WebRequestProcessor());
	}

	/**
	 * 销毁
	 */
	public static void destory() {
		__doDestroy();
	}

	/**
	 * @return 获取当前配置体系框架初始化配置对象
	 */
	public static IWebMvcConfig getConfig() {
		return (IWebMvcConfig) MVC.getConfig();
	}

}
