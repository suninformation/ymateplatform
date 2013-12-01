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
package net.ymate.platform.mvc.web;

import java.util.Map;

import net.ymate.platform.mvc.web.annotation.FileUpload;

/**
 * <p>
 * IWebMultipartHandler
 * </p>
 * <p>
 * 类型为"multipart/form-data"表单请求处理接口；
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
 *          <td>2012-12-26下午12:50:42</td>
 *          </tr>
 *          </table>
 */
public interface IWebMultipartHandler {

	public final static String FILE_UPLOAD_STATUS = "__file_upload_status__";

	/**
	 * 执行处理过程
	 * 
	 * @param upload 文件上传注解对象
	 * @throws Exception
	 */
	public void doHandler(FileUpload upload) throws Exception;

	/**
	 * @return 返回表单字段映射
	 */
	public Map<String, String[]> getFieldMap();

	/**
	 * @return 返回上传文件映射
	 */
	public Map<String, IUploadFileWrapper[]> getFileMap();

}
