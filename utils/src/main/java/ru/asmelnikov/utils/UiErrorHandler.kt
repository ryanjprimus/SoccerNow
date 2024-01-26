package ru.asmelnikov.utils

fun ErrorsTypesHttp.getErrorMessage(stringResourceProvider: StringResourceProvider): String {
    return when (this) {
        is ErrorsTypesHttp.Https400Errors -> {
            when (this.errorCode) {
                429 -> stringResourceProvider.getString(
                    R.string.http_429_errors,
                    arrayOf(this.code ?: "")
                )

                else -> stringResourceProvider.getString(
                    R.string.http_400_errors,
                    arrayOf(this.code ?: "")
                )
            }
        }

        is ErrorsTypesHttp.Https500Errors -> {
            stringResourceProvider.getString(
                R.string.http_500_errors,
                arrayOf(this.errorMessage ?: "")
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
                R.string.http_network_error,
                arrayOf(this.errorMessage ?: "")
            )
        }

        else -> {
            stringResourceProvider.getString(
                R.string.http_unknown_error,
                arrayOf(this.errorMessage ?: "")
            )
        }
    }
}