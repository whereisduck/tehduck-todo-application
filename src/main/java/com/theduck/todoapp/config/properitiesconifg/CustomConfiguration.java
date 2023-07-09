package com.theduck.todoapp.config.properitiesconifg;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CustomConfigurationProperties.class)
class CustomConfiguration {
}
