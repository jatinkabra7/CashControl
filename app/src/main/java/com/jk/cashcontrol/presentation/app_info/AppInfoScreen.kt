package com.jk.cashcontrol.presentation.app_info

import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.jk.cashcontrol.R
import com.jk.cashcontrol.presentation.theme.CustomBlue
import com.jk.cashcontrol.presentation.theme.CustomDarkBlue
import kotlinx.coroutines.delay

@Composable
fun AppInfoScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val privacyPolicyUrl = "https://sites.google.com/view/jatinkabra-cashcontrol/home"

    val bmcUrl = "https://buymeacoffee.com/jatinkabra"

    var isAnimationFinished by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.app_info_horizontal_padding))
    ) {

        AppInfoTopBar(
            navigateUp = navigateUp
        )

        AnimatedLogo(
            onAnimationFinish = { isAnimationFinished = true }
        )

        Crossfade(
            targetState = isAnimationFinished,
            animationSpec = tween(1000, easing = FastOutSlowInEasing)
        ) {
            if (it) {
                DetailsSection(
                    textColor = Color.White,
                    onPrivacyPolicyClick = {
                        val intent = Intent(Intent.ACTION_VIEW, privacyPolicyUrl.toUri())
                        context.startActivity(intent)
                    },
                    onSupportMeClick = {
                        val intent = Intent(Intent.ACTION_VIEW, bmcUrl.toUri())
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
private fun AnimatedLogo(
    onAnimationFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {

        var startAnimation by rememberSaveable { mutableStateOf(false) }

        val animatedScale by animateFloatAsState(
            targetValue = if(startAnimation) 1f else 2f,
            animationSpec = tween(5000, easing = FastOutSlowInEasing)
        )

        val animatedYOffset by animateDpAsState(
            targetValue = if(startAnimation) 0.dp else 125.dp,
            animationSpec = tween(3000, easing = FastOutSlowInEasing)
        )

        LaunchedEffect(Unit) {
            delay(1000)
            startAnimation = true
            delay(2000)
            onAnimationFinish()
        }

        Image(
            painter = painterResource(R.drawable.cash_control_logo_circle_02),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.app_info_logo_size))
                .scale(animatedScale)
                .offset(y = animatedYOffset)
        )
    }

}



@Composable
private fun DetailsSection(
    textColor: Color,
    onPrivacyPolicyClick: () -> Unit,
    onSupportMeClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val gradient = Brush.verticalGradient(
        colors = listOf(CustomBlue, CustomDarkBlue)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(Modifier.height(dimensionResource(id = R.dimen.app_info_spacer_small)))

        Text(
            text = "Cash Control",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.app_info_spacer_medium)))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth(0.8f)
                .height(IntrinsicSize.Min)
                .clip(RoundedCornerShape(10.dp))
                .background(gradient)
                .padding(
                    horizontal = dimensionResource(id = R.dimen.app_info_horizontal_padding),
                    vertical =dimensionResource(id = R.dimen.app_info_vertical_padding)
                )
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "App Version",
                    fontStyle = FontStyle.Italic,
                    color = textColor
                )

                Spacer(Modifier.height(dimensionResource(id = R.dimen.app_info_spacer_small)))

                Text(
                    text = "Developed By",
                    fontStyle = FontStyle.Italic,
                    color = textColor
                )

                Spacer(Modifier.height(dimensionResource(id = R.dimen.app_info_spacer_small)))

                Text(
                    text = "Privacy Policy",
                    fontStyle = FontStyle.Italic,
                    color = textColor
                )
            }

            Spacer(Modifier.width(dimensionResource(id = R.dimen.app_info_spacer_small)))

            VerticalDivider(color = Color.White)

            Spacer(Modifier.width(dimensionResource(id = R.dimen.app_info_spacer_small)))

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "2.0",
                    color = textColor
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Jatin Kabra",
                    color = textColor
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Link",
                    color = textColor,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {onPrivacyPolicyClick()}
                )
            }
        }

        Spacer(Modifier.height(dimensionResource(id = R.dimen.app_info_spacer_small)))

        Text(
            text = "Support Me",
            color = textColor,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {onSupportMeClick()}
        )
    }

}

@Composable
private fun AppInfoTopBar(
    navigateUp: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = navigateUp
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_arrow_back_24),
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(Modifier.width(dimensionResource(id = R.dimen.app_info_spacer_small)))

        Text(
            text = "App Info",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Light,
            color = Color.White
        )
    }
}
