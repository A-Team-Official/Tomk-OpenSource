/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.command.commands


import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.SettingsUtils
import net.ccbluex.liquidbounce.utils.misc.StringUtils
import oh.yalan.NativeClass
import org.lwjgl.input.Keyboard
import verify.Crypt
import verify.HttpUtil
import verify.TestQiniu
import verify.WbxMain
import java.io.File
import java.io.IOException
import javax.swing.JOptionPane
import kotlin.jvm.internal.Intrinsics
@NativeClass
class ConfigSettingsCommand : Command("ConfigSettings", "ConfigSettings") {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            when {
                args[1].equals("load", ignoreCase = true) -> {
                    val pass:String?
                    Intrinsics.checkParameterIsNotNull(args, "args")
                    if (args.size > 2) {
                        val url = HttpUtil.webget("https://hwid.oss-cn-hangzhou.aliyuncs.com/Config.txt")//这个是公告也就是无法加载的提示
                        LiquidBounce.moduleManager.getModule(args[1])
                        val str1 = args[2]
                        val str2: String
                        val str3: String
                        val pass2:String
                        if (str1 == "default") {
                            if (WbxMain.cheakuser() == 0) {
                                JOptionPane.showMessageDialog(null, "This is the default parameter and you don't have permission to use it", "failed", 0)
                            } else {
                                str2 = HttpUtil.webget("http://rs2rfmq35.hn-bkt.clouddn.com//default.txt")
                                Intrinsics.checkExpressionValueIsNotNull(Crypt.decrypt(str2), "Crypt.decrypt(return1)")
                                SettingsUtils.executeScript(Crypt.decrypt(str2))

                                chat("§7[§3§lConfig§7]The default parameters have been successfully loaded, press the following key:")
                                Bind()
                                ClientUtils.displayChatMessage("[" + LiquidBounce.CLIENT_NAME + "]" + "LoadSuccess!")
                            }
                        }else{
                            try {
                                str2 = HttpUtil.webget("http://rs2rfmq35.hn-bkt.clouddn.com//$str1.txt")
                                str3 = Crypt.decrypt(str2)
                                if (str3.contains("[have-pass:")){
                                    pass = JOptionPane.showInputDialog("Enter a password for this configuration with a password")
                                    pass2 = "[have-pass:" + pass +"]"
                                    if (pass == null){
                                        JOptionPane.showMessageDialog(null,"Please input passwords")
                                    }
                                    if (str3.contains(pass2)){
                                        Intrinsics.checkExpressionValueIsNotNull(Crypt.decrypt(str2), "Crypt.decrypt(return1)")
                                        SettingsUtils.executeScript(Crypt.decrypt(str2))
                                        try {
                                            chat("§7[§3§lConfig§7]Loaded successfully$str1,Config,Press the keys below:")
                                            Bind()
                                            ClientUtils.displayChatMessage("[" + LiquidBounce.CLIENT_NAME + "]" + "LoadSuccess!")

                                        } catch (iOException: IOException) {
                                            iOException.printStackTrace()
                                            chat("§7[§3§lConfig§7]$url")
                                        }

                                    }else{
                                        JOptionPane.showMessageDialog(null,"Error password")
                                    }

                                }else{
                                    Intrinsics.checkExpressionValueIsNotNull(Crypt.decrypt(str2), "Crypt.decrypt(return1)")
                                    SettingsUtils.executeScript(Crypt.decrypt(str2))
                                    println(Crypt.decrypt(str2))

                                    try {
                                        chat("§7[§3§lConfig§7]Successful to loadconfih $str1,config,Press the keys below:")
                                        Bind()
                                        ClientUtils.displayChatMessage("[" + LiquidBounce.CLIENT_NAME + "]" + "LoadSuccess!")

                                    } catch (iOException: IOException) {
                                        iOException.printStackTrace()
                                        chat("§7[§3§lConfig§7]$url")
                                    }
                                }


                            } catch (iOException: IOException) {
                                iOException.printStackTrace()
                                chat("§7[§3§lConfig§7]$url")
                            }
                        }
                    }

                    chatSyntax("ConfigSettings load <name>")
                    return
                }

