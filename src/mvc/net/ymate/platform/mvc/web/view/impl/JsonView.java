/**
 * <p>文件名:	JsonView.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-WebMVC</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.view.impl;

import javax.servlet.http.HttpServletResponse;

import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.view.AbstractWebView;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 * JsonView
 * </p>
 * <p>
 * JSON视图实现类；
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
 *          <td>2011-10-23上午11:27:16</td>
 *          </tr>
 *          </table>
 */
public class JsonView extends AbstractWebView {

	public static final String JSON_CONTENT_TYPE = "application/json";
	public static final String JAVASCRIPT_CONTENT_TYPE = "text/javascript";

	protected Object jsonObj;
	protected boolean withContentType;
	protected String jsonpCallback;

	/**
	 * 构造器
	 * @param obj JSON对象
	 */
	public JsonView(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
	}

	/**
	 * 构造器
	 * 
	 * @param jsonArr JSON集合对象
	 */
	public JsonView(JSONArray jsonArr) {
		this.jsonObj = jsonArr;
	}

	/**
	 * 构造器
	 * 
	 * @param jsonStr JSON字符串
	 * @throws JSONException JSON异常
	 */
	public JsonView(String jsonStr) throws JSONException {
		if (StringUtils.isNotBlank(jsonStr) && jsonStr.trim().charAt(0) == '[') {
			this.jsonObj = new JSONArray(jsonStr);
		} else {
			this.jsonObj = new JSONObject(jsonStr);
		}
	}

	/**
	 * @return 设置ContentType为"application/json"或"text/javascript"，默认为空
	 */
	public JsonView withContentType() {
		this.withContentType = true;
		return this;
	}

	/**
	 * @param callback 回调方法名称
	 * @return 设置并采用JSONP方式输出，回调方法名称由参数callback指定，若callback参数无效则不启用
	 */
	public JsonView withJsonpCallback(String callback) {
		this.jsonpCallback = StringUtils.defaultIfEmpty(callback, null);
		return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.AbstractWebView#renderView()
	 */
	protected void renderView() throws Exception {
		HttpServletResponse response = WebContext.getResponse();
		if (StringUtils.isNotBlank(getContentType())) {
			response.setContentType(getContentType());
		} else if (this.withContentType) {
			if (this.jsonpCallback != null) {
				response.setContentType(JSON_CONTENT_TYPE);
			} else {
				response.setContentType(JAVASCRIPT_CONTENT_TYPE);
			}
		}
		StringBuilder _jsonStr = new StringBuilder(jsonObj.toString());
		if (this.jsonpCallback != null) {
			_jsonStr.insert(0, this.jsonpCallback + "(").append(");");
		}
		IOUtils.write(_jsonStr.toString(), response.getOutputStream());
	}

}
