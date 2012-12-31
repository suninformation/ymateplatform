/**
 * <p>文件名:	DateTimeUtils.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Commons</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * DateTimeUtils
 * </p>
 * <p>
 * 日期时间数据处理工具类；
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
 *          <td>2010-4-18上午02:40:41</td>
 *          </tr>
 *          </table>
 */
public class DateTimeUtils {
	
	/** 时间修正 */
	private static long timeMillisOffset;

	/**
	 * 私有构造器， 防止被实例化
	 */
	private DateTimeUtils() {
	}

	/**
	 * 设定时间修正
	 *
	 * @param timeMillisOffset
	 * @return
	 */
	public static void setTimeMillisOffset(long timeMillisOffset) {
		DateTimeUtils.timeMillisOffset = timeMillisOffset;
	}

	/**
	 * 统一的获取当前时间的方法，可以使用各种方式进行修正，获得的是一个相对稳定的时间源
	 *
	 * @return
	 */
	public static long currentTimeMillis() {
		return System.currentTimeMillis() + timeMillisOffset;
	}

	/**
	 * 获取全局时间的秒数
	 *
	 * @return
	 */
	public static long currentTimeMillisUTC() {
		return currentTimeMillis() / 1000L;
	}

	/**
	 * 获得当前时间
	 *
	 * @return
	 */
	public static Date currentTime() {
		return new Date(currentTimeMillis());
	}

	/**
	 * 系统当前时间
	 *
	 * @return
	 */
	public static long systemTimeMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * 系统当前时间
	 *
	 * @return
	 */
	public static Date systemTime() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 获取当前UTC时间
	 *
	 * @return
	 */
	public static int systemTimeUTC() {
		return (int) (systemTimeMillis() / 1000);
	}
	
	/**
	 * 格式化日期时间输出字符串
	 * 
	 * @param time 日期时间值
	 * @param pattern 日期时间输出模式，若为空则使用yyyy-MM-dd HH:mm:ss.SSS作为默认
	 * @return
	 */
	public static String formatTime(long time, String pattern) {
		if (time < 0) {
			time = 0;
		}
		if (StringUtils.isBlank(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		return new SimpleDateFormat(pattern).format(new Date(time));
	}

}
