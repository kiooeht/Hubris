package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.R64BitClover;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.daily.mods.Vintage;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

@SpirePatch(
        clz=MonsterRoom.class,
        method="dropReward"
)
public class R64BitCloverPatch
{
    public static void Postfix(MonsterRoom __instance)
    {
        if (!ModHelper.isModEnabled(Vintage.ID)) {
            AbstractRelic relic = AbstractDungeon.player.getRelic(R64BitClover.ID);
            relic.onTrigger();
        }
    }
}
