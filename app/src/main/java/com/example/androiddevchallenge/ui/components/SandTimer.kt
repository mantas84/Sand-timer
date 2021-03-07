/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
// @Preview
fun SandClockFrame(color: Color = Color.LightGray) {

    val height1 = 300.dp
    val width1 = 400.dp
    val padding1 = 16.dp

    Box(
        modifier = Modifier
            .size(height1, width1)
            .padding(padding1)
    ) {

        val path = Path()
        path.apply {

            val height = with(LocalDensity.current) { height1.toPx() - 2 * padding1.toPx() }
            val width = with(LocalDensity.current) { width1.toPx() - 2 * padding1.toPx() }
            val gapRadius = with(LocalDensity.current) { 10.dp.toPx() }

            val size = Size(height, width)

            val centerH = size.height.div(2f)
            val centerW = size.width.div(2f)
            val leftBound = size.width * 0.1f
            val rightBound = size.width * 0.9f

            val height30 = size.height * 0.3f
            val height70 = size.height * 0.7f
            val centerRight = centerW + gapRadius
            val centerLeft = centerW - gapRadius

            moveTo(leftBound, 0f)
            lineTo(rightBound, 0f)
            cubicTo(size.width, height30, centerRight, height30, centerRight, centerH)
            cubicTo(centerRight, height70, size.width, height70, rightBound, size.height)
            lineTo(leftBound, size.height)
            cubicTo(0f, height70, centerLeft, height70, centerLeft, centerH)
            cubicTo(centerLeft, height30, 0f, height30, leftBound, 0f)
        }

        Canvas(
            modifier = Modifier,
            onDraw = {
                this.drawPath(
                    path = path,
                    color = color,
                    style = Stroke(width = 12.dp.value, cap = StrokeCap.Round)
                )
            }
        )
    }
}

@Composable
// @Preview
fun SandClockTop(percentage: Float = 0.5f, color: Color = Color.Blue) {

    val height1 = 300.dp
    val width1 = 400.dp
    val padding1 = 32.dp

    Box(
        modifier = Modifier
            .size(height1, width1)
            .padding(padding1, 24.dp)
    ) {

        val padding = with(LocalDensity.current) { padding1.toPx() }
        val height = with(LocalDensity.current) { height1.toPx() - 2 * padding }
        val width = with(LocalDensity.current) { width1.toPx() - 2 * padding }

        val size = Size(height, width)

        val centerH = size.height.div(2f)

        val path = Path()
        path.apply {

            val centerW = size.width.div(2f)
            val leftBound = size.width * 0.1f
            val rightBound = size.width * 0.9f

            val height30 = size.height * 0.3f

            moveTo(leftBound, 0f)
            lineTo(rightBound, 0f)
            cubicTo(size.width, height30, centerW, height30, centerW, centerH)
            cubicTo(centerW, height30, 0f, height30, leftBound, 0f)
        }

        val fraction = topFraction(percentage)

        val clipPath = Path()
        clipPath.apply {
            addRect(Rect(0f, centerH * fraction, size.width, size.height.div(2f)))
        }

        val p3 = Path()
        p3.op(clipPath, path, PathOperation.intersect)

        Canvas(
            modifier = Modifier.fillMaxWidth(),
            onDraw = {
                this.drawPath(
                    path = p3,
                    color = color,
                )
            }
        )
    }
}

@Composable
@Preview
fun SandClockBottom(percentage: Float = 0.5f, color: Color = Color.Blue) {

    val height1 = 300.dp
    val width1 = 400.dp
    val padding1 = 32.dp

    Box(
        modifier = Modifier
            .size(height1, width1)
            .padding(padding1, 40.dp)
    ) {

        val padding = with(LocalDensity.current) { padding1.toPx() }
        val height = with(LocalDensity.current) { height1.toPx() - 2 * padding }
        val width = with(LocalDensity.current) { width1.toPx() - 2 * padding }

        val fraction = bottomFraction(percentage)
        val fractionFaster = bottomFractionFaster(percentage)

        val size = Size(height, width)

        val path = Path()
        path.apply {

            val centerH = bottomHeight(fraction, size.height) // from full to half
            val centerW = size.width.div(2f) // fixed
            val leftBound = size.width * (0.5f - 0.4f * fractionFaster) // from centerW to 0.1w
            val rightBound = size.width * (0.5f + 0.4f * fractionFaster) // from centerW to 0.9w

            val height70 =
                size.height * 0.7f + (size.height * 0.3f * (1f - fraction)) // from full to 0.7

            val point1 = size.width * (0.5f + 0.5f * fraction)
            val point2 = size.width * (0.5f - 0.5f * fraction)

            moveTo(centerW, centerH)
            cubicTo(centerW, height70, point1, height70, rightBound, size.height)
            lineTo(leftBound, size.height)
            cubicTo(point2, height70, centerW, height70, centerW, centerH)
        }

        Canvas(
            modifier = Modifier.fillMaxWidth(),
            onDraw = {
                this.drawPath(
                    path = path,
                    color = color
                )
            }
        )
    }
}

private fun topFraction(percentage: Float): Float = percentage * (0.4f * percentage + 0.6f)
private fun bottomFraction(percentage: Float) = percentage
private fun bottomFractionFaster(percentage: Float) = min(percentage * 3f, 1f)
private fun bottomHeight(fraction: Float, fullHeight: Float) =
    fullHeight.div(2.0f) * (2f - fraction)

@Composable
@Preview
fun MyLine(isRunning: Boolean = true, color: Color = Color.Red) {

    val alpha: Float by animateFloatAsState(if (isRunning) 1f else 0.0f)

    val height1 = 300.dp
    val width1 = 400.dp
    val padding1 = 32.dp

    Box(
        modifier = Modifier
            .size(height1, width1)
            .padding(padding1, 40.dp)
            .graphicsLayer(alpha = alpha)
    ) {

        val padding = with(LocalDensity.current) { padding1.toPx() }
        val height = with(LocalDensity.current) { height1.toPx() - 2 * padding }
        val width = with(LocalDensity.current) { width1.toPx() - 2 * padding }
        val radius = with(LocalDensity.current) { 1.dp.toPx() }
        val diff = with(LocalDensity.current) { 24.dp.toPx() }

        val size = Size(height, width)

        val halfHeight = size.height.div(2f) - diff
        val centerW = size.width.div(2f)

        val path = Path()
        path.apply {
            moveTo(centerW, halfHeight)
            lineTo(centerW, size.height)
        }

        Canvas(
            modifier = Modifier.fillMaxWidth(),
            onDraw = {
                this.drawPath(
                    path = path,
                    color = color,
                    style = Stroke(width = radius)
                )
            }
        )
    }
}

@Composable
@Preview
fun SandTime(
    percentage: Float = 0.5f,
    isRunning: Boolean = false,
    frameColor: Color = Color.Blue,
    fillColor: Color = Color.Yellow
) {
    SandClockFrame(frameColor)
    SandClockTop(percentage, fillColor)
    SandClockBottom(percentage, fillColor)
    MyLine(isRunning, fillColor)
}
