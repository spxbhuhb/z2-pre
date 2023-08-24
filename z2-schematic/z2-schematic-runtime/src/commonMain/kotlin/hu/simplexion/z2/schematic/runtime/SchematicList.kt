package hu.simplexion.z2.schematic.runtime

import hu.simplexion.z2.commons.event.EventCentral
import hu.simplexion.z2.commons.util.nextHandle
import hu.simplexion.z2.schematic.runtime.schema.ListSchemaField

class SchematicList<ST>(
    val backingList : MutableList<ST>,
    val field: ListSchemaField<ST>
) : MutableList<ST> {

    /**
     * The unique handle of this schematic list instance.
     */
    val handle = nextHandle()

    override val size: Int
        get() = backingList.size

    override fun clear() {
        backingList.clear()
        EventCentral.fire(SchematicListEvent(handle, this, field))
    }

    fun Boolean.fireIf(): Boolean {
        EventCentral.fire(SchematicListEvent(handle, this@SchematicList, field))
        return this
    }

    override fun addAll(elements: Collection<ST>): Boolean {
        return backingList.addAll(elements).fireIf()
    }

    override fun addAll(index: Int, elements: Collection<ST>): Boolean {
        return backingList.addAll(index, elements).fireIf()
    }

    override fun add(index: Int, element: ST) {
        return backingList.add(index, element).also {
            EventCentral.fire(SchematicListEvent(handle, this, field))
        }
    }

    override fun add(element: ST): Boolean {
        return backingList.add(element).fireIf()
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
        return backingList.removeAt(index).also {
            EventCentral.fire(SchematicListEvent(handle, this, field))
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<ST> {
        return backingList.subList(fromIndex, toIndex)
    }

    override fun set(index: Int, element: ST): ST {
        return backingList.set(index, element).also {
            EventCentral.fire(SchematicListEvent(handle, this, field))
        }
    }

    override fun retainAll(elements: Collection<ST>): Boolean {
        return backingList.retainAll(elements).fireIf()
    }

    override fun removeAll(elements: Collection<ST>): Boolean {
        return backingList.removeAll(elements).fireIf()
    }

    override fun remove(element: ST): Boolean {
        return backingList.remove(element).fireIf()
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