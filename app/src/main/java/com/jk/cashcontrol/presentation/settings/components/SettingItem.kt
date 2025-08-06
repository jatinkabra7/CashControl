package com.jk.cashcontrol.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SettingItem(
    label: String,
    contentDescription: String,
    contentColor: Color,
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showSwitch: Boolean = false,
    isSwitchEnabled: Boolean = false
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(100))
            .background(Color.DarkGray.copy(0.7f))
            .padding(horizontal = 10.dp)
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier
                .size(30.dp)
        )

        Spacer(Modifier.width(20.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = contentColor
        )


        Spacer(Modifier.weight(1f))

        if(showSwitch) {

            Switch(
                checked = isSwitchEnabled,
                onCheckedChange = { onClick() }
            )
        }
    }
}