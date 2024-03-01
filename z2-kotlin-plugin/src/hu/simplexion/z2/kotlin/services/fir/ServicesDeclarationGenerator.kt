package hu.simplexion.z2.kotlin.services.fir

import hu.simplexion.z2.kotlin.services.Names
import hu.simplexion.z2.kotlin.services.ServicesPluginKey
import hu.simplexion.z2.kotlin.services.Strings
import hu.simplexion.z2.kotlin.services.serviceConsumerName
import hu.simplexion.z2.kotlin.util.isFromPlugin
import hu.simplexion.z2.kotlin.util.superTypeContains
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.extensions.NestedClassGenerationContext
import org.jetbrains.kotlin.fir.plugin.createConstructor
import org.jetbrains.kotlin.fir.plugin.createMemberFunction
import org.jetbrains.kotlin.fir.plugin.createMemberProperty
import org.jetbrains.kotlin.fir.plugin.createNestedClass
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.resolve.providers.getClassDeclaredFunctionSymbols
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

/**
 * Add declarations for service interfaces (interfaces that extend `Service`).
 *
 * - a nested class with name `<service-interface-name>$Consumer`:
 *   - implements the service interface (added as supertype)
 *   - has an empty constructor
 *   - has a `serviceFqName : String` property
 *   - has overrides for all functions defined in the service interface
 */
class ServicesDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    companion object {
        val serviceConsumerClasses = mutableMapOf<ClassId, ServiceConsumerClass>()
    }

    class ServiceConsumerClass(
        val serviceInterface: ClassId,
        val functionNames: List<Name>
    )

    @OptIn(SymbolInternals::class)
    override fun getNestedClassifiersNames(classSymbol: FirClassSymbol<*>, context: NestedClassGenerationContext): Set<Name> {
        if (classSymbol.fir.classKind != ClassKind.INTERFACE) return emptySet()

        if (! classSymbol.superTypeContains(Strings.SERVICE_INTERFACE, Strings.SERVICE_INTERFACE_FQ)) {
            return emptySet()
        }

        val consumerName = classSymbol.name.serviceConsumerName
        val classId = classSymbol.classId.createNestedClassId(consumerName)

        serviceConsumerClasses[classId] = ServiceConsumerClass(
            classSymbol.classId,
            classSymbol.declarationSymbols.filterIsInstance<FirNamedFunctionSymbol>().map { it.name }
        )

        return setOf(consumerName)
    }

    override fun generateNestedClassLikeDeclaration(
        owner: FirClassSymbol<*>,
        name: Name,
        context: NestedClassGenerationContext
    ): FirClassLikeSymbol<*>? {

        if (name != owner.name.serviceConsumerName) return null

        val firClass = createNestedClass(owner, name, ServicesPluginKey) {
            superType(owner.defaultType())
        }

        return firClass.symbol
    }

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        val declarations = serviceConsumerClasses[classSymbol.classId] ?: return emptySet()

        return setOf(
            Names.SERVICE_NAME_PROPERTY,
            SpecialNames.INIT
        ) + declarations.functionNames
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        return listOf(
            createConstructor(context.owner, ServicesPluginKey, isPrimary = true, generateDelegatedNoArgConstructorCall = true).symbol
        )
    }

    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        if (context.isForeign) return emptyList()
        if (callableId.callableName != Names.SERVICE_NAME_PROPERTY) return emptyList()

        return listOf(
            createMemberProperty(context !!.owner, ServicesPluginKey, callableId.callableName, session.builtinTypes.stringType.coneType, isVal = false).symbol
        )
    }

    override fun generateFunctions(callableId: CallableId, context: MemberGenerationContext?): List<FirNamedFunctionSymbol> {
        if (context.isForeign) return emptyList()

        val serviceConsumerClasses = serviceConsumerClasses[callableId.classId] ?: return emptyList()

        val functionName = callableId.callableName
        val interfaceFunctions = session.symbolProvider.getClassDeclaredFunctionSymbols(serviceConsumerClasses.serviceInterface, functionName)

        return interfaceFunctions.map { interfaceFunction ->
            createMemberFunction(context !!.owner, ServicesPluginKey, callableId.callableName, interfaceFunction.resolvedReturnType) {
                status {
                    isSuspend = true
                }
                interfaceFunction.valueParameterSymbols.forEach { valueParameterSymbol ->
                    valueParameter(
                        valueParameterSymbol.name,
                        valueParameterSymbol.resolvedReturnType
                    )
                }
            }.symbol
        }
    }

    val MemberGenerationContext?.isForeign
        get() = ! isFromPlugin(ServicesPluginKey)
}