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

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * IPAddressUtils
 * </p>
 * <p>
 * IP地址工具类，支持IPV4/6版本；
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
 *          <td>2011-7-5下午12:29:12</td>
 *          </tr>
 *          </table>
 */
public class IPAddressUtils {

	private static final int IPV6Length = 8;// IPV6地址的分段
	private static final int IPV4Length = 4;// IPV6地址分段
	private static final int IPV4ParmLength = 2;// 一个IPV4分段占的长度
	private static final int IPV6ParmLength = 4;// 一个IPV6分段占的长

	private static String __localHostname = "";

	private static String __localHostIps = "";

	/**
	 * 构造器
	 */
	private IPAddressUtils() {
	}

	/**
	 * 获取本机名称
	 * 
	 * @return
	 */
	public static String getLocalHostName() {
		if (__localHostname.equals("")) {
			try {
				__localHostname = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				__localHostname = "UnknownHost";
			}
		}
		return __localHostname;
	}

	/**
	 * 获取本地所有的ip地址，组成字符串返回（以“,”分隔）
	 * 
	 * @return
	 */
	public static String getLocalHostIps() {
		if (__localHostIps.equals("")) {
			StringBuffer sb = new StringBuffer();
			InetAddress[] ias;
			try {
				ias = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
				for (int i = 0; i < ias.length; i++) {
					String ip = ias[i].getHostAddress();
					sb.append(ip);
					sb.append(",");
				}
				__localHostIps = sb.toString();
			} catch (UnknownHostException e) {
				__localHostIps = "";
			}
		}
		return __localHostIps;
	}

	/**
	 * 检查IPv4地址的合法性
	 * 
	 * @param ip
	 * @return
	 */
	public final static boolean isIPv4Format(String ip) {
		Pattern patt = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
		Matcher mat = patt.matcher(ip);
		return mat.matches();
	}

	/**
	 * 获取一个DNS或计算机名称所对应的IP地址数组
	 * 
	 * @param dnsip
	 * @return
	 */
	public final static String[] getDnsIPs(String dnsip) {
		String ips[] = null;
		try {
			InetAddress ias[] = InetAddress.getAllByName(dnsip);
			ips = new String[ias.length];
			for (int i = 0; i < ias.length; i++) {
				ips[i] = ias[i].getHostAddress();
				ias[i] = null;
			}
		} catch (UnknownHostException e) {
			ips = null;
		}
		return ips;
	}

	// -------------=================--------------------

