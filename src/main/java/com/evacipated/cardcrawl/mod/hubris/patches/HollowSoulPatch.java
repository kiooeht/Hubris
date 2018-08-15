package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.HollowSoul;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

@SpirePatch(
        cls="com.megacrit.cardcrawl.characters.AbstractPlayer",
        method="damage",
        paramtypes={"com.megacrit.cardcrawl.cards.DamageInfo"}
)
public class HollowSoulPatch
{
    @SpireInsertPatch
    public static SpireReturn Insert(AbstractPlayer __instance, DamageInfo info)
    {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !__instance.hasRelic(MarkOfTheBloom.ID)) {
            if (__instance.hasRelic(HollowSoul.ID)) {
                if (__instance.getRelic(HollowSoul.ID).counter == -1) {
                    __instance.currentHealth = 0;
                    __instance.getRelic(HollowSoul.ID).onTrigger();
                    return SpireReturn.Return(null);
                } else {
                    // Reset Max HP to normal to stop losing out on Stuffed points
                    HollowSoul soul = (HollowSoul) __instance.getRelic(HollowSoul.ID);
                    soul.restore();
                    soul.setCounter(-2);
                }
            }
        }
        return SpireReturn.Continue();
    }

    public static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher("com.megacrit.cardcrawl.characters.AbstractPlayer", "isDead");

            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
