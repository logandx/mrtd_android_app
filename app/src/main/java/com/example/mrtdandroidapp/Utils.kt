package com.example.mrtdandroidapp

class Utils {
     fun toHex( value: ByteArray): String =
         value.joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

     fun decodeHex(value: String): ByteArray {
        check(value.length % 2 == 0) { "Must have an even length" }
        return value.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }

}