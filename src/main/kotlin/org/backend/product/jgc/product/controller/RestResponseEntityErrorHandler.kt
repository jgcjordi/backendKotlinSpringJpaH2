package org.backend.product.jgc.product.controller

import org.backend.product.jgc.product.dto.ApiResponse
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityNotFoundException

@ControllerAdvice
class RestResponseEntityErrorHandler:ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus,
                                              request: WebRequest): ResponseEntity<Any> { //si la excepticion es del framework sobreescibimos la ResponseEntityExceptionHandler()

        val result:Map<String, List<String?>>  = ex.bindingResult.fieldErrors.groupBy({it.field}, {it.defaultMessage})
        return ResponseEntity.status(status).headers(headers).body(result)
    }

    @ExceptionHandler(DuplicateKeyException::class, EntityNotFoundException::class)//si la excepticion no viene de alguna del framework podemos usar esta anotacion para responder a errores concretos, ejemplo
    fun handleJpa(exception:Exception): ResponseEntity<ApiResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse(
                title = exception::class.simpleName.toString(),
                message = exception.localizedMessage)
        )
    }
}