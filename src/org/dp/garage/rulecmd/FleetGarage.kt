package org.dp.garage.rulecmd

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CampaignFleetAPI
import com.fs.starfarer.api.campaign.InteractionDialogAPI
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.campaign.rules.MemoryAPI
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin
import com.fs.starfarer.api.util.Misc
import org.dp.garage.DriftToMarket

class FleetGarage : BaseCommandPlugin() {
    companion object{
        const val SUPPLY_USAGE_MULTIPLIER = 0.2f
        const val STAT_MOD_ID = "GFS_STAT_MOD"
        const val IS_PARKED_MEM_KEY = "\$GFS_IS_FLEET_PARKED"
        const val MULT_DESC = "Fleet is parked in local garage"
        fun isParked(): Boolean = Global.getSector().memoryWithoutUpdate.getBoolean(IS_PARKED_MEM_KEY)
        fun setParked(value: Boolean) {
            Global.getSector().memoryWithoutUpdate[IS_PARKED_MEM_KEY] = value
        }
        fun getMarket(memoryMap: MutableMap<String, MemoryAPI>?): MarketAPI? = Misc.getPlayerMarkets(false).firstOrNull { it.id == (memoryMap?.get("market")?.get("\$id") as? String) }
        val playerFleet: CampaignFleetAPI
            get() = Global.getSector().playerFleet
    }
    private fun parkFleet(){
        playerFleet.forceSync()
        playerFleet.fleetData.membersInPriorityOrder.forEach {
            it.stats.suppliesPerMonth.modifyMult(STAT_MOD_ID, SUPPLY_USAGE_MULTIPLIER)
        }

        playerFleet.stats.detectedRangeMod.modifyMult(STAT_MOD_ID, 0f, MULT_DESC)
        playerFleet.stats.fleetwideMaxBurnMod.modifyFlat(STAT_MOD_ID, -20f, MULT_DESC)
        playerFleet.forceSync()
        setParked(true)

    }
    private fun launchFleet(){
        playerFleet.forceSync()
        playerFleet.membersWithFightersCopy.forEach {
            it.stats.suppliesPerMonth.unmodify(STAT_MOD_ID)
        }
        playerFleet.stats.detectedRangeMod.unmodify(STAT_MOD_ID)
        playerFleet.stats.fleetwideMaxBurnMod.unmodify(STAT_MOD_ID)
        playerFleet.forceSync()
        setParked(false)
    }

    override fun execute(
        ruleId: String?,
        dialog: InteractionDialogAPI?,
        params: MutableList<Misc.Token>?,
        memoryMap: MutableMap<String, MemoryAPI>?
    ): Boolean {
        if(isParked()){
            launchFleet()
        }else{
            parkFleet()
            getMarket(memoryMap)?.let { m ->
                Global.getSector().addTransientScript(DriftToMarket(m))
            }
        }
        return true
    }
}