package com.home.piperbike

import org.springframework.data.repository.CrudRepository
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*
import kotlin.jvm.internal.FunctionReference
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaMethod

class Extensions {
    companion object {
        val SUPPORTED_MAPPING_ANNOTATIONS = listOf(
                GetMapping::class.java,
                PutMapping::class.java,
                PostMapping::class.java,
                DeleteMapping::class.java,
                RequestMapping::class.java
        )
        val PARAMETER_NAME_REGEX = Regex("\\{([a-zA-Z0-9]+)}.*")
    }
}

inline fun <T, R> T.tryCatch(block: (T) -> R, catchException: (Throwable) -> R): R {
    return try {
        block(this)
    } catch (e: Throwable) {
        catchException(e)
    }
}

inline fun <T, R> T.tryOrNull(block: (T) -> R): R? = this.tryCatch(block) { null }

fun UUID.removeHyphens(): String {
    return this.toString().replace("-", "")
}

fun KFunction<*>.getRawUrl(): String {
    val owner = this.javaMethod?.declaringClass?.kotlin ?: (this as FunctionReference).owner as KClass<*>

    val foundAnnotations = Extensions.SUPPORTED_MAPPING_ANNOTATIONS
            .map { this.javaMethod?.getAnnotationsByType(it)?.firstOrNull() }
            .filter { it != null }

    if (foundAnnotations.count() == 0) {
        throw IllegalAccessException("Method $this is not annotated with any of the following: @GetMapping, @PutMapping, @PostMapping, @DeleteMapping, @RequestMapping")
    }

    if (foundAnnotations.count() > 1) {
        throw IllegalAccessException("Method $this is annotated with more than one request mapping")
    }

    val annotation = foundAnnotations.first()
    var suffix = when (annotation) {
        is GetMapping -> annotation.value
        is PutMapping -> annotation.value
        is PostMapping -> annotation.value
        is DeleteMapping -> annotation.value
        is RequestMapping -> annotation.value
        else -> throw IllegalAccessException("Annotation not supported: $annotation")
    }.firstOrNull().orEmpty().trim()

    // Ensure prefix starts and ends with /
    var prefix = owner.findAnnotation<RequestMapping>()?.value?.get(0).orEmpty().trim()
    if (!prefix.startsWith("/")) {
        prefix = "/$prefix"
    }
    if (!prefix.endsWith("/")) {
        prefix += "/"
    }
    // Ensure suffix does not start or end with /
    if (suffix.startsWith("/")) {
        suffix = suffix.substring(1).trim()
    }
    if (suffix.endsWith("/")) {
        suffix = suffix.substring(0, suffix.length - 1).trim()
    }
    // If suffix is blank, ensure prefix does not end with /
    if (suffix.isBlank()) {
        prefix = prefix.substring(0, prefix.length - 1)
    }
    return prefix + suffix
}

fun KFunction<*>.getUrl(pathVariables: Map<String, Any?> = emptyMap(), queryParams: Map<String, Any?> = emptyMap()): String {
    val rawUrl = this.getRawUrl()
    val result = pathVariables
            .toList()
            .filter { it.second != null }
            .fold(rawUrl) { url, (key, value) ->
                url.replace("{$key}", value.toString(), ignoreCase = false)
                        .also { current ->
                            if (current == url) {
                                throw IllegalArgumentException("Parameter {$key} does not exist on url $rawUrl for method $this")
                            }
                        }
            }
            .also { url ->
                Extensions.PARAMETER_NAME_REGEX.find(url)?.let { match ->
                    throw MissingFormatArgumentException("Parameter ${match.value} was not replaced in url $rawUrl for method $this")
                }
            }
            .let { url ->
                if (queryParams.isEmpty()) {
                    url
                } else {
                    queryParams.keys.fold(url) { acc, key ->
                        if (queryParams[key] == null) {
                            return@fold acc
                        }
                        val prefix = if (acc.contains('?')) "&" else "?"

                        acc + prefix + key + "=" + queryParams[key].toString()
                    }
                }
            }

    return result
}

fun <T, ID> CrudRepository<T, ID>.findOne(id: ID): T? = this.findById(id).orElse(null)