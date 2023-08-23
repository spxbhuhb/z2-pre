package hu.simplexion.z2.schematic.runtime.schema

class SchemaFieldNotFound(className : String, fieldName : String) : Exception("schema field not found: $className.$fieldName")

class SchemaFieldIsImmutable(className : String, fieldName : String) : Exception("schema field is immutable: $className.$fieldName")