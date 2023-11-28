package com.example.productservice.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

//@Configuration
//@EnableR2dbcRepositories(basePackages = "com.example.productservice.persistence")
//public class DatabaseConfiguration {
//
//  @Value("${spring.r2dbc.username}")
//  private static String user;
//  @Value("${spring.r2dbc.password}")
//  private static String password;
//  @Value("${spring.r2dbc.name}")
//  private static String database;
//
////  public ConnectionFactory connectionFactory() {
////    return ConnectionFactories.get(ConnectionFactoryOptions.builder()
////            .option(DRIVER, "postgresql")
////            .option(HOST, "localhost")
////            .option(PORT, 5432)  // optional, defaults to 5432
////            .option(USER, user)
////            .option(PASSWORD, password)
////            .option(DATABASE, database)  // optional
////            .build());
////  }
//
//  @Bean
//  public R2dbcEntityTemplate r2dbcEntityTemplate() {
//    ConnectionFactory connectionFactory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
//            .option(DRIVER, "postgresql")
//            .option(HOST, "localhost")
//            .option(PORT, 5432)  // optional, defaults to 5432
//            .option(USER, user)
//            .option(PASSWORD, password)
//            .option(DATABASE, database)  // optional
//            .build());
//
//    return new R2dbcEntityTemplate(connectionFactory);
//  }
//
//}
