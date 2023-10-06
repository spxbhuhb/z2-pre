package hu.simplexion.z2.worker.ui

import hu.simplexion.z2.commons.localization.icon.LocalizedIconProvider

object workerIcons : WorkerIcons

interface WorkerIcons : LocalizedIconProvider {
    val worker get() = static("forward_circle")
}