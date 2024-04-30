package com.primus.utils

import android.content.Context
import androidx.annotation.StringRes

interface StringResourceProvider {

    fun getString(@StringRes resourceId: Int): String

    class StringResourceProviderImpl(private val context: Context) : StringResourceProvider {
        override fun getString(resourceId: Int): String {
            return context.getString(resourceId)
        }
    }
}