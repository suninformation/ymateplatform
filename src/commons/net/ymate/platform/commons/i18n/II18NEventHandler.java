/**
 * <p>文件名:	II18NEventHandler.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * <p>
 * II18NEventHandler
 * </p>
 * <p>
 * 国际化资源管理器事件监听处理器；
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
 *          <td>2013-4-14下午2:44:08</td>
 *          </tr>
 *          </table>
 */
public interface II18NEventHandler {

	/**
	 * @return 加载当前Locale
	 */
	public Locale loadCurrentLocale();

	/**
	 * @param locale 当Locale改变时处理此方法
	 */
	public void onLocaleChanged(Locale locale);

	/**
	 * @param resourceName 资源名称
	 * @return 加载资源文件的具体处理方法
	 * @throws IOException
	 */
	public InputStream onLoadProperties(String resourceName) throws IOException;

}
