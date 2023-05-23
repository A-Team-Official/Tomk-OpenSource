package net.ccbluex.liquidbounce.ui.font

import net.ccbluex.liquidbounce.ui.font.AbstractCachedFont

class CachedVectorFont(val list: Int, val width: Int) : AbstractCachedFont(System.currentTimeMillis()) {
    override fun finalize() {
       // GL11.glDeleteLists(list, 1)
    }
}
