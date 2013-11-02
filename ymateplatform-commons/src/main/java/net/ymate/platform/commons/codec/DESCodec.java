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

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import net.ymate.platform.commons.util.RuntimeUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * DESCodec
 * </p>
 * <p>
 * DES编解码类；
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
 *          <td>2011-6-14下午12:24:17</td>
 *          </tr>
 *          </table>
 */
public class DESCodec {

	private static final Log _LOG = LogFactory.getLog(DESCodec.class);

	private final static String DES = "DES";

    private SecureRandom __random;
	private SecretKey __secretKey;
	private Cipher __cipher;

	/**
	 * 构造器
	 * @param desKey 密钥，不能为空且长度为8位以上
	 * @throws Exception
	 */
	private DESCodec(String desKey) throws Exception {
		// DES算法要求有一个可信任的随机数源
		this.__random = new SecureRandom();
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
		this.__secretKey = SecretKeyFactory.getInstance(DES).generateSecret(new DESKeySpec(desKey.getBytes()));
		// Cipher对象实际完成加密操作
		this.__cipher = Cipher.getInstance(DES);
	}

	/**
	 * 创建DES编码工具助手类实例
	 * 
	 * @param desKey 密钥，不能为空且长度为8位以上
	 * @return
	 * @throws Exception
	 */
	public static DESCodec create(String desKey) throws Exception {
		return new DESCodec(desKey);
	}

	/**
	 * 加密
	 * 
	 * @param src 数据源
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] src) throws Exception {
		// 用密匙初始化Cipher对象
		this.__cipher.init(Cipher.ENCRYPT_MODE, this.__secretKey, this.__random);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return this.__cipher.doFinal(src);
	}

	/**
	 * 解密
	 * 
	 * @param src 数据源
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] src) throws Exception {
		// 用密匙初始化Cipher对象
		this.__cipher.init(Cipher.DECRYPT_MODE, this.__secretKey, this.__random);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return this.__cipher.doFinal(src);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public final String decrypt(String data) {
		try {
			return new String(decrypt(hex2byte(data.getBytes())));
		} catch (Exception e) {
			_LOG.warn("", RuntimeUtils.unwrapThrow(e));
		}
		return null;
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public final String encrypt(String data) {
		try {
			return byte2hex(encrypt(data.getBytes()));
		} catch (Exception e) {
			_LOG.warn("", RuntimeUtils.unwrapThrow(e));
		}
		return null;
	}

	/**
	 * 二进制转字符串
	 * 
	 * @param b
	 * @return
	 */
	public String byte2hex(byte[] b) {
		String hs = "";
		String _stmp;
        for (byte aB : b) {
            _stmp = (Integer.toHexString(aB & 0XFF));
            if (_stmp.length() == 1) {
                hs = hs + "0" + _stmp;
            } else {
                hs = hs + _stmp;
            }
        }
		return hs.toUpperCase();
	}

	public byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

}
