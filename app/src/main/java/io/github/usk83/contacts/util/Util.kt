package io.github.usk83.contacts.util

fun normalizeString(str: String): String {
    return str.trim().replace("\\s+".toRegex(), " ")
}
