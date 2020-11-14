package files

class FileNotFoundException(filename: String, message: String = "$filename does not exist"): RuntimeException(message)
