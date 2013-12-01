/*
 * Copyright 2007-2107 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.platform.mvc.web.view.impl;

import net.ymate.platform.mvc.web.WebMVC;
import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.view.AbstractWebView;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * TextView
 * </p>
 * <p>
 * 文本视图实现类；
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
 *          <td>2011-10-23上午11:15:43</td>
 *          </tr>
 *          </table>
 */
public class TextView extends AbstractWebView {

	/**
	 * 文本内容
	 */
	protected String text;

	/**
	 * 构造器
	 */
	public TextView() {
		text = "";
	}

	/**
	 * 构造器
	 * 
	 * @param text 输出文本
	 */
	public TextView(String text) {
		this.text = text;
	}

	/**
	 * 构造器
	 * 
	 * @param text 输出文本
	 * @param contentType 内容类型
	 */
	public TextView(String text, String contentType) {
		this.text = text;
		setContentType(contentType);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.AbstractWebView#renderView()
	 */
	protected void renderView() throws Exception {
		if (StringUtils.isNotBlank(getContentType())) {
			WebContext.getResponse().setContentType(getContentType());
		}
		IOUtils.write(this.text, WebContext.getResponse().getOutputStream(), StringUtils.defaultIfEmpty(WebMVC.getConfig().getCharsetEncoding(), WebContext.getResponse().getCharacterEncoding()));
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
