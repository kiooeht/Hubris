package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.cards.colorless.Herb;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class BundleOfHerbs extends HubrisRelic
{
    public static final String ID = "hubris:BundleOfHerbs";

    public BundleOfHerbs()
    {
        super(ID, "bundleOfHerbs.png", RelicTier.RARE, LandingSound.FLAT);

        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(Herb.NAME, Herb.NAME + " is a #b" + Herb.COST + " cost Skill card that gives #yRegen and is #yFleeting."));
        tips.add(new PowerTip("Fleeting", GameDictionary.keywords.get("fleeting")));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip()
    {
        gainHerbs();
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        if (room instanceof RestRoom) {
            flash();
            gainHerbs();
        }
    }

    private void gainHerbs()
    {
        AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new Herb(), Settings.WIDTH * 0.25f, Settings.HEIGHT / 2.0f, true));
        AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new Herb(), Settings.WIDTH * 0.75f, Settings.HEIGHT / 2.0f, true));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BundleOfHerbs();
    }
}
