/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.ccbluex.liquidbounce.api.Wrapper
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import net.ccbluex.liquidbounce.cape.CapeAPI.registerCapeService
import net.ccbluex.liquidbounce.event.ClientShutdownEvent
import net.ccbluex.liquidbounce.event.EventManager
import net.ccbluex.liquidbounce.features.command.CommandManager
import net.ccbluex.liquidbounce.features.command.commands.*
import net.ccbluex.liquidbounce.features.module.ModuleManager
import net.ccbluex.liquidbounce.features.module.modules.combat.*
import net.ccbluex.liquidbounce.features.module.modules.exploit.*
import net.ccbluex.liquidbounce.features.module.modules.misc.*
import net.ccbluex.liquidbounce.features.module.modules.movement.*
import net.ccbluex.liquidbounce.features.module.modules.player.*
import net.ccbluex.liquidbounce.features.module.modules.render.*
import net.ccbluex.liquidbounce.features.module.modules.tomk.*
import net.ccbluex.liquidbounce.features.module.modules.world.*
import net.ccbluex.liquidbounce.features.module.modules.world.Timer
import net.ccbluex.liquidbounce.features.special.AntiForge
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof
import net.ccbluex.liquidbounce.features.special.DonatorCape
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.injection.backend.Backend
import net.ccbluex.liquidbounce.script.ScriptManager
import net.ccbluex.liquidbounce.script.remapper.Remapper.loadSrg
import net.ccbluex.liquidbounce.tabs.BlocksTab
import net.ccbluex.liquidbounce.tabs.ExploitsTab
import net.ccbluex.liquidbounce.tabs.HeadsTab
import net.ccbluex.liquidbounce.ui.client.GuiWelcome
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui
import net.ccbluex.liquidbounce.ui.client.hud.HUD
import net.ccbluex.liquidbounce.ui.client.hud.HUD.Companion.createDefault
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ClassUtils.hasForge
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.InventoryUtils
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.misc.HttpUtils
import net.ccbluex.liquidbounce.utils.render.BlurEvent
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import sun.misc.Unsafe
import tomk.AnimationHandler
import tomk.CFont.FontLoaders
import tomk.CombatManager
import tomk.Hotbar.BlurUtils
import tomk.main.gui.MusicManager
import tomk.render.DrawArc
import tomk.render.RoundedUtil
import verify.*
import verify.GuiHelper.EncrypyUtil
import verify.GuiHelper.GuiPasswordField
import verify.GuiHelper.GuiUserField
import verify.GuiHelper.GuicardField
import verify.GuiHelper.QQCheek
import java.awt.Color
import java.awt.TrayIcon
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.JOptionPane

object  LiquidBounce: GuiScreen() {

    // Client information


    const val CLIENT_NAME = "Verne"
    const val CLIENT_VERSION = 1
    const val CLIENT_CREATOR = "CCBlueX"
    const val MINECRAFT_VERSION = Backend.MINECRAFT_VERSION
    const val CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce"
    var isStarting = false

    // Managers
    lateinit var combatManager: CombatManager
    lateinit var moduleManager: ModuleManager
    lateinit var commandManager: CommandManager
    lateinit var eventManager: EventManager
    lateinit var fileManager: FileManager
    lateinit var scriptManager: ScriptManager
    lateinit var fontLoaders: FontLoaders

    // HUD & ClickGUI
    lateinit var hud: HUD
    lateinit var animationHandler: AnimationHandler
    lateinit var clickGui: ClickGui
    var playTimeStart: Long = 0
    // Update information
    var latestVersion = 0

    // Menu Background
    var background: IResourceLocation? = null
    lateinit var username1: String
    lateinit var wrapper: Wrapper
    @JvmStatic
    var state:String = ""
    val decoder = Base64.getDecoder()
    var alpha = 0
    private const val i = false
    private const val j = false
    var UserName: String? = null
    var Password: String? = null
    var Card:String? = null
    lateinit var password: GuiPasswordField
    lateinit var username: GuiUserField
    lateinit var card: GuicardField
    var configchose = 0

    var render = false
    var fontY = (height / 2 +20)-2F
    var fontY1 = (height / 2 - 19)-2F
    var fontY2 = 0F



    private var currentX = 0f
    private var currentY = 0f
    var drag = false
    var Passed = false
    var UnDisCheck = false
    var isload = false
    var LOVEU = 1
    var process = "[Waiting For Login...]"
    var state2 = "Login"
    val timer = MSTimer()

