/*
 * Copyright 2007-2107 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.platform.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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

	private static Map<String, String[]> __timeZoneIDs = new LinkedHashMap<String, String[]>(32);
	static {
		__timeZoneIDs.put("-12", new String[] { "GMT-12:00", "(GMT -12:00) Eniwetok, Kwajalein" });
		__timeZoneIDs.put("-11", new String[] { "GMT-11:00", "(GMT -11:00) Midway Island, Samoa" });
		__timeZoneIDs.put("-10", new String[] { "GMT-10:00", "(GMT -10:00) Hawaii" });
		__timeZoneIDs.put("-9", new String[] { "GMT-09:00", "(GMT -09:00) Alaska" });
		__timeZoneIDs.put("-8", new String[] { "GMT-08:00", "(GMT -08:00) Pacific Time (US &amp; Canada), Tijuana" });
		__timeZoneIDs.put("-7", new String[] { "GMT-07:00", "(GMT -07:00) Mountain Time (US &amp; Canada), Arizona" });
		__timeZoneIDs.put("-6", new String[] { "GMT-06:00", "(GMT -06:00) Central Time (US &amp; Canada), Mexico City" });
		__timeZoneIDs.put("-5", new String[] { "GMT-05:00", "(GMT -05:00) Eastern Time (US &amp; Canada), Bogota, Lima, Quito" });
		__timeZoneIDs.put("-4", new String[] { "GMT-04:00", "(GMT -04:00) Atlantic Time (Canada), Caracas, La Paz" });
		__timeZoneIDs.put("-3.5", new String[] { "GMT-03:30", "(GMT -03:30) Newfoundland" });
		__timeZoneIDs.put("-3", new String[] { "GMT-03:00", "(GMT -03:00) Brassila, Buenos Aires, Georgetown, Falkland Is" });
		__timeZoneIDs.put("-2", new String[] { "GMT-02:00", "(GMT -02:00) Mid-Atlantic, Ascension Is., St. Helena" });
		__timeZoneIDs.put("-1", new String[] { "GMT-01:00", "(GMT -01:00) Azores, Cape Verde Islands" });
		__timeZoneIDs.put("0", new String[] { "GMT", "(GMT) Casablanca, Dublin, Edinburgh, London, Lisbon, Monrovia" });
		__timeZoneIDs.put("1", new String[] { "GMT+01:00", "(GMT +01:00) Amsterdam, Berlin, Brussels, Madrid, Paris, Rome" });
		__timeZoneIDs.put("2", new String[] { "GMT+02:00", "(GMT +02:00) Cairo, Helsinki, Kaliningrad, South Africa" });
		__timeZoneIDs.put("3", new String[] { "GMT+03:00", "(GMT +03:00) Baghdad, Riyadh, Moscow, Nairobi" });
		__timeZoneIDs.put("3.5", new String[] { "GMT+03:30", "(GMT +03:30) Tehran" });
		__timeZoneIDs.put("4", new String[] { "GMT+04:00", "(GMT +04:00) Abu Dhabi, Baku, Muscat, Tbilisi" });
		__timeZoneIDs.put("4.5", new String[] { "GMT+04:30", "(GMT +04:30) Kabul" });
		__timeZoneIDs.put("5", new String[] { "GMT+05:00", "(GMT +05:00) Ekaterinburg, Islamabad, Karachi, Tashkent" });
		__timeZoneIDs.put("5.5", new String[] { "GMT+05:30", "(GMT +05:30) Bombay, Calcutta, Madras, New Delhi" });
		__timeZoneIDs.put("5.75", new String[] { "GMT+05:45", "(GMT +05:45) Katmandu" });
		__timeZoneIDs.put("6", new String[] { "GMT+06:00", "(GMT +06:00) Almaty, Colombo, Dhaka, Novosibirsk" });
		__timeZoneIDs.put("6.5", new String[] { "GMT+06:30", "(GMT +06:30) Rangoon" });
		__timeZoneIDs.put("7", new String[] { "GMT+07:00", "(GMT +07:00) Bangkok, Hanoi, Jakarta" });
		__timeZoneIDs.put("8", new String[] { "GMT+08:00", "(GMT +08:00) Beijing, Hong Kong, Perth, Singapore, Taipei" });
		__timeZoneIDs.put("9", new String[] { "GMT+09:00", "(GMT +09:00) Osaka, Sapporo, Seoul, Tokyo, Yakutsk" });
		__timeZoneIDs.put("9.5", new String[] { "GMT+09:30", "(GMT +09:30) Adelaide, Darwin" });
		__timeZoneIDs.put("10", new String[] { "GMT+10:00", "(GMT +10:00) Canberra, Guam, Melbourne, Sydney, Vladivostok" });
		__timeZoneIDs.put("11", new String[] { "GMT+11:00", "(GMT +11:00) Magadan, New Caledonia, Solomon Islands" });
		__timeZoneIDs.put("12", new String[] { "GMT+12:00", "(GMT +12:00) Auckland, Wellington, Fiji, Marshall Island" });
	}

	public static Map<String, String[]> getTimeZoneIDs() {
		return __timeZoneIDs;
	}

	public static SimpleDateFormat getSimpleDateFormat(String format, String timeoffset) {
		SimpleDateFormat _format = new SimpleDateFormat(format, Locale.ENGLISH);
		if (StringUtils.isNotBlank(timeoffset) && __timeZoneIDs.containsKey(timeoffset)) {
			_format.setTimeZone(TimeZone.getTimeZone(__timeZoneIDs.get(timeoffset)[0]));
		}
		return _format;
	}

	/** 时间修正 */
	private static String timeOffset;

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
	public static void setTimeOffset(String timeOffset) {
		DateTimeUtils.timeOffset = timeOffset;
	}

	/**
	 * 统一的获取当前时间的方法
	 *
	 * @return
	 */
	public static long currentTimeMillis() {
		return System.currentTimeMillis();
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
		return getSimpleDateFormat(pattern, timeOffset).format(new Date(time));
	}

	public static String formatTime(long time, String pattern, String timeoffset) {
		if (time < 0) {
			time = 0;
		}
		if (StringUtils.isBlank(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		return getSimpleDateFormat(pattern, timeoffset).format(new Date(time));
	}

	public static Date parseDateTime(String dateTime, String pattern) throws ParseException {
		if (StringUtils.isBlank(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return getSimpleDateFormat(pattern, timeOffset).parse(dateTime);
	}

}
