package org.backend.product.jgc.product.conficuration

import org.backend.product.jgc.product.ProductApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

//http://localhost:8080/swagger-ui.html
@Configuration
@EnableSwagger2
class Swagger {

    //para modificar el funcionamiento de swagger pisando el contenedor de objetos de spring
    @Bean
    fun api():Docket = Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(ProductApplication::class.java.`package`.name))//los paths de las api que tiene que analizar, en este caso tdo el proyecto
            .paths(PathSelectors.any())//esto es para restringir los paths que se van a renderizar, queremos que se rendericen todos.
            .build()
}