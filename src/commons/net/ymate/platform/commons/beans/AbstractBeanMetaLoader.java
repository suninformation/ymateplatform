/**
 * <p>文件名:	AbstractBeanMetaLoader.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.beans;

import java.util.List;

/**
 * <p>
 * AbstractBeanMetaLoader
 * </p>
 * <p>
 * 对象元描述加载器接口抽象实现类；
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
 *          <td>2012-12-15下午2:27:07</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractBeanMetaLoader implements IBeanMetaLoader {

	protected List<IBeanMeta> beanMetaList;

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.IBeanMetaLoader#loadBeanMetas()
	 */
	public List<IBeanMeta> loadBeanMetas() {
		return beanMetaList;
	}

}
