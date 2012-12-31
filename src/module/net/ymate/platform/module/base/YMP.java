/**
 * <p>文件名:	YMP.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.module.base;

import java.io.InputStream;
import java.util.Properties;

import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.module.base.impl.DefaultModuleLoader;

import org.apache.commons.lang.time.StopWatch;

/**
 * <p>
 * YMP
 * </p>
 * <p>
 * 框架核心模块管理器；
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
 *          <td>2012-12-23下午5:52:44</td>
 *          </tr>
 *          </table>
 */
public class YMP {

	public final static String VERSION = "1.1.0";

	public static boolean IS_DEV_MODEL;

	public static boolean IS_INITED;

	private static IModuleLoader __MODULE_LOADER;

	/**
	 * 初始化框架模块
	 */
	public static void initialize()  {
		if (!IS_INITED) {
			Properties _configs = new Properties();
			InputStream _in = YMP.class.getClassLoader().getResourceAsStream("ymp-conf.properties");
			if (_in == null) {
				System.err.println("[警告：未找到框架配置文件ymp-conf.properties，尝试加载默认配置]");
				_in = YMP.class.getClassLoader().getResourceAsStream("META-INF/ymp-default-conf.properties");
			}
			if (_in != null) {
				try {
					_configs.load(_in);
					IS_DEV_MODEL = new BlurObject(_configs.getProperty("ymp.dev_model")).toBooleanValue();
					__MODULE_LOADER = (IModuleLoader) Class.forName(_configs.getProperty("ymp.module_loader_impl_class")).newInstance();
				} catch (Exception e) {
					__MODULE_LOADER = new DefaultModuleLoader();
				}
			} else {
				System.err.println("[警告：框架配置加载失败，请确认ymp-conf.properties文件是否存在]");
			}
			StopWatch _stopWatch = new StopWatch();
			_stopWatch.start();
			try {
				__MODULE_LOADER.initialize(_configs);
				IS_INITED = true;
			} catch (Exception e) {
				System.err.println("[警告：" + e.getMessage() + "]");
				e.printStackTrace();
			} finally {
				_stopWatch.stop();
				System.out.println("[信息：模块初始化操作" + (IS_INITED ? "完毕" : "失败") + "，耗时" + _stopWatch.getTime() + "ms]");
			}
		}
	}

	/**
	 * 销毁
	 */
	public static void destroy() {
		if (IS_INITED && __MODULE_LOADER != null) {
			try {
				__MODULE_LOADER.destroy();
			} catch (Exception e) {
				//~~~
			}
			__MODULE_LOADER = null;
			IS_INITED = false;
		}
	}

}
