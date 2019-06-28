package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
public class FixSingleCardViewCurseBack
{
    @SpireInsertPatch(
            rloc=1,
            localvars={"card", "tmpImg"}
    )
    public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card, @ByRef TextureAtlas.AtlasRegion[] tmpImg)
    {
        if (card.color == AbstractCard.CardColor.CURSE) {
            if (card.type == AbstractCard.CardType.ATTACK) {
                tmpImg[0] = ImageMaster.CARD_SKILL_BG_BLACK_L;
            } else if (card.type == AbstractCard.CardType.POWER) {
                tmpImg[0] = ImageMaster.CARD_SKILL_BG_BLACK_L;
            }
        }
    }
}