	/**
	 * IPV6、IPV4转化为十六进制串
	 * 
	 * @param ipAddress
	 * @return
	 */
	public static String encode(String ipAddress) {
		String Key = "";
		int dotFlag = ipAddress.indexOf(".");
		int colonFlag = ipAddress.indexOf(":");
		int dColonFlag = ipAddress.indexOf("::");
		ipAddress = ipAddress.replace(".", "&");
		ipAddress = ipAddress.replace(":", "&");
		// ipv4 address
		if (dotFlag != -1 && colonFlag == -1) {
			String[] arr = ipAddress.split("&");
			for (int i = 0; i < IPV6Length - IPV4ParmLength; i++) {
				Key += "0000";
			}
			for (int j = 0; j < IPV4Length; j++) {
				arr[j] = Integer.toHexString(Integer.parseInt(arr[j]));
				for (int k = 0; k < (IPV4ParmLength - arr[j].length()); k++) {
					Key += "0";
				}
				Key += arr[j];
			}
		}
		// Mixed address with ipv4 and ipv6
		if (dotFlag != -1 && colonFlag != -1 && dColonFlag == -1) {
			String[] arr = ipAddress.split("&");
			for (int i = 0; i < IPV6Length - IPV4ParmLength; i++) {
				for (int k = 0; k < (IPV6ParmLength - arr[i].length()); k++) {
					Key += "0";
				}
				Key += arr[i];
			}
			for (int j = 0; j < IPV4Length; j++) {
				arr[j] = Integer.toHexString(Integer.parseInt(arr[j]));
				for (int k = 0; k < (IPV4ParmLength - arr[j].length()); k++) {
					Key += "0";
				}
				Key += arr[j];
			}
		}
		// Mixed address with ipv4 and ipv6,and there are more than one '0'
		// address param
		if (dColonFlag != -1 && dotFlag != -1) {
			String[] arr = ipAddress.split("&");
			String[] arrParams = new String[IPV6Length + IPV4ParmLength];
			int indexFlag = 0;
			int pFlag = 0;
			if ("".equals(arr[0])) {
				for (int j = 0; j < (IPV6Length + IPV4ParmLength - (arr.length - 2)); j++) {
					arrParams[j] = "0000";
					indexFlag++;
				}
				for (int i = 2; i < arr.length; i++) {
					arrParams[indexFlag] = arr[i];
					indexFlag++;
				}
			} else {
				for (int i = 0; i < arr.length; i++) {
					if ("".equals(arr[i])) {
						for (int j = 0; j < (IPV6Length + IPV4ParmLength - arr.length + 1); j++) {
							arrParams[indexFlag] = "0000";
							indexFlag++;
						}
					} else {
						arrParams[indexFlag] = arr[i];
						indexFlag++;
					}
				}
			}

			for (int i = 0; i < IPV6Length - IPV4ParmLength; i++) {
				for (int k = 0; k < (IPV6ParmLength - arrParams[i].length()); k++) {
					Key += "0";
				}
				Key += arrParams[i];
				pFlag++;
			}
			for (int j = 0; j < IPV4Length; j++) {
				arrParams[pFlag] = Integer.toHexString(Integer.parseInt(arrParams[pFlag]));
				for (int k = 0; k < (IPV4ParmLength - arrParams[pFlag].length()); k++) {
					Key += "0";
				}
				Key += arrParams[pFlag];
				pFlag++;
			}
		}
		// ipv6 address
		if (dColonFlag == -1 && dotFlag == -1 && colonFlag != -1) {
			String[] arrParams = ipAddress.split("&");
			for (int i = 0; i < IPV6Length; i++) {
				for (int k = 0; k < (IPV6ParmLength - arrParams[i].length()); k++) {
					Key += "0";
				}
				Key += arrParams[i];
			}
		}
		if (dColonFlag != -1 && dotFlag == -1) {
			String[] arr = ipAddress.split("&");
			String[] arrParams = new String[IPV6Length];
			int indexFlag = 0;
			if ("".equals(arr[0])) {
				for (int j = 0; j < (IPV6Length - (arr.length - 2)); j++) {
					arrParams[j] = "0000";
					indexFlag++;
				}
				for (int i = 2; i < arr.length; i++) {
					arrParams[indexFlag] = arr[i];
					i++;
					indexFlag++;
				}
			} else {
				for (int i = 0; i < arr.length; i++) {
					if ("".equals(arr[i])) {
						for (int j = 0; j < (IPV6Length - arr.length + 1); j++) {
							arrParams[indexFlag] = "0000";
							indexFlag++;
						}
					} else {
						arrParams[indexFlag] = arr[i];
						indexFlag++;
					}
				}
			}
			for (int i = 0; i < IPV6Length; i++) {
				for (int k = 0; k < (IPV6ParmLength - arrParams[i].length()); k++) {
					Key += "0";
				}
				Key += arrParams[i];
			}
		}
		return Key;
	}

	/**
	 * 十六进制串转化为IP地址
	 * 
	 * @param key
	 * @return
	 */
	public static String decode(String key) {
		String IPV6Address = "";
		String IPV4Address = "";
		String IPAddress = "";
		String strKey = "";
		String ip1 = key.substring(0, 24);
		String ip2 = key.substring(24);
		String tIP1 = ip1.replace("0000", "").trim();
		if (!"".equals(tIP1) && !"FFFF".equals(tIP1)) {
			while (!"".equals(key)) {
				strKey = key.substring(0, 4);
				key = key.substring(4);
				strKey = strKey.replace("0000", "0");
				if ("".equals(IPV6Address)) {
					IPV6Address = strKey;
				} else {
					IPV6Address += ":" + strKey;
				}
			}
			IPAddress = IPV6Address;
		}
		if ("FFFF".equals(tIP1) || "".equals(tIP1)) {
			IPV6Address = "::" + tIP1;
			while (!"".equals(ip2)) {
				strKey = ip2.substring(0, 2);
				ip2 = ip2.substring(2);
				if ("".equals(IPV4Address)) {
					IPV4Address = String.valueOf(Integer.parseInt(strKey, 16));
				} else {
					IPV4Address += "." + String.valueOf(Integer.parseInt(strKey, 16));
				}
			}
			if ("FFFF".equals(tIP1)) {
				IPAddress = IPV6Address + ":" + IPV4Address + "(IPV6)/" + IPV4Address + "(IPV4)";
			}
			if ("".equals(tIP1)) {
				IPAddress = IPV6Address + IPV4Address + "(IPV6)/" + IPV4Address + "(IPV4)";
			}
		}
		return IPAddress;
	}

