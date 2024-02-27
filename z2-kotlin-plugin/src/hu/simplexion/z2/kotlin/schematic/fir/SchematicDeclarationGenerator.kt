package hu.simplexion.z2.kotlin.schematic.fir

import hu.simplexion.z2.kotlin.schematic.RUNTIME_PACKAGE
import hu.simplexion.z2.kotlin.schematic.SCHEMATIC_CLASS
import hu.simplexion.z2.kotlin.schematic.SCHEMATIC_COMPANION_CLASS
import hu.simplexion.z2.kotlin.schematic.SCHEMATIC_FQNAME_PROPERTY
import org.jetbrains.kotlin.GeneratedDeclarationKey
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
import org.jetbrains.kotlin.fir.types.FirResolvedTypeRef
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.name.*
import org.jetbrains.kotlin.text

class SchematicDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    companion object {
        val SCHEMATIC_CLASS_ID = ClassId(FqName(RUNTIME_PACKAGE), Name.identifier(SCHEMATIC_CLASS))

        val SCHEMATIC_COMPANION_CLASS_ID = ClassId(FqName(RUNTIME_PACKAGE), Name.identifier(SCHEMATIC_COMPANION_CLASS))

        val SCHEMATIC_FQNAME_NAME = Name.identifier(SCHEMATIC_FQNAME_PROPERTY)

        object Key : GeneratedDeclarationKey()
    }

    @OptIn(SymbolInternals::class)
    override fun getNestedClassifiersNames(classSymbol: FirClassSymbol<*>, context: NestedClassGenerationContext): Set<Name> {
        val result = mutableSetOf<Name>()
        if (classSymbol.fir.superTypeRefs.any { it.source.text?.contains("Schematic<") == true }) {
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

    @OptIn(SymbolInternals::class)
    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        if (! classSymbol.isCompanion) return emptySet()

        val result = mutableSetOf<Name>()
        if (classSymbol.isSchematicCompanion) {
            result += SpecialNames.INIT
            result += SCHEMATIC_FQNAME_NAME
        }
        return result
    }

    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        if (!context.isSchematicCompanion) return emptyList()
        checkNotNull(context)

        if (callableId.callableName != SCHEMATIC_FQNAME_NAME) return emptyList()

        val property = createMemberProperty(context.owner, Key, SCHEMATIC_FQNAME_NAME, session.builtinTypes.stringType.coneType, isVal = true, hasBackingField = true)

        return listOf(property.symbol)
    }

    private fun generateCompanionDeclaration(owner: FirRegularClassSymbol): FirRegularClassSymbol? {
        if (owner.companionObjectSymbol != null || owner.isCompanion) return null
        val companion = createCompanionObject(owner, Key) {
            superType(SCHEMATIC_COMPANION_CLASS_ID.constructClassLikeType(arrayOf(owner.defaultType()), false))
        }
        return companion.symbol
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        if (!context.isSchematicCompanion) return emptyList()
        val constructor = createConstructor(context.owner, Key, isPrimary = true, generateDelegatedNoArgConstructorCall = true)
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