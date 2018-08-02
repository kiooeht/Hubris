package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.BottledHeart;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Necronomicon;

public class BottledHeartPatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.relics.Necronomicon",
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

    @SpirePatch(
            cls="com.megacrit.cardcrawl.core.AbstractCreature",
            method="increaseMaxHp"
    )
    public static class IncreaseMaxHealth
    {
        @SpireInsertPatch(
                rloc=5
        )
        public static void Insert(AbstractCreature __instance, int amount, boolean showEffect)
        {
            if (__instance == AbstractDungeon.player && AbstractDungeon.player.hasRelic(BottledHeart.ID)) {
                AbstractDungeon.player.maxHealth -= amount;
                AbstractRelic relic = AbstractDungeon.player.getRelic(BottledHeart.ID);
                relic.setCounter(relic.counter + amount);
            }
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.core.AbstractCreature",
            method="decreaseMaxHealth"
    )
    public static class DecreaseMaxHealth
    {
        @SpireInsertPatch(
                rloc=5
        )
        public static void Insert(AbstractCreature __instance, int amount)
        {
            if (__instance == AbstractDungeon.player && AbstractDungeon.player.hasRelic(BottledHeart.ID)) {
                AbstractRelic relic = AbstractDungeon.player.getRelic(BottledHeart.ID);
                if (relic.counter >= 0) {
                    AbstractDungeon.player.maxHealth += amount;
                    relic.setCounter(relic.counter - amount);
                }
            }
        }
    }
}
