package utility

sealed class FilesResult<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : FilesResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : FilesResult<T>(data, message)
    class Loading<T> : FilesResult<T>()

}