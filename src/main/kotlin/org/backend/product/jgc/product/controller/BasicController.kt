package org.backend.product.jgc.product.controller

import io.swagger.annotations.ApiOperation
import org.backend.product.jgc.product.service.BasicCrud
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

abstract class BasicController<T, ID>(private val basicCrud: BasicCrud<T, ID>) {

//    @Autowired marca una dependencia para que la inyecte spring, para ello la clase se debe etiquetar para que spring
//    la controle, si esta variable la incluimos en el constructor esto no seria necesario porque spring lo haria
//    automaticamente
//    @Autowired
//    private lateinit var productService: ProductService

    @ApiOperation("Get all entities")//para añadir una descripcion a la documentación creada por swagger.
    @GetMapping//este endpoint es el del requestmapping cuando es un get a secas
    fun findAll() = basicCrud.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: ID): ResponseEntity<T> {
        val entity = basicCrud.findById(id)
        return ResponseEntity.status(if (entity != null) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }

    @PostMapping
    fun save(@Valid @RequestBody body: T) = ResponseEntity.status(HttpStatus.CREATED).body(this.basicCrud.save(body)) //@Valid: valida que se cumplen las reglas de la clase, tampaño valor minimo.

    @PutMapping
    fun update(@RequestBody body: T) = this.basicCrud.update(body)

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: ID) = this.basicCrud.deleteById(id)
}