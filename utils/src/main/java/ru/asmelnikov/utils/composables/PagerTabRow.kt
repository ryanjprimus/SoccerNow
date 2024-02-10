package ru.asmelnikov.utils.composables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerTabRow(
    tabTitles: List<String>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    onTabSelected: (Int) -> Unit,
    pagerState: PagerState,
) {
    val density = LocalDensity.current
    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabTitles.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
        containerColor = containerColor,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .myTabIndicatorOffset(
                        tabPositions[selectedIndex],
                        tabWidth = tabWidths[selectedIndex],
                        pagerState = pagerState
                    )
                    .height(3.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
            )
        }
    ) {

        tabTitles.forEachIndexed { index, title ->
            val selected = selectedIndex == index

            Tab(
                text = {
                    Text(
                        text = title,
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleSmall,
                        onTextLayout = { textLayoutResult ->
                            tabWidths[index] = with(density) { textLayoutResult.size.width.toDp() }
                        }
                    )
                },
                selected = selected,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.myTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp,
    pagerState: PagerState
): Modifier = composed {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing), label = ""
    )

    val tabCenter = currentTabPosition.left + (currentTabPosition.width / 2)
    val currentPageOffsetFraction = pagerState.currentPageOffsetFraction
    val tabOffset = tabWidth * currentPageOffsetFraction
    val indicatorCenter = tabCenter + tabOffset * 2
    val indicatorOffset by animateDpAsState(
        targetValue = indicatorCenter - (currentTabWidth / 2),
        animationSpec = tween(durationMillis = 100, easing = LinearEasing), label = ""
    )

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}