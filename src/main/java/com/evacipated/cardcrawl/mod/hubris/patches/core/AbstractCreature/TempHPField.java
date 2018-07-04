package com.evacipated.cardcrawl.mod.hubris.patches.core.AbstractCreature;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

@SpirePatch(
        cls="com.megacrit.cardcrawl.core.AbstractCreature",
        method=SpirePatch.CLASS
)
public class TempHPField
{
    public static SpireField<Integer> tempHp = new SpireField<>(0);
}
