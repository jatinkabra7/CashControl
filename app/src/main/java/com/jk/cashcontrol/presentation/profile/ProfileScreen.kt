package com.jk.cashcontrol.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.User

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user : User,
    onLogout : () -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
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

        Spacer(Modifier.height(10.dp))

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
                .size(150.dp)
                .border(width = 5.dp, color = Color.DarkGray, shape = CircleShape)
                .align(Alignment.CenterHorizontally)

        )

        Spacer(Modifier.height(30.dp))

        Item(s = "Name:", t = user.name)

        Spacer(Modifier.height(10.dp))

        HorizontalDivider()

        Spacer(Modifier.height(10.dp))

        Item(s = "Email: ", t = user.email)

        Spacer(Modifier.height(10.dp))

        HorizontalDivider()

        Spacer(Modifier.height(10.dp))

        Item(s = "User Id: ", t = user.id)

        Spacer(Modifier.height(10.dp))

        HorizontalDivider()

        Spacer(Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .width(200.dp)
                .background(color = Color.Red.copy(0.7f), shape = RoundedCornerShape(100))
                .align(Alignment.CenterHorizontally)
                .clickable {
                    onLogout()
                }
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .width(200.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.icons8_google_logo),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(30.dp)
                )

                Text(
                    text = "Logout",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }


    }
}

@Composable
fun Item(s : String, t : String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = s,
            color = Color.White,
            fontSize = 24.sp
        )

        Spacer(Modifier.width(20.dp))

        Text(
            text = t,
            color = Color.DarkGray,
            fontSize = 20.sp,
            modifier = Modifier
                .basicMarquee()
        )
    }
}