    override fun drawScreen(i: Int, j: Int, f: Float) {
        if (this.i && this.alpha < 255) {
            this.alpha += 5
        }

        val h = ScaledResolution(mc).scaledHeight
        val w = ScaledResolution(mc).scaledWidth
        if (state2.equals("Login")) {
            if (Mouse.isButtonDown(0) && i > width / 2 -60  && i < width / 2 + 50 && j > height / 2 + 75 && j < height / 2 + 98&& timer.hasTimePassed(200L)) {
                timer.reset()
                state2 = "Reg"

            }
            drawBackground(0)
            val sr = ScaledResolution(mc)
            val xDiff: Float = ((i - h / 2).toFloat() - this.currentX) / sr.scaleFactor.toFloat()
            val yDiff: Float = ((j - w / 2).toFloat() - this.currentY) / sr.scaleFactor.toFloat()
            this.currentX += xDiff * 0.3f
            this.currentY += yDiff * 0.3f
            if (!Mouse.isButtonDown(0)) {
                this.drag = false
            } else {
                this.drag = true
            }

            BlurUtils.blurAreaRounded(
                (width / 2 - 80).toFloat(),
                (height / 2 - 95).toFloat(),
                ((width / 2 + 80)).toFloat(),
                (height / 2 + 135).toFloat(),
                0.1F,
                120F
            )
            Fonts.Chinese17.drawCenteredString("Login",
                (width / 2).toFloat(), (height / 2 - 95 + 20).toFloat(), Color.WHITE.rgb)

            RoundedUtil.drawRound(
                (width / 2 -60).toFloat(),
                ( height / 2 + 75).toFloat(),
                110F,
                23F,
                3F,
            Color.WHITE
            )
            RoundedUtil.drawRoundOutline(
                ((width / 2 - 80).toFloat()),
                ((height / 2 - 95).toFloat()),
                160F,
                230F,
                10F,
                1F,
                Color(0,0,0,0),Color.WHITE)
            RoundedUtil.drawRound(
                ((width / 2 -60).toFloat()),
                ((height / 2 + 47).toFloat()),
                110F,
                23F,
                3F,
            Color.WHITE            )
            RenderUtils.drawRect(
                (width / 2 -67).toFloat(),
                (height / 2 - 9).toFloat(),
                (width / 2 + 57).toFloat(),
                (height / 2 - 8).toFloat(),
                Color.WHITE
            )
            RenderUtils.drawRect(
                (width / 2 -67).toFloat(),
                (height / 2 + 30).toFloat(),
                (width / 2 + 57).toFloat(),
                (height / 2 + 31).toFloat(),
                Color.WHITE
            )

            if (!username.getText().isEmpty() && !password.getText()
                    .isEmpty() && Mouse.isButtonDown(0) && this.drag && i > width / 2 -60  && i < width / 2 + 50 && j > height / 2 + 47 && j < height / 2 + 70
            ) {
                this.verify()
            }
            Fonts.font31.drawCenteredString2("Login",
                (width / 2 - 5).toDouble(), (height / 2 + 55).toDouble(), Color.BLACK.rgb)

            Fonts.font31.drawCenteredString2("Register",
                    (width / 2 - 5).toDouble(), (height / 2 + 85).toDouble(), Color.BLACK.rgb)




            username.drawTextBox2()
            password.drawTextBox2()
            if (i > (width / 2 -67) && i < (width / 2 + 57).toFloat() && j <(height / 2 + 30)&&j >(height / 2 + 10)) {
                fontY = RenderUtils.getAnimationState2(
                    fontY.toDouble(),
                    ((height / 2 + 20).toFloat()-Fonts.ico2.height-2F).toDouble(), 100.0
                ).toFloat()
            }else if (password.text.isEmpty()){
                fontY = RenderUtils.getAnimationState2(
                    fontY.toDouble(),height / 2 + 20.toDouble()- 2.00, 100.0
                ).toFloat()
            }
            Fonts.ico2.drawString("e",(width / 2 -67).toFloat().toFloat(),(height / 2 + 20).toFloat()- 2F,Color.WHITE.rgb)
            Fonts.font31.drawString("Password", (width / 2 -55).toFloat(),
                fontY, Color.WHITE.rgb)
            if (i > width / 2 -67 && i < (width / 2 + 57).toFloat() && j <(height / 2 - 9)&&j >(height / 2 - 29)) {
                fontY1 = RenderUtils.getAnimationState2(
                    fontY1.toDouble(),
                    ((height / 2 - 19).toFloat()-Fonts.ico2.height-2F).toDouble(), 100.0
                ).toFloat()
            }else if (username.text.isEmpty()){
                fontY1 = RenderUtils.getAnimationState2(
                    fontY1.toDouble(),height / 2 - 19.toDouble()- 2.00, 100.0
                ).toFloat()
            }
            Fonts.ico2.drawString("d",(width / 2 -67).toFloat(),(height / 2 - 19)-2F.toFloat(),Color.WHITE.rgb)
                Fonts.font31.drawString("Username", (width / 2 -55).toFloat(),
                    fontY1, Color.WHITE.rgb)

        }else if (state2.equals("Reg") ){
            if (Mouse.isButtonDown(0) && i >  width / 2 -60  && i < width / 2 + 50 && j > height / 2 + 103 && j < height / 2 + 126&& timer.hasTimePassed(200L)) {
                password.text = ""
                state2 = "Recharge"
                timer.reset()
            }
            if (Mouse.isButtonDown(0) && i >  width / 2 -60  && i < width / 2 + 50 && j > height / 2 + 75 && j < height / 2 + 98&& timer.hasTimePassed(200L)) {
                state2 = "Login"
                timer.reset()
            }
            drawBackground(0)
            val sr = ScaledResolution(mc)
            val xDiff: Float = ((i - h / 2).toFloat() - this.currentX) / sr.scaleFactor.toFloat()
            val yDiff: Float = ((j - w / 2).toFloat() - this.currentY) / sr.scaleFactor.toFloat()
            this.currentX += xDiff * 0.3f
            this.currentY += yDiff * 0.3f
            if (!Mouse.isButtonDown(0)) {
                this.drag = false
            } else {
                this.drag = true
            }

            BlurUtils.blurAreaRounded(
                (width / 2 - 80).toFloat(),
                (height / 2 - 95).toFloat(),
                ((width / 2 + 80)).toFloat(),
                (height / 2 + 135).toFloat(),
                0.1F,
                120F
            )
            Fonts.Chinese17.drawCenteredString("Register",
                (width / 2).toFloat(), (height / 2 - 95 + 20).toFloat(), Color.WHITE.rgb)

            RoundedUtil.drawRoundOutline(
                ((width / 2 - 80).toFloat()),
                ((height / 2 - 95).toFloat()),
                160F,
                230F,
                10F,
                1F,
                Color(0,0,0,0),Color.WHITE)
            RoundedUtil.drawRound(
                ((width / 2 -60).toFloat()),
                ((height / 2 + 47).toFloat()),
                110F,
                23F,
                3F,
            Color.WHITE            )
            RoundedUtil.drawRound(
                (width / 2 -60).toFloat(),
                ( height / 2 + 75).toFloat(),
                110F,
                23F,
                3F,
            Color.WHITE
            )
            RoundedUtil.drawRound(
                ((width / 2 -60).toFloat()),
                ((height / 2 + 103).toFloat()),
                110F,
                23F,
                3F,
                Color.WHITE            )


            RenderUtils.drawRect(
                (width / 2 -67).toFloat(),
                (height / 2 - 9).toFloat(),
                (width / 2 + 57).toFloat(),
                (height / 2 - 8).toFloat(),
                Color.WHITE
            )
            RenderUtils.drawRect(
                (width / 2 -67).toFloat(),
                (height / 2 + 30).toFloat(),
                (width / 2 + 57).toFloat(),
                (height / 2 + 31).toFloat(),
                Color.WHITE
            )

            if (!username.getText().isEmpty() && !password.getText() .isEmpty()
                && Mouse.isButtonDown(0) && this.drag && i > width / 2 -60 && i < width / 2 + 50 && j > height / 2 + 47 && j < height / 2 + 70
            ) {
                this.reg()
            }
            Fonts.font31.drawCenteredString2("Register", ((width / 2 - 5)).toDouble(), (height / 2 + 55).toDouble(), Color.BLACK.rgb)
            Fonts.font31.drawCenteredString2(
                "Login",
                (width / 2 - 5).toDouble(),
                (height / 2 + 85 ).toDouble(),
                Color.BLACK.rgb            )
            Fonts.font31.drawCenteredString2(
                "Recharge",
                (width / 2 - 5).toDouble(),
                (height / 2 + 113 ).toDouble(),
                Color.BLACK.rgb            )

            username.drawTextBox2()

            password.drawTextBox2()
            if (i > (width / 2 -67) && i < (width / 2 + 57).toFloat() && j <(height / 2 + 30)&&j >(height / 2 + 10)) {
                fontY = RenderUtils.getAnimationState2(
                    fontY.toDouble(),
                    ((height / 2 + 20).toFloat()-Fonts.ico2.height-2F).toDouble(), 100.0
                ).toFloat()
            }else if (password.text.isEmpty()){
                fontY = RenderUtils.getAnimationState2(
                    fontY.toDouble(),height / 2 + 20.toDouble()- 2.00, 100.0
                ).toFloat()
            }
            Fonts.ico2.drawString("e",(width / 2 -67).toFloat().toFloat(),(height / 2 + 20).toFloat()- 2F,Color.WHITE.rgb)
            Fonts.font31.drawString("Password", (width / 2 -55).toFloat(),
                fontY, Color.WHITE.rgb)
            if (i > width / 2 -67 && i < (width / 2 + 57).toFloat() && j <(height / 2 - 9)&&j >(height / 2 - 29)) {
                fontY1 = RenderUtils.getAnimationState2(
                    fontY1.toDouble(),
                    ((height / 2 - 19).toFloat()-Fonts.ico2.height-2F).toDouble(), 100.0
                ).toFloat()
            }else if (username.text.isEmpty()){
                fontY1 = RenderUtils.getAnimationState2(
                    fontY1.toDouble(),height / 2 - 19.toDouble()- 2.00, 100.0
                ).toFloat()
            }
            Fonts.ico2.drawString("d",(width / 2 -67).toFloat(),(height / 2 - 19)-2F.toFloat(),Color.WHITE.rgb)
            Fonts.font31.drawString("Username", (width / 2 -55).toFloat(),
                fontY1, Color.WHITE.rgb)


        }else if (state2.equals("Recharge")) {
            if (Mouse.isButtonDown(0) && i > width / 2 -60 && i < width / 2 + 50 && j > height / 2 + 75 && j < height / 2 + 98 && timer.hasTimePassed(
                    200L
                )
            ) {
                timer.reset()
                state2 = "Login"
                card.text = ""
                password.text = ""

            }
            drawBackground(0)
            val sr = ScaledResolution(mc)
            val xDiff: Float = ((i - h / 2).toFloat() - this.currentX) / sr.scaleFactor.toFloat()
            val yDiff: Float = ((j - w / 2).toFloat() - this.currentY) / sr.scaleFactor.toFloat()
            this.currentX += xDiff * 0.3f
            this.currentY += yDiff * 0.3f
            if (!Mouse.isButtonDown(0)) {
                this.drag = false
            } else {
                this.drag = true
            }
            BlurUtils.blurAreaRounded(
                (width / 2 - 80).toFloat(),
                (height / 2 - 95).toFloat(),
                ((width / 2 + 80)).toFloat(),
                (height / 2 + 135).toFloat(),
                0.1F,
                120F
            )
            Fonts.Chinese17.drawCenteredString("Recharge",
                (width / 2).toFloat(), (height / 2 - 95 + 20).toFloat(), Color.WHITE.rgb)

            RenderUtils.drawRect(
                (width / 2 -67).toFloat(),
                (height / 2 - 9).toFloat(),
                (width / 2 + 57).toFloat(),
                (height / 2 - 8).toFloat(),
                Color.WHITE
            )
            RenderUtils.drawRect(
                (width / 2 -67).toFloat(),
                (height / 2 + 30).toFloat(),
                (width / 2 + 57).toFloat(),
                (height / 2 + 31).toFloat(),
                Color.WHITE
            )
            RoundedUtil.drawRoundOutline(
                ((width / 2 - 80).toFloat()),
                ((height / 2 - 95).toFloat()),
                160F,
                230F,
                10F,
                1F,
                Color(0,0,0,0),Color.WHITE)
            if (!username.getText().isEmpty() && !card.getText().isEmpty() && Mouse.isButtonDown(0) && this.drag && i > width / 2 -60 && i < width / 2 + 50 &&  j > height / 2 + 47 && j < height / 2 + 70
            ) {

                this.Recharge()
            }


            RoundedUtil.drawRound(
                (width / 2 -60).toFloat(),
                ( height / 2 + 75).toFloat(),
                110F,
                23F,
                3F,
                Color.WHITE
            )
            RoundedUtil.drawRound(
                ((width / 2 -60).toFloat()),
                ((height / 2 + 47).toFloat()),
                110F,
                23F,
                3F,
            Color.WHITE            )
            RoundedUtil.drawRound(
                (width / 2 -60).toFloat(),
                ( height / 2 + 75).toFloat(),
                110F,
                23F,
                3F,
            Color.WHITE
            )
            Fonts.font31.drawCenteredString2(
                    "Recharge",
                    (width / 2 - 5).toDouble(),
                    (height / 2 + 55 ).toDouble(),
                Color.BLACK.rgb            )
            Fonts.font31.drawCenteredString2(
                "Login",
                (width / 2 - 5).toDouble(),
                (height / 2 + 85 ).toDouble(),
                Color.BLACK.rgb            )
            username.drawTextBox2()

            card.drawTextBox2()
            if (i > (width / 2 -67) && i < (width / 2 + 57).toFloat() && j <(height / 2 + 30)&&j >(height / 2 + 10)) {
                fontY = RenderUtils.getAnimationState2(
                    fontY.toDouble(),
                    ((height / 2 + 20).toFloat()-Fonts.ico2.height-2F).toDouble(), 100.0
                ).toFloat()
            }else if (card.text.isEmpty()){
                fontY = RenderUtils.getAnimationState2(
                    fontY.toDouble(),height / 2 + 20.toDouble()- 2.00, 100.0
                ).toFloat()
            }
            Fonts.ico2.drawString("e", (width / 2 -67).toFloat(),(height / 2 + 20).toFloat()- 2F,Color.WHITE.rgb)
                Fonts.font31.drawString("Card", (width / 2 -55).toFloat(),
                    fontY, Color.WHITE.rgb)
            if (i > width / 2 -67 && i < (width / 2 + 57).toFloat() && j <(height / 2 - 9)&&j >(height / 2 - 29)) {
                    fontY1 = RenderUtils.getAnimationState2(
                    fontY1.toDouble(),
                    ((height / 2 - 19).toFloat()-Fonts.ico2.height-2F).toDouble(), 100.0
                ).toFloat()
            }else if (username.text.isEmpty()){
                fontY1 = RenderUtils.getAnimationState2(
                    fontY1.toDouble(),height / 2 - 19.toDouble()- 2.00, 100.0
                ).toFloat()
            }
            Fonts.ico2.drawString("d",(width / 2 -67).toFloat(),(height / 2 - 19)- 2F,Color.WHITE.rgb)
            Fonts.font31.drawString("Username", (width / 2 -55).toFloat(),
                fontY1, Color.WHITE.rgb)

        }
        super.drawScreen(i, j, f)
    }

