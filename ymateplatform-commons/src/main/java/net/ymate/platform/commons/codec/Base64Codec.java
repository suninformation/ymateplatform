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
package net.ymate.platform.commons.codec;

import java.io.UnsupportedEncodingException;

import net.ymate.platform.commons.util.RuntimeUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>
 * Base64Codec
 * </p>
 * <p>
 * BASE64编解码类；
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
 *          <td>2010-3-28下午03:39:14</td>
 *          </tr>
 *          </table>
 */
public class Base64Codec {

	private static final Log _LOG = LogFactory.getLog(Base64Codec.class);

	/** 适配字符 */
	protected static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
	
	/** 密码 */
	protected static byte[] codes = new byte[256];

	static {
		for (int i = 0; i < 256; i++) {
			codes[i] = -1;
		}
		for (int i = 'A'; i <= 'Z'; i++) {
			codes[i] = (byte) (i - 'A');
		}
		for (int i = 'a'; i <= 'z'; i++) {
			codes[i] = (byte) (26 + i - 'a');
		}
		for (int i = '0'; i <= '9'; i++) {
			codes[i] = (byte) (52 + i - '0');
		}
		codes['+'] = 62;
		codes['/'] = 63;
	}
	
	/**
	 * 构造器
	 */
	private Base64Codec() {
	}

	/**
	 * 获得String的Base64编码字符串, 使用utf-8作为内码进行编码
	 *
	 * @param dataStr 预编码字符串
	 * @return 经过编码的字符串
	 */
	public static String encode(String dataStr) {
		if (dataStr != null) {
			try {
				return new String(encode(dataStr.getBytes("utf-8")));
			} catch (UnsupportedEncodingException e) {
				_LOG.warn("", RuntimeUtils.unwrapThrow(e));
			}
		}
		return new String(encode("".getBytes()));
	}

	/**
	 * 获得指定内码String的Base64编码
	 *
	 * @param dataStr 预编码字符串
	 * @param charset 内码名, 如果为空则使用utf-8
	 * @return 编码后字符串
	 */
	public static String encode(String dataStr, String charset) {
		if (StringUtils.isBlank(charset)) {
			charset = "utf-8";
		}
		if (StringUtils.isNotBlank(dataStr)) {
			try {
				return new String(encode(dataStr.getBytes(charset)));
			} catch (UnsupportedEncodingException e) {
				_LOG.warn("", RuntimeUtils.unwrapThrow(e));
			}
		}
		return new String(encode("".getBytes()));
	}

	/**
	 * 编码Base64
	 *
	 * @param data 预编码
	 * @return 编码后字符数组
	 */
	public static char[] encode(byte[] data) {
		char[] out = new char[((data.length + 2) / 3) * 4];
		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
			boolean quad = false;
			boolean trip = false;

			int val = (0xFF & (int) data[i]);
			val <<= 8;
			if ((i + 1) < data.length) {
				val |= (0xFF & (int) data[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length) {
				val |= (0xFF & (int) data[i + 2]);
				quad = true;
			}
			out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = alphabet[val & 0x3F];
			val >>= 6;
			out[index] = alphabet[val & 0x3F];
		}
		return out;
	}

	/**
	 * 获得Base64编码字符串的原文, 默认使用utf-8作为内码进行解码
	 *
	 * @param base64Str Base64字符串
	 * @return 编码后字符串数据
	 */
	public static String decode(String base64Str) {
		if (base64Str != null) {
			try {
				return new String(decode(base64Str.toCharArray()), "utf-8");
			} catch (UnsupportedEncodingException e) {
				_LOG.warn("", RuntimeUtils.unwrapThrow(e));
			}
		}
		return "";
	}

	/**
	 * 获得Base64编码字符串的原文, 使用指定charset作为内码进行解码
	 *
	 * @param base64Str Base64字符串
	 * @param charset 内码名, 默认使用utf-8
	 * @return 解码后字符串
	 */
	public static String decode(String base64Str, String charset) {
		if (StringUtils.isBlank(charset)) {
			charset = "utf-8";
		}
		if (StringUtils.isNotBlank(base64Str)) {
			try {
				return new String(decode(base64Str.toCharArray()), charset);
			} catch (UnsupportedEncodingException e) {
				_LOG.warn("", RuntimeUtils.unwrapThrow(e));
			}
		}
		return "";
	}

	/**
	 * 解码Base64
	 *
	 * @param data 预解码数据
	 * @return 解码后数据的字节数组
	 */
	static public byte[] decode(char[] data) {
		int tempLen = data.length;
		for (int ix = 0; ix < data.length; ix++) {
			if ((data[ix] > 255) || codes[data[ix]] < 0) {
				--tempLen;
			}
		}
		int len = (tempLen / 4) * 3;
		if ((tempLen % 4) == 3) {
			len += 2;
		}
		if ((tempLen % 4) == 2) {
			len += 1;
		}
		byte[] out = new byte[len];
		int shift = 0;
		int accum = 0;
		int index = 0;
		for (int ix = 0; ix < data.length; ix++) {
			int value = (data[ix] > 255) ? -1 : codes[data[ix]];
			if (value >= 0) {
				accum <<= 6;
				shift += 6;
				accum |= value;
				if (shift >= 8) {
					shift -= 8;
					out[index++] = (byte) ((accum >> shift) & 0xff);
				}
			}
		}
		if (index != out.length) {
			// 数据长度判断错误
			throw new Error("Miscalculated data length (wrote " + index + " instead of " + out.length + ")");
		}
		return out;
	}
	
}
