package hu.simplexion.z2.email.model

enum class EmailStatus {
    Preparation,
    SendWait,
    Sent,
    RetryWait,
    Failed
}