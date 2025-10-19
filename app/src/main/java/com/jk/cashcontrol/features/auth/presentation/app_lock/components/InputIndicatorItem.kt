package com.jk.cashcontrol.features.auth.presentation.app_lock.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun InputIndicatorItem(
    pin: String,
    correctPin: String,
    setupPin: String,
    position: Int,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {

    val contentColor = if(isActive) Color.White else Color.Gray

    val animatedSize = remember { Animatable(1f, Float.VectorConverter) }

    LaunchedEffect(pin.length) {
        if(pin.length == 4) {
            if((correctPin.isNotEmpty() && pin == correctPin) || (setupPin.isNotEmpty() && pin == setupPin)) {
                delay((200*position).toLong())
                animatedSize.animateTo(
                    targetValue = 1.5f,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
                animatedSize.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
            }
            else {

            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(10.dp)
            .scale(animatedSize.value)
            .clip(CircleShape)
            .background(contentColor)
    ) {}
}