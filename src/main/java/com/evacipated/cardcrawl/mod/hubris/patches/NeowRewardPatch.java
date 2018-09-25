package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.BlackHole;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(
        clz=AbstractDungeon.class,
        method="returnRandomRelicKey"
)
public class NeowRewardPatch
{
    private static List<String> startingRelicBlacklist = new ArrayList<>();

    static
    {
        startingRelicBlacklist.add(BlackHole.ID);
    }

    public static String Postfix(String __result, AbstractRelic.RelicTier tier)
    {
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room != null) {
            if (room instanceof NeowRoom) {
                if (startingRelicBlacklist.contains(__result)) {
                    return AbstractDungeon.returnRandomRelicKey(tier);
                }
            }
        }

        return __result;
    }
}
