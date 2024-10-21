package org.tap2pix.pos

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import org.tap2pix.pos.Tap2PixApduService.Companion

class QrActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_qr)

        val total:String? = intent.getStringExtra("total_amount")

        postPos(total!!)

        // Aplicar preenchimento para as barras de sistema (status e navegação)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun generateQRCode(text: String): Bitmap {
        val width = 320
        val height = 320
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix =
                codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val color = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                    bitmap.setPixel(x, y, color)
                }
            }
        } catch (e: WriterException) {

            Log.d(TAG, "generateQRCode: ${e.message}")

        }
        return bitmap
    }

    private fun onCreateQr(qr:String){
        // Carregar o arquivo de logo da pasta assets
        val logoBytes = try {
            assets.open("logo_icon.png").readBytes()
        } catch (e: IOException) {
            Log.e("QRCodeError", "Erro ao carregar logo dos assets", e)
            ByteArray(0) // Se não conseguir carregar, retorna um array vazio
        }

        // Configurar o ImageView para exibir o QR Code
        val qrView = findViewById<ImageView>(R.id.paymentQr)
        val progressBar = findViewById<ProgressBar>(R.id.imageProgressBar)

        val bitmap = generateQRCode(qr)
        qrView.setImageBitmap(bitmap)

        qrView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun onSendNFC(qr: String){
       val intent = Intent(this, Tap2PixApduService::class.java)
       intent.putExtra("ndefUrl", qr)
       startService(intent)

        Log.d((Tap2PixApduService.TAG), "responseApdu: $qr")
    }

    private fun postPos(total: String) {

        val dataExmple = "https://tap2pix.app/?qr=$total"

        onSendNFC(dataExmple)
    }
}