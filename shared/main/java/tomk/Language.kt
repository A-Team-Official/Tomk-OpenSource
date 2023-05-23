package tomk

import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class Language(val locale: String) {

    private val translateMap = HashMap<String, String>()

    init {
        read(locale)
    }

    private fun find(): InputStream {
        val split = locale.replace("-", "_").split("_")

        if(split.size > 1) {
            val str = split[0].toLowerCase() + "-" + split[1].toUpperCase()
            LanguageManager::class.java.classLoader.getResourceAsStream("assets/minecraft/liquidbounce/translations/${str}/source.properties")?.let {
                return it
            }
        }

        val str = split[0].toLowerCase()
        LanguageManager::class.java.classLoader.getResourceAsStream("assets/minecraft/liquidbounce/translations/${str}/source.properties")?.let {
            return it
        }

        LanguageManager::class.java.classLoader.getResourceAsStream("assets/minecraft/liquidbounce/translations/source.properties")?.let {
            return it
        }

        throw IllegalStateException("Can't find language file! Try sync gitsubmodule if this is a custom build!")
    }

    private fun read(locale: String) {
        val prop = Properties()

        prop.load(InputStreamReader(find(), Charsets.UTF_8))

        for ((key, value) in prop.entries) {
            if (key is String && value is String) {
                translateMap[key] = value
            }
        }
    }

    fun get(key: String): String {
        return translateMap[key] ?: key
    }
}