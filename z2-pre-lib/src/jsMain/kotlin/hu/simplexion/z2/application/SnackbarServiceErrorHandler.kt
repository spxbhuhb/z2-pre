package hu.simplexion.z2.application

import hu.simplexion.z2.browser.material.snackbar.errorSnackbar
import hu.simplexion.z2.browser.material.snackbar.warningSnackbar
import hu.simplexion.z2.localization.text.commonStrings
import hu.simplexion.z2.services.transport.ResponseEnvelope
import hu.simplexion.z2.services.transport.ServiceCallStatus
import hu.simplexion.z2.services.transport.ServiceErrorHandler

/**
 * Shows a snackbar when a service call results in an error. Except for:
 *
 * - [ServiceCallStatus.AuthenticationFail]
 * - [ServiceCallStatus.AuthenticationFailLocked]
 */
class SnackbarServiceErrorHandler : ServiceErrorHandler() {

    override fun connectionError(ex: Exception) {
        errorSnackbar(commonStrings.communicationError)
    }

    override fun callError(serviceName: String, funName: String, responseEnvelope: ResponseEnvelope) {
        when (responseEnvelope.status) {
            ServiceCallStatus.AuthenticationFail,
            ServiceCallStatus.AuthenticationFailLocked -> {
                Unit // these should be handled by the login dialog
            }

            ServiceCallStatus.Timeout -> {
                errorSnackbar(commonStrings.timeoutError)
            }

            ServiceCallStatus.Logout -> {
                // nothing to do here, the logout functions should redirect the user
            }

            ServiceCallStatus.InvalidSession -> {
                warningSnackbar(commonStrings.expiredSessionRedirectToLogin)
            }

            ServiceCallStatus.AccessDenied -> {
                errorSnackbar(commonStrings.expiredSession)
            }

            else -> {
                println(responseEnvelope.dump())
                errorSnackbar(commonStrings.responseError)
            }
        }
    }

}