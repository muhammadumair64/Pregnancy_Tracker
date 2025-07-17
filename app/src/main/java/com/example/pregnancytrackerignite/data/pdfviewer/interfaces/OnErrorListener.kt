package com.example.pregnancytrackerignite.data.pdfviewer.interfaces

import java.lang.Exception

interface OnErrorListener {

    fun onFileLoadError(e : Exception)

    fun onAttachViewError(e : Exception)

    fun onPdfRendererError(e : Exception)
}