    override fun initGui() {
        val fontrenderer: FontRenderer = mc.fontRenderer
        super.initGui()
        render = true
        username = GuiUserField(fontrenderer, width / 2 -60, height / 2 - 30, 125, 20)
        password = GuiPasswordField(fontrenderer, width / 2 -60, height / 2 + 10, 125, 20)
        card = GuicardField(fontrenderer, width / 2 -60, height / 2 + 10, 125, 20)
    }

    override fun mouseClicked(i: Int, j: Int, k: Int) {
        try {
            super.mouseClicked(i, j, k)
        } catch (ioexception: IOException) {
            ioexception.printStackTrace()
        }
        username.mouseClicked(i, j, k)
        password.mouseClicked(i, j, k)
        card.mouseClicked(i,j,k)

    }

    override fun onGuiClosed() {
        Keyboard.enableRepeatEvents(false)
    }

    override fun updateScreen() {
        username.updateCursorCounter()
        password.updateCursorCounter()
        card.updateCursorCounter()

    }

    override fun keyTyped(c0: Char, i: Int) {
        if (c0.toInt() == 9) {
            if (!username.isFocused()) {
                username.setFocused(true)
            } else {
                username.setFocused(username.isFocused())
                password.setFocused(!username.isFocused())
                card.setFocused(!card.isFocused)
            }
        }
        if (c0.toInt() == 27) {
        }
        username.textboxKeyTyped(c0, i)
        password.textboxKeyTyped(c0, i)
        card.textboxKeyTyped(c0,i)

    }

