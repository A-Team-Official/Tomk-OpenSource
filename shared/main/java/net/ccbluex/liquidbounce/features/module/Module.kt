/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.Listenable
import net.ccbluex.liquidbounce.injection.backend.Backend
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.render.ColorUtils.stripColor
import net.ccbluex.liquidbounce.value.Value
import org.lwjgl.input.Keyboard

open class Module : MinecraftInstance(), Listenable {
    var isSupported: Boolean

    // Module information
    // TODO: Remove ModuleInfo and change to constructor (#Kotlin)
    var name: String
    var description: String
    var category: ModuleCategory
    var keyBind = Keyboard.CHAR_NONE
        set(keyBind) {
            field = keyBind

            if (!LiquidBounce.isStarting)
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.modulesConfig)
        }
    var array = true
        set(array) {
            field = array

            if (!LiquidBounce.isStarting)
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.modulesConfig)
        }
    private val canEnable: Boolean

    var slideStep = 0F

    init {
        val moduleInfo = javaClass.getAnnotation(ModuleInfo::class.java)!!

        name = moduleInfo.name
        description = moduleInfo.description
        category = moduleInfo.category
        keyBind = moduleInfo.keyBind
        array = moduleInfo.array
        canEnable = moduleInfo.canEnable
        isSupported = Backend.REPRESENTED_BACKEND_VERSION in moduleInfo.supportedVersions
    }

    // Current state of module
    var state = false
        set(value) {
            if (field == value)
                return

            // Call toggle
            onToggle(value)

            // Play sound and add notification
            if (!LiquidBounce.isStarting) {
                if(value){
                    mc.soundHandler.playSound("random.click", 1F)
                    LiquidBounce.hud.addNotification(Notification("Module toggled", name + " is " +"Enabled", NotifyType.SUCCESS))
                }else{
                    mc.soundHandler.playSound("random.click", 1F)
                    LiquidBounce.hud.addNotification(Notification("Module toggled",name + " is " +"Disabled", NotifyType.ERROR))
                }
            }

            // Call on enabled or disabled
            if (value) {
                onEnable()

                if (canEnable)
                    field = true
            } else {
                onDisable()
                field = false
            }

            // Save module state
            LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.modulesConfig)
        }


    // HUD
    val hue = Math.random().toFloat()
    var slide = 0F
    var BreakName : Boolean = false
    var higt = 0F

    // Tag
    open val tag: String?
        get() = null

    val tagName: String
        get() = "$name${if (tag == null) "" else " §7$tag"}"

    val colorlessTagName: String
        get() = "$name${if (tag == null) "" else " " + stripColor(tag)}"

    fun breakname(toggle : Boolean) : String {
        var detName = name

        if(toggle) {
            when (detName.toLowerCase()) {
                "autotool" -> return "Auto Tool"
                "noslow" -> return "No Slow"
                "chestaura" -> return "Chest Aura"
                "cheststealer" -> return "Chest Stealer"
                "invcleaner" -> return "Inv Cleaner"
                "invmove" -> return "Inv Move"
                "autopot" -> return "Auto Potion"
                "blockfly" -> return "Block Fly"
                "fastbreak" -> return "Fast Break"
                "fastplace" -> return "Fast Place"
                "noswing" -> return "No Swing"
                "nametags" -> return "Name Tags"
                "itemesp" -> return "Item ESP"
                "freecam" -> return "Free Cam"
                "blockesp" -> return "Block ESP"
                "antiblind" -> return "Anti Blind"
                "blockoverlay" -> return "Block Overlay"
                "nofall" -> return "No Fall"
                "fastuse" -> return "Fast Use"
                "autorespawn" -> return "Auto Respawn"
                "autofish" -> return "Auto Fish"
                "antiafk" -> return "Anti AFK"
                "anticactus" -> return "Anti Cactus"
                "potionsaver" -> return "Potion Saver"
                "safewalk" -> return "Safe Walk"
                "noweb" -> return "No Web"
                "noclip" -> return "No Clip"
                "longjump" -> return "Long Jump"
                "liquidwalk" -> return "Liquid Walk"
                "highjump" -> return "High Jump"
                "icespeed" -> return "Ice Speed"
                "faststairs" -> return "Fast Stairs"
                "fastclimb" -> return "Fast Climb"
                "antifall" -> return "Anti Fall"
                "bufferspeed" -> return "Buffer Speed"
                "blockwalk" -> return "Block Walk"
                "autowalk" -> return "Auto Walk"
                "midclick" -> return "Mid Click"
                "liquidchat" -> return "Liquid Chat"
                "antibot" -> return "Anti Bot"
                "skinderp" -> return "Skin Derp"
                "pingspoof" -> return "Ping Spoof"
                "consolespammer" -> return "Console Spammer"
                "keepcontainer" -> return "Keep Container"
                "abortbreaking" -> return "Abort Breaking"
                "nofriends" -> return "No Friends"
                "hitbox" -> return "Hit Box"
                "fastbow" -> return "Fast Bow"
                "bowaimbot" -> return "Bow Aimbot"
                "autoweapon" -> return "Auto Weapon"
                "autosoup" -> return "Auto Soup"
                "autoclicker" -> return "Auto Clicker"
                "autobow" -> return "Auto Bow"
                "autoarmor" -> return "Auto Armor"
                "speedmine" -> return "Speed Mine"
                "targethud" -> return "Target HUD"
                "pointeresp" -> return "Pointer ESP"
                "playerface" -> return "Player Face"
                "itemrotate" -> return "Item Rotate"
                "targetstrafe" -> return "Target Strafe"
                "hytrun" -> return "HYT Run"
                "autojump" -> return "Auto Jump"
                "memoryfixer" -> return "Memory Fixer"
                "lagback" -> return "Lag Back"
                "autohead" -> return "Auto Head"
                "autosword" -> return "Auto Sword"
                "keepalive" -> return "Keep Alive"
                "killaura" -> return "Kill Aura"



            }
        }
        return detName
    }
    /**
     * Toggle module
     */
    fun toggle() {
        state = !state
    }

    /**
     * Called when module toggled
     */
    open fun onToggle(state: Boolean) {}

    /**
     * Called when module enabled
     */
    open fun onEnable() {}

    /**
     * Called when module disabled
     */
    open fun onDisable() {}

    /**
     * Get module by [valueName]
     */
    open fun getValue(valueName: String) = values.find { it.name.equals(valueName, ignoreCase = true) }

    /**
     * Get all values of module
     */
    open val values: List<Value<*>>
        get() = javaClass.declaredFields.map { valueField ->
            valueField.isAccessible = true
            valueField[this]
        }.filterIsInstance<Value<*>>()

    /**
     * Events should be handled when module is enabled
     */
    override fun handleEvents() = state
}