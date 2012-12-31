/**
 * <p>文件名:	RepositoryBeanMetaLoader.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.support;

import net.ymate.platform.commons.beans.impl.AnnotationBeanMetaLoader;
import net.ymate.platform.persistence.jdbc.annotation.Repository;

/**
 * <p>
 * RepositoryBeanMetaLoader
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
 *          <td>2012-12-27下午11:58:35</td>
 *          </tr>
 *          </table>
 */
public class RepositoryBeanMetaLoader extends AnnotationBeanMetaLoader<Repository> {

	/**
	 * 构造器
	 * 
	 * @param clazz 目标存储器类对象
	 */
	public RepositoryBeanMetaLoader(Class<?> clazz) {
		super(Repository.class, clazz);
	}

	/**
	 * 构造器
	 * 
	 * @param packageNames 目标存储器包名称集合
	 */
	public RepositoryBeanMetaLoader(String... packageNames) {
		super(Repository.class, packageNames);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.impl.AnnotationBeanMetaLoader#getAnnotationValue(java.lang.annotation.Annotation)
	 */
	protected String getAnnotationValue(Repository clazz) {
		return clazz.value();
	}

}
