package hu.simplexion.z2.browser.demo.pages.administration

import hu.simplexion.z2.commons.i18n.LocalizedTextStore
import hu.simplexion.z2.commons.util.UUID

@Suppress("unused")
internal object strings : LocalizedTextStore(UUID("c44188ac-8ae0-4fe3-8aad-1bca38c3418b")) {
    val administration by "Administration"

    val account by "Accounts"
    val accountSupport by account.support("Create, edit, lock and unlock accounts, assign roles, give new password to users.")

    val connection by "Connections"
    val connectionSupport by connection.support("Manage connections to other systems, such as e-mail or database servers.")

    val impressum by "Impressum"
    val impressumSupport by impressum.support("Privacy, terms, etc.")

    val history by "Histories"
    val historySupport by history.support("Security, technical and business level event histories.")

    val role by "Roles"
    val roleSupport by role.support("List top-level user roles, create new ones, list users by role.")

    val securityPolicy by "Security Policy"
    val securityPolicySupport by securityPolicy.support("Set the security policy. Allowed login attempts, password strength.")

    val template by "Templates"
    val templateSupport by template.support("Templates for outgoing e-mails, generated documents.")

}