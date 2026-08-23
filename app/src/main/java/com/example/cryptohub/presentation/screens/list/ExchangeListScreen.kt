package com.example.cryptohub.presentation.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cryptohub.R
import com.example.cryptohub.core.util.formatUSD
import com.example.cryptohub.domain.models.ExchangeListItem
import com.example.cryptohub.presentation.components.CryptoPriceText
import com.example.cryptohub.presentation.components.EmptyView
import com.example.cryptohub.presentation.components.ErrorView
import com.example.cryptohub.presentation.components.ExchangeListLoadingShimmer
import org.koin.androidx.compose.koinViewModel

private object ListUIConstants {
    val SCREEN_PADDING = 16.dp
    val ITEM_SPACING = 12.dp
    val CARD_CORNER_RADIUS = 16.dp
    val CARD_ELEVATION = 2.dp
    val LOGO_CONTAINER_SIZE = 52.dp
    const val LOGO_ALPHA = 0.2f
    val PAGINATION_INDICATOR_SIZE = 24.dp
    val PAGINATION_INDICATOR_STROKE = 2.dp
    const val ANIMATION_OFFSET_FACTOR = 50
    const val PRICE_FONT_SIZE = 14
    const val DATE_TAKE_LENGTH = 10
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeListScreen(
    onExchangeClick: (ExchangeListItem) -> Unit,
    viewModel: ExchangeListViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_crypto_hub),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            stringResource(R.string.title_exchanges),
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.btn_retry))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            if (uiState.value.isLoading && uiState.value.exchanges.isEmpty()) {
                ExchangeListLoadingShimmer()
            } else if (uiState.value.error != null && uiState.value.exchanges.isEmpty()) {
                ErrorView(
                    message = uiState.value.error ?: stringResource(R.string.error_unknown),
                    onRetry = { viewModel.loadExchanges() }
                )
            } else if (uiState.value.exchanges.isEmpty() && !uiState.value.isLoading) {
                EmptyView()
            } else {
                ExchangesList(
                    exchanges = uiState.value.exchanges,
                    onExchangeClick = onExchangeClick,
                    isLoadingMore = uiState.value.isPaginationLoading,
                    onLoadMore = { viewModel.loadExchanges(isNextPage = true) }
                )
            }
        }
    }
}

@Composable
fun ExchangesList(
    exchanges: List<ExchangeListItem>,
    onExchangeClick: (ExchangeListItem) -> Unit,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(ListUIConstants.ITEM_SPACING),
        contentPadding = PaddingValues(ListUIConstants.SCREEN_PADDING)
    ) {
        itemsIndexed(exchanges) { index, exchange ->
            if (index >= exchanges.size - 1) {
                LaunchedEffect(Unit) {
                    onLoadMore()
                }
            }

            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { ListUIConstants.ANIMATION_OFFSET_FACTOR * (index + 1) }
                ) + fadeIn()
            ) {
                ExchangeListItemCard(
                    exchange = exchange,
                    onClick = { onExchangeClick(exchange) }
                )
            }
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ListUIConstants.SCREEN_PADDING),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(ListUIConstants.PAGINATION_INDICATOR_SIZE),
                        strokeWidth = ListUIConstants.PAGINATION_INDICATOR_STROKE,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun ExchangeListItemCard(
    exchange: ExchangeListItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(ListUIConstants.CARD_CORNER_RADIUS),
        elevation = CardDefaults.cardElevation(defaultElevation = ListUIConstants.CARD_ELEVATION)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ListUIConstants.SCREEN_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExchangeLogo(exchange.logo, exchange.name)

            Column(
                modifier = Modifier
                    .padding(start = ListUIConstants.SCREEN_PADDING)
                    .weight(1f)
            ) {
                Text(
                    text = exchange.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Launch: ${exchange.dateLaunched?.take(ListUIConstants.DATE_TAKE_LENGTH) ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ExchangeVolumeStat(exchange.spotVolumeUsd)
        }
    }
}

@Composable
private fun ExchangeLogo(logo: String?, name: String) {
    Box(
        modifier = Modifier
            .size(ListUIConstants.LOGO_CONTAINER_SIZE)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = ListUIConstants.LOGO_ALPHA)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = logo,
            contentDescription = name,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ExchangeVolumeStat(volumeUsd: Double) {
    Column(horizontalAlignment = Alignment.End) {
        CryptoPriceText(
            text = volumeUsd.formatUSD(),
            fontSize = ListUIConstants.PRICE_FONT_SIZE,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "24h Volume",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
