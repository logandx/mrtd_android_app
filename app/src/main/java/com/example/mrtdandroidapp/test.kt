//package com.example.mrtdandroidapp
//
//import com.example.mrtdandroidapp.Crt288xurDrv.Crt288x
//
//package com.kienlongbank.mrtd.mrtd_android
//
//import android.util.Log
//import com.example.icreader.NativeReader
//import net.sf.scuba.smartcards.CardService
//import net.sf.scuba.smartcards.CommandAPDU
//import net.sf.scuba.smartcards.ResponseAPDU
//
//class AdapterException(message: String) : Exception(message)
//
//class Adapter : CardService() {
//
//    private var isOpened: Boolean = false
//
//    private fun ByteArray.toHex(): String =
//        joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }
//
//    private fun String.decodeHex(): ByteArray {
//        check(length % 2 == 0) { "Must have an even length" }
//
//        return chunked(2)
//            .map { it.toInt(16).toByte() }
//            .toByteArray()
//    }
//
//    override fun open() {
//        return
//    }
//
//    override fun isOpen(): Boolean {
//        return isOpened
//    }
//
//    override fun transmit(commandAPDU: CommandAPDU): ResponseAPDU {
//        try {
//            val inputChipData = commandAPDU.bytes
//            val intOutChipDataLength = IntArray(3)
//            val byteOutChipData = ByteArray(4048)
//            val ulInChipDataLength = inputChipData.size
//
//            val cardStatus = IntArray(
//                1)
//
//            val cardStatusRequestResponse =
//                NativeReader.GetCardState(NativeReader.icdev, 0x31, cardStatus)
//
//            // Log.i("APDU", "APDU:  ${cardStatus[0]}  $cardStatusRequestResponse")
//            ProcessPercent.getInstance().increaseIndex()
//
//            when {
//                cardStatus[0] == 0 -> {
//                    throw AdapterException("00000 - Nothing but success")
//                }
//
//                cardStatus[0] == 12291 -> {
//                    // Log.d("APDU", "12291 - Success")
//                }
//
//                cardStatus[0] == 12290 -> {
//                    throw AdapterException("12290 - No Card")
//                }
//
//                cardStatus[0] == 12292 -> {
//                    throw AdapterException("12290 - Card Power Off")
//                }
//
//                cardStatus[0] == 12293 -> {
//                    throw AdapterException("12290 - Fail to Power On Card")
//                }
//
//                cardStatus[0] == 12294 -> {
//                    throw AdapterException("12290 - APDU Timeout")
//                }
//            }
//
//            val responseCommand = Crt288x(
//                NativeReader.icdev,
//                0x31,
//                ulInChipDataLength,
//                inputChipData,
//                intOutChipDataLength,
//                byteOutChipData
//            )
//
//            if(responseCommand != 0) {
//                throw  AdapterException("00000 - response Command $responseCommand")
//            }
//            val hexData = byteOutChipData.toHex()
//            val data = hexData.substring(0, intOutChipDataLength[0] * 2)
//            val apduBytes = ByteArray(intOutChipDataLength[0] + 2)
//            System.arraycopy(
//                byteOutChipData.copyOfRange(2, 2 + intOutChipDataLength[0]),
//                0,
//                apduBytes,
//                0,
//                intOutChipDataLength[0]
//            )
//            apduBytes[intOutChipDataLength[0]] = byteOutChipData[0]
//            apduBytes[intOutChipDataLength[0] + 1] = byteOutChipData[1]
//
//            return ResponseAPDU(data.decodeHex())
//        } catch (e: Exception) {
//            if(e is AdapterException) {
//                Log.i("Adapter", e.message.toString())
//            }
//            val errorResponse = ByteArray(2, init = {0})
//            return ResponseAPDU(errorResponse)
//        }
//    }
//
//    override fun getATR(): ByteArray {
//        return byteArrayOf()
//    }
//
//    override fun close() {
//        return
//    }
//
//    override fun isConnectionLost(e: Exception?): Boolean {
//        return !isOpened
//    }
//}
