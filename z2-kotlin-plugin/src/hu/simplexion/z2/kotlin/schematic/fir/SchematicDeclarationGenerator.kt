package hu.simplexion.z2.kotlin.schematic.fir

import hu.simplexion.z2.kotlin.schematic.ClassIds
import hu.simplexion.z2.kotlin.schematic.Names
import hu.simplexion.z2.kotlin.schematic.SchematicPluginKey
import hu.simplexion.z2.kotlin.schematic.Strings
import hu.simplexion.z2.kotlin.util.superTypeContains
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.utils.isCompanion
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.extensions.NestedClassGenerationContext
import org.jetbrains.kotlin.fir.plugin.createCompanionObject
import org.jetbrains.kotlin.fir.plugin.createConstructor
import org.jetbrains.kotlin.fir.plugin.createMemberProperty
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.fir.types.*
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.text

/**
 * Add declarations for schematic classes (classes that extend `Schematic` or `SchematicEntity`).
 *
 * - a companion object
 *   - implements `SchematicCompanion<T>` where `T` is the schematic class (added as supertype)
 *   - has an empty constructor
 *   - has a `schematicFqName : String` property
 *   - has a `schematicSchema: Schema<T>` property
 */
class SchematicDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    companion object {
        // class id of schematic class T : Schema<T> type
        val schemaTypes = mutableMapOf<ClassId, ConeClassLikeType>()
    }

    override fun getNestedClassifiersNames(classSymbol: FirClassSymbol<*>, context: NestedClassGenerationContext): Set<Name> =

        if (classSymbol.superTypeContains(Strings.SCHEMATIC_PREFIX, Strings.SCHEMATIC_ENTITY_PREFIX)) {
            schemaTypes[classSymbol.classId] = ClassIds.SCHEMATIC_SCHEMA.constructClassLikeType(arrayOf(classSymbol.defaultType()), false)
            setOf(SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT)
        } else {
            emptySet()
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
        if (owner.companionObjectSymbol != null || owner.isCompanion) return null

        val companion = createCompanionObject(owner, SchematicPluginKey) {
            superType(ClassIds.SCHEMATIC_COMPANION.constructClassLikeType(arrayOf(owner.defaultType()), false))
        }

        return companion.symbol
    }

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        if (! classSymbol.isCompanion) return emptySet()

        val result = mutableSetOf<Name>()
        if (classSymbol.isSchematicCompanion) {
            result += SpecialNames.INIT
            result += Names.SCHEMATIC_FQNAME_PROPERTY
            result += Names.SCHEMATIC_SCHEMA_PROPERTY
        }
        return result
    }

    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        if (! context.isSchematicCompanion) return emptyList()
        checkNotNull(context)

        return when (callableId.callableName) {

            Names.SCHEMATIC_FQNAME_PROPERTY -> {
                listOf(
                    createMemberProperty(
                        context.owner,
                        SchematicPluginKey,
                        Names.SCHEMATIC_FQNAME_PROPERTY,
                        session.builtinTypes.stringType.coneType,
                        isVal = true,
                        hasBackingField = false
                    ).symbol
                )
            }

            Names.SCHEMATIC_SCHEMA_PROPERTY -> {
                val schematicClassId = context.owner.classId.outerClassId
                val schemaType = requireNotNull(schemaTypes[schematicClassId])

                listOf(
                    createMemberProperty(
                        context.owner,
                        SchematicPluginKey,
                        Names.SCHEMATIC_SCHEMA_PROPERTY,
                        schemaType,
                        isVal = true,
                        hasBackingField = true
                    ).symbol
                )
            }

            else -> emptyList()
        }
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        if (! context.isSchematicCompanion) return emptyList()
        val constructor = createConstructor(context.owner, SchematicPluginKey, isPrimary = true, generateDelegatedNoArgConstructorCall = true)
        return listOf(constructor.symbol)
    }

    // ----  Conditions  -------------------------------

    // FIXME [PLUGIN] dirty recognition of schematic companion
    val FirTypeRef.isSchematicCompanion: Boolean
        get() {
            return if (this is FirResolvedTypeRef) {
                this.type.toString().contains("SchematicCompanion<")
            } else {
                source.text?.contains("SchematicCompanion<") == true
            }
        }

    @OptIn(SymbolInternals::class)
    val FirClassSymbol<*>.isSchematicCompanion: Boolean
        get() = fir.superTypeRefs.any { it.isSchematicCompanion }

    val MemberGenerationContext?.isSchematicCompanion: Boolean
        get() = (this != null && owner.isCompanion && owner.isSchematicCompanion) // FIXME [PLUGIN] dirty recognition of schematic companion

}