package org.backend.product.jgc.product

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

inline fun <reified T> ResultActions.bodyTo(mapper: ObjectMapper = jacksonObjectMapper()): T {
    return mapper.readValue(this.andReturn().response.contentAsByteArray)
}

fun MockHttpServletRequestBuilder.body(
        data: Any,
        mapper: ObjectMapper = jacksonObjectMapper(),
        mediaType: MediaType = MediaType.APPLICATION_JSON_UTF8): MockHttpServletRequestBuilder {
    return this.content(mapper.writeValueAsBytes(data)).contentType(mediaType)
}