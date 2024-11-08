package com.cyber.restory.presentation.contact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyber.restory.data.model.ContactRequest
import com.cyber.restory.domain.usecase.SubmitContactRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val submitContactRequestUseCase: SubmitContactRequestUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState

    private val _submitSuccess = MutableSharedFlow<Unit>()
    val submitSuccess: SharedFlow<Unit> = _submitSuccess

    private val _submitError = MutableSharedFlow<String>()
    val submitError: SharedFlow<String> = _submitError

    fun updateSpaceName(name: String, cursorPosition: Int) {
        _uiState.update { it.copy(
            spaceName = name,
            spaceNameCursor = cursorPosition
        ) }
        updateSubmitButtonState()
    }

    fun updateSpaceAddress(address: String, cursorPosition: Int) {
        _uiState.update { it.copy(
            spaceAddress = address,
            spaceAddressCursor = cursorPosition
        ) }
        updateSubmitButtonState()
    }

    fun updateRequestDetails(details: String, cursorPosition: Int) {
        _uiState.update { it.copy(
            requestDetails = details,
            requestDetailsCursor = cursorPosition
        ) }
        updateCharCount()
        updateSubmitButtonState()
    }

    private fun updateCharCount() {
        val currentLength = _uiState.value.requestDetails.length
        _uiState.update { it.copy(charCount = "$currentLength/500") }
    }

    private fun updateSubmitButtonState() {
        val isEnabled = _uiState.value.run {
            spaceName.isNotEmpty() && spaceAddress.isNotEmpty() && requestDetails.isNotEmpty()
        }
        _uiState.update { it.copy(isSubmitEnabled = isEnabled) }
    }

    // TODO 추후 서버 API 연동
    fun submitRequest() {
        viewModelScope.launch {
            val request = ContactRequest(
                spaceName = _uiState.value.spaceName,
                spaceAddress = _uiState.value.spaceAddress,
                requestDetails = _uiState.value.requestDetails
            )
            runCatching {
                submitContactRequestUseCase(request)
            }.onSuccess {
                _submitSuccess.emit(Unit)
            }.onFailure { error ->
                _submitError.emit(error.message ?: "Unknown error occurred")
            }
        }
    }
}

data class ContactUiState(
    val spaceName: String = "",
    val spaceNameCursor: Int = 0,
    val spaceAddress: String = "",
    val spaceAddressCursor: Int = 0,
    val requestDetails: String = "",
    val requestDetailsCursor: Int = 0,
    val charCount: String = "0/500",
    val isSubmitEnabled: Boolean = false
)