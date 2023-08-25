package hu.simplexion.z2.history.model

object HistoryFlags {
    const val SECURITY = 0x1
    const val TECHNICAL = 0x2
    const val ERROR = 0x04
    const val BUSINESS = 0x08
    const val SETTING = 0x10
    const val ALL = 0xff
}