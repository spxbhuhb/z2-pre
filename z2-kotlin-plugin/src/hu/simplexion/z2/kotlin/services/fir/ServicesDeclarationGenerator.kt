package hu.simplexion.z2.kotlin.services.fir

import hu.simplexion.z2.kotlin.services.ServicesPluginKey
import hu.simplexion.z2.kotlin.services.ir.SERVICE_INTERFACE_FULL
import hu.simplexion.z2.kotlin.services.ir.SERVICE_INTERFACE_SHORT
import hu.simplexion.z2.kotlin.services.ir.SERVICE_NAME_PROPERTY
import hu.simplexion.z2.kotlin.services.ir.serviceConsumerName
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
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
import org.jetbrains.kotlin.text

class ServicesDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    companion object {
        val serviceInterfaceNames = listOf(SERVICE_INTERFACE_SHORT.asString, SERVICE_INTERFACE_FULL.asString)

        val serviceConsumerClasses = mutableMapOf<ClassId, ServiceConsumerClass>()
    }

    class ServiceConsumerClass(
        val serviceInterface: ClassId,
        val functionNames: List<Name>
    )

    @OptIn(SymbolInternals::class)
    override fun getNestedClassifiersNames(classSymbol: FirClassSymbol<*>, context: NestedClassGenerationContext): Set<Name> {
        if (classSymbol.fir.classKind != ClassKind.INTERFACE) return emptySet()

        if (classSymbol.fir.superTypeRefs.none { it.source.text?.trim() in serviceInterfaceNames }) return emptySet()

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
            SERVICE_NAME_PROPERTY.asName,
            SpecialNames.INIT
        ) + declarations.functionNames
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        return listOf(
            createConstructor(context.owner, ServicesPluginKey, isPrimary = true, generateDelegatedNoArgConstructorCall = true).symbol
        )
    }

    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        if (! isThisPlugin(context)) return emptyList()
        if (callableId.callableName != SERVICE_NAME_PROPERTY.asName) return emptyList()

        return listOf(
            createMemberProperty(context !!.owner, ServicesPluginKey, callableId.callableName, session.builtinTypes.stringType.coneType, isVal = false).symbol
        )
    }

    override fun generateFunctions(callableId: CallableId, context: MemberGenerationContext?): List<FirNamedFunctionSymbol> {
        if (! isThisPlugin(context)) return emptyList()

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

    fun isThisPlugin(context: MemberGenerationContext?): Boolean {
        val owner = context?.owner ?: return false
        val ownerKey = (owner.origin as? FirDeclarationOrigin.Plugin)?.key ?: return false
        return ownerKey == ServicesPluginKey
    }

}