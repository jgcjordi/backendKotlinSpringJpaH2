package org.backend.product.jgc.product.service

import org.backend.product.jgc.product.dao.ProductDAO
import org.backend.product.jgc.product.domain.Product
import org.backend.product.jgc.product.utils.update
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class ProductService(val productDAO: ProductDAO) : BasicCrud<Product, String> {

    override fun findAll(): List<Product> = productDAO.findAll()
    override fun findById(id: String): Product? = this.productDAO.findByIdOrNull(id)
    override fun save(t: Product): Product{
        return if(!this.productDAO.existsById(t.name)) this.productDAO.save(t) else throw  DuplicateKeyException("${t.name} does exists")
    }

    override fun update(t: Product): Product{
        return if(this.productDAO.existsById(t.name)) this.productDAO.save(t) else throw  EntityNotFoundException("${t.name} does not exists")
    }
    override fun deleteById(id: String): Product{
        return this.findById(id)?.apply {
            this@ProductService.productDAO.deleteById(this.name)
        } ?: throw EntityNotFoundException("$id does not exists")
    }
}