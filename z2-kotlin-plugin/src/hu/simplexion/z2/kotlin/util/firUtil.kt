package hu.simplexion.z2.kotlin.util

import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.text

@OptIn(SymbolInternals::class)
fun FirClassSymbol<*>.superTypeContains(vararg names : String) : Boolean =
    this
        .fir
        .superTypeRefs
        .mapNotNull { it.source.text }
        .any { source ->
            names.any { it in source }
        }
