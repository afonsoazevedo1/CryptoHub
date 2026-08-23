package com.example.cryptohub.presentation.screens.detail

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.cryptohub.R
import com.example.cryptohub.core.util.formatUSD
import com.example.cryptohub.domain.models.Coin
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.presentation.components.CryptoPriceText
import com.example.cryptohub.presentation.components.ErrorView
import com.example.cryptohub.presentation.components.ExchangeListLoadingShimmer
import org.koin.androidx.compose.koinViewModel

private object DetailUIConstants {
    val SCREEN_PADDING = 16.dp
    val SECTION_SPACING = 24.dp
    val HEADER_IMAGE_SIZE = 100.dp
    val LOGO_SIZE = 70.dp
    val HEADER_RADIUS = 24.dp
    val CARD_RADIUS = 16.dp
    val INFO_CARD_PADDING = 16.dp
    val STAT_CARD_SPACING = 12.dp
    const val LOGO_BG_ALPHA = 0.1f
    const val WEBSITE_SURFACE_ALPHA = 0.1f
    val WEBSITE_HORIZONTAL_PADDING = 12.dp
    val WEBSITE_VERTICAL_PADDING = 6.dp
    val ICON_SIZE = 14.dp
    val SPACER_HEIGHT_SMALL = 4.dp
    val SPACER_HEIGHT_MEDIUM = 12.dp
    val SPACER_HEIGHT_LARGE = 16.dp
    val SPACER_WIDTH = 6.dp
    val BOTTOM_SPACER_HEIGHT = 32.dp
    val LINE_HEIGHT = 22.sp
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: ExchangeDetailViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.value.exchange?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Filled.ArrowBack,
                            contentDescription = stringResource(R.string.btn_back),
                            tint = colorScheme.onSurface
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(paddingValues)
        ) {
            when {
                uiState.value.isLoading && uiState.value.exchange?.description == null -> {
                    ExchangeListLoadingShimmer()
                }
                uiState.value.error != null -> {
                    ErrorView(
                        message = uiState.value.error ?: stringResource(R.string.error_unknown),
                        onRetry = { viewModel.loadExchangeDetail() }
                    )
                }
                uiState.value.exchange != null -> {
                    ExchangeDetailContent(
                        exchange = uiState.value.exchange!!,
                        onWebsiteClick = { url ->
                            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExchangeDetailContent(
    exchange: ExchangeDetail,
    onWebsiteClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(DetailUIConstants.SECTION_SPACING),
        contentPadding = PaddingValues(DetailUIConstants.SCREEN_PADDING)
    ) {
        item {
            ExchangeHeaderCard(exchange, onWebsiteClick)
        }

        item {
            ExchangeStatsSection(exchange)
        }

        val description = exchange.description
        if (!description.isNullOrEmpty()) {
            item {
                ExchangeDescriptionSection(description)
            }
        }

        if (exchange.currencies.isNotEmpty()) {
            item {
                SectionTitle(stringResource(R.string.label_cryptocurrencies))
            }

            items(exchange.currencies) { coin ->
                AssetListItem(coin)
            }
        }

        item { Spacer(modifier = Modifier.height(DetailUIConstants.BOTTOM_SPACER_HEIGHT)) }
    }
}

@Composable
private fun ExchangeHeaderCard(
    exchange: ExchangeDetail,
    onWebsiteClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        shape = RoundedCornerShape(DetailUIConstants.HEADER_RADIUS)
    ) {
        Column(
            modifier = Modifier.padding(DetailUIConstants.SECTION_SPACING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(DetailUIConstants.HEADER_IMAGE_SIZE)
                    .clip(CircleShape)
                    .background(colorScheme.primary.copy(alpha = DetailUIConstants.LOGO_BG_ALPHA)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = exchange.logo,
                    contentDescription = exchange.name,
                    modifier = Modifier
                        .size(DetailUIConstants.LOGO_SIZE)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(DetailUIConstants.SPACER_HEIGHT_LARGE))
            Text(
                text = exchange.name,
                style = typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Exchange ID: ${exchange.id}",
                style = typography.bodySmall,
                color = colorScheme.onSurfaceVariant
            )

            val website = exchange.website
            if (!website.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(DetailUIConstants.SPACER_HEIGHT_MEDIUM))
                WebsiteLink(website, onWebsiteClick)
            }
        }
    }
}

@Composable
private fun WebsiteLink(
    url: String,
    onClick: (String) -> Unit,
) {
    Surface(
        onClick = { onClick(url) },
        color = colorScheme.primary.copy(alpha = DetailUIConstants.WEBSITE_SURFACE_ALPHA),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = DetailUIConstants.WEBSITE_HORIZONTAL_PADDING,
                vertical = DetailUIConstants.WEBSITE_VERTICAL_PADDING
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(DetailUIConstants.ICON_SIZE),
                tint = colorScheme.primary
            )
            Spacer(modifier = Modifier.width(DetailUIConstants.SPACER_WIDTH))
            Text(
                text = url,
                style = typography.labelMedium,
                color = colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
private fun ExchangeStatsSection(exchange: ExchangeDetail) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(DetailUIConstants.STAT_CARD_SPACING)
    ) {
        InfoStatCard(
            label = "Maker Fee",
            value = "${exchange.makerFee ?: 0.0}%",
            modifier = Modifier.weight(1f)
        )
        InfoStatCard(
            label = "Taker Fee",
            value = "${exchange.takerFee ?: 0.0}%",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ExchangeDescriptionSection(description: String) {
    Column {
        SectionTitle(stringResource(R.string.label_description))
        Text(
            text = description,
            style = typography.bodyMedium,
            color = colorScheme.onSurfaceVariant,
            lineHeight = DetailUIConstants.LINE_HEIGHT
        )
    }
}

@Composable
private fun InfoStatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        shape = RoundedCornerShape(DetailUIConstants.CARD_RADIUS)
    ) {
        Column(modifier = Modifier.padding(DetailUIConstants.INFO_CARD_PADDING)) {
            Text(
                text = label,
                style = typography.labelSmall,
                color = colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(DetailUIConstants.SPACER_HEIGHT_SMALL))
            Text(
                text = value,
                style = typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = typography.titleMedium,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(bottom = DetailUIConstants.WEBSITE_HORIZONTAL_PADDING),
        color = colorScheme.primary
    )
}

@Composable
private fun AssetListItem(coin: Coin) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = DetailUIConstants.SPACER_HEIGHT_MEDIUM),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = coin.name, style = typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(
                text = coin.symbol,
                style = typography.bodySmall,
                color = colorScheme.onSurfaceVariant
            )
        }
        CryptoPriceText(
            text = coin.priceUsd?.formatUSD() ?: "N/A",
            color = if (coin.priceUsd != null) colorScheme.secondary else colorScheme.onSurfaceVariant
        )
    }
}
