package org.dp.garage

import com.fs.starfarer.api.BaseModPlugin
import com.fs.starfarer.api.Global

class GarageForSalePlugin : BaseModPlugin() {

    override fun onGameLoad(newGame: Boolean) {
        super.onGameLoad(newGame)
        Global.getSector().registerPlugin(GarageCampaignPlugin())
    }
}