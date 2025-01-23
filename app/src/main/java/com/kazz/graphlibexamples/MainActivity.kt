package com.kazz.graphlibexamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kazz.graphlibexamples.ui.theme.GraphLibExamplesTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text("Vico graphic lib examples")
                        }
                    )
                }
            ) { innerPadding ->
//                Column(
//                    modifier = Modifier
//                        .padding(innerPadding)
//                        .fillMaxSize()
//                        .verticalScroll(rememberScrollState())
//                ) {
//                    GraphLibExamplesTheme {
//                        BasicLineChart(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 16.dp)
//                        )
//                        GoldPrices(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 16.dp)
//                        )
//                        ElectricCarSales(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 16.dp)
//                        )
//
//                        Spacer(Modifier.padding(54.dp))
//
//                        // FIXME (La librairie ne propose pas d'Horizontal chart.
//                        // c'est une feature que l'on utilise dans plusieurs écrans de la partie
//                        // commerce de cube instore.)
//                        HorizontalLineChart(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 16.dp)
//                                .rotate(90f)
//                        )
//                    }
//                }
//                SimpleBarChart(revenueData)
//                HorizontalBarChart(revenueDataSmall)
                EnhancedBarChart(revenueDataSmall, month, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun SimpleBarChart(data: List<Float>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val barWidth = size.width / (data.size * 2)
        val maxValue = data.maxOrNull() ?: 0f

        data.forEachIndexed { index, value ->
            val barHeight = (value / maxValue) * size.height
            drawRect(
                color = Color.Blue,
                topLeft = Offset(
                    x = index * barWidth * 2,
                    y = size.height - barHeight
                ),
                size = Size(barWidth, barHeight)
            )
        }
    }
}

@Composable
fun HorizontalBarChart(data: List<Float>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val barHeight = size.height / (data.size * 2)
        val maxValue = data.maxOrNull() ?: Float.NaN

        data.forEachIndexed { index, value ->
            val barWidth = (value / maxValue) * size.width
            drawRect(
                color = Color.Blue,
                topLeft = Offset(
                    x = 0f,
                    y = index * barHeight * 2
                ),
                size = Size(barWidth, barHeight)
            )
        }
    }
}

@Composable
fun EnhancedBarChart(
    data: List<Float>,
    month: List<String>,
    title: String = "Revenus Mensuels",
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                text = title,
            )
            val textMeasurer = rememberTextMeasurer()
            val max = month.maxBy { it.length }
            val textLayoutResult = textMeasurer.measure(text = AnnotatedString(max))
            val textSize = textLayoutResult.size

            Canvas(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val barHeight = size.height / (month.size * 10)
                val maxValue = data.maxOrNull() ?: Float.NaN

                val step = 10.0.pow((maxValue.toInt().toString().length - 1).toDouble())
                val numTicks = (maxValue / step).toInt()
                val tickWidth = (size.width) / (numTicks + 1)
                val tickValue = maxValue / numTicks

                data.forEachIndexed { index, value ->
                    val barWidth = (value / maxValue) * (size.width - textSize.width) //- 200f
                    val color = if (index == 0) Color(0xFF64B5F6) else Color(0xFF1976D2)

                    drawText(
                        textMeasurer,
                        month[index],
                        topLeft = Offset(
                            x = 0f,
                            y = index * barHeight * 2 + barHeight
                        )
                    )

                    drawRect(
                        color = color,
                        topLeft = Offset(
                            x = 5f + textSize.width,
                            y = index * barHeight * 2 + barHeight
                        ),
                        size = Size(barWidth, barHeight)
                    )
                }

                for (i in 0..numTicks) {
                    println("i : $i")
                    val leg = convertToK(tickValue.toInt() * i)
                    drawText(
                        textMeasurer,
                        leg,
                        topLeft = Offset(
                            x = textSize.width + (tickWidth * i) -
                                    (textMeasurer.measure(
                                        text = AnnotatedString(
                                            leg
                                        )
                                    ).size.width),
                            y = barHeight - textSize.height,
                        )
                    )

                    drawRect(
                        color = Color.Gray,
                        topLeft = Offset(
                            x = textSize.width + (tickWidth * i),
                            y = barHeight
                        ),
                        size = Size(width = 4f, height = 185f)
                    )
                }
            }
        }
    }
}

fun convertToK(nb: Int): String {
    return if (nb >= 1000) {
        "€${nb / 1000}K"
    } else "€$nb"
}

val revenueDataSmall = listOf(
    45f,  // GMV Y
    62000f,  // GMV Y-1
)

val month = listOf(
    "GMV Y",
    "GMV Y-1"
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GraphLibExamplesTheme {
        BasicLineChart(Modifier)
    }
}