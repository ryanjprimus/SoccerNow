package com.primus.team_info.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.primus.domain.models.Article
import com.primus.domain.models.News
import com.primus.utils.R
import com.primus.utils.composables.EmptyContent
import com.primus.utils.composables.SubComposeAsyncImageCommon
import com.primus.utils.composables.shimmerEffect
import com.primus.utils.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsList(
    news: News,
    isLoading: Boolean,
    onReloadClick: () -> Unit
) {
    val context = LocalContext.current
    when {
        isLoading -> ShimmerEffect()
        !isLoading && news.articles.isEmpty() -> EmptyContent(onReloadClick = onReloadClick)
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1),
                contentPadding = PaddingValues(all = MaterialTheme.dimens.small1)
            ) {
                item {
                    Text(
                        text = "Team news",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                items(
                    items = news.articles,
                ) { article ->
                    ArticleItem(
                        modifier = Modifier.animateItemPlacement(),
                        article = article,
                        onArticleClick = { url ->
                            Intent(Intent.ACTION_VIEW).also {
                                it.data = Uri.parse(url)
                                if (it.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(it)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleItem(
    modifier: Modifier = Modifier,
    article: Article,
    onArticleClick: (String) -> Unit
) {
    Row(
        modifier = modifier
            .background(Color.Transparent)
            .clickable { onArticleClick.invoke(article.url) }) {
        SubComposeAsyncImageCommon(
            modifier = Modifier,
            imageUri = article.urlToImage,
            shape = MaterialTheme.shapes.medium,
            size = MaterialTheme.dimens.champLogoDefaultSize
        )
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.small1)
                .height(MaterialTheme.dimens.champLogoDefaultSize)
                .background(Color.Transparent)
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.bodyMedium.copy(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.source.name,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.width(MaterialTheme.dimens.extraSmall1))
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = null,
                    modifier = Modifier.size(MaterialTheme.dimens.small3),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(MaterialTheme.dimens.extraSmall1))
                Text(
                    text = article.publishedAt,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun ArticleCardShimmerEffect(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(MaterialTheme.dimens.champLogoDefaultSize)
                .clip(MaterialTheme.shapes.medium)
                .shimmerEffect()
        )
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.extraSmall1)
                .height(MaterialTheme.dimens.champLogoDefaultSize)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(horizontal = MaterialTheme.dimens.small3)
                    .shimmerEffect()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(horizontal = MaterialTheme.dimens.small3)
                        .height(15.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
private fun ShimmerEffect() {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)) {
        repeat(10) {
            ArticleCardShimmerEffect(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
        }
    }
}