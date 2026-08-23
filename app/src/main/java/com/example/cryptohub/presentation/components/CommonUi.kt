package com.example.cryptohub.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptohub.R

private object UIConstants {
    // Shimmer
    const val SHIMMER_MIN_ALPHA = 0.3f
    const val SHIMMER_MAX_ALPHA = 0.7f
    const val SHIMMER_DURATION = 800
    val SHIMMER_CORNER_RADIUS = 12.dp
    
    // Typography
    const val DEFAULT_CRYPTO_PRICE_SIZE = 16
    
    // List Item Shimmer
    val LIST_ITEM_PADDING = 16.dp
    val LOGO_SHIMMER_SIZE = 56.dp
    const val TITLE_WIDTH_FRACTION = 0.6f
    const val SUBTITLE_WIDTH_FRACTION = 0.4f
    val TITLE_SHIMMER_HEIGHT = 16.dp
    val SUBTITLE_SHIMMER_HEIGHT = 12.dp
    val SPACER_HEIGHT = 8.dp
    const val LOADING_SHIMMER_COUNT = 5
    
    // Error View
    val ERROR_VIEW_PADDING = 16.dp
    val ERROR_TEXT_BOTTOM_PADDING = 16.dp
}

@Composable
fun ShimmerEffect(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = UIConstants.SHIMMER_MIN_ALPHA,
        targetValue = UIConstants.SHIMMER_MAX_ALPHA,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = UIConstants.SHIMMER_DURATION, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    Box(
        modifier = modifier
            .alpha(alpha)
            .background(
                color = colorScheme.outline,
                shape = RoundedCornerShape(UIConstants.SHIMMER_CORNER_RADIUS)
            )
    )
}

@Composable
fun CryptoPriceText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = colorScheme.onSurface,
    fontSize: Int = UIConstants.DEFAULT_CRYPTO_PRICE_SIZE
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace
    )
}

@Composable
fun ExchangeListItemShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(UIConstants.LIST_ITEM_PADDING),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerEffect(
            modifier = Modifier
                .size(UIConstants.LOGO_SHIMMER_SIZE)
                .background(color = colorScheme.surfaceVariant, shape = CircleShape)
        )
        Column(
            modifier = Modifier
                .padding(start = UIConstants.LIST_ITEM_PADDING)
                .weight(1f)
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth(UIConstants.TITLE_WIDTH_FRACTION)
                    .height(UIConstants.TITLE_SHIMMER_HEIGHT)
            )
            Spacer(modifier = Modifier.height(UIConstants.SPACER_HEIGHT))
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth(UIConstants.SUBTITLE_WIDTH_FRACTION)
                    .height(UIConstants.SUBTITLE_SHIMMER_HEIGHT)
            )
        }
    }
}

@Composable
fun ExchangeListLoadingShimmer() {
    LazyColumn {
        items(UIConstants.LOADING_SHIMMER_COUNT) {
            ExchangeListItemShimmer()
        }
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(UIConstants.ERROR_VIEW_PADDING),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = UIConstants.ERROR_TEXT_BOTTOM_PADDING)
        )
        Button(onClick = onRetry) {
            Text(stringResource(R.string.btn_retry))
        }
    }
}

@Composable
fun EmptyView(
    message: String = stringResource(R.string.empty_list),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
