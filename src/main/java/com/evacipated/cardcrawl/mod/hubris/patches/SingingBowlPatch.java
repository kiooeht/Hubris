package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.SingingBowl;

import java.lang.reflect.Field;
import java.util.Map;

@SpirePatch(
        cls="com.megacrit.cardcrawl.localization.LocalizedStrings",
        method=SpirePatch.CONSTRUCTOR
)
public class SingingBowlPatch
{
    public static void Postfix(LocalizedStrings __instance)
    {
        if (!CardCrawlGame.playerName.equals("Rhapsody")) {
            return;
        }

        try {
            Field f = LocalizedStrings.class.getDeclaredField("relics");
            f.setAccessible(true);

            Map<String, RelicStrings> relics = (Map<String, RelicStrings>) f.get(__instance);

            RelicStrings singingBowlStrings = relics.get(SingingBowl.ID);
            singingBowlStrings.NAME = "SINGING BRRRBL";
            singingBowlStrings.FLAVOR = "Brughwrguhugr? Dburr bghurbur.";
        } catch (IllegalAccessException | NoSuchFieldException ignore) {
        }
    }
}
