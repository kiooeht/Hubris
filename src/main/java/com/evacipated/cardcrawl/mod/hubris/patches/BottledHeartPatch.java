package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.BottledHeart;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Necronomicon;

public class BottledHeartPatch
{
    @SpirePatch(
            clz=Necronomicon.class,
            method="onEquip"
    )
    public static class NecronomiconSynergy
    {
        public static void Prefix(Necronomicon __instance)
        {
            if (AbstractDungeon.player.hasRelic(BottledHeart.ID)) {
                AbstractRelic relic = AbstractDungeon.player.getRelic(BottledHeart.ID);
                relic.setCounter(relic.counter + 10);
                relic.flash();
            }
        }
    }
}
