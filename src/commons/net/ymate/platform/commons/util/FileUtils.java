/**
 * <p>文件名:	FileUtils.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Commons</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.util;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * FileUtils
 * </p>
 * <p>
 * 文件处理工具类；
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
 *          <td>2010-4-23上午01:14:10</td>
 *          </tr>
 *          </table>
 */
public class FileUtils {

	public static final Map<String, String> MIME_TYPE_MAPS = new HashMap<String, String>();
	
	static {
		Properties _configs = new Properties();
		InputStream _in = FileUtils.class.getClassLoader().getResourceAsStream("mimetypes-conf.properties");
		if (_in == null) {
			_in = FileUtils.class.getClassLoader().getResourceAsStream("META-INF/mimetypes-default-conf.properties");
		}
		if (_in != null) {
			try {
				_configs.load(_in);
				for (Object _key : _configs.keySet()) {
					String[] _values = StringUtils.split(_configs.getProperty((String) _key, ""), "|");
					for (String _value : _values) {
						MIME_TYPE_MAPS.put(_value, (String) _key);
					}
				}
			} catch (Exception e) {}
		}
	}

	/**
	 * 获取文件最后一次被修改的时间
	 * 
	 * @param path
	 * @return 0-表示文件不存在或无权限读取，大于0为修改时间
	 */
	public static long getLastModifiedTimestamp(String path) {
		long l = 0;
		File f = new File(path);
		if (f.exists()) {
			try {
				l = f.lastModified();
			} catch (SecurityException e) {
				l = 0;
			}
		}
		return l;
	}

	/**
	 * 获取某个类的物理文件时间戳，一般用来确定某个类的编译时间
	 *
	 * @param clazz 类对象
	 * @return 生成类文件的时间戳，单位毫秒
	 */
	public static long getClassLastModifiedTimestamp(Class<?> clazz) {
		return getLastModifiedTimestamp(clazz.getResource(clazz.getSimpleName() + ".class").getFile());
	}

	/**
	 * 创建目录
	 * 
	 * @param path 目录路径及名称
	 * @param override 是否忽略已存在，若忽略则当目录存在时直接跳过创建并返回true，否则返回false
	 * @return
	 */
	public static boolean mkdirs(String path, boolean override) {
		boolean _flag = false;
		File _path = new File(path);
		if (!_path.exists()) {
			_path.mkdirs();
			_flag = true;
		} else {
			if (override) {
				_flag = true;
			}
		}
		return _flag;
	}

	/**
	 * 修复并创建目标文件目录
	 * 
	 * @param dir 待处理的文件夹路径
	 * @return 修改并创建的目标文件夹路径
	 */
	public static String fixAndMkDir(String dir) {
		File _file = new File(dir);
		if (!_file.exists()) {
			_file.mkdirs();
		}
		return fixSeparator(_file.getPath());
	}

	/**
	 * 修复文件夹路径（必须以'\'结束）
	 * 
	 * @param dir 待修复的文件夹路径
	 * @return 修复后的文件夹路径
	 */
	public static String fixSeparator(String dir) {
		if (!dir.endsWith(File.separator)) {
			dir += File.separator;
		}
		return dir;
	}

	/**
	 * @param filePath
	 * @return 删除文件名中的路径，只返回文件名
	 */
	public static String getNameFromPath(String filePath) {
		int i = filePath.lastIndexOf('/');
		if (i == -1) {
			return filePath;
		} else {
			return filePath.substring(i + 1);
		}
	}

	/**
	 * @param fileName 原始文件名称
	 * @return 提取文件扩展名称，若不存在扩展名则返回原始文件名称
	 */
	public static String getExtName(String fileName) {
		String suffix = "";
        int pos = fileName.lastIndexOf('.');
        if (pos > 0 && pos < fileName.length() - 1) {
            suffix = fileName.substring(pos + 1);
        }
        return suffix;
	}

	/**
	 * @param url
	 * @return 将URL地址转换成File对象, 若url指向的是jar包中文件，则返回null
	 */
	public static File toFile(URL url) {
		if ((url == null) || (!url.getProtocol().equals("file"))) {
			return null;
		}
		String filename = url.getFile().replace('/', File.separatorChar);
		int pos = 0;
		while ((pos = filename.indexOf('%', pos)) >= 0) {
			if (pos + 2 < filename.length()) {
				String hexStr = filename.substring(pos + 1, pos + 3);
				char ch = (char) Integer.parseInt(hexStr, 16);
				filename = filename.substring(0, pos) + ch + filename.substring(pos + 3);
			}
		}
		return new File(filename);
	}

	/**
	 * @param filePath
	 * @return 将文件路径转换成URL对象,返回值可能为NULL, 若想将jar包中文件，必须使用URL.toString()方法生成filePath参数—即以"jar:"开头
	 */
	public static URL toURL(String filePath) {
		if (StringUtils.isBlank(filePath)) {
			throw new IllegalArgumentException("Illegal argument 'filePath'!");
		}
		try {
			if (!filePath.startsWith("jar:") && !filePath.startsWith("file:")
					&& !filePath.startsWith("zip:")
					&& !filePath.startsWith("http:")
					&& !filePath.startsWith("ftp:")) {
				File _f = new File(filePath);
				return _f.toURI().toURL();
			}
			return new URL(filePath);
		} catch (MalformedURLException e) {
            // 忽略...
        }
		return null;
	}

}
