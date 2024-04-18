package hu.simplexion.z2.content.impl

import hu.simplexion.z2.content.model.Content
import java.nio.file.Path

interface ContentPlacementStrategy {
    fun dataPathOf(content : Content) : Path
    fun statusPathOf(content : Content) : Path
}