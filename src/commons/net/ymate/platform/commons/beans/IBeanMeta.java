/**
 * <p>文件名:	IBeanMeta.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.beans;


/**
 * <p>
 * IBeanMeta
 * </p>
 * <p>
 * 对象元描述接口；
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
 *          <td>2012-12-15下午2:13:01</td>
 *          </tr>
 *          </table>
 */
public interface IBeanMeta {

	/**
	 * @return 返回对象Id
	 */
	public String getId();

	/**
	 * @return 返回对象类名称
	 */
	public String getClassName();

	/**
	 * @return 返回对象实现的接口集合
	 */
	public String[] getInterfaceNames();

	/**
	 * @return 返回对象实例
	 */
	public Object getObject();

}
