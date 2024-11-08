package com.cyber.restory.presentation.contact

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.cyber.restory.databinding.ActivityContactBinding
import com.cyber.restory.presentation.contact.viewmodel.ContactViewModel
import com.cyber.restory.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactActivity : AppCompatActivity() {
    private val binding: ActivityContactBinding by lazy { ActivityContactBinding.inflate(layoutInflater) }
    private val viewModel: ContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        with(binding) {
            etSpaceName.addTextChangedListener {
                viewModel.updateSpaceName(it.toString(), etSpaceName.selectionStart)
            }
            etSpaceAddress.addTextChangedListener {
                viewModel.updateSpaceAddress(it.toString(), etSpaceAddress.selectionStart)
            }
            etRequestDetails.addTextChangedListener {
                viewModel.updateRequestDetails(it.toString(), etRequestDetails.selectionStart)
            }
            btnSubmit.setOnClickListener {
//            viewModel.submitRequest()
                /*
                * 서버 요청 필요
                * */
                ToastUtils.showToast("준비중입니다.")
            }

            toolbarContact.setNavigationOnClickListener {
                finish()
            }
        }
    }

    // TODO: lifecycle 확장함수 공통화
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.submitSuccess.collect {
                ToastUtils.showToast("요청이 성공적으로 제출되었습니다.")
                finish()
            }
        }

        lifecycleScope.launch {
            viewModel.submitError.collect { errorMessage ->
                ToastUtils.showToast("요청 제출에 실패했습니다")
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateEditTextIfNeeded(binding.etSpaceName, state.spaceName, state.spaceNameCursor)
                updateEditTextIfNeeded(binding.etSpaceAddress, state.spaceAddress, state.spaceAddressCursor)
                updateEditTextIfNeeded(binding.etRequestDetails, state.requestDetails, state.requestDetailsCursor)
                binding.tvCharCount.text = state.charCount
                binding.btnSubmit.isEnabled = state.isSubmitEnabled
            }
        }
    }

    private fun updateEditTextIfNeeded(editText: EditText, newText: String, newCursorPosition: Int) {
        if (editText.text.toString() != newText) {
            editText.setText(newText)
            editText.setSelection(newCursorPosition.coerceIn(0, newText.length))
        }
    }
}