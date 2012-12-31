/**
 * <p>文件名:	RepositoryBeanFactory.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.support;

import java.util.List;

import net.ymate.platform.commons.beans.IBeanMeta;
import net.ymate.platform.commons.beans.IBeanMetaLoader;
import net.ymate.platform.commons.beans.impl.AnnotationBeanFactory;
import net.ymate.platform.persistence.jdbc.annotation.Repository;

/**
 * <p>
 * RepositoryBeanFactory
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
 *          <td>2012-12-30上午1:35:07</td>
 *          </tr>
 *          </table>
 */
public class RepositoryBeanFactory extends AnnotationBeanFactory {

	public RepositoryBeanFactory(String...packageNames) {
		this.packageNames = packageNames;
		this.doLoadBeanMeta();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.impl.AnnotationBeanFactory#loadBeanMetas()
	 */
	protected List<IBeanMeta> loadBeanMetas() {
		return new RepositoryBeanMetaLoader(this.packageNames).loadBeanMetas();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.impl.AnnotationBeanFactory#add(java.lang.Class)
	 */
	public IBeanMeta add(Class<?> clazz) {
		if (clazz.isAnnotationPresent(Repository.class)) {
			IBeanMetaLoader _loader = new RepositoryBeanMetaLoader(clazz);
			for (IBeanMeta _meta : _loader.loadBeanMetas()) {
				if (!this.beanMap.containsKey(_meta.getClassName())) {
					this.beanMetaList.add(_meta);
					this.addBeanMeta(_meta);
					return _meta;
				}
			}
		}
		return null;
	}

}
