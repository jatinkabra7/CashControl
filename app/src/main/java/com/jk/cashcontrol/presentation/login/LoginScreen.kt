package com.jk.cashcontrol.presentation.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.presentation.theme.ForegroundColor

sealed class PagerItem {
    data class Logo(
        val title: String,
        val logoRes: Int,
        val backgroundColor: Color
    ) : PagerItem()
    data class Feature(
        val title: String,
        val description: String,
        val backgroundColor: Color,
        val textColor: Color
    ) : PagerItem()
}

val pagerItems = listOf(
    PagerItem.Logo("Cash Control", R.drawable.cash_control_logo_circle_02, ForegroundColor),
    PagerItem.Feature("Track Your Expenses", "Easily record and categorize your daily expenses to see where your money goes.", Color(0xFFC5E1A5), Color(0xFF000000)),
    PagerItem.Feature("Set Budgets", "Create custom budgets to control your spending and achieve your financial goals.", Color(0xFF80CBC4), Color(0xFF000000)),
    PagerItem.Feature("View Reports", "Analyze your spending habits with insightful charts and reports.", Color(0xFFF48FB1), Color(0xFF000000))
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        pageCount = { pagerItems.size }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            when (val item = pagerItems[page]) {
                is PagerItem.Logo -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(200.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = item.backgroundColor
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Image(
                                painter = painterResource(item.logoRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100))
                                    .size(100.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = item.title,
                                color = Color.White,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                is PagerItem.Feature -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(200.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = item.backgroundColor
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = item.textColor,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = item.textColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        Row(
            Modifier
                .height(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.White else Color.Gray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Start tracking your expenses with Cash Control!",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        )

        Spacer(Modifier.height(64.dp))

        Box(
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .fillMaxWidth()
                .padding(10.dp)
                .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(30))
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.icons8_google_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                )

                Spacer(Modifier.width(20.dp))

                Text(
                    text = "Login With Google",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}