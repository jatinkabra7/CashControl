package com.jk.cashcontrol.features.expense_tracker.presentation.profile

import android.content.ClipData
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.jk.cashcontrol.R
import com.jk.cashcontrol.features.auth.domain.model.User
import com.jk.cashcontrol.features.expense_tracker.presentation.settings.SettingsActions
import com.jk.cashcontrol.features.expense_tracker.presentation.settings.SettingsScreen
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    user: User,
    appLockStatus: Boolean,
    bannerAd: @Composable () -> Unit,
    onAction: (SettingsActions) -> Unit,
    navigateToAppInfoScreen: () -> Unit,
    navigateToAppLockScreen: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    isAccountDeleting: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    var isSettingsVisible by rememberSaveable { mutableStateOf(false) }

    val xOffset by animateDpAsState(
        targetValue = if (isSettingsVisible) 0.dp else dimensionResource(R.dimen.profile_settings_panel_width),
        animationSpec = tween(250, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = dimensionResource(R.dimen.profile_padding_medium))
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensionResource(R.dimen.profile_padding_medium))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Light,
                    color = Color.White
                )

                IconButton(onClick = { isSettingsVisible = !isSettingsVisible }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_settings_24),
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }

            val imageRequest = ImageRequest.Builder(context)
                .data(user.imageUrl)
                .crossfade(true)
                .build()

            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                placeholder = painterResource(R.drawable.cash_control_logo_circle_02),
                error = painterResource(R.drawable.cash_control_logo_circle_02),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(dimensionResource(R.dimen.profile_image_size))
                    .border(
                        width = dimensionResource(R.dimen.profile_border_width),
                        color = Color.DarkGray,
                        shape = CircleShape
                    )
                    .align(Alignment.CenterHorizontally)
            )

            UserDetails(
                user = user,
                onCopy = {
                    val clipData = ClipData.newPlainText("Copied", it)
                    scope.launch {
                        clipboard.setClipEntry(ClipEntry(clipData))
                        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            bannerAd()
            bannerAd()
        }

        if (isSettingsVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray.copy(0.3f))
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) { isSettingsVisible = false }
            )
        }

        SettingsScreen(
            appLockStatus = appLockStatus,
            onAction = onAction,
            navigateToAppInfoScreen = navigateToAppInfoScreen,
            navigateToAppLockScreen = navigateToAppLockScreen,
            onLogout = onLogout,
            onDeleteAccount = onDeleteAccount,
            isAccountDeleting = isAccountDeleting,
            modifier = modifier
                .width(dimensionResource(R.dimen.profile_settings_panel_width))
                .align(Alignment.CenterEnd)
                .offset(x = xOffset)
        )
    }
}

@Composable
private fun UserDetails(modifier: Modifier = Modifier, user: User, onCopy: (String) -> Unit) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(dimensionResource(R.dimen.profile_padding_small)))

        ProfileItem(s = "Name:", t = user.name, onCopy = onCopy)

        Spacer(Modifier.height(dimensionResource(R.dimen.profile_padding_small)))
        HorizontalDivider()
        Spacer(Modifier.height(dimensionResource(R.dimen.profile_padding_small)))

        ProfileItem(s = "Email:", t = user.email, onCopy = onCopy)

        Spacer(Modifier.height(dimensionResource(R.dimen.profile_padding_small)))
        HorizontalDivider()
        Spacer(Modifier.height(dimensionResource(R.dimen.profile_padding_small)))

        ProfileItem(s = "User Id:", t = user.id, onCopy = onCopy)

        Spacer(Modifier.height(dimensionResource(R.dimen.profile_padding_small)))
        HorizontalDivider()
        Spacer(Modifier.height(dimensionResource(R.dimen.profile_padding_large)))
    }
}

@Composable
private fun ProfileItem(s: String, t: String, onCopy: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = s,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(Modifier.width(dimensionResource(R.dimen.profile_padding_small)))

        Text(
            text = t,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            overflow = Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(dimensionResource(R.dimen.profile_padding_small)))

        IconButton(onClick = { onCopy(t) }) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_content_copy_24),
                contentDescription = "Copy Content",
                tint = Color.White,
                modifier = Modifier.size(dimensionResource(R.dimen.profile_icon_size))
            )
        }
    }
}
