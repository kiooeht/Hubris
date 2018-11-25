package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.cards.curses.SpiceAddiction;
import com.evacipated.cardcrawl.mod.hubris.relics.DisguiseKit;
import com.evacipated.cardcrawl.mod.hubris.relics.Spice;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

public class SpicePatch
{
    @SpirePatch(
            clz=AbstractDungeon.class,
            method="returnEndRandomRelicKey"
    )
    public static class ReturnEndRandomRelicKey
    {
        public static String Postfix(String __result, AbstractRelic.RelicTier tier)
        {
            if (!Spice.ID.equals(__result) && Spice.replaceWithSpice()) {
                return Spice.ID;
            } else {
                return __result;
            }
        }
    }

    @SpirePatch(
            clz=AbstractDungeon.class,
            method="returnRandomRelicKey"
    )
    public static class ReturnRandomRelicKey
    {
        public static String Postfix(String __result, AbstractRelic.RelicTier tier)
        {
            if (!Spice.ID.equals(__result) && Spice.replaceWithSpice()) {
                return Spice.ID;
            } else {
                return __result;
            }
        }
    }

    public static class SpawnRelicAndObtain
    {
        @SpirePatch(
                clz=AbstractRoom.class,
                method="spawnRelicAndObtain"
        )
        public static class Nested
        {
            public static void Raw(CtBehavior ctMethodToPatch) throws CannotCompileException
            {
                String src = "if (com.evacipated.cardcrawl.mod.hubris.patches.SpicePatch.SpawnRelicAndObtain.Stop(relic)) { return; }";
                ctMethodToPatch.insertBefore(src);
            }
        }

        public static boolean Stop(AbstractRelic relic)
        {
            if (Spice.ID.equals(relic.relicId) && AbstractDungeon.player.hasRelic(Spice.ID)) {
                Spice spice = (Spice) AbstractDungeon.player.getRelic(Spice.ID);
                spice.increment();
                spice.flash();
                return true;
            }
            return false;
        }
    }

    @SpirePatch(
            clz=AbstractDungeon.class,
            method="getRewardCards"
    )
    public static class AddCardReward
    {
        public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> __result)
        {
            if (AbstractDungeon.player.hasRelic(Spice.ID)) {
                for (int i=0; i<__result.size(); ++i) {
                    if (Spice.replaceCardWithSpice()) {
                        __result.set(i, new SpiceAddiction());
                    }
                }
            }

            return __result;
        }
    }
}
