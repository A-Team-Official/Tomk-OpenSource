package net.ccbluex.liquidbounce.ui.font

abstract class AbstractCachedFont(var lastUsage: Long) {
    abstract fun finalize()
}