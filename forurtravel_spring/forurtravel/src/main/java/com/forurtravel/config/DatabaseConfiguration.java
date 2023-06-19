package com.forurtravel.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(
		basePackages = "com.forurtravel.**.model.mapper"
)
public class DatabaseConfiguration {}