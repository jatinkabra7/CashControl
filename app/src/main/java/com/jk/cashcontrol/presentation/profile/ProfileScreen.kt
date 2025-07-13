package com.jk.cashcontrol.presentation.profile

import android.content.ClipData
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.User
import com.jk.cashcontrol.presentation.home.HomeAction
import com.jk.cashcontrol.presentation.theme.CustomDarkOrange
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user : User,
    onLogout : () -> Unit,
    onDeleteAccount: () -> Unit,
    isDeleting: Boolean
) {

    val context = LocalContext.current

    val clipboard = LocalClipboard.current

    val scope = rememberCoroutineScope()

    var deleteAccountDialogTextFieldValue by rememberSaveable { mutableStateOf("") }

    var isDeleteAccountDialogVisible by rememberSaveable { mutableStateOf(false) }

    if(isDeleteAccountDialogVisible) {
        AlertDialog(
            onDismissRequest = {isDeleteAccountDialogVisible = false},
            title = {
                Text(
                    text = "DELETE ACCOUNT",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OutlinedTextField(
                    enabled = !isDeleting,
                    value = deleteAccountDialogTextFieldValue,
                    onValueChange = { deleteAccountDialogTextFieldValue = it },
                    supportingText = {
                        Text(
                            text = "Type \"DELETE\" and press Delete. Please select the current account when prompted.",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    enabled = deleteAccountDialogTextFieldValue == "DELETE" && !isDeleting,
                    onClick = onDeleteAccount
                ) {
                    Text(
                        text = if(!isDeleting) "Delete" else "Deleting"
                    )
                }
            },
            dismissButton = {
                if(!isDeleting) {
                    TextButton(
                        onClick = { isDeleteAccountDialogVisible = false }
                    ) {
                        Text(
                            text = "Cancel"
                        )
                    }
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(10.dp)
    ) {

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Profile",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Start)
        )

        val imageRequest = ImageRequest
            .Builder(context)
            .data(user.imageUrl)
            .crossfade(true)
            .build()

        AsyncImage(
            model = imageRequest,
            contentDescription = null,
            placeholder = painterResource(R.drawable.cashcontrollogopng),
            error = painterResource(R.drawable.cashcontrollogopng),
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
                }
            }
        )

        ActionButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            buttonText = "Logout",
            buttonIcon = R.drawable.icons8_google_logo,
            onClick = onLogout
        )

        Spacer(Modifier.height(10.dp))

        ActionButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            buttonText = "Delete Account",
            buttonIcon = R.drawable.baseline_dangerous_24,
            onClick = { isDeleteAccountDialogVisible = true }
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = "App Version: 2.0",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally),
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

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    buttonIcon: Int,
    onClick: () -> Unit
) {

    val brush = Brush.verticalGradient(listOf(Color.Red.copy(0.7f), CustomDarkOrange))

    Box(
        modifier = modifier
            .background(brush = brush, shape = RoundedCornerShape(100))
            .clickable {
                onClick()
            }
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(buttonIcon),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = buttonText,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

}