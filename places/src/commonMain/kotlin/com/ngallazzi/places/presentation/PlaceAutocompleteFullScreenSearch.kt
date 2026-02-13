package com.ngallazzi.places.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.Locale
import com.ngallazzi.places.KMPPlaces
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PlaceAutocompleteFullScreenSearch(
    searchHint: String,
    useText: String,
    languageCode: String = Locale.current.language,
    leadingIcon: ImageVector,
    clearIcon: ImageVector?,
    onClose: () -> Unit,
    onError: (Throwable) -> Unit,
    onExpandedChanged: (Boolean) -> Unit,
    onAddressSelected: (AddressResult) -> Unit,
) {
    val helper = remember {
        PlacesHelper(KMPPlaces.getApiKey())
    }

    val coroutineScope = rememberCoroutineScope()

    val viewModel = remember {
        PlaceAutoCompleteTextFieldModel(
            helper = helper,
            languageCode = languageCode,
            initialText = ""
        )
    }

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.error) {
        state.error?.let { onError(it) }
    }

    var expanded by rememberSaveable { mutableStateOf(true) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            inputField = {
                SearchBarDefaults.InputField(
                    modifier = Modifier.focusRequester(focusRequester),
                    query = state.textFieldValue.text,
                    onQueryChange = {
                        viewModel.onValueChange(
                            TextFieldValue(
                                text = it,
                                selection = TextRange(it.length)
                            )
                        )
                    },
                    onSearch = {},
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(searchHint) },
                    leadingIcon = {
                        IconButton(onClick = onClose) {
                            Icon(leadingIcon, null)
                        }
                    },
                    trailingIcon = {
                        if (state.textFieldValue.text.isNotEmpty() && clearIcon != null) {
                            IconButton(onClick = {
                                viewModel.onValueChange(TextFieldValue(""))
                            }) {
                                Icon(clearIcon, null)
                            }
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = {
                expanded = it
                onExpandedChanged(it)
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(state.suggestions) { suggestion ->
                    ListItem(
                        headlineContent = {
                            Text(suggestion.description)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    viewModel.onSuggestionSelected(
                                        suggestion,
                                        onPlaceDetailsRetrieved = {
                                            onAddressSelected(
                                                AddressResult.FromPlaces(it)
                                            )
                                        }
                                    )
                                }
                            }
                    )
                }

                val typedText = state.textFieldValue.text

                if (typedText.isNotBlank()) {
                    item {
                        ListItem(
                            headlineContent = {
                                Text("$useText \"$typedText\"")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAddressSelected(
                                        AddressResult.Manual(typedText)
                                    )
                                }
                        )
                    }
                }
            }
        }
    }
}
