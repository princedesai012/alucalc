package com.softellix.alucalc.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softellix.alucalc.data.model.InvoiceRequest
import com.softellix.alucalc.data.model.InvoiceResponse
import com.softellix.alucalc.data.repository.AluRepository
import com.softellix.alucalc.utils.PdfReportGenerator
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class InvoiceViewModel(
    private val repository: AluRepository = AluRepository()
) : ViewModel() {

    var invoicesList by mutableStateOf<List<InvoiceResponse>>(emptyList())
        private set

    var currentInvoice by mutableStateOf<InvoiceResponse?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchInvoices() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.getInvoices().onSuccess { list ->
                invoicesList = list
            }.onFailure { err ->
                errorMessage = err.localizedMessage
            }
            isLoading = false
        }
    }

    fun fetchInvoiceDetails(invoiceId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.getInvoice(invoiceId).onSuccess { inv ->
                currentInvoice = inv
            }.onFailure { err ->
                errorMessage = err.localizedMessage
            }
            isLoading = false
        }
    }

    fun createInvoice(req: InvoiceRequest, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.createInvoice(req).onSuccess { inv ->
                onSuccess(inv.id)
            }.onFailure { err ->
                errorMessage = err.localizedMessage
            }
            isLoading = false
        }
    }

    fun deleteInvoice(invoiceId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.deleteInvoice(invoiceId).onSuccess {
                currentInvoice = null
                onSuccess()
            }.onFailure { err ->
                errorMessage = err.localizedMessage
            }
            isLoading = false
        }
    }

    fun downloadAndShareInvoicePdf(
        context: Context,
        invoiceId: String,
        invoiceNo: String,
        isShare: Boolean
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.downloadInvoicePdf(invoiceId).onSuccess { responseBody ->
                try {
                    val safeInvoiceNo = invoiceNo.replace("/", "_").replace("\\", "_")
                    val fileName = "Invoice_$safeInvoiceNo.pdf"
                    val downloadsDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
                    val pdfFile = File(downloadsDir, fileName)

                    FileOutputStream(pdfFile).use { out ->
                        out.write(responseBody.bytes())
                    }

                    if (!isShare) {
                        android.widget.Toast.makeText(context, "Invoice PDF Downloaded!", android.widget.Toast.LENGTH_SHORT).show()
                    }

                    PdfReportGenerator.openOrSharePdf(context, pdfFile, isShare)
                } catch (e: Exception) {
                    errorMessage = "Failed to save PDF: ${e.localizedMessage}"
                }
            }.onFailure { err ->
                errorMessage = err.localizedMessage
            }
            isLoading = false
        }
    }
}
