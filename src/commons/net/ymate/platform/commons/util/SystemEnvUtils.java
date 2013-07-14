/**
 * <p>文件名:	SystemEnvUtils.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Commons</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * SystemEnvUtils
 * </p>
 * <p>
 * 系统环境变量工具类
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
 *          <td>2010-8-2上午10:11:43</td>
 *          </tr>
 *          </table>
 */
public class SystemEnvUtils {

	private static final Log _LOG = LogFactory.getLog(SystemEnvUtils.class);

	/**
	 * 系统环境变量映射
	 */
	private static final Map<String, String> SYSTEM_ENV_MAP = new HashMap<String, String>();

	static {
		initSystemEnvs();
	}

	/**
	 * 私有构造器， 防止被实例化
	 */
	private SystemEnvUtils() {
	}

	/**
	 * 初始化系统环境，获取当前系统环境变量
	 */
	public static void initSystemEnvs() {
		Process p = null;
		try {
			if (SystemUtils.IS_OS_WINDOWS) {
				p = Runtime.getRuntime().exec("cmd /c set");
			} else if (SystemUtils.IS_OS_UNIX) {
				p = Runtime.getRuntime().exec("/bin/sh -c set");
			} else {
				_LOG.warn("未知操作系统 os.name=" + SystemUtils.OS_NAME);
				SYSTEM_ENV_MAP.clear();
			}
			if (p != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = br.readLine()) != null) {
					int i = line.indexOf("=");
					if (i > -1) {
						String key = line.substring(0, i);
						String value = line.substring(i + 1);
						SYSTEM_ENV_MAP.put(key, value);
					}
				}
			}
		} catch (IOException e) {
			_LOG.warn("获取系统环境变量失败", RuntimeUtils.unwrapThrow(e));
		}
	}

	/**
	 * 获取系统运行时，可以进行缓存
	 *
	 * @return 环境变量对应表
	 */
	public static Map<String, String> getSystemEnvs() {
		if (SYSTEM_ENV_MAP.isEmpty()) {
			initSystemEnvs();
		}
		return SYSTEM_ENV_MAP;
	}

	/**
	 * 获取指定名称的环境值
	 *
	 * @param envName 环境名，如果为空，返回null
	 * @return 当指定名称为空或者对应名称环境变量不存在时返回空
	 */
	public static String getSystemEnv(String envName) {
		if (StringUtils.isNotBlank(envName)) {
			if (SYSTEM_ENV_MAP.isEmpty()) {
				initSystemEnvs();
			}
			return SYSTEM_ENV_MAP.get(envName);
		}
		return null;
	}

}
