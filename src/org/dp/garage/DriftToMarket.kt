package org.dp.garage

import com.fs.starfarer.api.EveryFrameScript
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.LocationAPI
import com.fs.starfarer.api.campaign.econ.MarketAPI
import org.dp.garage.rulecmd.FleetGarage
import org.lazywizard.lazylib.ext.minus

class DriftToMarket(private val market: MarketAPI): EveryFrameScript {

    override fun isDone(): Boolean = !FleetGarage.isParked()

    override fun runWhilePaused(): Boolean = false

    override fun advance(amount: Float) {
        val pf = Global.getSector().playerFleet ?: return
        if(pf.containingLocation != market.containingLocation) return
        val marketLoc = market.planetEntity
        val dx = pf.location - marketLoc.location
        if(dx.length() > market.planetEntity.radius * 0.1f){
            pf.location.set(marketLoc.location.x, marketLoc.location.y)
        }
        pf.velocity.set(marketLoc.velocity.x, marketLoc.velocity.y)


    }
}