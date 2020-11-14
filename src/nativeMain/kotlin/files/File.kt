package files

import kotlinx.cinterop.*
import platform.posix.FILE
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen

/**
 * Used for reading files.
 * */
class File(val filename: String) {

    companion object {
        const val LINE_SEPARATOR = "\n"
        const val EMPTY = ""
    }

    val name = filename.substringAfterLast("/")

    fun exists(): Boolean {
        return accessFile { mem, handle ->
            handle != null
        }
    }

    fun readAllLines() = lines()
        .joinToString(LINE_SEPARATOR)

    fun lines(): Sequence<String> {
        return accessFile { mem, handle ->
            val list = mutableListOf<String>()

            val bufferLength = 64 * 1024
            val buffer = mem.allocArray<ByteVar>(bufferLength)

            while (true) {
                val nextLine = fgets(buffer, bufferLength, handle)?.toKString()

                if (nextLine == null || nextLine.isEmpty()) {
                    break
                } else {
                    list += nextLine.replace(LINE_SEPARATOR, EMPTY)
                }
            }

            list.asSequence()
        }
    }

    private fun <T> accessFile(accessor: (MemScope, CPointer<FILE>?) -> (T)): T {
        val handle = open()

        try {
            return memScoped {
                accessor.invoke(memScope, handle)
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        } finally {
            if (handle != null) {
                close(handle)
            }
        }
    }

    private fun open(): CPointer<FILE>? {
        return fopen(filename, "r")
    }

    private fun close(handle: CPointer<FILE>): Int {
        return fclose(handle)
    }

    override fun toString()= "File(name=$name)"
}
