/**
 * <p>文件名:	IUploadFileWrapper.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * IUploadFileWrapper
 * </p>
 * <p>
 * 上传文件包装器；
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
 *          <td>2012-12-26下午1:24:51</td>
 *          </tr>
 *          </table>
 */
public interface IUploadFileWrapper {

	/**
	 * @return 获取完整的文件名及路径
	 */
	public String getPath();

	/**
	 * @return 获取文件名称
	 */
	public String getName();

	/**
	 * @return 获取文件大小
	 */
	public long getSize();

	/**
	 * @return 获取文件Content-Type
	 */
	public String getContentType();

	/**
	 * 转移文件
	 * 
	 * @param dest 目标
	 * @throws Exception
	 */
	public void transferTo(File dest) throws Exception;

	/**
	 * 保存文件
	 * 
	 * @param dest 目标
	 * @throws Exception
	 */
	public void writeTo(File dest) throws Exception;

	/**
	 * 删除文件
	 */
	public void delete();

	/**
	 * @return 获取文件输入流对象
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException;

	/**
	 * @return 获取文件输出流对象
	 * @throws IOException
	 */
	public OutputStream getOutputStream() throws IOException;

}
