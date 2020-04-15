package org.backend.product.jgc.product.dao

import org.backend.product.jgc.product.domain.Product
import org.backend.product.jgc.product.domain.Provider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository//es opcional al heredar de jpa repository pero son buenas practicas
interface ProductDAO: JpaRepository<Product, String>

@Repository
interface ProviderDAO: JpaRepository<Provider, Int>