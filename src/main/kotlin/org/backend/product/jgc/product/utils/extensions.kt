package org.backend.product.jgc.product.utils

fun <E> MutableSet<E>.update(element: E):Boolean {
    return this.remove(element) && this.add(element)
}