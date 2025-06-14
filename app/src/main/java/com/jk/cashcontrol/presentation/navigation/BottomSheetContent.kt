package com.jk.cashcontrol.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.presentation.theme.CustomDarkOrange

@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    onIncomeButtonClick : () -> Unit,
    onExpenseButtonClick : () -> Unit
) {

    Box(
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(0.5f)
                    .background(shape = RoundedCornerShape(20), color = Color.DarkGray.copy(0.5f))
                    .clickable {
                        onIncomeButtonClick()
                    }
                    .padding(10.dp)

            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_downward_24),
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "Add Income",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            Spacer(Modifier.width(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(0.5f)
                    .background(shape = RoundedCornerShape(20), color = Color.DarkGray.copy(0.5f))
                    .clickable {
                        onExpenseButtonClick()
                    }
                    .padding(10.dp)

            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_upward_24),
                    contentDescription = null,
                    tint = CustomDarkOrange,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "Add Expense",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }

}