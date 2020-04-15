package org.backend.product.jgc.product.service

import org.backend.product.jgc.product.dao.ProviderDAO
import org.backend.product.jgc.product.domain.Provider
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class ProviderService(val providerDao: ProviderDAO):BasicCrud<Provider, Int> {

    override fun findAll(): List<Provider> = this.providerDao.findAll()
    override fun findById(id: Int): Provider? = this.providerDao.findByIdOrNull(id)
    override fun save(t: Provider): Provider{
        return if(!this.providerDao.existsById(t.id)) this.providerDao.save(t) else throw  DuplicateKeyException("${t.name} does exists")
    }
    override fun update(t: Provider): Provider{
        return if(this.providerDao.existsById(t.id)) this.providerDao.save(t) else throw  EntityNotFoundException("${t.name} does not exists")
    }
    override fun deleteById(id: Int): Provider{
        return this.findById(id)?.apply {
            this@ProviderService.providerDao.deleteById(this.id)
        } ?: throw EntityNotFoundException("$id does not exists")
    }
}