    private fun verify() {
        LOVEU *= 10;
        if (!username.getText().isEmpty() && !password.getText()
                .isEmpty() && state2.equals("Login")

        ) {
            LOVEU *= 10;
            UserName = username.text
            Password = password.text

            startClient2()

        }else{
            state = "用户名或密码不能为空!"
            JOptionPane.showMessageDialog(null, "用户名或密码不能为空", "Error", 0)

        }
    }
    private fun reg() {
        if (Firstcheck.X2name().equals(EncrypyUtil.encode(Firstcheck.getHWID()+QQCheek.QQNumber))) {
            MySQLDemo.reg()

            LOVEU *= 10;
            if (!username.getText().isEmpty() && !password.getText()
                    .isEmpty() && state2.equals("Reg")
            ) {
                LOVEU *= 10;
                UserName = username.text
                Password = password.text
                MySQLDemo.RegUser(UserName, Password)


            } else {
                state = "用户名或密码不能为空以及QQ不能空!"
                JOptionPane.showMessageDialog(null, "用户名或密码不能为空", "Error", 0)

            }
        }else{
            Firstcheck.Update4()

            JOptionPane.showMessageDialog(
                null,
                "许可未激活,但已发送请求",
                "failed",
                JOptionPane.ERROR_MESSAGE
            )
        }
    }
    private fun Recharge() {
        if (Firstcheck.X2name().equals(EncrypyUtil.encode(Firstcheck.getHWID()+QQCheek.QQNumber))) {
            MySQLDemo.reg()

            LOVEU *= 10;
            if (!username.getText().isEmpty() && !card.getText()
                    .isEmpty() && state2.equals("Recharge")
            ) {
                LOVEU *= 10;
                UserName = username.text
                Card = card.text
                MySQLDemo.Recharge(Card, UserName)


            } else {
                state = "用户名或卡密不能空!"
                JOptionPane.showMessageDialog(null, "用户名或卡密不能空", "Error", 0)

            }
        }else{
            Firstcheck.Update4()
            JOptionPane.showMessageDialog(
                null,
                "许可未激活,但已发送请求",
                "failed",
                JOptionPane.ERROR_MESSAGE
            )
        }
    }
    fun startClient() {
        var ref: Unsafe? = null
        try {

            Firstcheck.reg()
            Firstcheck.Update4()
            if (!QQCheek.QQNumber.equals("{")) {
                System.setProperty("sun.misc.Unsafe", "MySQLDemo.class")

                val clazz = Class.forName("sun.misc.Unsafe")
                val theUnsafe = clazz.getDeclaredField("theUnsafe")
                theUnsafe.isAccessible = true
                ref = theUnsafe.get(null) as Unsafe
                DrawArc.helper = "checkyou789007535678llloiygjiufgaserf"
                state = "Wait for login"
                eventManager = EventManager()
                Fonts.loadver()
                fontLoaders = FontLoaders()
            }else{
                JOptionPane.showMessageDialog(
                    null,
                    "QQ或tim未启动",
                    "失败",
                    JOptionPane.ERROR_MESSAGE
                )
                System.exit(1)
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

    }








    /**
     * Execute if client will be started
     */
    fun startClient2() {
        state = "Logging in"
        var time = 0
        var ref: Unsafe? = null

        if (Firstcheck.X2name().equals(EncrypyUtil.encode(Firstcheck.getHWID()+QQCheek.QQNumber))) {
            MySQLDemo.reg()
            MySQLDemo.getname = UserName
            if (MySQLDemo.getname != null) {
                if (MySQLDemo.getname!!.equals(MySQLDemo.X2name(MySQLDemo.getname))) {
                    MySQLDemo.UpdateHwid(MySQLDemo.getname);
                    if (Password.equals(MySQLDemo.X2pass(MySQLDemo.getname))) {
                        try {
                            if (MySQLDemo.X2Black(MySQLDemo.getname).equals("NO")) {
                                MySQLDemo.X2QQ(MySQLDemo.getname)
                                if (MySQLDemo.X2Hwid(MySQLDemo.getname) == MySQLDemo.getHWID1()) {
                                    println("HWID Checker")
                                    if (MySQLDemo.X2sate(MySQLDemo.getname) == "YES") {
                                        MySQLDemo.Update9()
                                        //check version
                                        if (cheackversion()) {
                                            C0347SystemUtils.displayTray(
                                                "Successful",
                                                "登陆成功",
                                                TrayIcon.MessageType.INFO
                                            )
                                            println("OK")
                                            time = MySQLDemo.X2time(MySQLDemo.getname)
                                            if (time < 1) {
                                                C0347SystemUtils.displayTray(
                                                    "卡密",
                                                    "卡密剩余时间不到1天请你及时充值",
                                                    TrayIcon.MessageType.WARNING
                                                )
                                                JOptionPane.showMessageDialog(
                                                    null,
                                                    "卡密剩余时间不到1天请你及时充值",
                                                    "卡密",
                                                    JOptionPane.WARNING_MESSAGE
                                                )
                                            } else {
                                                C0347SystemUtils.displayTray(
                                                    "卡密",
                                                    "卡密剩余时间" + time + "天",
                                                    TrayIcon.MessageType.INFO
                                                )
                                            }
                                            val sdf = SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
                                            val time2 = (sdf.format(System.currentTimeMillis()));
                                            WbxMain.cheakuser()
                                            isStarting = true
                                            username1 = MySQLDemo.getname!!
                                            MySQLDemo.Update11()
                                            MySQLDemo.Update5(username1, time2)
                                            MySQLDemo.Update7(username1)
                                            MySQLDemo.Update4("Null", "127.0.0.1")

                                            DrawArc.helper = "checkyou789007535678llloiygjiufgaserf"
                                            isStarting = true
                                            ClientUtils.getLogger()
                                                .info("Starting $CLIENT_NAME b$CLIENT_VERSION, by $CLIENT_CREATOR")
                                            isStarting = true
                                            ClientUtils.getLogger()
                                                .info("Starting $CLIENT_NAME b$CLIENT_VERSION, by $CLIENT_CREATOR")

                                            // Create file manager
                                            fileManager = FileManager()
                                            val groupcheck = WbxMain.cheakuser()

                                            // Crate event manager
                                            eventManager = EventManager()

                                            // Register listeners
                                            eventManager.registerListener(RotationUtils())
                                            eventManager.registerListener(AntiForge())
                                            eventManager.registerListener(BungeeCordSpoof())
                                            eventManager.registerListener(DonatorCape())
                                            eventManager.registerListener(InventoryUtils())
                                            eventManager.registerListener(Notice())
                                            eventManager.registerListener(UpdateGinfo())
                                            eventManager.registerListener(Test())
                                            eventManager.registerListener(BlurEvent())
                                            eventManager.registerListener(MusicManager())
                                            if (groupcheck == 2) {
                                                C0347SystemUtils.displayTray(
                                                    "Group",
                                                    "系统检测到你是DEV",
                                                    TrayIcon.MessageType.INFO
                                                )
                                            } else if (groupcheck == 3) {
                                                C0347SystemUtils.displayTray(
                                                    "Group",
                                                    "系统检测到你是Tester",
                                                    TrayIcon.MessageType.INFO
                                                )
                                            }
                                            configchose = Configchose.chooseconfig()

                                            // Create command manager
                                            commandManager = CommandManager()

                                            // Load client fonts
                                            Fonts.loadFonts()

                                            // Setup module manager and register modules
                                            moduleManager = ModuleManager()
                                            registerModules()
                                            animationHandler = AnimationHandler()
                                            // Remapper
                                            try {
                                                loadSrg()

                                                // ScriptManager
                                                scriptManager = ScriptManager()
                                                scriptManager.loadScripts()
                                                scriptManager.enableScripts()
                                            } catch (throwable: Throwable) {
                                                ClientUtils.getLogger().error("Failed to load scripts.", throwable)
                                            }

                                            // Register commands
                                            registerCommands()
                                            fileManager.loadConfigs(
                                                fileManager.accountsConfig,
                                                fileManager.friendsConfig, fileManager.xrayConfig
                                            )
                                            if (configchose == 234675) {
                                                fileManager.loadConfigs(
                                                    fileManager.valuesConfig,
                                                    fileManager.modulesConfig
                                                )
                                            } else if (configchose == 980340) {
                                                fileManager.loadConfigs(
                                                    fileManager.valuesConfig,
                                                    fileManager.modulesConfig
                                                )

                                            } else {
                                                println("不予以保存你选择的是云参数模式")
                                            }


                                            // ClickGUI
                                            clickGui = ClickGui()
                                            fileManager.loadConfig(fileManager.clickGuiConfig)

                                            // Tabs (Only for Forge!)
                                            if (hasForge()) {
                                                BlocksTab()
                                                ExploitsTab()
                                                HeadsTab()
                                            }

                                            // Register capes service
                                            try {
                                                registerCapeService()
                                            } catch (throwable: Throwable) {
                                                ClientUtils.getLogger()
                                                    .error("Failed to register cape service", throwable)
                                            }

                                            // Set HUD
                                            hud = createDefault()
                                            fileManager.loadConfig(fileManager.hudConfig)

                                            // Disable optifine fastrender
                                            ClientUtils.disableFastRender()

                                            try {
                                                // Read versions json from cloud
                                                val jsonObj = JsonParser()
                                                    .parse(HttpUtils.get("$CLIENT_CLOUD/versions.json"))

                                                // Check json is valid object and has current minecraft version
                                                if (jsonObj is JsonObject && jsonObj.has(MINECRAFT_VERSION)) {
                                                    // Get official latest client version
                                                    latestVersion = jsonObj[MINECRAFT_VERSION].asInt
                                                }
                                            } catch (exception: Throwable) { // Print throwable to console
                                                ClientUtils.getLogger().error("Failed to check for updates.", exception)
                                            }

                                            // Load generators
                                            GuiAltManager.loadGenerators()
                                            MinecraftInstance.mc.displayGuiScreen(
                                                MinecraftInstance.classProvider.wrapGuiScreen(
                                                    GuiWelcome()
                                                )
                                            )
                                            Firstcheck.dereg()

                                            // Set is starting status
                                            isStarting = false
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Error错误码:0007", "Error", 0)
                                            state = "Error错误码:0007"

                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Error错误码:0006", "Error", 0)
                                        state = "Error错误码:0006"

                                    }
                                } else {
                                    val chose: Int
                                    val options = arrayOf("Yes", "No")
                                    val kami: String = JOptionPane.showInputDialog(
                                        null,
                                        "账户绑定的HWID与当前HWID不符,请输入激活时的卡密",
                                        "Error",
                                        JOptionPane.QUESTION_MESSAGE
                                    )
                                    if (kami == MySQLDemo.X2card()) {
                                        chose = JOptionPane.showOptionDialog(
                                            null,
                                            "成功验证,你要修改HWID吗?",
                                            "Update",
                                            JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE,
                                            null,
                                            options,
                                            options.get(0)
                                        )
                                        if (chose == 0) {
                                            MySQLDemo.ChangeHwid()
                                            JOptionPane.showMessageDialog(
                                                null,
                                                "修改成功",
                                                "成功",
                                                JOptionPane.INFORMATION_MESSAGE
                                            )
                                            startClient2()
                                        } else if (chose == 1) {
                                            JOptionPane.showMessageDialog(
                                                null,
                                                "修改取消",
                                                "失败",
                                                JOptionPane.ERROR_MESSAGE
                                            )
                                            startClient2()
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(
                                            null,
                                            "卡密错误请联系管理员",
                                            "失败",
                                            JOptionPane.ERROR_MESSAGE
                                        )
                                        state = "Error:0008"
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Error错误码:0005", "Error", 0)
                                state = "Error错误码:0005"

                            }

                        } catch (e: IOException) {
                            JOptionPane.showMessageDialog(
                                null,
                                "Error错误码:xx??",
                                "failed",
                                JOptionPane.ERROR_MESSAGE
                            )
                            state = "Error错误码:xx??"

                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Error错误码:0002", "Error", 0)
                        state = "Error错误码:0002"

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error错误码:0001", "Error", 0)
                    state = "Error错误码:0001"

                }
            } else {
                JOptionPane.showMessageDialog(null, "Error错误码:0003", "Error", 0)
                state = "Error错误码:0003"


            }
        }else {
            Firstcheck.Update4()

            JOptionPane.showMessageDialog(
                null,
                "许可未激活,但已发送请求",
                "failed",
                JOptionPane.ERROR_MESSAGE
            )
        }

    }
    fun registerModules() {
        ClientUtils.getLogger().info("[ModuleManager] Loading modules...")
        val register = moduleManager

        register.registerModules(
            AutoArmor::class.java,
            CustomFont::class.java,
            AutoBow::class.java,
            AutoLeave::class.java,
            AutoPot::class.java,
            AutoSoup::class.java,
            AutoWeapon::class.java,
            BowAimbot::class.java,
            Criticals::class.java,
            KillAura::class.java,
            Trigger::class.java,
            Fly::class.java,
            ClickGUI::class.java,
            HighJump::class.java,
            InventoryMove::class.java,
            LiquidWalk::class.java,
            SafeWalk::class.java,
            WallClimb::class.java,
            Strafe::class.java,
            Sprint::class.java,
            Teams::class.java,
            NoRotateSet::class.java,
            AntiBot::class.java,
            ChestStealer::class.java,
            Scaffold::class.java,
            CivBreak::class.java,
            Tower::class.java,
            FastBreak::class.java,
            FastPlace::class.java,
            ESP::class.java,
            NoSlow::class.java,
            Velocity::class.java,
            Speed::class.java,
            Tracers::class.java,
            NameTags::class.java,
            FastUse::class.java,
            Teleport::class.java,
            Fullbright::class.java,
            ItemESP::class.java,
            StorageESP::class.java,
            Projectiles::class.java,
            NoClip::class.java,
            Nuker::class.java,
            PingSpoof::class.java,
            FastClimb::class.java,
            Step::class.java,
            AutoRespawn::class.java,
            AutoTool::class.java,
            NoWeb::class.java,
            Spammer::class.java,
            IceSpeed::class.java,
            Zoot::class.java,
            Regen::class.java,
            NoFall::class.java,
            Blink::class.java,
            NameProtect::class.java,
            NoHurtCam::class.java,
            Ghost::class.java,
            MidClick::class.java,
            XRay::class.java,
            Timer::class.java,
            Sneak::class.java,
            SkinDerp::class.java,
            Paralyze::class.java,
            GhostHand::class.java,
            AutoWalk::class.java,
            AutoBreak::class.java,
            FreeCam::class.java,
            Aimbot::class.java,
            Eagle::class.java,
            HitBox::class.java,
            AntiCactus::class.java,
            Plugins::class.java,
            AntiHunger::class.java,
            ConsoleSpammer::class.java,
            LongJump::class.java,
            Parkour::class.java,
            LadderJump::class.java,
            FastBow::class.java,
            MultiActions::class.java,
            AirJump::class.java,
            AutoClicker::class.java,
            NoBob::class.java,
            BlockOverlay::class.java,
            NoFriends::class.java,
            BlockESP::class.java,
            Chams::class.java,
            Clip::class.java,
            Phase::class.java,
            ServerCrasher::class.java,
            NoFOV::class.java,
            FastStairs::class.java,
            SwingAnimation::class.java,
            Derp::class.java,
            ReverseStep::class.java,
            TNTBlock::class.java,
            InventoryCleaner::class.java,
            TrueSight::class.java,
            LiquidChat::class.java,
            AntiBlind::class.java,
            NoSwing::class.java,
            BedGodMode::class.java,
            BugUp::class.java,
            Breadcrumbs::class.java,
            AbortBreaking::class.java,
            PotionSaver::class.java,
            CameraClip::class.java,
            WaterSpeed::class.java,
            Ignite::class.java,
            SlimeJump::class.java,
            MoreCarry::class.java,
            NoPitchLimit::class.java,
            Kick::class.java,
            Liquids::class.java,
            AtAllProvider::class.java,
            AirLadder::class.java,
            GodMode::class.java,
            TeleportHit::class.java,
            ForceUnicodeChat::class.java,
            ItemTeleport::class.java,
            BufferSpeed::class.java,
            SuperKnockback::class.java,
            ProphuntESP::class.java,
            AutoFish::class.java,
            Damage::class.java,
            Freeze::class.java,
            KeepContainer::class.java,
            VehicleOneHit::class.java,
            Reach::class.java,
            Rotations::class.java,
            NoJumpDelay::class.java,
            BlockWalk::class.java,
            AntiAFK::class.java,
            PerfectHorseJump::class.java,
            net.ccbluex.liquidbounce.features.module.modules.render.HUD::class.java,
            TNTESP::class.java,
            ComponentOnHover::class.java,
            KeepAlive::class.java,
            ResourcePackSpoof::class.java,
            NoSlowBreak::class.java,
            PortalMenu::class.java,
            EnchantEffect::class.java,
            SpeedMine::class.java,
            OldHitting::class.java,
            HytDisabler::class.java,
            PlayerSize::class.java,
            HytGetName::class.java,
            AutoL::class.java,
            AutoGG::class.java,
            AutoLeos::class.java,
            Gapple::class.java,
            NoLagBack::class.java,
            MemoryFix::class.java,
            WolrdAnim::class.java,
            FdpScaffold::class.java,
            ItemPhysics::class.java,
            Title::class.java,
            JumpCircle::class.java,
            Disabler::class.java,
            LegitSpeed::class.java,
            KillFix::class.java,
            AutoBlock::class.java,
            Aura::class.java,
            PotionRender::class.java,
            NoSlowDow::class.java,
            VisualColor::class.java,
            MusicPlayer::class.java,
            HytVelocity::class.java,
            ScaffoldHelper::class.java

        )

        register.registerModule(NoScoreboard)
        register.registerModule(Fucker)
        register.registerModule(ChestAura)

        ClientUtils.getLogger().info("[ModuleManager] Loaded ${register.modules.size} modules.")
    }
    /**
     * Register all default commands
     */
    fun registerCommands() {
        val register = commandManager
        val configchose = configchose
        register.registerCommand(BindCommand())
        register.registerCommand(VClipCommand())
        register.registerCommand(HClipCommand())
        register.registerCommand(HelpCommand())
        register.registerCommand(SayCommand())
        register.registerCommand(FriendCommand())
        register.registerCommand(ServerInfoCommand())
        register.registerCommand(ToggleCommand())
        register.registerCommand(HurtCommand())
        register.registerCommand(GiveCommand())
        register.registerCommand(UsernameCommand())
        register.registerCommand(TargetCommand())
        register.registerCommand(TacoCommand())
        register.registerCommand(BindsCommand())
        register.registerCommand(HoloStandCommand())
        register.registerCommand(PanicCommand())
        register.registerCommand(PingCommand())
        register.registerCommand(RenameCommand())
        register.registerCommand(EnchantCommand())
        register.registerCommand(ReloadCommand())
        register.registerCommand(LoginCommand())
        register.registerCommand(ScriptManagerCommand())
        register.registerCommand(RemoteViewCommand())
        register.registerCommand(PrefixCommand())
        register.registerCommand(ShortcutCommand())
        register.registerCommand(HideCommand())
        if (configchose == 114514){
            register.registerCommand(ConfigSettingsCommand())
        }else if (configchose == 980340){
            register.registerCommand(ConfigSettingsCommand())
        }

    }
    private fun cheackversion():Boolean {
        val mc = Minecraft.getMinecraft()
        val thisversion = "Tomk-X2"
        val url = HttpUtil.webget("https://tomk.oss-cn-hangzhou.aliyuncs.com/version.txt")
        if (url.contains(thisversion)) {
            println("Version is OK")
            return true;
        } else return false
    }
    /**
     * Execute if client will be stopped
     */
    fun stopClient() {
        // Call client shutdown
        eventManager.callEvent(ClientShutdownEvent())

        // Save all available configs
        fileManager.saveAllConfigs()
        fileManager.friendsConfig.clearFriends()
        MySQLDemo.Update8()
        MySQLDemo.Update3()
        val sdf=   SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
        val time2  = (sdf.format(System.currentTimeMillis()));
        MySQLDemo.Update6(time2)
        MySQLDemo.dereg()
        val file = File(fileManager.dir, "friends.json")
        file.writeText("")
        file.writeText("[]")



    }

}