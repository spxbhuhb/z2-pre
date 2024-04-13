package hu.simplexion.z2.kotlin.schematic.fir

import hu.simplexion.z2.kotlin.schematic.ClassIds
import hu.simplexion.z2.kotlin.schematic.Strings
import hu.simplexion.z2.kotlin.util.superTypeContains
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirClassLikeDeclaration
import org.jetbrains.kotlin.fir.declarations.utils.isCompanion
import org.jetbrains.kotlin.fir.extensions.FirSupertypeGenerationExtension
import org.jetbrains.kotlin.fir.resolve.getContainingDeclaration
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.types.FirResolvedTypeRef
import org.jetbrains.kotlin.fir.types.builder.buildResolvedTypeRef
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.fir.types.constructType

/*
 * Add `SchematicCompanion<T>` or `SchematicEntityCompanion<T> to supertypes of the companion object.
 */
class SchematicSupertypeGenerator(session: FirSession) : FirSupertypeGenerationExtension(session) {

    context(TypeResolveServiceContainer)
    override fun computeAdditionalSupertypes(
        classLikeDeclaration: FirClassLikeDeclaration,
        resolvedSupertypes: List<FirResolvedTypeRef>
    ): List<FirResolvedTypeRef> {

        val hasEntityCompanion = resolvedSupertypes.any { it.type.classId == ClassIds.SCHEMATIC_ENTITY_COMPANION }
        if (hasEntityCompanion) return emptyList() // means that it has SchematicCompanion as well

        val schematicClass = classLikeDeclaration.getContainingDeclaration(session) ?: return emptyList()
        val schematicClassType = schematicClass.symbol.constructType(emptyArray(), false)

        val needEntityCompanion = schematicClass.anyOf(Strings.SCHEMATIC_ENTITY_PATTERN)

        val needSchematicCompanion = if (needEntityCompanion) false else resolvedSupertypes.none { it.type.classId == ClassIds.SCHEMATIC_COMPANION }

        val result = mutableListOf<FirResolvedTypeRef>()

        when {
            needEntityCompanion -> {
                result += buildResolvedTypeRef {
                    type = ClassIds.SCHEMATIC_ENTITY_COMPANION.constructClassLikeType(arrayOf(schematicClassType), isNullable = false)
                }
            }

            needSchematicCompanion -> {
                result += buildResolvedTypeRef {
                        type = ClassIds.SCHEMATIC_COMPANION.constructClassLikeType(arrayOf(schematicClassType), isNullable = false)
                    }
            }
        }

        return result
    }

    override fun needTransformSupertypes(declaration: FirClassLikeDeclaration): Boolean {
        if (! declaration.symbol.isCompanion) return false
        val containingDeclaration = declaration.getContainingDeclaration(session) ?: return false
        return containingDeclaration.anyOf(Strings.SCHEMATIC_PATTERN, Strings.SCHEMATIC_ENTITY_PATTERN)
    }

    fun FirClassLikeDeclaration.anyOf(vararg supertypes : String) : Boolean {
        val symbol = symbol as? FirClassSymbol<*> ?: return false
        return symbol.superTypeContains(*supertypes)
    }
}