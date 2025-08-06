package com.jk.cashcontrol.presentation.profile

import android.content.ClipData
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.User
import com.jk.cashcontrol.presentation.settings.SettingsActions
import com.jk.cashcontrol.presentation.settings.SettingsScreen
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    user : User,
    appLockStatus: Boolean,
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
        targetValue = if(isSettingsVisible) (0).dp else 250.dp,
        animationSpec = tween(500, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp)
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(10.dp)
        ) {

            Spacer(Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                IconButton(onClick = {
                    isSettingsVisible = !isSettingsVisible
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_settings_24),
                        contentDescription = "Settings",
                        modifier = Modifier.size(30.dp),
                        tint = Color.White
                    )
                }
            }


            val imageRequest = ImageRequest
                .Builder(context)
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
                    .size(100.dp)
                    .border(width = 3.dp, color = Color.DarkGray, shape = CircleShape)
                    .align(Alignment.CenterHorizontally)
            )

            UserDetails(
                user = user,
                onCopy = {
                    val clipData = ClipData.newPlainText("Copied",it)
                    scope.launch {
                        clipboard.setClipEntry(ClipEntry(clipData))
                        Toast.makeText(context,"Copied", Toast.LENGTH_SHORT).show()
                    }
                }
            )

        }

        if(isSettingsVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray.copy(0.3f))
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) {isSettingsVisible = false}
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
                .width(250.dp)
                .align(Alignment.CenterEnd)
                .offset(x = xOffset)
        )
    }

}

@Composable
private fun UserDetails(modifier: Modifier = Modifier, user: User, onCopy: (String) -> Unit) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(5.dp))

        ProfileItem(s = "Name:", t = user.name, onCopy = onCopy)

        Spacer(Modifier.height(5.dp))

        HorizontalDivider()

        Spacer(Modifier.height(5.dp))

        ProfileItem(s = "Email: ", t = user.email, onCopy = onCopy)

        Spacer(Modifier.height(5.dp))

        HorizontalDivider()

        Spacer(Modifier.height(5.dp))

        ProfileItem(s = "User Id: ", t = user.id, onCopy = onCopy)

        Spacer(Modifier.height(5.dp))

        HorizontalDivider()

        Spacer(Modifier.height(30.dp))
    }
}

@Composable
private fun ProfileItem(s : String, t : String, onCopy: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = s,
            color = Color.White,
            fontSize = 18.sp
        )

        Spacer(Modifier.width(5.dp))

        Text(
            text = t,
            color = Color.Gray,
            fontSize = 16.sp,
            overflow = Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(5.dp))

        IconButton(onClick = {onCopy(t)}) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_content_copy_24),
                contentDescription = "Copy Content",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

    }
}