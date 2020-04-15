package org.backend.product.jgc.product.domain

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.Size

@Entity//marca la clase como entidad de la base de datos.
data class Product(//Al usar "data", kotlin sobrescribe los operadores, sin "data" dos objetos no son iguales porque se comparan sus referencias, con data si que son iguales porque se comparan sus atributos, si todos sus atributos son iguales.
        @Id//este es el atributo que hace como identificador unico para la bd.
        @get:Size(min = 3, max = 20)
        val name: String,
        @get:Min(0)
        var price: Double? = 55.5,
        @get:Min(0)
        var stock: Int = 0,
        @ManyToOne
        var provider: Provider) {

    override fun equals(other: Any?): Boolean {
        other
                ?: return false  // operador elvis "?:" si la variable es nula hace lo que hay a la derecha del operdador, si no es nula devuelve el valor de la izquierda.
        if (other === this) return true //tres iguales comparan la referencia, sera true si son el mismo objeto.
        if (this.javaClass != other.javaClass) return false // aqui comparamos el tipo, la clase.
        other as Product // casteamos other como Product porque ya sabemos que es del mismo tipo
        return this.name == other.name //en este caso queremos que nos diga si son iguales, cuando tienen el mismo nombre, por eso sobrescribimos la funcion en el ejercicio
    }

    override fun hashCode(): Int {//se usa internamente junto a equals para definir si dos objetos son iguales o no como hemos sobrescrito equals tenemos que hacer esto.
        return name.hashCode()
    }
}

@Entity
data class Provider(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)//crear id bajo demanda de forma autoincremental
        var id: Int = 0,
        @get:Size(min = 3, max = 20)
        var name: String,
        @get:Email
        var email: String) {

    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (other === this) return true
        if (this.javaClass != other.javaClass) return false
        other as Provider
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}