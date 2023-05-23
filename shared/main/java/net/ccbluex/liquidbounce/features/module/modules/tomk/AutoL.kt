package net.ccbluex.liquidbounce.features.module.modules.tomk

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.TextValue
import java.util.*

@ModuleInfo(name = "AutoL", category = ModuleCategory.TOMK, description = "修复版")
class AutoL : Module() {
    private val L = BoolValue ("L", true)
    var AutoLmsg = arrayOf(
            "止余天坑太子爷",
            "Tomk天坑第一",
            "反复的鼓励你的",
            "你快去买Tomk",
            "为什么被我打跑了",
            "我是止余神的",
            "速度进行殴打",
            "我可是Tomk",
            "Tomk是最吊的",
            "你跟我叫什么",
            "止余Tomk无敌",
            "你打不过的",
            "我可是真防砍",
            "满转头呜呜呜",
            "给你打的晕头",
            "Tomk天坑第一配",
            "Tomk无敌",
            "你打不过Tomk",
            "速度Tomk吧",
            "你拿什么打",
            "拿你的无知评价Tomk",
            "是不是厉害的配置",
            "反复的就是骂你",
            "没事我就是闲的打你",
            "别跟我扣子你不是那个",
            "huayuting 16 L",
            "我这个宣传检测得到吗",
            "你的止余哥哥很舒服的",
            "一天我就是500躺着挣",
            "已经回来了",
            "殴打所有人ok？？",
            "止余的Tomk"

    )
    // Target
    var target: IEntityLivingBase? = null
    var kill = 0

    @EventTarget
    fun onAttack(event: AttackEvent) {
        target = event.targetEntity as IEntityLivingBase?
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (target!!.health <= 0.1) {
            kill += 1
            if (L.get()) {
                val r = Random()
                mc.thePlayer!!.sendChatMessage(AutoLmsg.get(r.nextInt(AutoLmsg.size)) )
            }
            target = null
        }
    }

    fun kills() : Int {
        return kill
    }
    override val tag: String?
        get() = "Kill $kill"
}
