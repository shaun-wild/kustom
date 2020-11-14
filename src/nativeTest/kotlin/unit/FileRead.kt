import files.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FileRead {

    val file = File("src/nativeTest/resources/example.ku")

    val fileContents = "a = 5\nprintln(a)"

    @Test
    fun `file exists`() {
        assertTrue(file.exists())
    }

    @Test
    fun `read file`() {
        val lines = file.readAllLines()
        assertEquals(fileContents, lines)
    }
}
