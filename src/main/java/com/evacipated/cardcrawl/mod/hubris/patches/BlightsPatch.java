package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.blights.Reinasbane;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;

@SpirePatch(
        clz=BlightHelper.class,
        method="getBlight"
)
public class BlightsPatch
{
    public static SpireReturn<AbstractBlight> Prefix(String id)
    {
        if (Reinasbane.ID.equals(id)) {
            return SpireReturn.Return(new Reinasbane());
        }
        return SpireReturn.Continue();
    }
}
