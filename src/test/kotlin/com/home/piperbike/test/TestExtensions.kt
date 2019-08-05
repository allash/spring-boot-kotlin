package com.home.piperbike.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.home.piperbike.api.shared.dto.DtoError
import com.home.piperbike.api.shared.exception.ServiceException
import com.home.piperbike.config.AppConfig
import com.home.piperbike.getUrl
import org.assertj.core.api.Assertions
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import kotlin.reflect.KFunction

class TestExtensions {
    companion object {
        lateinit var OBJECT_MAPPER: ObjectMapper
    }
}

fun MockHttpServletRequestBuilder.setBody(body: Any?): MockHttpServletRequestBuilder {
    body ?: return this
    when (body) {
        is String -> this.accept(MediaType.TEXT_PLAIN).content(body)
        else -> this.accept(MediaType.APPLICATION_JSON).also { it.jsonBody(body) }
    }
    return this
}

fun MockHttpServletRequestBuilder.params(body: Any): MockHttpServletRequestBuilder {
    body::class.java.declaredFields.forEach { field ->
        field.isAccessible = true
        val value = field.get(body)
        if (value is Array<*>) {

            value.forEach {
                if (it != null) {
                    this.param(field.name, it.toString())
                }
            }
        } else {
            if (value != null) {
                this.param(field.name, value.toString())
            }
        }
    }
    return this
}

fun toParams(body: Any): Map<String, Any> {
    val returnMap = mutableMapOf<String, Any>()
    body::class.java.declaredFields.forEach { field ->
        field.isAccessible = true
        val value = field.get(body)
        value ?: return@forEach
        if (value is Array<*>) {
            value.forEach { returnMap[field.name] = it?.toString()!! }
        } else {
            returnMap[field.name] = value.toString()
        }
    }
    return returnMap
}

fun MockHttpServletRequestBuilder.jsonBody(obj: Any): MockHttpServletRequestBuilder {
    return this.contentType("application/json")
            .content(TestExtensions.OBJECT_MAPPER.writeValueAsString(obj))
}

fun KFunction<*>.post(
        body: Any? = null,
        pathVariables: Map<String, Any> = emptyMap(),
        queryParams: Map<String, Any> = emptyMap()
): MockHttpServletRequestBuilder {
    return MockMvcRequestBuilders.post(this.getUrl(pathVariables, queryParams))
            .also { if (body != null) it.setBody(body) }
}

fun KFunction<*>.put(
        body: Any? = null,
        pathVariables: Map<String, Any> = emptyMap(),
        queryParams: Map<String, Any> = emptyMap()
): MockHttpServletRequestBuilder {
    return MockMvcRequestBuilders.put(this.getUrl(pathVariables, queryParams))
            .also { it.setBody(body) }
}

fun KFunction<*>.get(
        pathVariables: Map<String, Any?> = emptyMap(),
        queryParams: Map<String, Any?> = emptyMap()
): MockHttpServletRequestBuilder {
    return MockMvcRequestBuilders.get(this.getUrl(pathVariables, queryParams))
            .accept(MediaType.APPLICATION_JSON)
}

fun KFunction<*>.delete(
        pathVariables: Map<String, Any> = emptyMap(),
        queryParams: Map<String, Any> = emptyMap()
): MockHttpServletRequestBuilder {
    return MockMvcRequestBuilders.delete(this.getUrl(pathVariables, queryParams))
            .accept(MediaType.APPLICATION_JSON)
}

fun MockHttpServletRequestBuilder.withHeaders(headers: HttpHeaders): MockHttpServletRequestBuilder =
        this.headers(headers)

fun MockHttpServletRequestBuilder.withSessionToken(token: String): MockHttpServletRequestBuilder =
        this.header(AppConfig.AUTH_TOKEN_HEADER, token)

fun ResultActions.andExpectError(exception: ServiceException) {
    val response = this.andReturnResult<DtoError>()
    Assertions.assertThat(response.error.toString()).isEqualTo(exception.i18nMessage.toString())
}

inline fun <reified T> ResultActions.andReturnResult(): T {
    val json = this.andReturn().response.contentAsString
    return TestExtensions.OBJECT_MAPPER.readValue(json, T::class.java)
}

inline fun <reified T> ResultActions.andReturnResultList(): List<T> {
    val json = this.andReturn().response.contentAsString
    val type = TestExtensions.OBJECT_MAPPER.typeFactory.constructCollectionLikeType(ArrayList::class.java, T::class.java)
    return TestExtensions.OBJECT_MAPPER.readValue(json, type)
}
