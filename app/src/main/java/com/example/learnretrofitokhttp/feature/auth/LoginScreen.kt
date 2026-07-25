package com.example.learnretrofitokhttp.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learnretrofitokhttp.R
import com.example.learnretrofitokhttp.ui.theme.LearnRetrofitOkHttpTheme

@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Calcul du message d'erreur à chaque recomposition
    val errorMessage = when (uiState.error) {
        AuthError.EMPTY_FIELDS -> {
            stringResource(R.string.login_error_empty_fields)
        }

        AuthError.INVALID_CREDENTIALS -> {
            stringResource(R.string.login_error_invalid_credentials)
        }

        AuthError.NETWORK -> {
            stringResource(R.string.login_error_network)
        }

        AuthError.SERVER -> {
            stringResource(R.string.login_error_server)
        }

        null -> null
    }

    // Construction de l'écran
    // imePadding()         ajoute l’espace occupé par le clavier virtuel.
    // verticalScroll()     permet de faire défiler le formulaire lorsque l’écran disponible
    //                      devient trop petit, notamment avec le clavier ouvert.

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = uiState.email,
            onValueChange = onEmailChanged,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
            label = {
                Text(stringResource(R.string.login_email_label))
            },
            isError = uiState.error != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChanged,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
            label = {
                Text(stringResource(R.string.login_password_label))
            },
            isError = uiState.error != null,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onLoginClick()
                }
            )
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )

                Spacer(modifier = Modifier.size(8.dp))
            }

            Text(
                text = if (uiState.isLoading) {
                    stringResource(R.string.login_in_progress)
                } else {
                    stringResource(R.string.login_button)
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LearnRetrofitOkHttpTheme {
        LoginScreen(
            uiState = AuthUiState(
                email = "utilisateur@example.com"
            ),
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClick = {}
        )
    }
}
