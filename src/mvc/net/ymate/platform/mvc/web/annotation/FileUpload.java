/**
 * <p>文件名:	FileUpload.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * FileUpload
 * </p>
 * <p>
 * 文件上传(类型为"multipart/form-data"表单请求)注解；
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
 *          <td>2012-12-24下午11:21:24</td>
 *          </tr>
 *          </table>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FileUpload {

	/**
	 * @return 设置当前上传的单个文件大小上限，默认为-1则采用框架默认设置
	 */
	long sizeMax() default -1;

	/**
	 * @return 设置当前上传的文件总大小上限，默认为-1则采用框架默认设置
	 */
	long totalSizeMax() default -1;

	/**
	 * @return 设置内存缓冲区的大小（单位字节），默认为-1则采用框架默认设置
	 */
	int sizeThreshold() default -1;

}
