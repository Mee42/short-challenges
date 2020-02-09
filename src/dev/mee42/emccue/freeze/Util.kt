package dev.mee42.emccue.freeze
fun String.append(other: String) = this + other
fun String.appendToFront(other: String) = other + this
fun <R> Any?.thenReturn(retur: R):R = retur
infix fun String.equalsIgnoreCase(other: String) = this.toUpperCase() == other.toUpperCase()