                args[1].equals("save", ignoreCase = true) -> {
                    if (args.size > 2) {
                        val configname: String
                        val pass: String?
                        var mess: String
                        val chose: Int
                        val chose2: Int
                        val options = arrayOf("Yes", "No")
                        val options2 = arrayOf("Yes", "No")
                        if (WbxMain.cheakuser() != 0) {
                            try {


                                val values = true
                                val binds = true
                                val states = true
                                if (!values && !binds && !states) {
                                    chatSyntaxError()
                                    return
                                }

                                chat("§9Creating settings...")
                                val settingsScript = SettingsUtils.generateScript(values, binds, states)
                                chat("§9Saving settings...")
                                configname = args[2]
                                pass =
                                    JOptionPane.showInputDialog("Please input config passwords(if you don't need it click cancel)")

                                chose = JOptionPane.showOptionDialog(
                                    null,
                                    "Do you want to upload this parameter?",
                                    "Update",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    null,
                                    options,
                                    options[0]
                                )

                                val TestFile = File(LiquidBounce.fileManager.dir, "$configname.txt")
                                if (pass != null) {
                                    mess = "[have-pass:$pass]$settingsScript"
                                } else {
                                    mess = settingsScript

                                }
                                if (WbxMain.cheakuser() == 2) {
                                    chose2 = JOptionPane.showOptionDialog(
                                        null,
                                        "You are Dev will you setting this config to official config?",
                                        "Setting",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE,
                                        null,
                                        options2,
                                        options2[0]
                                    )
                                    if (chose2 == 0) {
                                        mess += ""
                                        Crypt.test(mess)
                                        TestFile.writeText(Crypt.crp)

                                    } else {
                                        Crypt.test(mess)
                                        TestFile.writeText(Crypt.crp)
                                    }

                                } else {
                                    Crypt.test(mess)
                                    TestFile.writeText(Crypt.crp)
                                }

                                if (chose == 0) {
                                    TestQiniu.main(TestFile.toString(), "$configname.txt")
                                    TestFile.delete()
                                    chat("The file has been uploaded to the cloud file name$configname")
                                } else {
                                    chat("§6Successfully written out to the profile file name:$configname")
                                }
                                chat("§6Settings saved successfully.")

                            } catch (throwable: Throwable) {
                                chat("§cFailed to create local config: §3${throwable.message}")
                                ClientUtils.getLogger().error("Failed to create local config.", throwable)
                                val values = true
                                val binds = true
                                val states = true
                                println(SettingsUtils.generateScript(values, binds, states))
                                chatSyntax("ConfigSetting save  [name]")
                                return
                            }
                            return
                        } else {
                            JOptionPane.showMessageDialog(
                                null,
                                "Your Group is User You are not authorized to use this instruction",
                                "failed",
                                0
                            )
                            chat("§cYou don't hava power to use this command")
                        }

                    }


                }

                args[1].equals("delete", ignoreCase = true) -> {
                    if (WbxMain.cheakuser() == 2 || WbxMain.cheakuser() == 3) {
                        if (args.size > 2)
                            try {
                                val chose: Int
                                val pass2:String
                                val password:String
                                val options = arrayOf("Yes", "No")
                                val str2:String
                                val str3:String
                                val delete: String = args[2]
                                str2 = HttpUtil.webget("http://rs2rfmq35.hn-bkt.clouddn.com//$delete.txt")
                                str3 = Crypt.decrypt(str2)
                                chose = JOptionPane.showOptionDialog(
                                    null,
                                    "Are you sure to delete this config?",
                                    "Update",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    null,
                                    options,
                                    options[0]
                                )
                                System.out.println(str3)
                                if (chose == 0) {
                                    if (str3.contains("iamofficalconfig")) {
                                        JOptionPane.showMessageDialog(null,"This is official config anybody don’t have permission to delete")
                                        chat("§cYou can tell Dev to delete this config")

                                    }else{
                                        if (WbxMain.cheakuser() == 2) {
                                            TestQiniu.deleteFile("$delete.txt")
                                            chat("§6Settings file deleted successfully.")
                                            JOptionPane.showMessageDialog(
                                                null,
                                                "You are Dev you can at will delete configs"
                                            )
                                        } else if (str3.contains("[have-pass:")) {
                                            password = JOptionPane.showInputDialog("This config hava passwords please input parer passwords")
                                            pass2 = "[have-pass:" + password + "]"
                                            if (str3.contains(pass2)) {
                                                TestQiniu.deleteFile("$delete.txt")
                                                chat("§6Settings file deleted successfully.")
                                            } else {
                                                JOptionPane.showMessageDialog(
                                                    null,
                                                    "Password was failed", "Error", 0
                                                )
                                            }
                                        } else {
                                            TestQiniu.deleteFile("$delete.txt")
                                            chat("§6Settings file deleted successfully.")
                                        }
                                    }
                                }else {
                                    chat("§Canceled.")
                                }
                            } catch (throwable: Throwable) {
                                chat("§cFailed to create local config: §3${throwable.message}")
                            }



                        chatSyntax("ConfigSetting delete [name]")
                        return
                    }else{
                        JOptionPane.showMessageDialog(null,"just Tester or Dev can delete configs","Error",0)
                        chat("§cFailed to create local config: §3just Tester or Dev can delete configs")
                    }
                }
                args[1].equals("clean", ignoreCase = true) ->{
                    val chose:Int
                    val options = arrayOf("Yes", "No")
                    if (WbxMain.cheakuser() == 2) {
                        chose  = JOptionPane.showOptionDialog(null,"Are you sure you want to empty the configuration repository?","Chose",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0])
                        if (chose == 0){
                            TestQiniu.clean()
                            chat("All configurations of the repository were successfully deleted")
                        }else{
                            chat("Canceled")

                        }

                    }else{
                        JOptionPane.showMessageDialog(null,"This command is only available to dev")
                    }

                }

                args[1].equals("list", ignoreCase = true) -> {
                    chat("§cSettings:")
                    val  allconfig  = TestQiniu.listObjects1()
                    chat("§7[§3§lConfig§7]目前库中有\n"+allconfig+"参数")
                }
            }
        }
        chatSyntax("ConfigSetting <load/save/list/delete/clean>")
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        return when (args.size) {
            1 -> listOf("delete", "list", "load", "save","clean").filter { it.startsWith(args[0], true) }

            else -> emptyList()
        }
    }
    fun Bind(){
        chat("§c§lBinds")
        LiquidBounce.moduleManager.modules.filter { it.keyBind != Keyboard.KEY_NONE }.forEach {
            ClientUtils.displayChatMessage("§6> §c${it.name}: §a§l${Keyboard.getKeyName(it.keyBind)}")
        }
    }

}