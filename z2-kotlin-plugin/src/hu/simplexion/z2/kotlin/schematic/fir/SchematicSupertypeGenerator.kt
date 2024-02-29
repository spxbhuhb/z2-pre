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
 * Add `SchematicCompanion<T>` to supertypes of the companion object.
 */
class SchematicSupertypeGenerator(session: FirSession) : FirSupertypeGenerationExtension(session) {

    context(TypeResolveServiceContainer)
    override fun computeAdditionalSupertypes(
        classLikeDeclaration: FirClassLikeDeclaration,
        resolvedSupertypes: List<FirResolvedTypeRef>
    ): List<FirResolvedTypeRef> {
        if (resolvedSupertypes.any { it.type.classId == ClassIds.SCHEMATIC_COMPANION }) return emptyList()

        val schematicClass = classLikeDeclaration.getContainingDeclaration(session) ?: return emptyList()
        val schematicClassType = schematicClass.symbol.constructType(emptyArray(), false)

        return listOf(
            buildResolvedTypeRef {
                type = ClassIds.SCHEMATIC_COMPANION.constructClassLikeType(arrayOf(schematicClassType), isNullable = false)
            }
        )
    }

    override fun needTransformSupertypes(declaration: FirClassLikeDeclaration): Boolean {
        if (!declaration.symbol.isCompanion) return false

        val containingDeclaration = declaration.getContainingDeclaration(session) ?: return false
        val symbol = containingDeclaration.symbol as? FirClassSymbol<*> ?: return false
        return symbol.superTypeContains(Strings.SCHEMATIC_PREFIX, Strings.SCHEMATIC_ENTITY_PREFIX)
    }

}