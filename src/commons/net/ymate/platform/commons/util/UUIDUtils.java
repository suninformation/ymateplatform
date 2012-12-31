/**
 * <p>文件名:	UUIDUtils.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Commons</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.util;

import java.util.Random;
import java.util.UUID;


/**
 * <p>
 * UUIDUtils
 * </p>
 * <p>
 * UUID生成器；
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
 *          <td>2010-10-20下午02:02:40</td>
 *          </tr>
 *          </table>
 */
public class UUIDUtils {

	private static final String __randChars = "0123456789abcdefghigklmnopqrstuvtxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";

	private final static String chars64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789^~abcdefghijklmnopqrstuvwxyz";

	/**
	 * 返回一个唯一的16位字符串。 基于： 32位当前时间，32位对象的identityHashCode和32位随机数
	 * 
	 * @param o Object对象
	 * @return 一个16位长度的字符串

	 */
	public final static String generateCharUUID(Object o) {
		long id1 = System.currentTimeMillis() & 0xFFFFFFFFL;
		long id2 = System.identityHashCode(o);
		long id3 = randomLong(-0x80000000L, 0x80000000L) & 0xFFFFFFFFL;
		id1 <<= 16;
		id1 += (id2 & 0xFFFF0000L) >> 16;
		id3 += (id2 & 0x0000FFFFL) << 32;
		String out = __convert(id1, 6, chars64) + __convert(id3, 6, chars64);
		out = out.replaceAll(" ", "o");
		return out;
	}

	public final static String generateNumberUUID(Object o) {
		long id1 = System.currentTimeMillis() & 0xFFFFFFFFL;
		long id2 = System.identityHashCode(o);
		long id3 = randomLong(-0x80000000L, 0x80000000L) & 0xFFFFFFFFL;
		id1 <<= 16;
		id1 += (id2 & 0xFFFF0000L) >> 16;
		id3 += (id2 & 0x0000FFFFL) << 32;
		return "" + id1 + id3;
	}

	public final static String generatePrefixHostUUID(Object o) {
		long id1 = System.currentTimeMillis() & 0xFFFFFFFFL;
		long id2 = System.identityHashCode(o);
		long id3 = randomLong(-0x80000000L, 0x80000000L) & 0xFFFFFFFFL;
		id1 <<= 16;
		id1 += (id2 & 0xFFFF0000L) >> 16;
		id3 += (id2 & 0x0000FFFFL) << 32;
		return IPAddressUtils.getLocalHostName() + "@" + id1 + id3;
	}

	/**
	 * 返回10个随机字符（基于目前时间和一个随机字符串）
	 * 
	 * @return String
	 */
	public final static String generate() {
		long id1 = System.currentTimeMillis() & 0x3FFFFFFFL;
		long id3 = randomLong(-0x80000000L, 0x80000000L) & 0x3FFFFFFFL;
		String out = __convert(id1, 6, chars64) + __convert(id3, 6, chars64);
		out = out.replaceAll(" ", "o");
		return out;
	}

	/**
	 * @return 返回采用JDK自身UUID生成器生成主键并替换 '-' 字符及转换为大写字母
	 */
	public final static String uuid() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	/**
	 * Converts number to string
	 * 
	 * @param x value to convert
	 * @param n conversion base
	 * @param d string with characters for conversion.
	 * @return converted number as string
	 */
	private static String __convert(long x, int n, String d) {
		if (x == 0) {
			return "0";
		}
		String r = "";
		int m = 1 << n;
		m--;
		while (x != 0) {
			r = d.charAt((int) (x & m)) + r;
			x = x >>> n;
		}
		return r;
	}

	///-------------------------------------------------

	/**
	 * 生成随机字符串
	 * 
	 * @param length 长度
	 * @param isOnlyNum 是否仅使用数字
	 * @return
	 */
	public static String randomString(int length, boolean isOnlyNum) {
		int size = isOnlyNum ? 10 : 62;
		StringBuffer hash = new StringBuffer(length);
		Random _rnd = new Random(System.currentTimeMillis());
		for (int i = 0; i < length; i++) {
			hash.append(__randChars.charAt(_rnd.nextInt(size)));
		}
		return hash.toString();
	}

	/**
	 * Generates pseudo-random long from specific range. Generated number is
	 * great or equals to min parameter value and less then max parameter value.
	 * 
	 * @param min lower (inclusive) boundary
	 * @param max higher (exclusive) boundary
	 * 
	 * @return pseudo-random value
	 */
	public static long randomLong(long min, long max) {
		return min + (long) (Math.random() * (max - min));
	}

	/**
	 * Generates pseudo-random integer from specific range. Generated number is
	 * great or equals to min parameter value and less then max parameter value.
	 * 
	 * @param min lower (inclusive) boundary
	 * @param max higher (exclusive) boundary
	 * 
	 * @return pseudo-random value
	 */
	public static int randomInt(int min, int max) {
		return min + (int) (Math.random() * (max - min));
	}

}
