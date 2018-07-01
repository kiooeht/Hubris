package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.cards.curses.Hubris;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.TipHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;

@SpirePatch(
        cls="com.megacrit.cardcrawl.helpers.TipHelper",
        method="renderKeywords"
)
public class HubrisRemoveKeywords
{
    private static Field cardField = null;

    public static void Prefix(float x, float y, SpriteBatch sb , ArrayList<String> keywords)
    {
        try {
            if (cardField == null) {
                cardField = TipHelper.class.getDeclaredField("card");
                cardField.setAccessible(true);
            }
            AbstractCard card = (AbstractCard) cardField.get(null);

            if (card.cardID.equals(Hubris.ID)) {
                keywords.remove("strength");
                keywords.remove("dexterity");
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
