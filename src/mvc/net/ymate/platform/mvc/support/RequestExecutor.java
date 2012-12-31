/**
 * <p>文件名:	RequestExecutor.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.support;

import net.ymate.platform.mvc.filter.IFilterChain;
import net.ymate.platform.mvc.view.IView;

/**
 * <p>
 * RequestExecutor
 * </p>
 * <p>
 * MVC请求执行器；
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
 *          <td>2012-12-14下午4:30:27</td>
 *          </tr>
 *          </table>
 */
public class RequestExecutor {

	protected final RequestMeta requestMeta;
	protected final IFilterChain chain;

	/**
	 * 构造器
	 * 
	 * @param meta MVC请求元数据描述对象
	 */
	public RequestExecutor(RequestMeta meta) {
		this.requestMeta = meta;
		this.chain = null;
	}

	/**
	 * 构造器
	 * 
	 * @param meta MVC请求元数据描述对象
	 * @param chain 拦截器执行链对象
	 */
	public RequestExecutor(RequestMeta meta, IFilterChain chain) {
		this.requestMeta = meta;
		this.chain = chain;
	}

	/**
	 * @return 处理并返回方法对象invoke时所需的参数集合
	 */
	protected Object[] getMethodParams() {
		return new Object[this.requestMeta.getParameterTypes().length];
	}

	/**
	 * @param result 方法执行结果对象
	 * @return 处理执行结果转IView视图对象
	 * @throws Exception 抛出任何可能异常
	 */
	protected IView processMethodResultToView(Object result) throws Exception {
		return (IView) result;
	}

	/**
	 * @return 执行并返回执行结果视图对象
	 * @throws Exception 抛出任何可能异常
	 */
	public IView execute() throws Exception {
		IView _view = null;
		if (chain != null) {
			_view = chain.doChain(this.requestMeta);
		}
		if (_view == null) {
			Object _result = this.requestMeta.getMethod().invoke(this.requestMeta.getTarget(), this.getMethodParams());
			_view = this.processMethodResultToView(_result);
		}
		return _view;
	}

}
