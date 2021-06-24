package com.mainline.magic.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "mcp")
public class McpProperties {
	
	private String master;
	private String clear;
	private String loadbalancePath;
	private String mtsPath;
	private String mtsResultPath;
	private String insuranceTypeKey;
	private String insuranceType;
	private String magictermsDocumentKey;
	private String magictermsDocumentType;
	private String quartzDb;
	private String threadSize;
	private String cronExpression;
	private String asyncFlag;
}
