package hu.simplexion.z2.site.boot

import hu.simplexion.z2.browser.material.snackbar.errorSnackbar
import hu.simplexion.z2.localization.text.commonStrings
import hu.simplexion.z2.service.ktor.client.BasicWebSocketServiceTransport
import hu.simplexion.z2.service.transport.ResponseEnvelope

class SnackbarWebSocketTransport(
    path : String,
    trace : Boolean
) : BasicWebSocketServiceTransport(
    path,
    trace
) {
    override fun connectionError(ex: Exception) {
        errorSnackbar(commonStrings.communicationError)
    }

    override fun timeoutError(call: OutgoingCall) {
        errorSnackbar(commonStrings.timeoutError)
    }

    override fun responseError(serviceName: String, funName: String, responseEnvelope: ResponseEnvelope) {
        errorSnackbar(commonStrings.responseError)
    }

}