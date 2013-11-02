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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.ymate.platform.commons.util.RuntimeUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * MD5Codec
 * </p>
 * <p>
 * MD5编解码类；
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
 *          <td>2010-3-28下午03:57:17</td>
 *          </tr>
 *          </table>
 */
public class MD5Codec {

	private static final Log _LOG = LogFactory.getLog(MD5Codec.class);

	/** 小写十六进制字符 */
	protected final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

	/** 大写十六进制字符 */
	protected final static String[] HEXDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
	
	/**
	 * 构造器
	 */
	private MD5Codec() {
	}
	
	/**
	 * 二进制数组转十六进制字符串
	 *
	 * @param b
	 * @return
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
        for (byte _b : b) {
            resultSb.append(byteToHexString(_b));
        }
		return resultSb.toString();
	}

	/**
	 * 二进制数转十六进制字符串
	 *
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		return hexDigits[n / 16] + hexDigits[n % 16];
	}

	/**
	 * 对文件生成MD5码
	 *
	 * @param fileAbsolutePath 文件绝对路径
	 * @return 生成的MD5码, 不为空
	 */
	public static String encodeFile(String fileAbsolutePath) {
		String resultString = "";
		// 不论什麽异常，都不会影响计算，只是如果异常发生，那么该值为"",显然不是md5结果串
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			File file = new File(fileAbsolutePath);
			if (file.exists()) {
				FileInputStream in = new FileInputStream(file);
				byte[] fileBytes = new byte[(int) file.length()];
				in.read(fileBytes);
				resultString = byteArrayToHexString(md.digest(fileBytes));
				in.close();
			}
		} catch (NoSuchAlgorithmException e) {
			_LOG.warn("", RuntimeUtils.unwrapThrow(e));
		} catch (FileNotFoundException e) {
			_LOG.warn("", RuntimeUtils.unwrapThrow(e));
		} catch (IOException e) {
			_LOG.warn("", RuntimeUtils.unwrapThrow(e));
		}
		return resultString;
	}

	/**
	 * 进行MD5编码，获得字节数组
	 *
	 * @param origin
	 * @return
	 */
	public static byte[] encodeToBytes(String origin) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(origin.getBytes());
		} catch (Exception e) {
			_LOG.warn("无法获得MD5实例", RuntimeUtils.unwrapThrow(e));
		}
		return new byte[0];
	}

	/**
	 * 进行MD5编码
	 *
	 * @param origin
	 * @return
	 */
	public static String encode(String origin) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return byteArrayToHexString(md.digest(origin.getBytes()));
		} catch (Exception e) {
			_LOG.warn("无法获得MD5实例", RuntimeUtils.unwrapThrow(e));
		}
		return "";
	}

	/**
	 * 进行MD5编码
	 *
	 * @param origin
	 * @return
	 */
	public static byte[] encode(byte[] origin) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(origin);
		} catch (Exception e) {
			_LOG.warn("无法获得MD5实例", RuntimeUtils.unwrapThrow(e));
		}
		return new byte[0];
	}

	/**
	 * 使用Java的MessageDigest类将x字符串进行MD5加密
	 *
	 * @param x String
	 * @return String
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String md5(String x) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(x.getBytes("UTF8"));
		byte s[] = m.digest();
		StringBuilder result = new StringBuilder();
        for (byte value : s) {
            result.append(Integer.toHexString((0x000000FF & value) | 0xFFFFFF00).substring(6));
        }
		return result.toString();
	}
	
}
