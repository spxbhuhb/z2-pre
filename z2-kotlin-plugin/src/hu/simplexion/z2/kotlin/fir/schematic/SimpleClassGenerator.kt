package hu.simplexion.z2.kotlin.fir.schematic

import hu.simplexion.z2.kotlin.ir.schematic.RUNTIME_PACKAGE
import hu.simplexion.z2.kotlin.ir.schematic.SCHEMATIC_CLASS
import hu.simplexion.z2.kotlin.ir.schematic.SCHEMATIC_COMPANION_CLASS
import org.jetbrains.kotlin.GeneratedDeclarationKey
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.NestedClassGenerationContext
import org.jetbrains.kotlin.fir.plugin.createCompanionObject
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

class SchematicDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    companion object {
        val SCHEMATIC_CLASS_ID = ClassId(FqName(RUNTIME_PACKAGE), Name.identifier(SCHEMATIC_CLASS))

        val SCHEMATIC_COMPANION_CLASS_ID = ClassId(FqName(RUNTIME_PACKAGE), Name.identifier(SCHEMATIC_COMPANION_CLASS))

        object Key : GeneratedDeclarationKey()
    }

    override fun getNestedClassifiersNames(classSymbol: FirClassSymbol<*>, context: NestedClassGenerationContext): Set<Name> {
        val result = mutableSetOf<Name>()
        if (classSymbol.resolvedSuperTypes.any { it.type.classId == SCHEMATIC_CLASS_ID }) {
            result += SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT
        }
        return result
    }

    override fun generateNestedClassLikeDeclaration(
        owner: FirClassSymbol<*>,
        name: Name,
        context: NestedClassGenerationContext
    ): FirClassLikeSymbol<*>? {
        if (owner !is FirRegularClassSymbol) return null
        return when (name) {
            SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT -> generateCompanionDeclaration(owner)
            else -> error("Can't generate class ${owner.classId.createNestedClassId(name).asSingleFqName()}")
        }
    }

    private fun generateCompanionDeclaration(owner: FirRegularClassSymbol): FirRegularClassSymbol? {
        if (owner.companionObjectSymbol != null) return null
        val companion = createCompanionObject(owner, Key) {
            superType(SCHEMATIC_COMPANION_CLASS_ID.constructClassLikeType(emptyArray(), false))
        }
        return companion.symbol
    }

}