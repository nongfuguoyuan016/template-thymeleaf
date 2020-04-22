package com.xc.template.config;

import com.xc.template.system.utils.UserUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
import com.xc.template.common.utils.StringUtils;

/**
 * shiro 权限校验
 * <shiroPermission name="permission1,permission2" type="have(default) | lack" logical="and(default) | or"></>
 * have & and: 全都有才渲染
 * hava & or: 有一个就能渲染
 * lack & and：全都缺失就渲染
 * lack & or： 有一个缺失就能渲染
 */
public class ShiroPermissionProcessor extends AbstractElementTagProcessor{
	
	private static final int PRECEDENCE = 10000;
	
	private static final String TAG_NAME ="shiroPermission";
	
	public ShiroPermissionProcessor(String dialectPrefix) {
		super(
				// 此处理器将仅应用于HTML模式
				TemplateMode.HTML,
				// 要应用于名称的匹配前缀
				dialectPrefix,
				// 标签名称：匹配此名称的特定标签
				TAG_NAME,
				// 没有要应用于标签名称的前缀
				false,
				// 无属性名称：将通过标签名称匹配
				null,
				// 没有要应用于属性名称的前缀
				false,
				// 优先(内部方言自己的优先)
				PRECEDENCE);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler handler) {
		IAttribute name = tag.getAttribute("name");
		IAttribute type = tag.getAttribute("type");
		IAttribute logical = tag.getAttribute("logical");
		if(null == name || StringUtils.isBlank(name.getValue())) {
			handler.removeElement();
		} else {
			// 是否渲染
			boolean render =  false;
			String permissionNames = name.getValue().replaceAll("\\s","").toLowerCase();
			boolean[] valids = UserUtils.getSubject().isPermitted(permissionNames.split(","));
			if(type != null && ShiroPermissionAttributeEnum.TYPE_OF_LACK.value.equals(type.getValue().trim().toLowerCase())) {
				// 是否缺失该权限
				if(logical != null && ShiroPermissionAttributeEnum.LOGICAL_OF_OR.value.equals(logical.getValue().trim().toLowerCase())) {
					// 有一个缺失就渲染
					for (boolean valid : valids) {
						if(!valid) {
							render = true;
							break;
						}
					}
				} else {
					//  必须全部缺失才能渲染, 即valid必须全部是false, render才能是true
					for (boolean valid : valids) {
						if(valid) {
							render = false;
							break;
						} else {
							render = true;
						}
					}
				}
			} else {
				// 是否持有该权限
				if(logical != null && ShiroPermissionAttributeEnum.LOGICAL_OF_OR.value.equals(logical.getValue().trim().toLowerCase())) {
					// 有一个就能渲染
					for (boolean valid : valids) {
						if(valid) {
							render = true;
							break;
						}
					}
				} else {
					//  必须全部持有
					for (boolean valid : valids) {
						if(valid) {
							render = true;
						} else {
							render = false;
							break;
						}
					}
				}
			}

			if(render) {
				handler.removeTags();
			}else {
				handler.removeElement();
			}
		}
	}

	private enum ShiroPermissionAttributeEnum {
		TYPE_OF_HAVE("have"), TYPE_OF_LACK("lack"), LOGICAL_OF_AND("and"), LOGICAL_OF_OR("or");

		String value;

		ShiroPermissionAttributeEnum(String value) {
			this.value = value;
		}
	}
	

}
