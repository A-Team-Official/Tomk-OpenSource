package tomk

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.ccbluex.liquidbounce.value.Value
import java.awt.Color

class ColorSetting(name: String, value: Color, canDisplay: () -> Boolean = { true }) :
    Value<Color>(name, value) {

    var hue = 0f
    var saturation = 1f
    var brightness = 1f
    var color: Color
        get() = Color.getHSBColor(hue, saturation, brightness)
        set(color) {
            var hsb = FloatArray(3)
            hsb = Color.RGBtoHSB(color.red, color.green, color.blue, hsb)
            hue = hsb[0]
            saturation = hsb[1]
            brightness = hsb[2]
        }
    @JvmName("getColor1")
    fun getColor(): Color? {
        return Color.getHSBColor(hue, saturation, brightness)
    }

    fun setColor(hue: Float, saturation: Float, brightness: Float) {
        this.hue = hue
        this.saturation = saturation
        this.brightness = brightness
    }
    @JvmName("getHue1")
    fun getHue(): Double {
        return hue.toDouble()
    }

    @JvmName("setHue1")
    fun setHue(hue: Float) {
        this.hue = hue
    }
    @JvmName("getSaturation1")
    fun getSaturation(): Double {
        return saturation.toDouble()
    }

    @JvmName("setSaturation1")
    fun setSaturation(saturation: Float) {
        this.saturation = saturation
    }
    @JvmName("getBrightness1")
    fun getBrightness(): Double {
        return brightness.toDouble()
    }

    @JvmName("setBrightness1")
    fun setBrightness(brightness: Float) {
        this.brightness = brightness
    }

    fun getConfigValue(): Int? {
        return getColor()!!.getRGB()
    }
    fun set(newValue: Number) {
        set(newValue.toInt())
    }
    override fun toJson() = JsonPrimitive(value.rgb)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = Color(element.asInt)
    }
}