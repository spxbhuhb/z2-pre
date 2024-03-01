package hu.simplexion.z2.kotlin.schematic.fir

import hu.simplexion.z2.kotlin.schematic.ClassIds
import hu.simplexion.z2.kotlin.schematic.Names
import hu.simplexion.z2.kotlin.schematic.SchematicPluginKey
import hu.simplexion.z2.kotlin.schematic.Strings
import hu.simplexion.z2.kotlin.util.isFromPlugin
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
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.fir.types.ConeClassLikeType
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

/**
 * Add declarations for schematic classes (classes that extend `Schematic` or `SchematicEntity`).
 *
 * - a companion object
 *   - implements `SchematicCompanion<T>` or `SchematicEntityCompanion<T>` where `T` is the schematic class (added as supertype)
 *   - has an empty constructor
 *   - has a `schematicFqName : String` property
 *   - has a `schematicSchema: Schema<T>` property
 *   - has a `schematicStore: SchematicEntityStore<T>` property if it is a `SchematicEntity`
 */
class SchematicDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    companion object {
        // class id of schematic class T : Schema<T> type
        val classDataBySchematic = mutableMapOf<ClassId, ClassData>()
    }

    class ClassData(
        val entity: Boolean,
        val schemaType: ConeClassLikeType,
        val storeType: ConeClassLikeType?
    )

    override fun getNestedClassifiersNames(classSymbol: FirClassSymbol<*>, context: NestedClassGenerationContext): Set<Name> {

        val isSchematic = classSymbol.superTypeContains(Strings.SCHEMATIC_PATTERN, Strings.SCHEMATIC_ENTITY_PATTERN)
        if (! isSchematic) return emptySet()

        val isEntity = classSymbol.superTypeContains(Strings.SCHEMATIC_ENTITY_PATTERN)

        val schemaType = ClassIds.SCHEMATIC_SCHEMA.constructClassLikeType(arrayOf(classSymbol.defaultType()), false)

        val storeType = if (isEntity) {
            ClassIds.SCHEMATIC_ENTITY_STORE_CLASS.constructClassLikeType(arrayOf(classSymbol.defaultType()), false)
        } else {
            null
        }

        classDataBySchematic[classSymbol.classId] = ClassData(isEntity, schemaType, storeType)

        return setOf(SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT)
    }


    override fun generateNestedClassLikeDeclaration(
        owner: FirClassSymbol<*>,
        name: Name,
        context: NestedClassGenerationContext
    ): FirClassLikeSymbol<*>? {
        if (owner.classId !in classDataBySchematic || owner !is FirRegularClassSymbol) return null

        return when (name) {
            SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT -> generateCompanionDeclaration(owner)
            else -> error("Can't generate class ${owner.classId.createNestedClassId(name).asSingleFqName()}")
        }
    }

    private fun generateCompanionDeclaration(owner: FirRegularClassSymbol): FirRegularClassSymbol? {
        if (owner.companionObjectSymbol != null || owner.isCompanion) return null

        val classData = classDataBySchematic[owner.classId] ?: return null

        val superType = if (classData.entity) {
            ClassIds.SCHEMATIC_ENTITY_COMPANION
        } else {
            ClassIds.SCHEMATIC_COMPANION
        }

        val companion = createCompanionObject(owner, SchematicPluginKey) {
            superType(superType.constructClassLikeType(arrayOf(owner.defaultType()), false))
        }

        return companion.symbol
    }

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        val classData = context.classData ?: return emptySet()

        val result = mutableSetOf<Name>()

        result += SpecialNames.INIT
        result += Names.SCHEMATIC_FQNAME_PROPERTY
        result += Names.SCHEMATIC_SCHEMA_PROPERTY

        if (classData.entity) result += Names.SCHEMATIC_STORE_PROPERTY

        return result
    }

    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        val classData = context.classData ?: return emptyList()
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
                listOf(
                    createMemberProperty(
                        context.owner,
                        SchematicPluginKey,
                        Names.SCHEMATIC_SCHEMA_PROPERTY,
                        classData.schemaType,
                        isVal = true,
                        hasBackingField = true
                    ).symbol
                )
            }

            Names.SCHEMATIC_STORE_PROPERTY -> {
                listOf(
                    createMemberProperty(
                        context.owner,
                        SchematicPluginKey,
                        Names.SCHEMATIC_STORE_PROPERTY,
                        classData.storeType!!,
                        isVal = false,
                        hasBackingField = true
                    ).symbol
                )
            }

            else -> emptyList()
        }
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        if (context.isForeign) return emptyList()
        val constructor = createConstructor(context.owner, SchematicPluginKey, isPrimary = true, generateDelegatedNoArgConstructorCall = true)
        return listOf(constructor.symbol)
    }

    val MemberGenerationContext?.classData
        get() = this?.let { classDataBySchematic[owner.classId.outerClassId] }

    val MemberGenerationContext?.isForeign
        get() = ! isFromPlugin(SchematicPluginKey)
}