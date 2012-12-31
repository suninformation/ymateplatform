/**
 * <p>文件名:	AnnotationBeanFactory.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.beans.impl;

import java.util.List;

import net.ymate.platform.commons.beans.AbstractBeanFactory;
import net.ymate.platform.commons.beans.IBeanMetaLoader;
import net.ymate.platform.commons.beans.IBeanMeta;
import net.ymate.platform.commons.beans.annotation.Bean;

/**
 * <p>
 * AnnotationBeanFactory
 * </p>
 * <p>
 * 基于Annotation特性的对象工厂接口实现类；
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
 *          <td>2012-12-15下午5:27:13</td>
 *          </tr>
 *          </table>
 */
public class AnnotationBeanFactory extends AbstractBeanFactory {

	protected String[] packageNames;

	/**
	 * 构造器
	 * 
	 * @param packageNames 自动扫描的包名称集合
	 */
	public AnnotationBeanFactory(String...packageNames) {
		this.packageNames = packageNames;
		this.doLoadBeanMeta();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.AbstractBeanFactory#loadBeanMetas()
	 */
	protected List<IBeanMeta> loadBeanMetas() {
		return new AnnotationBeanMetaLoader<Bean>(Bean.class, this.packageNames).loadBeanMetas();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.IBeanFactory#add(java.lang.Class)
	 */
	public IBeanMeta add(Class<?> clazz) {
		IBeanMetaLoader _loader = new AnnotationBeanMetaLoader<Bean>(Bean.class, clazz);
		for (IBeanMeta _meta : _loader.loadBeanMetas()) {
			this.beanMetaList.add(_meta);
			this.addBeanMeta(_meta);
			return _meta;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.AbstractBeanFactory#addBeanMeta(net.ymate.platform.commons.beans.IBeanMeta)
	 */
	protected void addBeanMeta(IBeanMeta beanMeta) {
		if (beanMeta instanceof AnnotationBeanMeta) {
			this.addBeanMetaToMap(beanMeta);
		}
	}

}
