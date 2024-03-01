package hu.simplexion.z2.kotlin.schematic.fir

import hu.simplexion.z2.kotlin.schematic.ClassIds
import hu.simplexion.z2.kotlin.schematic.Names
import hu.simplexion.z2.kotlin.schematic.SchematicPluginKey
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.plugin.createMemberProperty
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirPropertySymbol
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.Name

/**
 * Add declarations for interfaces that extend `SchematicEntityStore`.
 *
 * - property `schematicEntityCompanion` (getter will be added by backend plugin)
 */
class SchematicEntityStoreDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        if (context.owner.resolvedSuperTypeRefs.none { it.type.classId == ClassIds.SCHEMATIC_ENTITY_STORE }) return emptySet()

        return setOf(Names.SCHEMATIC_ENTITY_COMPANION_PROPERTY)
    }

    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        checkNotNull(context)

        val supertype = context.owner.resolvedSuperTypeRefs.firstOrNull { it.type.classId == ClassIds.SCHEMATIC_ENTITY_STORE } ?: return emptyList()
        val schematicType = supertype.type.typeArguments.first()
        val type = ClassIds.SCHEMATIC_ENTITY_COMPANION.constructClassLikeType(arrayOf(schematicType), false)

        return when (callableId.callableName) {

            Names.SCHEMATIC_ENTITY_COMPANION_PROPERTY -> {
                listOf(
                    createMemberProperty(
                        context.owner,
                        SchematicPluginKey,
                        Names.SCHEMATIC_ENTITY_COMPANION_PROPERTY,
                        type,
                        isVal = true,
                        hasBackingField = false
                    ).symbol
                )
            }

            else -> emptyList()
        }
    }

}