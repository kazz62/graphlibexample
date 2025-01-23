package com.kazz.graphlibexamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kazz.graphlibexamples.ui.theme.GraphLibExamplesTheme

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
                val revenueDataSmall = listOf(
                    45f,  // GMV Y
                    47650f,  // GMV Y-1
                )

                val month = listOf(
                    "GMV Y",
                    "GMV Y-1"
                )

                val title: String = "Where am I? Day"
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
                EnhancedBarChart(
                    revenueDataSmall,
                    month,
                    title,
                    39888f,
                    modifier = Modifier.padding(innerPadding)
                )
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

/**
 * Horizontal Bar Chart used to reproduce the old Perfeco horizontal charts.
 * This composable is still a WIP and needs to be finished, documented and refacto.
 */
@Composable
fun EnhancedBarChart(
    data: List<Float>,
    year: List<String>,
    title: String,
    goal: Float,
    modifier: Modifier,
) {
    val blue = Color(0xFF64B5F6)
    val lightBlue = Color(0xFF1976D2)
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            color = lightBlue
        )
        val textMeasurer = rememberTextMeasurer()
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp)
        ) {
            val tickWidth = 2f
            val tickHeight = 185f

            val max = year.maxBy { it.length }
            val textLayoutResult = textMeasurer.measure(text = AnnotatedString(max))
            val textSize = textLayoutResult.size

            val barHeight = tickHeight / 3
            val maxValue = data.maxOrNull() ?: Float.NaN

            val numTicks = 4
            val tickGap = (size.width - textSize.width) / numTicks
            val tickValue = maxValue / numTicks

            val symbol = "€"

            data.forEachIndexed { index, value ->
                val barWidth = (value / maxValue) * (size.width - textSize.width)
                val color = if (index == 0) blue else lightBlue

                println("Canvas width: ${size.width}, Bar width: $barWidth, Max value: $maxValue")

                drawText(
                    textMeasurer,
                    year[index],
                    topLeft = Offset(
                        x = 0f,
                        y = index * barHeight * 2 + barHeight
                    )
                )

                drawRect(
                    color = color,
                    topLeft = Offset(
                        x = tickWidth + textSize.width.toFloat(),
                        y = index * barHeight * 2 + barHeight
                    ),
                    size = Size(barWidth, barHeight)
                )
            }

            for (i in 0..numTicks) {
                println("i : $i")
                val leg = convertToK(tickValue.toInt() * i, symbol)
                drawText(
                    textMeasurer,
                    leg,
                    topLeft = Offset(
                        x = textSize.width + (tickGap * i) -
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
                        x = textSize.width + (tickGap * i),
                        y = barHeight
                    ),
                    size = Size(width = tickWidth, height = tickHeight)
                )
            }

            val goalGap = (goal / maxValue) * (size.width - textSize.width)
            drawLine(
                color = Color.Green,
                start = Offset(
                    x = textSize.width + goalGap,
                    y = barHeight
                ),
                end = Offset(
                    x = textSize.width + goalGap,
                    y = tickHeight + barHeight
                ),
                strokeWidth = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )

            val goalK = convertToK(goal.toInt(), symbol = symbol)
            drawText(
                textMeasurer,
                goalK,
                topLeft = Offset(
                    x = textSize.width + goalGap + tickWidth,
                    y = barHeight + textSize.height,
                ),
                style = TextStyle(Color.Green),
            )
        }
    }
}

fun convertToK(nb: Int, symbol: String): String {
    return if (nb >= 1000) {
        "$symbol${nb / 1000}K"
    } else "$symbol$nb"
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GraphLibExamplesTheme {
        BasicLineChart(Modifier)
    }
}