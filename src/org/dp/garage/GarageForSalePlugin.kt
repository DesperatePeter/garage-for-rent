package org.dp.garage

import com.fs.starfarer.api.BaseModPlugin
import com.fs.starfarer.api.Global

/**
 * A Kotlin version of ExampleModPlugin.java.
 * Purely for comparison and convenience; this will not be used by the game
 * unless mod_info.json is edited to use it
 * (or it is renamed to "ExampleModPlugin" in order to replace the Java version).
 */
class GarageForSalePlugin : BaseModPlugin() {

    override fun onGameLoad(newGame: Boolean) {
        super.onGameLoad(newGame)
        Global.getSector().registerPlugin(GarageCampaignPlugin())
    }
}