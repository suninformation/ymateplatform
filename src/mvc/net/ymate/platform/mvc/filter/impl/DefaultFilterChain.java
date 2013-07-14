/**
 * <p>文件名:	DefaultFilterChain.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.filter.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.mvc.filter.IFilter;
import net.ymate.platform.mvc.filter.IFilterChain;
import net.ymate.platform.mvc.support.RequestMeta;
import net.ymate.platform.mvc.view.IView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * DefaultFilterChain
 * </p>
 * <p>
 * 拦截器执行链接口默认实现类；
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
 *          <td>2012-12-17下午9:42:13</td>
 *          </tr>
 *          </table>
 */
public class DefaultFilterChain implements IFilterChain {

	private static final Log _LOG = LogFactory.getLog(DefaultFilterChain.class);

	protected final List<PairObject<IFilter, String>> filters;

	/**
	 * 构造器
	 */
	public DefaultFilterChain() {
		this.filters = new ArrayList<PairObject<IFilter, String>>();
	}

	/**
	 * 构造器
	 * 
	 * @param filters 初始过滤器集合
	 */
	public DefaultFilterChain(Collection<PairObject<IFilter, String>> filters) {
		this.filters = filters==null ? new ArrayList<PairObject<IFilter, String>>() : new ArrayList<PairObject<IFilter, String>>(filters);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.filter.IFilterChain#add(net.ymate.platform.mvc.filter.IFilter)
	 */
	public void add(PairObject<IFilter, String> filter) {
		this.filters.add(filter);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.filter.IFilterChain#add(int, net.ymate.platform.mvc.filter.IFilter)
	 */
	public void add(int index, PairObject<IFilter, String> filter) {
		this.filters.add(index, filter);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.filter.IFilterChain#doChain(net.ymate.platform.mvc.support.RequestMeta)
	 */
	public IView doChain(RequestMeta meta) throws Exception {
		IView _view = null;
		for (PairObject<IFilter, String> _filter : this.filters) {
			_LOG.info("执行拦截器 [" + _filter.getKey().getClass().getName() + ", " + _filter.getValue() + "]");
			_view = _filter.getKey().doFilter(meta, _filter.getValue());
			if (_view != null) {
				break;
			}
		}
		return _view;
	}

}
