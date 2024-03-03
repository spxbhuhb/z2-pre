package hu.simplexion.z2.schematic

import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.field.ListSchemaField
import hu.simplexion.z2.util.nextHandle

class SchematicList<ST>(
    override var schematicParent: SchematicNode?,
    val backingList : MutableList<ST>,
    val field: ListSchemaField<ST, *>
) : MutableList<ST>, SchematicNode {

    override val schematicHandle = nextHandle()

    override var schematicListenerCount: Int = 0

    override fun fireEvent(field: SchemaField<*>) {
        TODO("schematic list events")
    }

    override val size: Int
        get() = backingList.size

    fun <T> T.fire(): T {
        schematicParent?.fireEvent(field)
        return this
    }

    fun Boolean.fireOnCondition(): Boolean {
        if (this) schematicParent?.fireEvent(field)
        return this
    }

    override fun clear() {
        backingList.clear()
        fire()
    }

    override fun addAll(elements: Collection<ST>): Boolean {
        return backingList.addAll(elements).fireOnCondition()
    }

    override fun addAll(index: Int, elements: Collection<ST>): Boolean {
        return backingList.addAll(index, elements).fireOnCondition()
    }

    override fun add(index: Int, element: ST) {
        return backingList.add(index, element).fire()
    }

    override fun add(element: ST): Boolean {
        return backingList.add(element).fireOnCondition()
    }

    override fun get(index: Int): ST {
        return backingList[index]
    }

    override fun isEmpty(): Boolean {
        return backingList.isEmpty()
    }

    override fun iterator(): MutableIterator<ST> {
        return backingList.iterator()
    }

    override fun listIterator(): MutableListIterator<ST> {
        return backingList.listIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<ST> {
        return backingList.listIterator(index)
    }

    override fun removeAt(index: Int): ST {
        return backingList.removeAt(index).fire()
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<ST> {
        return backingList.subList(fromIndex, toIndex)
    }

    override fun set(index: Int, element: ST): ST {
        return backingList.set(index, element).fire()
    }

    override fun retainAll(elements: Collection<ST>): Boolean {
        return backingList.retainAll(elements).fireOnCondition()
    }

    override fun removeAll(elements: Collection<ST>): Boolean {
        return backingList.removeAll(elements).fireOnCondition()
    }

    override fun remove(element: ST): Boolean {
        return backingList.remove(element).fireOnCondition()
    }

    override fun lastIndexOf(element: ST): Int {
        return backingList.lastIndexOf(element)
    }

    override fun indexOf(element: ST): Int {
        return backingList.indexOf(element)
    }

    override fun containsAll(elements: Collection<ST>): Boolean {
        return backingList.containsAll(elements)
    }

    override fun contains(element: ST): Boolean {
        return backingList.contains(element)
    }
}