package hu.simplexion.z2.content.impl

import hu.simplexion.z2.content.model.Content
import hu.simplexion.z2.content.model.ContentStatus
import hu.simplexion.z2.util.UUID

typealias UploadStatusCallback = (contentUuid: UUID<Content>, status: ContentStatus) -> Unit