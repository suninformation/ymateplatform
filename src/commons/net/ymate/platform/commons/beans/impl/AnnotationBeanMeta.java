/**
 * <p>文件名:	AnnotationBeanMeta.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.beans.impl;

import net.ymate.platform.commons.beans.IBeanMeta;

/**
 * <p>
 * AnnotationBeanMeta
 * </p>
 * <p>
 * 基于Annotation特性的对象元描述接口实现类；
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
 *          <td>2012-12-15下午5:43:04</td>
 *          </tr>
 *          </table>
 */
public class AnnotationBeanMeta implements IBeanMeta {

	private String id;
	private String className;
	private String[] interfaceNames;
	private Object object;

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.IBeanMeta#getId()
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.IBeanMeta#getClassName()
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.IBeanMeta#getInterfaceNames()
	 */
	public String[] getInterfaceNames() {
		return interfaceNames;
	}

	/**
	 * @param interfaceNames the interfaceNames to set
	 */
	public void setInterfaceNames(String[] interfaceNames) {
		this.interfaceNames = interfaceNames;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.beans.IBeanMeta#getObject()
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

}
