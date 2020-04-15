package org.backend.product.jgc.product.controller

import org.backend.product.jgc.product.domain.Product
import org.backend.product.jgc.product.domain.Provider
import org.backend.product.jgc.product.service.ProductService
import org.backend.product.jgc.product.service.ProviderService
import org.springframework.web.bind.annotation.*
import java.io.FileNotFoundException

@RestController//Esta anotacion de spring, etiqueta esta clase como un controlador, ataja las peticiones http que recibe nuestro servidor y las enruta a nuestro codigo
@RequestMapping("/api/v1/products")//especifica el endpoint del controlador
class ProductController(productService: ProductService): BasicController<Product, String>(productService){

    @GetMapping("/test")
    fun fileNotFoundTest(){
        throw FileNotFoundException("just test")
    }
}

@RestController
@RequestMapping("/api/v1/providers")
class ProviderController(providerService: ProviderService): BasicController<Provider, Int>(providerService)