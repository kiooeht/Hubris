package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.Towel;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

@SpirePatch(
        clz=AbstractRoom.class,
        method="update"
)
public class TowelPatch
{
    @SpireInsertPatch(
            locator=Locator.class
    )
    public static void Insert(AbstractRoom __instance)
    {
        AbstractRelic towel = AbstractDungeon.player.getRelic(Towel.ID);
        if (towel != null) {
            towel.onTrigger();
            // Re-setup the rewards screen to our changed rewards
            AbstractDungeon.combatRewardScreen.setupItemReward();
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            // Insert here to be after the game is saved
            // Avoids weird save/load issues
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "loading_post_combat");
            int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{found[found.length - 1]};
        }
    }
}
