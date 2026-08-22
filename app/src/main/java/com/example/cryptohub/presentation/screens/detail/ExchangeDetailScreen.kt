package com.example.cryptohub.presentation.screens.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.cryptohub.R
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.presentation.components.ErrorView
import com.example.cryptohub.presentation.components.ExchangeListLoadingShimmer
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: ExchangeDetailViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.value.exchange?.name ?: stringResource(R.string.title_exchange_detail)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.btn_back))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.value.isLoading -> {
                    ExchangeListLoadingShimmer()
                }
                uiState.value.error != null -> {
                    ErrorView(
                        message = uiState.value.error ?: stringResource(R.string.error_unknown),
                        onRetry = { viewModel.loadExchangeDetail() }
                    )
                }
                uiState.value.exchange != null -> {
                    ExchangeDetailContent(exchange = uiState.value.exchange!!)
                }
            }
        }
    }
}

@Composable
private fun ExchangeDetailContent(exchange: ExchangeDetail) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = exchange.logo,
                        contentDescription = exchange.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = exchange.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ID: ${exchange.id}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Description
        if (!exchange.description.isNullOrEmpty()) {
            item {
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.label_description),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = exchange.description ?: "",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Website
        if (!exchange.website.isNullOrEmpty()) {
            item {
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.label_website),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = exchange.website ?: "",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Fees
        item {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (exchange.makerFee != null) {
                        Column {
                            Text(
                                text = stringResource(R.string.label_maker_fee),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${exchange.makerFee}%",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    if (exchange.takerFee != null) {
                        Column {
                            Text(
                                text = stringResource(R.string.label_taker_fee),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${exchange.takerFee}%",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        // Cryptocurrencies
        if (exchange.currencies.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.label_cryptocurrencies),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(exchange.currencies) { coin ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = coin.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = coin.symbol,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Text(
                            text = formatUSD(coin.priceUsd),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Remove local ErrorContent
private fun formatUSD(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(value)
}