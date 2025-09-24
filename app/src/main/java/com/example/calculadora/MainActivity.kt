package com.example.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadora.ui.theme.CalculadoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorApp()
                }
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var displayValue by remember { mutableStateOf("0") }
    var operand1 by remember { mutableStateOf<Double?>(null) }
    var operator by remember { mutableStateOf<String?>(null) }
    var isNewNumber by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = displayValue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            fontSize = 48.sp,
            textAlign = TextAlign.End
        )

        Spacer(Modifier.height(16.dp))

        // Grid de botones
        val buttonLabels = listOf(
            "AC", "DEL", "/", "x",
            "7", "8", "9", "-",
            "4", "5", "6", "+",
            "1", "2", "3", "=",
            "0", "."
        )

        val gridRows = buttonLabels.chunked(4)

        gridRows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                row.forEach { label ->
                    Button(
                        onClick = {
                            when (label) {
                                in "0".."9", "." -> {
                                    if (isNewNumber) {
                                        displayValue = label
                                        isNewNumber = false
                                    } else {
                                        displayValue += label
                                    }
                                }
                                "AC" -> {
                                    displayValue = "0"
                                    operand1 = null
                                    operator = null
                                    isNewNumber = true
                                }
                                "DEL" -> {
                                    if (displayValue.length > 1) {
                                        displayValue = displayValue.dropLast(1)
                                    } else {
                                        displayValue = "0"
                                    }
                                }
                                "=", "+", "-", "x", "/" -> {
                                    if (operator != null) {
                                        val operand2 = displayValue.toDoubleOrNull() ?: 0.0
                                        val result = when (operator) {
                                            "+" -> operand1!! + operand2
                                            "-" -> operand1!! - operand2
                                            "x" -> operand1!! * operand2
                                            "/" -> if (operand2 != 0.0) operand1!! / operand2 else Double.NaN
                                            else -> Double.NaN
                                        }
                                        displayValue = if (result.isNaN()) "Error" else result.toString()
                                        operand1 = result
                                        operator = null
                                    } else {
                                        operand1 = displayValue.toDoubleOrNull()
                                        operator = label
                                    }
                                    isNewNumber = true
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .aspectRatio(1f) // Para que los botones sean cuadrado
                    ) {
                        Text(label)
                    }
                }
            }
        }
    }
}