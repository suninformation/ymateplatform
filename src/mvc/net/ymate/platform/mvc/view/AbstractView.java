/**
 * <p>文件名:	AbstractView.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * AbstractView
 * </p>
 * <p>
 * MVC视图接口抽象实现类；
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
 *          <td>2012-12-20下午6:56:56</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractView implements IView {

	protected final Map<String, Object> attributes;

	/**
	 * 构造器
	 */
	public AbstractView() {
		this.attributes = new HashMap<String, Object>();
	}

	/**
	 * 添加视图属性
	 * 
	 * @param key 属性键
	 * @param value 属性值
	 */
	public void addAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}

	/**
	 * @param key 属性键
	 * @return 返回键为key的属性值
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key) {
		return (T) this.attributes.get(key);
	}

	/**
	 * @return 返回视图对象的属性映射
	 */
	public Map<String, Object> getAttributes() {
		return Collections.unmodifiableMap(this.attributes);
	}

}
