package hu.simplexion.z2.content.impl

import hu.simplexion.z2.content.model.Content
import java.nio.file.Path

/**
 * Places all content files into the specified directory.
 */
class BasicPlacementStrategy(
    val directory : Path
) : ContentPlacementStrategy {
    override fun dataPathOf(content : Content) : Path = directory.resolve(content.uuid.toString() + ".blob")
    override fun statusPathOf(content : Content) : Path = directory.resolve(content.uuid.toString() + ".status")
}