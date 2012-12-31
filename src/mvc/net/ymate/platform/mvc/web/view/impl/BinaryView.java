/**
 * <p>文件名:	BinaryView.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-WebMVC</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.view.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.support.HttpHeaders;
import net.ymate.platform.mvc.web.view.AbstractWebView;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * BinaryView
 * </p>
 * <p>
 * 二进制数据流视图实现类；
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
 *          <td>2011-10-23上午11:32:55</td>
 *          </tr>
 *          </table>
 */
public class BinaryView extends AbstractWebView {

	private String fileName;
	private Object data;

	/**
	 * 构造器
	 */
	public BinaryView() {
	}

	/**
	 * 构造器
	 * @param data 数据对象
	 */
	public BinaryView(Object data) {
		this.data = data;
	}

	/**
	 * @param fileName 文件路径名称
	 * @param attachment 是否采用档案下载的方式
	 * @return 加载文件并转换成二进制视图类对象，若fileName文件不存在则返回NULL
	 * @throws Exception 任何可能发生的异常
	 */
	public static BinaryView loadFromFile(String fileName, boolean attachment) throws Exception {
		File _file = new File(fileName);
		if (_file.exists() && _file.isFile() && _file.canRead()) {
			BinaryView _view = new BinaryView(new FileInputStream(_file));
			String _path = _file.getPath();
			_view.setContentType(FileUtils.MIME_TYPE_MAPS.get(FileUtils.getExtName(_path)));
			if (attachment) {
				_view.setFileName(_file.getName());
			}
			return _view;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.AbstractWebView#renderView()
	 */
	protected void renderView() throws Exception {
		HttpServletResponse response = WebContext.getResponse();
		HttpServletRequest request = WebContext.getRequest();
		//
		if (StringUtils.isBlank(getContentType())) {
			this.setContentType("application/octet-stream");
		}
        response.setContentType(getContentType());
        //
        if (StringUtils.isBlank(fileName)) {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline");
        } else{
            StringBuilder _contentDisposotionSB = new StringBuilder("attachment;filename=");
            if (StringUtils.indexOfIgnoreCase(request.getHeader("User-Agent"), "firefox") > 0) {
                _contentDisposotionSB.append(new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
			} else {
                _contentDisposotionSB.append(URLEncoder.encode(fileName, "UTF-8"));
			}
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, _contentDisposotionSB.toString());
        }
        //
        if (this.data == null) {
			return;
        }
        // 文件
        if (this.data instanceof File) {
        	response.setContentLength((int) IOUtils.copyLarge(new FileInputStream((File) this.data), response.getOutputStream()));
        }
		// 字节数组
        else if (this.data instanceof byte[]) {
			byte[] _datas = (byte[]) this.data;
			IOUtils.write(_datas, response.getOutputStream());
			response.setContentLength(_datas.length);
		}
		// 字符数组
		else if (this.data instanceof char[]) {
			char[] _datas = (char[])this.data;
			IOUtils.write(_datas, response.getOutputStream());
			response.setContentLength(_datas.length);
		}
		// 文本流
		else if (this.data instanceof Reader) {
			Reader r = (Reader) this.data;
			IOUtils.copy(r, response.getOutputStream());
		}
		// 二进制流
		else if (this.data instanceof InputStream) {
			response.setContentLength((int) IOUtils.copyLarge((InputStream) this.data, response.getOutputStream()));
		}
		// 普通对象
		else {
			IOUtils.write(String.valueOf(data), response.getOutputStream());
		}
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
