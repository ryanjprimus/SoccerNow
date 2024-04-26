package com.primus.utils

import android.content.Context
import androidx.annotation.StringRes

// todo remove interface in domain layer and impl in data layer?
interface StringResourceProvider {

    fun getString(@StringRes resourceId: Int): String

    class StringResourceProviderImpl(private val context: Context) : StringResourceProvider {
        override fun getString(resourceId: Int): String {
            return context.getString(resourceId)
        }
    }
}