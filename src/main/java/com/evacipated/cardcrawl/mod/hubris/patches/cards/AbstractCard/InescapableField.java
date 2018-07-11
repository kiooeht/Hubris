package com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method=SpirePatch.CLASS
)
public class InescapableField
{
    public static SpireField<Boolean> inescapable = new SpireField<>(false);
}
