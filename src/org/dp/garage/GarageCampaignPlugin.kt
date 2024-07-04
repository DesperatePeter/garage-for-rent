package org.dp.garage

import com.fs.starfarer.api.PluginPick
import com.fs.starfarer.api.campaign.BaseCampaignPlugin
import com.fs.starfarer.api.campaign.CampaignFleetAPI
import com.fs.starfarer.api.campaign.CampaignPlugin
import com.fs.starfarer.api.campaign.InteractionDialogPlugin
import com.fs.starfarer.api.campaign.SectorEntityToken
import org.dp.garage.rulecmd.FleetGarage

class GarageCampaignPlugin: BaseCampaignPlugin() {
    override fun pickInteractionDialogPlugin(interactionTarget: SectorEntityToken?): PluginPick<InteractionDialogPlugin>? {
        if(!FleetGarage.isParked()) return null
        if(interactionTarget !is CampaignFleetAPI) return null
        return PluginPick<InteractionDialogPlugin>(ParkedFleetInteractionDialog(), CampaignPlugin.PickPriority.MOD_SET)
    }

    override fun getId(): String {
        return "GFS_FLEET_PARKED"
    }

    override fun isTransient(): Boolean = true
}