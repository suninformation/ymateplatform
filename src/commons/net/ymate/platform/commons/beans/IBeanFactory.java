/**
 * <p>文件名:	IBeanFactory.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.beans;

/**
 * <p>
 * IBeanFactory
 * </p>
 * <p>
 * 对象工厂接口；
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
 *          <td>2012-12-15下午2:16:33</td>
 *          </tr>
 *          </table>
 */
public interface IBeanFactory {

	/**
	 * @param clazz 对象类型
	 * @return 提取类型为clazz的对象实例，可能返回NULL
	 */
	public <T> T get(Class<T> clazz);

	/**
	 * @param id 对象Id
	 * @return 提取指定Id的类对象实例，可能返回NULL
	 */
	public <T> T get(String id);

	/**
	 * @param clazz 目标类对象
	 * @return 注册一个类到工厂中，并返回目标对象元描述，若注册失败则返回NULL
	 */
	public IBeanMeta add(Class<?> clazz);

}
