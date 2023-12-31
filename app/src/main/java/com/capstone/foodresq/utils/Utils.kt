package com.capstone.foodresq.utils

import android.graphics.Bitmap
import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import java.util.EnumMap

object Utils {

    fun isValidEmail(email : String) : Boolean{
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password : String) : Boolean{
        return !TextUtils.isEmpty(password) && password.length >= 8
    }

    fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T) {
                observer(t)
                removeObserver(this)
            }
        })
    }

    fun generateQrCode(text: String, width: Int, height: Int): Bitmap? {
        val hints: MutableMap<EncodeHintType, Any> = EnumMap(EncodeHintType::class.java)
        hints[EncodeHintType.MARGIN] = 0
        val writer = MultiFormatWriter()

        try {
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height, hints)
            val qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    qrCodeBitmap.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
                }
            }

            return qrCodeBitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        return null
    }

    fun formatPrice(amount: String): String {
        try {
            val value = amount.toInt()
            return "Rp${value / 1000}k"
        } catch (e: NumberFormatException) {
            return amount
        }
    }


}