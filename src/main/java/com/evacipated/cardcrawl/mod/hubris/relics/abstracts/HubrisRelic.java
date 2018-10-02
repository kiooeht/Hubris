package com.evacipated.cardcrawl.mod.hubris.relics.abstracts;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.stslib.relics.SuperRareRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public abstract class HubrisRelic extends AbstractRelic
{
    public HubrisRelic(String setId, String imgName, RelicTier tier, LandingSound sfx)
    {
        super(setId, "", tier, sfx);

        if (this instanceof SuperRareRelic) {
            this.tier = tier = RelicTier.RARE;
        }

        if (imgName.startsWith("test")) {
            img = ImageMaster.loadImage("images/relics/" + imgName);
            largeImg = ImageMaster.loadImage("images/largeRelics/" + imgName);
            outlineImg = ImageMaster.loadImage("images/relics/outline/" + imgName);
        }
        if (img == null || outlineImg == null) {
            img = ImageMaster.loadImage(HubrisMod.assetPath("images/relics/" + imgName));
            largeImg = ImageMaster.loadImage(HubrisMod.assetPath("images/largeRelics/" + imgName));
            outlineImg = ImageMaster.loadImage(HubrisMod.assetPath("images/relics/outline/" + imgName));
        }
    }
}
