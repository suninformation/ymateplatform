/**
 * <p>文件名:	DefaultWebMultipartHandler.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.impl;

import java.io.File;
import java.util.Map;

import net.ymate.platform.mvc.web.IUploadFileWrapper;
import net.ymate.platform.mvc.web.IWebMultipartHandler;
import net.ymate.platform.mvc.web.WebMVC;
import net.ymate.platform.mvc.web.annotation.FileUpload;
import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.support.FileUploadHelper;
import net.ymate.platform.mvc.web.support.FileUploadHelper.UploadFormWrapper;

/**
 * <p>
 * DefaultWebMultipartHandler
 * </p>
 * <p>
 * 
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
 *          <td>2012-12-26下午1:07:38</td>
 *          </tr>
 *          </table>
 */
public class DefaultWebMultipartHandler implements IWebMultipartHandler {

	private UploadFormWrapper __formWarpper;

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMultipartHandler#doHandler(net.ymate.platform.mvc.web.annotation.FileUpload)
	 */
	public void doHandler(FileUpload upload) throws Exception {
		// 绑定并初始化文件上传帮助类
		__formWarpper = FileUploadHelper.bind(WebContext.getRequest())
				.setUploadTempDir(new File(WebMVC.getConfig().getUploadTempDir()))
				.setFileSizeMax(upload.totalSizeMax() > -1 ? upload.totalSizeMax() : WebMVC.getConfig().getUploadTotalSizeMax())
				.setSizeMax(upload.sizeMax() > -1 ? upload.sizeMax() : WebMVC.getConfig().getUploadFileSizeMax())
				.setSizeThreshold(upload.sizeThreshold() > -1 ? upload.sizeThreshold() : WebMVC.getConfig().getUploadSizeThreshold())
				.processUpload();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMultipartHandler#getFieldMap()
	 */
	public Map<String, String[]> getFieldMap() {
		return __formWarpper.getFieldMap();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMultipartHandler#getFileMap()
	 */
	public Map<String, IUploadFileWrapper[]> getFileMap() {
		return __formWarpper.getFileMap();
	}

}
