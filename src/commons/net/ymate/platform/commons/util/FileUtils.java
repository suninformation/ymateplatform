/**
 * <p>文件名:	FileUtils.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Commons</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
		MIME_TYPE_MAPS.put("abs", "audio/x-mpeg");
		MIME_TYPE_MAPS.put("ai", "application/postscript");
		MIME_TYPE_MAPS.put("aif", "audio/x-aiff");
		MIME_TYPE_MAPS.put("aifc", "audio/x-aiff");
		MIME_TYPE_MAPS.put("aiff", "audio/x-aiff");
		MIME_TYPE_MAPS.put("aim", "application/x-aim");
		MIME_TYPE_MAPS.put("art", "image/x-jg");
		MIME_TYPE_MAPS.put("asf", "video/x-ms-asf");
		MIME_TYPE_MAPS.put("asx", "video/x-ms-asf");
		MIME_TYPE_MAPS.put("au", "audio/basic");
		MIME_TYPE_MAPS.put("avi", "video/x-msvideo");
		MIME_TYPE_MAPS.put("avx", "video/x-rad-screenplay");
		MIME_TYPE_MAPS.put("bcpio", "application/x-bcpio");
		MIME_TYPE_MAPS.put("bin", "application/octet-stream");
		MIME_TYPE_MAPS.put("bmp", "image/bmp");
		MIME_TYPE_MAPS.put("body", "text/html");
		MIME_TYPE_MAPS.put("cdf", "application/x-netcdf");
		MIME_TYPE_MAPS.put("cer", "application/x-x509-ca-cert");
		MIME_TYPE_MAPS.put("class", "application/java");
		MIME_TYPE_MAPS.put("cpio", "application/x-cpio");
		MIME_TYPE_MAPS.put("csh", "application/x-csh");
		MIME_TYPE_MAPS.put("css", "text/css");
		MIME_TYPE_MAPS.put("dib", "image/bmp");
		MIME_TYPE_MAPS.put("doc", "application/msword");
		MIME_TYPE_MAPS.put("dtd", "application/xml-dtd");
		MIME_TYPE_MAPS.put("dv", "video/x-dv");
		MIME_TYPE_MAPS.put("dvi", "application/x-dvi");
		MIME_TYPE_MAPS.put("eps", "application/postscript");
		MIME_TYPE_MAPS.put("etx", "text/x-setext");
		MIME_TYPE_MAPS.put("exe", "application/octet-stream");
		MIME_TYPE_MAPS.put("gif", "image/gif");
		MIME_TYPE_MAPS.put("gtar", "application/x-gtar");
		MIME_TYPE_MAPS.put("gz", "application/x-gzip");
		MIME_TYPE_MAPS.put("hdf", "application/x-hdf");
		MIME_TYPE_MAPS.put("htc", "text/x-component");
		MIME_TYPE_MAPS.put("htm", "text/html");
		MIME_TYPE_MAPS.put("html", "text/html");
		MIME_TYPE_MAPS.put("hqx", "application/mac-binhex40");
		MIME_TYPE_MAPS.put("ico", "image/x-icon");
		MIME_TYPE_MAPS.put("ief", "image/ief");
		MIME_TYPE_MAPS.put("jad", "text/vnd.sun.j2me.app-descriptor");
		MIME_TYPE_MAPS.put("jar", "application/java-archive");
		MIME_TYPE_MAPS.put("java", "text/plain");
		MIME_TYPE_MAPS.put("jnlp", "application/x-java-jnlp-file");
		MIME_TYPE_MAPS.put("jpe", "image/jpeg");
		MIME_TYPE_MAPS.put("jpeg", "image/jpeg");
		MIME_TYPE_MAPS.put("jpg", "image/jpeg");
		MIME_TYPE_MAPS.put("js", "text/javascript");
		MIME_TYPE_MAPS.put("jsf", "text/plain");
		MIME_TYPE_MAPS.put("jspf", "text/plain");
		MIME_TYPE_MAPS.put("kar", "audio/midi");
		MIME_TYPE_MAPS.put("latex", "application/x-latex");
		MIME_TYPE_MAPS.put("m3u", "audio/x-mpegurl");
		MIME_TYPE_MAPS.put("mac", "image/x-macpaint");
		MIME_TYPE_MAPS.put("man", "application/x-troff-man");
		MIME_TYPE_MAPS.put("mathml", "application/mathml+xml");
		MIME_TYPE_MAPS.put("me", "application/x-troff-me");
		MIME_TYPE_MAPS.put("mid", "audio/midi");
		MIME_TYPE_MAPS.put("midi", "audio/midi");
		MIME_TYPE_MAPS.put("mif", "application/vnd.mif");
		MIME_TYPE_MAPS.put("mov", "video/quicktime");
		MIME_TYPE_MAPS.put("movie", "video/x-sgi-movie");
		MIME_TYPE_MAPS.put("mp1", "audio/x-mpeg");
		MIME_TYPE_MAPS.put("mp2", "audio/mpeg");
		MIME_TYPE_MAPS.put("mp3", "audio/mpeg");
		MIME_TYPE_MAPS.put("mpa", "audio/x-mpeg");
		MIME_TYPE_MAPS.put("mpe", "video/mpeg");
		MIME_TYPE_MAPS.put("mpeg", "video/mpeg");
		MIME_TYPE_MAPS.put("mpega", "audio/x-mpeg");
		MIME_TYPE_MAPS.put("mpg", "video/mpeg");
		MIME_TYPE_MAPS.put("mpv2", "video/mpeg2");
		MIME_TYPE_MAPS.put("ms", "application/x-troff-ms");
		MIME_TYPE_MAPS.put("nc", "application/x-netcdf");
		MIME_TYPE_MAPS.put("oda", "application/oda");
		MIME_TYPE_MAPS.put("ogg", "application/ogg");
		MIME_TYPE_MAPS.put("pbm", "image/x-portable-bitmap");
		MIME_TYPE_MAPS.put("pct", "image/pict");
		MIME_TYPE_MAPS.put("pdf", "application/pdf");
		MIME_TYPE_MAPS.put("pgm", "image/x-portable-graymap");
		MIME_TYPE_MAPS.put("pic", "image/pict");
		MIME_TYPE_MAPS.put("pict", "image/pict");
		MIME_TYPE_MAPS.put("pls", "audio/x-scpls");
		MIME_TYPE_MAPS.put("png", "image/png");
		MIME_TYPE_MAPS.put("pnm", "image/x-portable-anymap");
		MIME_TYPE_MAPS.put("pnt", "image/x-macpaint");
		MIME_TYPE_MAPS.put("ppm", "image/x-portable-pixmap");
		MIME_TYPE_MAPS.put("pps", "application/vnd.ms-powerpoint");
		MIME_TYPE_MAPS.put("ppt", "application/vnd.ms-powerpoint");
		MIME_TYPE_MAPS.put("ps", "application/postscript");
		MIME_TYPE_MAPS.put("psd", "image/x-photoshop");
		MIME_TYPE_MAPS.put("qt", "video/quicktime");
		MIME_TYPE_MAPS.put("qti", "image/x-quicktime");
		MIME_TYPE_MAPS.put("qtif", "image/x-quicktime");
		MIME_TYPE_MAPS.put("ras", "image/x-cmu-raster");
		MIME_TYPE_MAPS.put("rdf", "application/rdf+xml");
		MIME_TYPE_MAPS.put("rgb", "image/x-rgb");
		MIME_TYPE_MAPS.put("rm", "application/vnd.rn-realmedia");
		MIME_TYPE_MAPS.put("roff", "application/x-troff");
		MIME_TYPE_MAPS.put("rtf", "text/rtf");
		MIME_TYPE_MAPS.put("rtx", "text/richtext");
		MIME_TYPE_MAPS.put("sh", "application/x-sh");
		MIME_TYPE_MAPS.put("shar", "application/x-shar");
		MIME_TYPE_MAPS.put("shtml", "text/x-server-parsed-html");
		MIME_TYPE_MAPS.put("smf", "audio/x-midi");
		MIME_TYPE_MAPS.put("sit", "application/x-stuffit");
		MIME_TYPE_MAPS.put("snd", "audio/basic");
		MIME_TYPE_MAPS.put("src", "application/x-wais-source");
		MIME_TYPE_MAPS.put("sv4cpio", "application/x-sv4cpio");
		MIME_TYPE_MAPS.put("sv4crc", "application/x-sv4crc");
		MIME_TYPE_MAPS.put("svg", "image/svg+xml");
		MIME_TYPE_MAPS.put("svgz", "image/svg");
		MIME_TYPE_MAPS.put("swf", "application/x-shockwave-flash");
		MIME_TYPE_MAPS.put("t", "application/x-troff");
		MIME_TYPE_MAPS.put("tar", "application/x-tar");
		MIME_TYPE_MAPS.put("tcl", "application/x-tcl");
		MIME_TYPE_MAPS.put("tex", "application/x-tex");
		MIME_TYPE_MAPS.put("texi", "application/x-texinfo");
		MIME_TYPE_MAPS.put("texinfo", "application/x-texinfo");
		MIME_TYPE_MAPS.put("tif", "image/tiff");
		MIME_TYPE_MAPS.put("tiff", "image/tiff");
		MIME_TYPE_MAPS.put("tr", "application/x-troff");
		MIME_TYPE_MAPS.put("tsv", "text/tab-separated-values");
		MIME_TYPE_MAPS.put("txt", "text/plain");
		MIME_TYPE_MAPS.put("ulw", "audio/basic");
		MIME_TYPE_MAPS.put("ustar", "application/x-ustar");
		MIME_TYPE_MAPS.put("vrml", "model/vrml");
		MIME_TYPE_MAPS.put("vsd", "application/x-visio");
		MIME_TYPE_MAPS.put("vxml", "application/voicexml+xml");
		MIME_TYPE_MAPS.put("wav", "audio/x-wav");
		MIME_TYPE_MAPS.put("wbmp", "image/vnd.wap.wbmp");
		MIME_TYPE_MAPS.put("wml", "text/vnd.wap.wml");
		MIME_TYPE_MAPS.put("wmlc", "application/vnd.wap.wmlc");
		MIME_TYPE_MAPS.put("wmls", "text/vnd.wap.wmlscript");
		MIME_TYPE_MAPS.put("wmlscriptc", "application/vnd.wap.wmlscriptc");
		MIME_TYPE_MAPS.put("wrl", "model/vrml");
		MIME_TYPE_MAPS.put("xbm", "image/x-xbitmap");
		MIME_TYPE_MAPS.put("xht", "application/xhtml+xml");
		MIME_TYPE_MAPS.put("xhtml", "application/xhtml+xml");
		MIME_TYPE_MAPS.put("xls", "application/vnd.ms-excel");
		MIME_TYPE_MAPS.put("csv", "application/octet-stream");
		MIME_TYPE_MAPS.put("xml", "application/xml");
		MIME_TYPE_MAPS.put("xpm", "image/x-xpixmap");
		MIME_TYPE_MAPS.put("xsl", "application/xml");
		MIME_TYPE_MAPS.put("xslt", "application/xslt+xml");
		MIME_TYPE_MAPS.put("xul", "application/vnd.mozilla.xul+xml");
		MIME_TYPE_MAPS.put("xwd", "image/x-xwindowdump");
		MIME_TYPE_MAPS.put("Z", "application/x-compress");
		MIME_TYPE_MAPS.put("z", "application/x-compress");
		MIME_TYPE_MAPS.put("zip", "application/zip");
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
		return fileName.substring(fileName.lastIndexOf(".") + 1);
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
