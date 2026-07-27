package com.example.learnretrofitokhttp.feature.tests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learnretrofitokhttp.R
import com.example.learnretrofitokhttp.data.remote.dto.BatteryDto
import com.example.learnretrofitokhttp.data.remote.dto.ProtocolDto
import com.example.learnretrofitokhttp.data.remote.dto.SmartphoneDto
import com.example.learnretrofitokhttp.data.remote.dto.SmartphoneModelDto
import com.example.learnretrofitokhttp.data.remote.dto.TestDto
import com.example.learnretrofitokhttp.ui.theme.LearnRetrofitOkHttpTheme

@Composable
fun TestsScreen(
    uiState: TestsUiState,
    onRetry: () -> Unit,
    onLogout: () -> Unit,
    isLoggingOut: Boolean,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        TestsUiState.Loading -> {
            LoadingContent(
                modifier = modifier
            )
        }

        is TestsUiState.Content -> {
            TestsContent(
                tests = uiState.tests,
                onLogout = onLogout,
                isLoggingOut = isLoggingOut,
                modifier = modifier
            )
        }

        is TestsUiState.Error -> {
            ErrorContent(
                error = uiState.reason,
                onRetry = onRetry,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun TestsContent(
    tests: List<TestDto>,
    onLogout: () -> Unit,
    isLoggingOut: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.tests_title),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedButton(
                onClick = onLogout,
                enabled = !isLoggingOut
            ) {
                Text(
                    text = if (isLoggingOut) {
                        stringResource(R.string.logout_in_progress)
                    } else {
                        stringResource(R.string.logout)
                    }
                )
            }
        }

        if (tests.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.tests_empty)
                )
            }
        } else {

            // Ne charge que les élements visibles.
            // Adapté aux listes longues
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = tests,
                    key = { test -> test.id }
                ) { test ->
                    TestCard(
                        test = test,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun TestCard(
    test: TestDto,
    modifier: Modifier = Modifier
) {
    val unavailable = stringResource(R.string.test_not_available)

    val smartphoneModel =
        test.smartphone?.model

    val smartphoneName = listOfNotNull(
        smartphoneModel?.manufacturer,
        smartphoneModel?.marketName
    )
        .joinToString(separator = " ")
        .ifBlank { unavailable }

    val batteryDescription =
        test.battery?.description ?: unavailable

    val protocolDescription =
        test.protocol?.description ?: unavailable

    val status =
        test.status ?: unavailable

    val identificationCount =
        test.identifications?.size ?: 0

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = test.description
                    ?: stringResource(
                        R.string.test_unknown_description
                    ),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(
                    R.string.test_status,
                    status
                )
            )

            Text(
                text = stringResource(
                    R.string.test_smartphone,
                    smartphoneName
                )
            )

            Text(
                text = stringResource(
                    R.string.test_battery,
                    batteryDescription
                )
            )

            Text(
                text = stringResource(
                    R.string.test_protocol,
                    protocolDescription
                )
            )

            Text(
                text = stringResource(
                    R.string.test_identifications,
                    identificationCount
                )
            )
        }
    }
}

@Composable
private fun ErrorContent(
    error: TestsError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val message = when (error) {
        TestsError.SESSION_EXPIRED -> {
            stringResource(
                R.string.tests_error_session_expired
            )
        }

        TestsError.ACCESS_DENIED -> {
            stringResource(
                R.string.tests_error_access_denied
            )
        }

        TestsError.NETWORK -> {
            stringResource(
                R.string.tests_error_network
            )
        }

        TestsError.SERVER -> {
            stringResource(
                R.string.tests_error_server
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.tests_retry)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestsScreenPreview() {
    LearnRetrofitOkHttpTheme {
        TestsScreen(
            uiState = TestsUiState.Content(
                tests = listOf(
                    TestDto(
                        id = "test-1",
                        status = "completed",
                        description = "Test de décharge",
                        dateCreated = null,
                        smartphone = SmartphoneDto(
                            id = "smartphone-1",
                            model = SmartphoneModelDto(
                                id = "model-1",
                                manufacturer = "Samsung",
                                marketName = "Galaxy A14"
                            )
                        ),
                        battery = BatteryDto(
                            id = "battery-1",
                            description = "Batterie principale",
                            designCapacity = 5000.0
                        ),
                        protocol = ProtocolDto(
                            id = "protocol-1",
                            description = "Décharge standard"
                        ),
                        identifications = emptyList()
                    )
                )
            ),
            onRetry = {},
            onLogout = {},
            isLoggingOut = false
        )
    }
}