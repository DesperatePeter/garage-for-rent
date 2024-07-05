package org.dp.garage

import com.fs.starfarer.api.EveryFrameScript
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.util.IntervalUtil
import org.dp.garage.rulecmd.FleetGarage
import org.lazywizard.lazylib.ext.minus

class FleetIsParkedScript(private val market: MarketAPI): EveryFrameScript {

    private val timer = IntervalUtil(1f, 1f)

    override fun isDone(): Boolean = !FleetGarage.isParked()

    override fun runWhilePaused(): Boolean = false

    override fun advance(amount: Float) {
        timer.advance(Global.getSector().clock.convertToDays(amount))
        if(timer.intervalElapsed()){
            Global.getSector().playerFleet.cargo.credits.subtract(1f)
            Global.getSector().campaignUI?.addMessage("Payed a parking fee of 1 credit!")
        }
        val pf = Global.getSector().playerFleet ?: return
        if(pf.containingLocation != market.containingLocation) return
        val marketLoc = market.planetEntity
        val dx = pf.location - marketLoc.location
        if(dx.length() > market.planetEntity.radius * 0.1f){
            pf.setLocation(marketLoc.location.x, marketLoc.location.y)
        }
        pf.setVelocity(marketLoc.velocity.x, marketLoc.velocity.y)
    }
}