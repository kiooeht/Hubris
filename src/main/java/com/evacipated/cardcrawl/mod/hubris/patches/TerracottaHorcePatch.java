package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.TerracottaHorce;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.DeathScreen;

@SpirePatch(
        clz=DeathScreen.class,
        method=SpirePatch.CONSTRUCTOR
)
public class TerracottaHorcePatch
{
    public static void Prefix(DeathScreen __instance, MonsterGroup m)
    {
        // Reset ascension level so it doesn't unlock extra ascension levels
        AbstractRelic relic = AbstractDungeon.player.getRelic(TerracottaHorce.ID);
        if (relic != null) {
            relic.onUnequip();
        }
    }
}
