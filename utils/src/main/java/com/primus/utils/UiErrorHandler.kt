package com.primus.utils

fun ErrorsTypesHttp.getErrorMessage(stringResourceProvider: StringResourceProvider): String {
    return when (this) {
        is ErrorsTypesHttp.Https400Errors -> {
            when (this.errorCode) {
                403 -> stringResourceProvider.getString(
                    R.string.http_403_errors
                )

                429 -> stringResourceProvider.getString(
                    R.string.http_429_errors
                )

                else -> stringResourceProvider.getString(
                    R.string.http_400_errors
                )
            }
        }

        is ErrorsTypesHttp.Https500Errors -> {
            stringResourceProvider.getString(
                R.string.http_500_errors
            )
        }

        is ErrorsTypesHttp.TimeoutException -> {
            stringResourceProvider.getString(R.string.http_timeout_error)
        }

        is ErrorsTypesHttp.MissingConnection -> {
            stringResourceProvider.getString(R.string.http_missing_error)
        }

        is ErrorsTypesHttp.NetworkError -> {
            stringResourceProvider.getString(
                R.string.http_network_error
            )
        }

        else -> {
            stringResourceProvider.getString(
                R.string.http_unknown_error
            )
        }
    }
}