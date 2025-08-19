package com.jk.cashcontrol.presentation.statistics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.presentation.utils.parseMarkdown

@Composable
fun Summary(
    text: String,
    modifier: Modifier = Modifier
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(20.dp), color = Color.DarkGray.copy(0.5f))
            .padding(
                horizontal = dimensionResource(id = R.dimen.statistics_screen_horizontal_padding),
                vertical = dimensionResource(id = R.dimen.statistics_screen_vertical_padding)
            )
    ) {
        Text(
            text = parseMarkdown(text),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(0.8f)
        )
    }

}