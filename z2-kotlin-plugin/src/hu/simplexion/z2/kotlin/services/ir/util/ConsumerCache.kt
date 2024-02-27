package hu.simplexion.z2.kotlin.services.ir.util

import hu.simplexion.z2.kotlin.services.ir.COMPANION_OBJECT_NAME
import hu.simplexion.z2.kotlin.services.ir.ServicesPluginContext
import org.jetbrains.kotlin.ir.backend.js.utils.asString
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

class ConsumerCache(
    val pluginContext: ServicesPluginContext
) {

    val consumers = mutableMapOf<IrType, IrClass>()

    operator fun get(type: IrType) =
        consumers.getOrPut(type) { add(type) }

    fun add(type: IrType): IrClass {
        val typeFqName = checkNotNull(type.classFqName) { "classFqName is null for ${type.asString()}"}
        val classId = ClassId(typeFqName.parent(), typeFqName.shortName()).createNestedClassId(Name.identifier(COMPANION_OBJECT_NAME))
        return checkNotNull(pluginContext.irContext.referenceClass(classId)) { "companion is missing for $classId"}.owner
    }

    fun add(type : IrType, consumer: IrClass) {
        consumers[type] = consumer
    }

}