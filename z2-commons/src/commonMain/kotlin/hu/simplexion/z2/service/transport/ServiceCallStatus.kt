package hu.simplexion.z2.service.transport

enum class ServiceCallStatus(
    val value: Int
) {
    Ok(0),
    ServiceNotFound(1),
    FunctionNotFound(2),
    Exception(3),
    Timeout(4),
    AccessDenied(5),
    AuthenticationFail(6),
    AuthenticationFailLocked(7)
}