package hu.simplexion.z2.browser.demo.chart

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.canvas
import hu.simplexion.z2.browser.material.px
import org.w3c.dom.CENTER
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextAlign
import org.w3c.dom.HTMLCanvasElement

fun Z2.chartDemo() =
    canvas {
        style.width = 800.px
        style.height = 400.px

        val canvasElement = (htmlElement as HTMLCanvasElement)
        canvasElement.style.imageRendering = "pixelated"
        val ctx = canvasElement.getContext("2d") as CanvasRenderingContext2D

        val data = listOf(100.0, 68.0, 80.0, 30.0, 40.0)
        val max = data.max()
        val labels = listOf("January", "February", "March", "April", "May")
        val barWidth = 50.0
        val barSpacing = 10.0
        val chartHeight = 300.0 // Maximum height of chart
        val xOffset = 50.0 // Starting x position of the first bar
        val yOffset = 350.0 // Y position to start drawing bars (from bottom)

        ctx.beginPath()
        ctx.fillStyle = "#ffffff"
        ctx.fillRect(0.0, 0.0, 800.0, 400.0)
        ctx.fillStyle = "#007bff"; // Bar color

        data.forEachIndexed { index, value ->
            val barHeight = (value / max) * chartHeight
            ctx.fillRect(xOffset + (index * (barWidth + barSpacing)), yOffset - barHeight, barWidth, barHeight)
            ctx.textAlign = CanvasTextAlign.CENTER
            ctx.fillText(labels[index], xOffset + (index * (barWidth + barSpacing)) + (barWidth / 2), yOffset + 20)
        }

        // Draw axis
        ctx.moveTo(30.0, 50.0)
        ctx.lineTo(30.0, yOffset)
        ctx.lineTo(canvasElement.width - 20.0, yOffset)
        ctx.stroke()
    }
