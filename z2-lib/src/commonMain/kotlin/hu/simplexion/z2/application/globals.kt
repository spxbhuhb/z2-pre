package hu.simplexion.z2.application

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.util.UUID

/**
 * The UUID that represents the application itself.
 *
 * It is safe to change this **before** you call any Z2 functions. This value is used
 * during the first time setup of the database, therefore you must keep it the same
 * over separate application runs.
 *
 * [APPLICATION_UUID] is the owner of all application-wide settings.
 */
var APPLICATION_UUID = UUID<Principal>("065ca04c-cd6d-7775-8000-5fa6142b4b7b")


/**
 * The principal name of the built-in security officer user.
 *
 * It is safe to change this **before** you call any Z2 functions. This value is used
 * during the first time setup of the database, therefore you must keep it the same
 * over separate application runs.
 */
var SECURITY_OFFICER_PRINCIPAL_NAME = "so"

/**
 * The UUID of the built-in security officer user.
 *
 * It is safe to change this **before** you call any Z2 functions. This value is used
 * during the first time setup of the database, therefore you must keep it the same
 * over separate application runs.
 */
var SECURITY_OFFICER_UUID = UUID<Principal>("065ca04d-6409-7a05-8000-07efb14cd596")

/**
 * The UUID of the built-in security officer role.
 *
 * It is safe to change this **before** you call any Z2 functions. This value is used
 * during the first time setup of the database, therefore you must keep it the same
 * over separate application runs.
 */
var SECURITY_OFFICER_ROLE_UUID = UUID<Role>("065ca04e-095c-713b-8000-beb6c07f18cd")