package org.backend.product.jgc.product.service

interface BasicCrud<T, ID> {//T, ID: esto son parametros genericos, se usan cuando las clases que implementan esta interfaz usaran tipos(clases) diferentes al implementarlos, genericos en tiempo de compilacion pero en tiempo de ejecucion seran de un tipo en particular
    fun findAll(): List<T>
    fun findById(id: ID): T?//puede ser nulo, puede no devolver nada
    fun save(t: T): T// si se ha guardado con exito
    fun update(t: T): T
    fun deleteById(id: ID): T
}