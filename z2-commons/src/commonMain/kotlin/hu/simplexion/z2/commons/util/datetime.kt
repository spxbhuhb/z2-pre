package hu.simplexion.z2.commons.util

import kotlinx.datetime.*

fun hereAndNow() : LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())