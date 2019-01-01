package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpirePatch(
        clz=CardRewardScreen.class,
        method="discoveryOpen",
        paramtypez={}
)
public class DiscoveryColorPatch
{
    public static AbstractCard.CardColor lookingForColor = null;
    public static int lookingForCount = 3;
    public static String lookingForProhibit = null;
    public static boolean lookingForUpgraded = false;

    @SpireInsertPatch(
            locator=Locator.class,
            localvars={"derp"}
    )
    public static void Insert(CardRewardScreen __instance, ArrayList<AbstractCard> derp)
    {
        if (lookingForColor != null) {
            derp.clear();
            while (derp.size() != lookingForCount) {
                boolean dupe = false;
                System.out.println(lookingForColor);
                AbstractCard tmp = getColorSpecificCard(lookingForProhibit, lookingForColor, AbstractDungeon.cardRandomRng);
                if (tmp.hasTag(AbstractCard.CardTags.HEALING)) {
                    dupe = true;
                }
                for (AbstractCard c : derp) {
                    if (c.cardID.equals(tmp.cardID)) {
                        dupe = true;
                        break;
                    }
                }
                if (!dupe) {
                    AbstractCard c = tmp.makeStatEquivalentCopy();
                    if (lookingForUpgraded) {
                        c.upgrade();
                    }
                    derp.add(c);
                }
            }
            __instance.rewardGroup = derp;
            lookingForColor = null;
            lookingForCount = 3;
            lookingForProhibit = null;
            lookingForUpgraded = false;
        }
    }

    private static AbstractCard getColorSpecificCard(String prohibit, AbstractCard.CardColor color, Random rng)
    {
        List<String> tmp = new ArrayList<>();
        for (Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
            if (c.getValue().color == color && !c.getKey().equals(prohibit)) {
                tmp.add(c.getKey());
            }
        }
        return CardLibrary.cards.get(tmp.get(rng.random(0, tmp.size() - 1)));
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(CardRewardScreen.class, "rewardGroup");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
