package com.home.piperbike.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class JacksonConfiguration {

    companion object {
        private val logger = LoggerFactory.getLogger(JacksonConfiguration::class.java)!!
    }

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        logger.info("Registering custom jackson object mapper")
        val objectMapper = Jackson2ObjectMapperBuilder.json().createXmlMapper(false).build<ObjectMapper>()
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
        objectMapper.registerKotlinModule()
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        return objectMapper
    }
}