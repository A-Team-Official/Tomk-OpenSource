
package net.ccbluex.liquidbounce.features.module.modules.tomk

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.enums.BlockType
import net.ccbluex.liquidbounce.api.enums.EnumFacingType
import net.ccbluex.liquidbounce.api.enums.StatType
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerBlockPlacement
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.*
import net.ccbluex.liquidbounce.utils.block.BlockUtils
import net.ccbluex.liquidbounce.utils.block.PlaceInfo
import net.ccbluex.liquidbounce.utils.block.PlaceInfo.Companion.get
import net.ccbluex.liquidbounce.utils.extensions.rayTraceWithServerSideRotation
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.utils.timer.TickTimer
import net.ccbluex.liquidbounce.utils.timer.TimeUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


@ModuleInfo(name = "FdpScaffold", description = "Scaffold" , category = ModuleCategory.TOMK)
class FdpScaffold : Module() {

    // Delay
    private val maxDelayValue: IntegerValue = object : IntegerValue("MaxDelay", 0, 0, 1000) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = minDelayValue.get()
            if (i > newValue) set(i)
        }
    }
    private val minDelayValue: IntegerValue = object : IntegerValue("MinDelay", 0, 0, 1000) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = maxDelayValue.get()
            if (i < newValue) set(i)
        }
    }
    private val placeableDelay = ListValue("PlaceableDelay", arrayOf("Normal", "Smart", "Off"), "Normal")

    // AutoBlock
    private val autoBlockValue = ListValue("AutoBlock", arrayOf("Spoof", "LiteSpoof", "Switch", "OFF"), "LiteSpoof")

    // Basic stuff
    private val sprintValue = ListValue("Sprint", arrayOf("Always", "Dynamic", "OnGround", "OffGround", "OFF"), "Always")
    private val swingValue = ListValue("Swing", arrayOf("Normal", "Packet", "None"), "Normal")
    private val searchValue = BoolValue("Search", true)
    private val downValue = BoolValue("Down", true)
    private val placeModeValue = ListValue("PlaceTiming", arrayOf("Pre", "Post"), "Post")

    // Eagle
    private val eagleValue = ListValue("Eagle", arrayOf("Slient", "Normal", "OFF"), "OFF")
    private val blocksToEagleValue = IntegerValue("BlocksToEagle", 0, 0, 10)

    // Expand
    private val expandLengthValue = IntegerValue("ExpandLength", 5, 1, 6)

    // Rotations
    private val rotationsValue = ListValue("Rotations", arrayOf("None", "Vanilla", "AAC" ,"Test1", "Test2"), "AAC")
    private val aacYawValue = IntegerValue("AACYawOffset", 0, 0, 90)
    //private val tolleyBridgeValue = IntegerValue("TolleyBridgeTick", 0, 0, 10)
    //private val tolleyYawValue = IntegerValue("TolleyYaw", 0, 0, 90)
    private val silentRotationValue = BoolValue("SilentRotation", true)
    private val minRotationSpeedValue:IntegerValue = object :IntegerValue("MinRotationSpeed", 180, 0, 180) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val v = maxRotationSpeedValue.get()
            if (v < newValue) set(v)
        }
    }
    private val maxRotationSpeedValue:IntegerValue = object :IntegerValue("MaxRotationSpeed", 180, 0, 180) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val v = minRotationSpeedValue.get()
            if (v > newValue) set(v)
        }
    }
    private val keepLengthValue = IntegerValue("KeepRotationTick", 0, 0, 20)
    //private var tolleyStayTick = 0
    //private var lastTickOnGround = false

    // Zitter
    //private val zitterValue = BoolValue("Zitter", false)
    private val zitterModeValue = ListValue("ZitterMode", arrayOf("Teleport", "Smooth", "OFF"), "OFF")
    private val zitterSpeed = FloatValue("ZitterSpeed", 0.13f, 0.1f, 0.3f)
    private val zitterStrength = FloatValue("ZitterStrength", 0.072f, 0.05f, 0.2f)

    // Game
    private val timerValue = FloatValue("Timer", 1f, 0.1f, 5f)
    private val motionspeedstatus = BoolValue("MotionSpeedSet", false)
    private val motionspeed = FloatValue("MotionSpeedValue", 0.1f, 0.05f, 1f)
    private val speedModifierValue = FloatValue("SpeedModifier", 1f, 0f, 2f)

    // Tower
    private val towerModeValue = ListValue(
            "TowerMode", arrayOf(
            "None",
            "Jump",
            "Motion",
            "ConstantMotion",
            "PlusMotion",
            "StableMotion",
            "MotionTP",
            "Packet",
            "Teleport",
            "AAC3.3.9",
            "AAC3.6.4",
            "AAC4.4Constant",
            "AAC4Jump"
    ), "None"
    )
    private val stopWhenBlockAbove = BoolValue("StopTowerWhenBlockAbove", true)
    private val towerActiveValue = ListValue("TowerActivation", arrayOf("Always", "PressSpace", "NoMove", "OFF"), "PressSpace")
    private val towerTimerValue = FloatValue("TowerTimer", 1f, 0.1f, 5f)

    // Safety
    private val sameYValue = ListValue("SameY", arrayOf("Simple", "AutoJump", "WhenSpeed", "OFF"), "WhenSpeed")
    private val safeWalkValue = ListValue("SafeWalk", arrayOf("Ground", "Air", "OFF"), "OFF")
    private val hitableCheck = ListValue("HitableCheck", arrayOf("Simple", "Strict", "OFF"), "Simple")

    // Extra click
    private val extraClickValue = ListValue("ExtraClick", arrayOf("EmptyC08", "AfterPlace", "OFF"), "OFF")
    private val extraClickMaxDelayValue: IntegerValue = object : IntegerValue("ExtraClickMaxDelay", 100, 20, 300) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = extraClickMinDelayValue.get()
            if (i > newValue) set(i)
        }
    }
    private val extraClickMinDelayValue: IntegerValue = object : IntegerValue("ExtraClickMinDelay", 50, 20, 300) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = extraClickMaxDelayValue.get()
            if (i < newValue) set(i)
        }
    }

    // Jump mode
    private val jumpMotionValue = FloatValue("TowerJumpMotion", 0.42f, 0.3681289f, 0.79f)
    private val jumpDelayValue = IntegerValue("TowerJumpDelay", 0, 0, 20)

    // Stable/PlusMotion
    private val stableMotionValue = FloatValue("TowerStableMotion", 0.42f, 0.1f, 1f)
    private val plusMotionValue = FloatValue("TowerPlusMotion", 0.1f, 0.01f, 0.2f)
    private val plusMaxMotionValue = FloatValue("TowerPlusMaxMotion", 0.8f, 0.1f, 2f)

    // ConstantMotion
    private val constantMotionValue = FloatValue("TowerConstantMotion", 0.42f, 0.1f, 1f)
    private val constantMotionJumpGroundValue = FloatValue("TowerConstantMotionJumpGround", 0.79f, 0.76f, 1f)

    // Teleport
    private val teleportHeightValue = FloatValue("TowerTeleportHeight", 1.15f, 0.1f, 5f)
    private val teleportDelayValue = IntegerValue("TowerTeleportDelay", 0, 0, 20)
    private val teleportGroundValue = BoolValue("TowerTeleportGround", true)
    private val teleportNoMotionValue = BoolValue("TowerTeleportNoMotion", false)

    // Visuals
    private val counterDisplayValue = BoolValue("Counter", true)
    private val markValue = BoolValue("Mark", false)
    private val r = IntegerValue("MarkRed", 255, 0, 255)
    private val g = IntegerValue("MarkGreen", 255, 0, 255)
    private val b = IntegerValue("MarkBlue", 255, 0, 255)
    private val a = IntegerValue("MarkAlpha", 255, 0, 255)
    private val outline = BoolValue("MarkOutline", false)

    /**
     * MODULE
     */
    // Target block
    private var targetPlace: PlaceInfo? = null

    // Last OnGround position
    private var lastGroundY = 0

    // Rotation lock
    private var lockRotation: Rotation? = null

    // Auto block slot
    private var slot = 0

    // Zitter Smooth
    private var zitterDirection = false

    // Delay
    private val delayTimer = MSTimer()
    private val zitterTimer = MSTimer()
    private val clickTimer = MSTimer()
    private val towerTimer = TickTimer()
    private var delay: Long = 0
    private var clickDelay: Long = 0
    private var lastPlace = 0

    // Eagle
    private var placedBlocksWithoutEagle = 0
    private var eagleSneaking = false

    // Down
    private var shouldGoDown = false
    private var jumpGround = 0.0
    private var towerStatus = false
    private var canSameY = false
    private var lastPlaceBlock: WBlockPos?=null
    private var afterPlaceC08:ICPacketPlayerBlockPlacement?=null

    /**
     * Enable module
     */
    override fun onEnable() {
        if (mc.thePlayer!! == null) return
        lastGroundY = mc.thePlayer!!!!.posY.toInt()
        lastPlace=2
        clickDelay=TimeUtils.randomDelay(extraClickMinDelayValue.get(), extraClickMaxDelayValue.get())
    }

    /**
     * Update event
     *
     * @param event
     */
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        //if(!mc.thePlayer!!.onGround) tolleyStayTick=0
        //    else tolleyStayTick++
        //if(tolleyStayTick>100) tolleyStayTick=100
        if(towerStatus && towerModeValue.get().toLowerCase()!="aac3.3.9" && towerModeValue.get().toLowerCase()!="aac4.4constant" && towerModeValue.get().toLowerCase()!="aac4jump") mc.timer.timerSpeed = towerTimerValue.get()
        if(!towerStatus) mc.timer.timerSpeed = timerValue.get()
        if (towerStatus || mc.thePlayer!!.isCollidedHorizontally) {
            canSameY = false
            lastGroundY = mc.thePlayer!!.posY.toInt()
        } else {
            when(sameYValue.get().toLowerCase()){
                "simple" -> {
                    canSameY = true
                }
                "autojump" -> {
                    canSameY = true
                    if (MovementUtils.isMoving)
                        mc.thePlayer!!.jump()
                }
                "whenspeed" -> {
                    canSameY=LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state
                }
                else -> {
                    canSameY = false
                }
            }
            if(mc.thePlayer!!.onGround)
                lastGroundY = mc.thePlayer!!.posY.toInt()
        }

        if(clickTimer.hasTimePassed(clickDelay)){
            when(extraClickValue.get().toLowerCase()){
                "emptyc08" -> mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerBlockPlacement(mc.thePlayer!!.inventory.getStackInSlot(slot)))
                "afterplace" -> {
                    if(afterPlaceC08!=null){
                        if(mc.thePlayer!!.getDistanceSqToCenter(lastPlaceBlock!!)<10){
                            mc.netHandler.addToSendQueue(afterPlaceC08!!)
                        }else{
                            afterPlaceC08=null
                        }
                    }
                }
            }
            clickDelay=TimeUtils.randomDelay(extraClickMinDelayValue.get(), extraClickMaxDelayValue.get())
            clickTimer.reset()
        }

        mc.thePlayer!!.sprinting = canSprint

        shouldGoDown = downValue.get() && mc.gameSettings.isKeyDown(mc.gameSettings.keyBindSneak) && blocksAmount > 1
        if (shouldGoDown) mc.gameSettings.keyBindSneak.pressed = false
        if (mc.thePlayer!!.onGround) {
            // Smooth Zitter
            if (zitterModeValue.get().equals("smooth", ignoreCase = true)) {
                if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindRight)) mc.gameSettings.keyBindRight.pressed = false
                if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindLeft)) mc.gameSettings.keyBindLeft.pressed = false
                if (zitterTimer.hasTimePassed(100)) {
                    zitterDirection = !zitterDirection
                    zitterTimer.reset()
                }
                if (zitterDirection) {
                    mc.gameSettings.keyBindRight.pressed = true
                    mc.gameSettings.keyBindLeft.pressed = false
                } else {
                    mc.gameSettings.keyBindRight.pressed = false
                    mc.gameSettings.keyBindLeft.pressed = true
                }
            }

            // Eagle
            if (!eagleValue.get().equals("off", ignoreCase = true) && !shouldGoDown) {
                if (placedBlocksWithoutEagle >= blocksToEagleValue.get()) {
                    val shouldEagle = mc.theWorld!!.getBlockState(
                            WBlockPos(
                                    mc.thePlayer!!.posX,
                                    mc.thePlayer!!.posY - 1.0, mc.thePlayer!!.posZ
                            )
                    ).block === classProvider.getBlockEnum(BlockType.AIR)
                    if (eagleValue.get().equals("slient", ignoreCase = true)) {
                        if (eagleSneaking != shouldEagle) {
                            mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, if (shouldEagle) ICPacketEntityAction.WAction.START_SNEAKING else ICPacketEntityAction.WAction.STOP_SNEAKING))
                        }
                        eagleSneaking = shouldEagle
                    } else mc.gameSettings.keyBindSneak.pressed = shouldEagle
                    placedBlocksWithoutEagle = 0
                } else placedBlocksWithoutEagle++
            }

            // Zitter
            if (zitterModeValue.get().equals("teleport", ignoreCase = true)) {
                MovementUtils.strafe(zitterSpeed.get())
                val yaw = Math.toRadians(mc.thePlayer!!.rotationYaw + if (zitterDirection) 90.0 else -90.0)
                mc.thePlayer!!.motionX -= sin(yaw) * zitterStrength.get()
                mc.thePlayer!!.motionZ += cos(yaw) * zitterStrength.get()
                zitterDirection = !zitterDirection
            }
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (mc.thePlayer!! == null) return
        val packet = event.packet

        // AutoBlock
        if (classProvider.isCPacketHeldItemChange(packet)) {
            slot = packet.asCPacketHeldItemChange().slotId
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        val eventState = event.eventState
        towerStatus = false;
        // Tower
        if(motionspeedstatus.get()) MovementUtils.setMotion(motionspeed.get().toDouble())
        towerStatus = (!stopWhenBlockAbove.get() || classProvider.isBlockAir(BlockUtils.getBlock(WBlockPos(mc.thePlayer!!.posX, mc.thePlayer!!.posY + 2, mc.thePlayer!!.posZ))))
        if(towerStatus) {
            //further checks
            when(towerActiveValue.get().toLowerCase()) {
                "off" -> towerStatus = false
                "always" -> {
                    towerStatus = (mc.gameSettings.keyBindLeft.isKeyDown
                            || mc.gameSettings.keyBindRight.isKeyDown || mc.gameSettings.keyBindForward.isKeyDown
                            || mc.gameSettings.keyBindBack.isKeyDown)
                }
                "pressspace" -> {
                    towerStatus = mc.gameSettings.keyBindJump.isKeyDown
                }
                "nomove" -> {
                    towerStatus = !(mc.gameSettings.keyBindLeft.isKeyDown
                            || mc.gameSettings.keyBindRight.isKeyDown || mc.gameSettings.keyBindForward.isKeyDown
                            || mc.gameSettings.keyBindBack.isKeyDown) && mc.gameSettings.keyBindJump.isKeyDown
                }
            }
        }
        if(towerStatus) move()
        // Lock Rotation
        if (rotationsValue.get() != "None" && keepLengthValue.get()>0 && lockRotation != null && silentRotationValue.get()) {
            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, lockRotation, getSpeed())
            RotationUtils.setTargetRotation(limitedRotation, keepLengthValue.get())
        }

        // Update and search for new block
        if (event.eventState == EventState.PRE) update()

        // Place block
        if (placeModeValue.get().equals(eventState.stateName, ignoreCase = true)) place()

        // Reset placeable delay
        if (targetPlace == null && !placeableDelay.get().equals("off", ignoreCase = true)) {
            if(placeableDelay.get().equals("Smart", ignoreCase = true)) {
                if(lastPlace==0) delayTimer.reset()
            }else delayTimer.reset()
        }
    }

    private fun fakeJump() {
        mc.thePlayer!!.isAirBorne = true
        mc.thePlayer!!.triggerAchievement(classProvider.getStatEnum(StatType.JUMP_STAT))
    }

    private fun move() {
        when (towerModeValue.get().toLowerCase()) {
            "none" -> {
                if (mc.thePlayer!!.onGround) {
                    fakeJump()
                    mc.thePlayer!!.motionY = 0.42
                }
            }
            "jump" -> {
                if (mc.thePlayer!!.onGround && towerTimer.hasTimePassed(jumpDelayValue.get())) {
                    fakeJump()
                    mc.thePlayer!!.motionY = jumpMotionValue.get().toDouble()
                    towerTimer.reset()
                }
            }
            "motion" -> {
                if (mc.thePlayer!!.onGround) {
                    fakeJump()
                    mc.thePlayer!!.motionY = 0.42
                } else if (mc.thePlayer!!.motionY < 0.1) mc.thePlayer!!.motionY = -0.3
            }
            "motiontp" -> {
                if (mc.thePlayer!!.onGround) {
                    fakeJump()
                    mc.thePlayer!!.motionY = 0.42
                } else if (mc.thePlayer!!.motionY < 0.23)
                    mc.thePlayer!!.setPosition(mc.thePlayer!!.posX, mc.thePlayer!!.posY, mc.thePlayer!!.posZ)
            }
            "packet" -> {
                if (mc.thePlayer!!.onGround && towerTimer.hasTimePassed(2)) {
                    fakeJump()
                    mc.netHandler.addToSendQueue(
                            classProvider.createCPacketPlayerPosition(
                                    mc.thePlayer!!.posX,
                                    mc.thePlayer!!.posY + 0.42, mc.thePlayer!!.posZ, false
                            )
                    )
                    mc.netHandler.addToSendQueue(
                            classProvider.createCPacketPlayerPosition(
                                    mc.thePlayer!!.posX,
                                    mc.thePlayer!!.posY + 0.753, mc.thePlayer!!.posZ, false
                            )
                    )
                    mc.thePlayer!!.setPosition(mc.thePlayer!!.posX, mc.thePlayer!!.posY + 1.0, mc.thePlayer!!.posZ)
                    towerTimer.reset()
                }
            }
            "teleport" -> {
                if (teleportNoMotionValue.get()) mc.thePlayer!!.motionY = 0.0
                if ((mc.thePlayer!!.onGround || !teleportGroundValue.get()) && towerTimer.hasTimePassed(teleportDelayValue.get())) {
                    fakeJump()
                    mc.thePlayer!!.setPositionAndUpdate(
                            mc.thePlayer!!.posX,
                            mc.thePlayer!!.posY + teleportHeightValue.get(),
                            mc.thePlayer!!.posZ
                    )
                    towerTimer.reset()
                }
            }
            "constantmotion" -> {
                if (mc.thePlayer!!.onGround) {
                    fakeJump()
                    jumpGround = mc.thePlayer!!.posY
                    mc.thePlayer!!.motionY = constantMotionValue.get().toDouble()
                }
                if (mc.thePlayer!!.posY > jumpGround + constantMotionJumpGroundValue.get()) {
                    fakeJump()
                    mc.thePlayer!!.setPosition(mc.thePlayer!!.posX, mc.thePlayer!!.posY, mc.thePlayer!!.posZ)
                    mc.thePlayer!!.motionY = constantMotionValue.get().toDouble()
                    jumpGround = mc.thePlayer!!.posY
                }
            }
            "plusmotion" -> {
                mc.thePlayer!!.motionY += plusMotionValue.get()
                if (mc.thePlayer!!.motionY >= plusMaxMotionValue.get()) {
                    mc.thePlayer!!.motionY = plusMaxMotionValue.get().toDouble()
                }
            }
            "stablemotion" -> {
                mc.thePlayer!!.motionY = stableMotionValue.get().toDouble()
            }
            "aac3.3.9" -> {
                if (mc.thePlayer!!.onGround) {
                    fakeJump()
                    mc.thePlayer!!.motionY = 0.4001
                }
                mc.timer.timerSpeed = 1f
                if (mc.thePlayer!!.motionY < 0) {
                    mc.thePlayer!!.motionY -= 0.00000945
                    mc.timer.timerSpeed = 1.6f
                }
            }
            "aac3.6.4" -> {
                if (mc.thePlayer!!.ticksExisted % 4 == 1) {
                    mc.thePlayer!!.motionY = 0.4195464
                    mc.thePlayer!!.setPosition(mc.thePlayer!!.posX - 0.035, mc.thePlayer!!.posY, mc.thePlayer!!.posZ)
                } else if (mc.thePlayer!!.ticksExisted % 4 == 0) {
                    mc.thePlayer!!.motionY = -0.5
                    mc.thePlayer!!.setPosition(mc.thePlayer!!.posX + 0.035, mc.thePlayer!!.posY, mc.thePlayer!!.posZ)
                }
            }
            "aac4.4constant" -> {
                if (mc.thePlayer!!.onGround) {
                    fakeJump()
                    jumpGround = mc.thePlayer!!.posY
                    mc.thePlayer!!.motionY = 0.42
                }
                mc.thePlayer!!.motionX = 0.0
                mc.thePlayer!!.motionZ = -0.00000001
                mc.thePlayer!!.jumpMovementFactor = 0.000F
                mc.timer.timerSpeed = 0.60f
                if (mc.thePlayer!!.posY > jumpGround + 0.99) {
                    fakeJump()
                    mc.thePlayer!!.setPosition(mc.thePlayer!!.posX, mc.thePlayer!!.posY-0.001335979112146, mc.thePlayer!!.posZ)
                    mc.thePlayer!!.motionY = 0.42
                    jumpGround = mc.thePlayer!!.posY
                    mc.timer.timerSpeed = 0.75f
                }
            }
            "aac4jump" -> {
                mc.timer.timerSpeed = 0.97f
                if (mc.thePlayer!!.onGround) {
                    fakeJump()
                    mc.thePlayer!!.motionY = 0.387565
                    mc.timer.timerSpeed = 1.05f
                }
            }
        }
    }

    private fun update() {
        if (if (!autoBlockValue.get().equals("off", ignoreCase = true)) InventoryUtils.findAutoBlockBlock() == -1 else mc.thePlayer!!.heldItem == null ||
                        !(classProvider.isItemBlock(mc.thePlayer!!.heldItem!!.item) && !InventoryUtils.isBlockListBlock(mc.thePlayer!!.heldItem!!.item!!.asItemBlock()))
        ) return
        findBlock(expandLengthValue.get()>1)
    }

    /**
     * Search for new target block
     */
    private fun findBlock(expand: Boolean) {
        val blockPosition = if (shouldGoDown) if (mc.thePlayer!!.posY == mc.thePlayer!!.posY.toInt() + 0.5) WBlockPos(
                mc.thePlayer!!.posX,
                mc.thePlayer!!.posY - 0.6,
                mc.thePlayer!!.posZ
        ) else WBlockPos(
                mc.thePlayer!!.posX, mc.thePlayer!!.posY - 0.6, mc.thePlayer!!.posZ
        ).down() else if (mc.thePlayer!!.posY == mc.thePlayer!!.posY.toInt() + 0.5 && !canSameY) WBlockPos(
                mc.thePlayer!!
        ) else if(canSameY && lastGroundY<=mc.thePlayer!!.posY) WBlockPos(
                mc.thePlayer!!.posX, lastGroundY-1.0, mc.thePlayer!!.posZ) else WBlockPos(
                mc.thePlayer!!.posX, mc.thePlayer!!.posY, mc.thePlayer!!.posZ
        ).down()
        if (!expand && (!BlockUtils.isReplaceable(blockPosition) || search(blockPosition, !shouldGoDown))) return
        if (expand) {
            for (i in 0 until expandLengthValue.get()) {
                if (search(
                                blockPosition.add(
                                        if (mc.thePlayer!!.horizontalFacing == classProvider.getEnumFacing(EnumFacingType.WEST)) -i else if (mc.thePlayer!!.horizontalFacing == classProvider.getEnumFacing(EnumFacingType.EAST)) i else 0,
                                        0,
                                        if (mc.thePlayer!!.horizontalFacing == classProvider.getEnumFacing(EnumFacingType.NORTH)) -i else if (mc.thePlayer!!.horizontalFacing == classProvider.getEnumFacing(EnumFacingType.SOUTH)) i else 0
                                ), false
                        )
                ) return
            }
        } else if (searchValue.get()) {
            for (x in -1..1) for (z in -1..1) if (search(blockPosition.add(x, 0, z), !shouldGoDown)) return
        }
    }

    /**
     * Place target block
     */
    private fun place() {
        if (targetPlace == null) {
            if (!placeableDelay.get().equals("Off", ignoreCase = true)) {
                if(lastPlace==0 && placeableDelay.get().equals("Smart", ignoreCase = true)) delayTimer.reset()
                if(placeableDelay.get().equals("Normal", ignoreCase = true)) delayTimer.reset()
                if(lastPlace>0) lastPlace--
            }
            return
        }
        if (!delayTimer.hasTimePassed(delay) || !towerStatus && canSameY && lastGroundY - 1 != targetPlace!!.vec3.yCoord.toInt())
            return

        if(!rotationsValue.get().equals("None",true)){
            val rayTraceInfo=mc.thePlayer!!.rayTraceWithServerSideRotation(5.0)
            when(hitableCheck.get().toLowerCase()){
                "simple" -> {
                    if(!rayTraceInfo!!.blockPos!!.equals(targetPlace!!.blockPos)){
                        return
                    }
                }
                "strict" -> {
                    if(!rayTraceInfo!!.blockPos!!.equals(targetPlace!!.blockPos)||rayTraceInfo.sideHit!=targetPlace!!.enumFacing){
                        return
                    }
                }
            }
        }

        val isDynamicSprint=sprintValue.get().equals("dynamic",true)
        var blockSlot = -1
        var itemStack = mc.thePlayer!!.heldItem
        if (mc.thePlayer!!.heldItem == null || !(classProvider.isItemBlock(mc.thePlayer!!.heldItem!!.item) && !InventoryUtils.isBlockListBlock(mc.thePlayer!!.heldItem!!.item!!.asItemBlock()))) {
            if (autoBlockValue.get().equals("off", ignoreCase = true)) return
            blockSlot = InventoryUtils.findAutoBlockBlock()
            if (blockSlot == -1) return
            if (autoBlockValue.get().equals("LiteSpoof", ignoreCase = true) || autoBlockValue.get().equals("Spoof", ignoreCase = true)) {
                mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange(blockSlot - 36))
            } else {
                mc.thePlayer!!.inventory.currentItem = blockSlot-36
            }
            itemStack = mc.thePlayer!!.inventoryContainer.getSlot(blockSlot).stack
        }
        if(isDynamicSprint)
            mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SPRINTING))
        if (mc.playerController.onPlayerRightClick(mc.thePlayer!!, mc.theWorld!!, itemStack, targetPlace!!.blockPos, targetPlace!!.enumFacing, targetPlace!!.vec3)) {
            //delayTimer.reset()
            delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get())
            if (mc.thePlayer!!.onGround) {
                val modifier = speedModifierValue.get()
                mc.thePlayer!!.motionX *= modifier.toDouble()
                mc.thePlayer!!.motionZ *= modifier.toDouble()
            }

            val swing=swingValue.get()
            if(swing.equals("packet",true)){
                mc.netHandler.addToSendQueue(classProvider.createCPacketAnimation())
            }else if(swing.equals("normal",true)){
                mc.thePlayer!!.swingItem()
            }
            lastPlace=2
            lastPlaceBlock=targetPlace!!.blockPos.add(targetPlace!!.enumFacing.directionVec)
            when(extraClickValue.get().toLowerCase()){
                "afterplace" -> {
                    // fake click
                    val blockPos=targetPlace!!.blockPos
                    val hitVec=targetPlace!!.vec3
                    afterPlaceC08= classProvider.createCPacketPlayerBlockPlacement(targetPlace!!.blockPos, targetPlace!!.enumFacing.index, itemStack
                            , (hitVec.xCoord - blockPos.x.toDouble()).toFloat(), (hitVec.yCoord - blockPos.y.toDouble()).toFloat(), (hitVec.zCoord - blockPos.z.toDouble()).toFloat())
                }
            }
        }
        if(isDynamicSprint)
            mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SPRINTING))

        if (autoBlockValue.get().equals("LiteSpoof", ignoreCase = true) && blockSlot >= 0)
            mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange(mc.thePlayer!!.inventory.currentItem))

        // Reset
        targetPlace = null
    }

    /**
     * Disable scaffold module
     */
    override fun onDisable() {
        //tolleyStayTick=999
        if (mc.thePlayer!! == null) return
        if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
            mc.gameSettings.keyBindSneak.pressed = false
            if (eagleSneaking) mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SNEAKING))
        }
        if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindRight)) mc.gameSettings.keyBindRight.pressed = false
        if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindLeft)) mc.gameSettings.keyBindLeft.pressed = false
        lockRotation = null
        mc.timer.timerSpeed = 1f
        shouldGoDown = false
        RotationUtils.reset()
        if (slot != mc.thePlayer!!.inventory.currentItem) mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange(mc.thePlayer!!.inventory.currentItem))
    }

    /**
     * Entity movement event
     *
     * @param event
     */
    @EventTarget
    fun onMove(event: MoveEvent) {
        if (safeWalkValue.get().equals("off", ignoreCase = true) || shouldGoDown) return
        if (safeWalkValue.get().equals("air", ignoreCase = true) || mc.thePlayer!!.onGround) event.isSafeWalk = true
    }

    /**
     * Scaffold visuals
     *
     * @param event
     */
    @EventTarget
    fun onRender2D(event: Render2DEvent?) {
        if (counterDisplayValue.get()) {
            drawTip()
        }
    }

    /**
     * Scaffold visuals
     *
     * @param event
     */
    @EventTarget
    fun onRender3D(event: Render3DEvent?) {
        if (!markValue.get()) return
        for (i in 0 until (expandLengthValue.get() + 1)) {
            val blockPos = WBlockPos(
                    mc.thePlayer!!.posX + if (mc.thePlayer!!.horizontalFacing == classProvider.getEnumFacing(EnumFacingType.WEST)) -i else if (mc.thePlayer!!.horizontalFacing == classProvider.getEnumFacing(EnumFacingType.EAST)) i else 0,
                    mc.thePlayer!!.posY - (if (mc.thePlayer!!.posY == mc.thePlayer!!.posY.toInt() + 0.5){ 0.0 } else{ 1.0 }) - (if (shouldGoDown){ 1.0 }else{ 0.0 }),
                    mc.thePlayer!!.posZ + if (mc.thePlayer!!.horizontalFacing == classProvider.getEnumFacing(EnumFacingType.NORTH)) -i else if (mc.thePlayer!!.horizontalFacing == classProvider.getEnumFacing(EnumFacingType.SOUTH)) i else 0
            )
            val placeInfo = get(blockPos)
            if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                RenderUtils.drawBlockBox2(blockPos, Color(r.get(), g.get(), b.get(), a.get()), outline.get(), true,1F)
                break
            }
        }
    }

    /**
     * Search for placeable block
     *
     * @param blockPosition pos
     * @param checks        visible
     * @return
     */
    private fun search(blockPosition: WBlockPos, checks: Boolean): Boolean {
        if (!BlockUtils.isReplaceable(blockPosition)) return false
        val eyesPos = WVec3(
                mc.thePlayer!!.posX,
                mc.thePlayer!!.entityBoundingBox.minY + mc.thePlayer!!.eyeHeight,
                mc.thePlayer!!.posZ
        )
        var placeRotation: PlaceRotation? = null
        for (facingType in EnumFacingType.values()) {
            val side = classProvider.getEnumFacing(facingType)
            val neighbor = blockPosition.offset(side)
            if (!BlockUtils.canBeClicked(neighbor)) continue
            val dirVec = WVec3(side.directionVec)
            var xSearch = 0.1
            while (xSearch < 0.9) {
                var ySearch = 0.1
                while (ySearch < 0.9) {
                    var zSearch = 0.1
                    while (zSearch < 0.9) {
                        val posVec = WVec3(blockPosition).addVector(xSearch, ySearch, zSearch)
                        val distanceSqPosVec = eyesPos.squareDistanceTo(posVec)
                        val hitVec = posVec.add(WVec3(dirVec.xCoord * 0.5, dirVec.yCoord * 0.5, dirVec.zCoord * 0.5))
                        if (checks && (eyesPos.squareDistanceTo(hitVec) > 18.0 || distanceSqPosVec > eyesPos.squareDistanceTo(
                                        posVec.add(dirVec)
                                ) || mc.theWorld!!.rayTraceBlocks(eyesPos, hitVec, false, true, false) != null)
                        ) {
                            zSearch += 0.1
                            continue
                        }

                        // face block
                        val diffX = hitVec.xCoord - eyesPos.xCoord
                        val diffY = hitVec.yCoord - eyesPos.yCoord
                        val diffZ = hitVec.zCoord - eyesPos.zCoord
                        val diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ).toDouble()
                        val rotation = Rotation(
                                WMathHelper.wrapAngleTo180_float(Math.toDegrees(atan2(diffZ, diffX)).toFloat() - 90f),
                                WMathHelper.wrapAngleTo180_float((-Math.toDegrees(atan2(diffY, diffXZ))).toFloat())
                        )
                        val rotationVector = RotationUtils.getVectorForRotation(rotation)
                        val vector = eyesPos.addVector(
                                rotationVector.xCoord * 4,
                                rotationVector.yCoord * 4,
                                rotationVector.zCoord * 4
                        )
                        val obj = mc.theWorld!!.rayTraceBlocks(eyesPos, vector, false, false, true)
                        if (!(obj!!.typeOfHit == IMovingObjectPosition.WMovingObjectType.BLOCK && obj.blockPos == neighbor)) {
                            zSearch += 0.1
                            continue
                        }
                        if (placeRotation == null || RotationUtils.getRotationDifference(rotation) < RotationUtils.getRotationDifference(
                                        placeRotation.rotation
                                )
                        ) placeRotation = PlaceRotation(PlaceInfo(neighbor, side.opposite, hitVec), rotation)
                        zSearch += 0.1
                    }
                    ySearch += 0.1
                }
                xSearch += 0.1
            }
        }
        if (placeRotation == null) return false
        if (rotationsValue.get() != "None") {
            var rotation: Rotation? = null
            when (rotationsValue.get().toLowerCase()) {
                "aac" -> {
                    if (!towerStatus) {
                        rotation = Rotation(mc.thePlayer!!.rotationYaw + (if (mc.thePlayer!!.movementInput.moveForward < 0) 0 else 180) + aacYawValue.get(), placeRotation.rotation.pitch)
                    }else{
                        rotation = placeRotation.rotation
                    }
                }
                "vanilla" -> {
                    rotation = placeRotation.rotation
                }
                "test1" -> {
                    var caluyaw = (java.lang.Math.round(placeRotation.rotation.yaw / 45) * 45).toFloat()
                    //Co丶Dynamic : Wo Shi Sha Bi
                    rotation = Rotation(caluyaw, placeRotation.rotation.pitch)
                }
                "test2" -> {
                    rotation = Rotation(((MovementUtils.direction * 180f / Math.PI).toFloat() + 135).toFloat(), placeRotation.rotation.pitch)
                }
            }
            if (rotation != null) {
                /*if(tolleyBridgeValue.get() > tolleyStayTick && (mc.thePlayer!!.onGround || lastTickOnGround ||
                    (!mc.theWorld!!.getCollisionBoxes(mc.thePlayer!!.entityBoundingBox.offset(
                            -mc.thePlayer!!.motionX,
                            0.98*(mc.thePlayer!!.motionY-0.08),
                            -mc.thePlayer!!.motionZ
                            )).isEmpty() && mc.thePlayer!!.motionY<=0)
                  ))
                    rotation = Rotation(
                        mc.thePlayer!!.rotationYaw + tolleyYawValue.get(),
                        placeRotation.rotation.pitch
                    )*/
                if (silentRotationValue.get()) {
                    val limitedRotation =
                            RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation, getSpeed())
                    RotationUtils.setTargetRotation(limitedRotation, keepLengthValue.get())
                } else {
                    mc.thePlayer!!.rotationYaw = rotation.yaw
                    mc.thePlayer!!.rotationPitch = rotation.pitch
                }
            }
            lockRotation = rotation
            //lastTickOnGround=mc.thePlayer!!.onGround
        }
        targetPlace = placeRotation.placeInfo
        return true
    }

    /**
     * @return hotbar blocks amount
     */
    val blocksAmount: Int
        get() {
            var amount = 0
            for (i in 36..44) {
                val itemStack = mc.thePlayer!!.inventoryContainer.getSlot(i).stack

                if (itemStack != null && classProvider.isItemBlock(itemStack.item))
                    amount += itemStack.stackSize
            }
            return amount
        }

    fun drawTip() {
        var width = 0
        if (blocksAmount >= 100 && blocksAmount < 1000) {
            width = 7
        }
        if (blocksAmount >= 10 && blocksAmount < 100) {
            width = 5
        }
        if (blocksAmount >= 0 && blocksAmount < 10) {
            width = 3
        }
        val blockSlot = InventoryUtils.findAutoBlockBlock()
        if (blockSlot == -1)
            return
        val itemStack = mc.thePlayer!!!!.inventoryContainer.getSlot(blockSlot).stack
        val res = classProvider.createScaledResolution(mc)
        GL11.glPushMatrix()
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        if (mc.theWorld!! != null) {
            RenderHelper.enableGUIStandardItemLighting()
        }
        GlStateManager.pushMatrix()
        GlStateManager.disableAlpha()
        GlStateManager.clear(256)
        mc.renderItem.zLevel = -150.0f
        mc.renderItem.renderItemAndEffectIntoGUI(itemStack!!, res.scaledWidth / 2 - 20, res.scaledHeight / 2 + 2)
        Fonts.font35.drawStringWithShadow(blocksAmount.toString() + " <- Blocks", (res.scaledWidth / 2), (res.scaledHeight / 2 + 7), Color.WHITE.rgb)
        mc.renderItem.zLevel = 0.0f
        GlStateManager.disableBlend()
        GlStateManager.scale(0.5f, 0.5f, 0.5f)
        GlStateManager.disableDepth()
        GlStateManager.disableLighting()
        GlStateManager.enableDepth()
        GlStateManager.scale(2.0f, 2.0f, 2.0f)
        GlStateManager.enableAlpha()
        GlStateManager.popMatrix()
        GL11.glPopMatrix()
    }

    fun getSpeed():Float{
        return (Math.random() * (maxRotationSpeedValue.get() - minRotationSpeedValue.get()) + minRotationSpeedValue.get()).toFloat()
    }

    fun roundYaw(rYaw: Float):Float{
        var lrYaw = rYaw
        while(lrYaw>360) {
            lrYaw -= 360
        }
        while(lrYaw<-360) {
            lrYaw += 360
        }
        return lrYaw
    }

    @EventTarget
    fun onJump(event: JumpEvent) {
        if (towerStatus)
            event.cancelEvent();
    }

    val canSprint: Boolean
        get() = when(sprintValue.get().toLowerCase()) {
            "always","dynamic" -> true
            "onground" -> mc.thePlayer!!.onGround
            "offground" -> !mc.thePlayer!!.onGround
            else -> false
        }

    override val tag: String
        get() = if(towerStatus){ "Tower" }else{ "Normal" }

    private val barrier = classProvider.createItemStack(functions.getItemById(166)!!, 0, 0)
}