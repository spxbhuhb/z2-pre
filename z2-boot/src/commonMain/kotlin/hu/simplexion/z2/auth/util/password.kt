package hu.simplexion.z2.auth.util

import hu.simplexion.z2.auth.model.PasswordCheck
import hu.simplexion.z2.auth.model.SecurityPolicy

fun SecurityPolicy.policyErrors(newPassword: String): MutableList<PasswordCheck> {
    val errors = mutableListOf<PasswordCheck>()

    if (passwordLengthMinimum != 0 && newPassword.length < passwordHistoryLength) errors += PasswordCheck.Length
    if (uppercaseMinimum != 0 && newPassword.count { it.isUpperCase() } < uppercaseMinimum) errors += PasswordCheck.Uppercase
    if (digitMinimum != 0 && newPassword.count { it.isDigit() } < digitMinimum) errors += PasswordCheck.Digit
    if (specialCharacterMinimum != 0 && newPassword.count { ! it.isLetterOrDigit() } < specialCharacterMinimum) errors += PasswordCheck.Special

    if (sameCharacterMaximum != 0) {
        val charMap = mutableMapOf<Char, Int>()
        for (char in newPassword.toCharArray()) {
            val count = charMap.getOrPut(char) { 0 } + 1
            charMap[char] = count
            if (count > sameCharacterMaximum) {
                errors += PasswordCheck.Same
                break
            }
        }
    }

    val entropyValue = calculatePasswordEntropy(newPassword)
    if (entropyValue < minEntropy.min) errors += PasswordCheck.Strength

    return errors
}