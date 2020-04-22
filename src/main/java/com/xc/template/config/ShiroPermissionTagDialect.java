package com.xc.template.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

@Component
public class ShiroPermissionTagDialect extends AbstractProcessorDialect implements IProcessorDialect {

	private static final String PREFIX = "sp";
	
	public ShiroPermissionTagDialect() {
		super("shiroPermission", PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
	}
	
	@Override
	public Set<IProcessor> getProcessors(String arg0) {
		Set<IProcessor> processors = new HashSet<IProcessor>();
		processors.add(new ShiroPermissionProcessor(PREFIX));
		return processors;
	}
	
	

}
