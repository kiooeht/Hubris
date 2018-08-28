package com.evacipated.cardcrawl.mod.hubris.relics.abstracts;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public abstract class HubrisRelic extends AbstractRelic
{
    public HubrisRelic(String setId, String imgName, RelicTier tier, LandingSound sfx)
    {
        super(setId, "", tier, sfx);

        img = ImageMaster.loadImage(HubrisMod.assetPath("images/relics/" + imgName));
        largeImg = ImageMaster.loadImage(HubrisMod.assetPath("images/largeRelics/" + imgName));
        outlineImg = ImageMaster.loadImage(HubrisMod.assetPath("images/relics/outline/" + imgName));
    }
}