	public static boolean isIPv6Format(String ip) {
		ip = ip.trim();
		// in many cases such as URLs, IPv6 addresses are wrapped by []
		if (ip.substring(0, 1).equals("[") && ip.substring(ip.length() - 1).equals("]")) {
			ip = ip.substring(1, ip.length() - 1);
		}

		return (1 < Pattern.compile(":").split(ip).length)
		// a valid IPv6 address should contains no less than 1, and no more than 7 “:” as separators
				&& (Pattern.compile(":").split(ip).length <= 8)
				// the address can be compressed, but “::” can appear only once
				&& (Pattern.compile("::").split(ip).length <= 2)
				// if a compressed address
				&& (Pattern.compile("::").split(ip).length == 2)

		// if starts with “::” – leading zeros are compressed
		? (((ip.substring(0, 2).equals("::")) ? Pattern
				.matches(
						"^::([\\da-f]{1,4}(:)){0,4}(([\\da-f]{1,4}(:)[\\da-f]{1,4})|([\\da-f]{1,4})|((\\d{1,3}.){3}\\d{1,3}))",
						ip)
				: Pattern
						.matches(
								"^([\\da-f]{1,4}(:|::)){1,5}(([\\da-f]{1,4}(:|::)[\\da-f]{1,4})|([\\da-f]{1,4})|((\\d{1,3}.){3}\\d{1,3}))",
								ip)))

				// if ends with "::" - ending zeros are compressed
				: ((ip.substring(ip.length() - 2).equals("::")) ? Pattern
						.matches("^([\\da-f]{1,4}(:|::)){1,7}", ip)
						: Pattern
								.matches(
										"^([\\da-f]{1,4}:){6}(([\\da-f]{1,4}:[\\da-f]{1,4})|((\\d{1,3}.){3}\\d{1,3}))",
										ip));
	}

	/**
	 * 获取本机 IPv6 地址 -- 有时为了能够注册 listener，开发人员需要使用本机的 IPv6 地址，这一地址不能简单得通过
	 * InetAddress.getLocalhost() 获得。因为这样有可能获得诸如 0:0:0:0:0:0:0:1
	 * 这样的特殊地址。使用这样的地址，其他服务器将无法把通知发送到本机上，因此必须先进行过滤，选出确实可用的地址。以下代码实现了这一功能，思路是遍历网
	 * 络接口的各个地址，直至找到符合要求的地址。
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getLocalIPv6Address() throws IOException {
		InetAddress inetAddress = null;
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		outer: 
		while (networkInterfaces.hasMoreElements()) {
			Enumeration<InetAddress> inetAds = networkInterfaces.nextElement().getInetAddresses();
			while (inetAds.hasMoreElements()) {
				inetAddress = inetAds.nextElement();
				// Check if it's ipv6 address and reserved address
				if (inetAddress instanceof Inet6Address && !isReservedAddr(inetAddress)) {
					break outer;
				}
			}
		}
		String ipAddr = inetAddress.getHostAddress();
		// Filter network card No
		int index = ipAddr.indexOf('%');
		if (index > 0) {
			ipAddr = ipAddr.substring(0, index);
		}
		return ipAddr;
	}

	/**
	 * Check if it's "local address" or "link local address" or "loopbackaddress"
	 * 
	 * @param ip address
	 * @return result
	 */
	private static boolean isReservedAddr(InetAddress inetAddr) {
		if (inetAddr.isAnyLocalAddress() || inetAddr.isLinkLocalAddress() || inetAddr.isLoopbackAddress()) {
			return true;
		}
		return false;
	}

}
