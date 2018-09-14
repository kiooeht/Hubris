package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.cards.curses.Wrath;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

@SpirePatch(
        clz=SingleCardViewPopup.class,
        method="renderCardBack"
)
public class FixSingleCardViewWrathBack
{
    @SpireInsertPatch(
            rloc=1,
            localvars={"card", "tmpImg"}
    )
    public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card, @ByRef Texture[] tmpImg)
    {
        if (card instanceof Wrath) {
            tmpImg[0] = ImageMaster.CARD_SKILL_BG_BLACK_L;
        }
    }
}
