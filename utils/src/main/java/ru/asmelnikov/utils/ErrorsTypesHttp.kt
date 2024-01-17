package ru.asmelnikov.utils

sealed class ErrorsTypesHttp(val code: Int? = null, val errorMessage: String? = null) {
    object Https400Errors : ErrorsTypesHttp()
    object Https500Errors : ErrorsTypesHttp()
    object TimeoutException : ErrorsTypesHttp()
    object MissingConnection : ErrorsTypesHttp()
    object PhotoUploadError : ErrorsTypesHttp()
    object FileSizeError : ErrorsTypesHttp()
    object EmptyFileError : ErrorsTypesHttp()
    object AlreadyConfirmed : ErrorsTypesHttp()
    class CloudDatabaseError(code: Int? = null) : ErrorsTypesHttp(code = code)
    class UnknownError(errorMessage: String? = null) : ErrorsTypesHttp(errorMessage = errorMessage)
    object VerificationError : ErrorsTypesHttp()
    object RolesError : ErrorsTypesHttp()
    object NotFoundInDataBaseError : ErrorsTypesHttp()
}
