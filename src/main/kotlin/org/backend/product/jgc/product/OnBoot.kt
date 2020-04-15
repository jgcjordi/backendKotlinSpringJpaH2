package org.backend.product.jgc.product

import org.backend.product.jgc.product.domain.Product
import org.backend.product.jgc.product.domain.Provider
import org.backend.product.jgc.product.service.ProductService
import org.backend.product.jgc.product.service.ProviderService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class OnBoot(private val productService: ProductService, private val providerService: ProviderService) : ApplicationRunner {
    //cargamos la db cuando no estamos
    override fun run(args: ApplicationArguments?) {


        val defaultProvider = Provider(id = 1, name = "Jordi", email = "jordigomis@gmail.com")

        if (!providerService.providerDao.existsById(defaultProvider.id)) this.providerService.save(defaultProvider)

        listOf(
                Product("Apple", 22.2, 5, defaultProvider),
                Product("Orange", 33.8, 15, defaultProvider),
                Product("Banana", 12.2, 21, defaultProvider)).forEach {
            if (!productService.productDAO.existsById(it.name)) {
                println("Saving -> ${it.name}")
                productService.save(it)
            }

        }
    }

}