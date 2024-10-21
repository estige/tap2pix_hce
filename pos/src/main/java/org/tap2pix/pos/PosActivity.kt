package org.tap2pix.pos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class PosActivity : AppCompatActivity() {

    private lateinit var tvAmount:TextView
    private lateinit var numberPad:GridLayout

    var tvTimestamp: TextView? = null
    var isTimestamp: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvAmount = findViewById<TextView>(R.id.tvAmount)
        numberPad = findViewById<GridLayout>(R.id.numberPad)

        setNumberPadListeners(numberPad)
    }

    private fun setNumberPadListeners(numberPad: GridLayout) {
        for (i in 0 until numberPad.childCount) {
            val child = numberPad.getChildAt(i)
            if (child is Button) {
                child.setOnClickListener { view ->
                    val button = view as Button
                    handleButtonClick(button.text.toString())
                }
            }
        }
    }

    private fun handleButtonClick(buttonText: String) {

        var currentAmount: String =
            tvAmount.getText().toString().replace("R$", "").replace(",", "").replace(".", "")
                .trim { it <= ' ' }

        if (buttonText == "✓") {

            val total = (currentAmount.toDouble() / 100.0).toString()
            val intent = Intent(this, QrActivity::class.java)
            Log.i("APDU", total )
            intent.putExtra("total_amount", total)
            startActivity(intent)

        } else {

            if (currentAmount == "0,00") {
                currentAmount = ""
            }
            currentAmount += buttonText
            tvAmount.setText("R$ " + formatCurrency(currentAmount))
        }
    }

    // Método para formatar a moeda
    private fun formatCurrency(amount: String): String {
        if (amount.isEmpty()) {
            return "0,00"
        }
        val value = amount.toDouble() / 100.0
        return String.format("%.2f", value).replace('.', ',')
    }
}