package com.mainline.magic.scheduler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "mcp")
public class McpProperties {
	
	private String master;
	private String clear;
	private String downloadPath;
}
