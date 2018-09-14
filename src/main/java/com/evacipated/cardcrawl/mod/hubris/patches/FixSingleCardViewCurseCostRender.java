package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

@SpirePatch(
        clz=SingleCardViewPopup.class,
        method="renderCost"
)
public class FixSingleCardViewCurseCostRender
{
    @SpireInsertPatch(
            locator=Locator.class,
            localvars={"card"}
    )
    public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card)
    {
        if (card.cost > -2) {
            if (card.color == AbstractCard.CardColor.CURSE) {
                sb.draw(ImageMaster.CARD_GRAY_ORB_L,
                        Settings.WIDTH / 2.0F - 82.0F - 270.0F * Settings.scale, Settings.HEIGHT / 2.0F - 82.0F + 380.0F * Settings.scale,
                        82.0F, 82.0F,
                        164.0F, 164.0F,
                        Settings.scale, Settings.scale,
                        0.0F,
                        0, 0,
                        164, 164,
                        false, false);
            }
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "color");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
