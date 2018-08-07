package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.cards.curses.*;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Pride;

import java.util.Arrays;
import java.util.List;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method="renderEnergy"
)
public class RenderCurseEnergyCost
{
    private static List<String> curseIDs = Arrays.asList(
            Hubris.ID,
            Wrath.ID,
            Sloth.ID,
            Lust.ID,
            Envy.ID
    );
    private static String save_ID;

    public static void Prefix(AbstractCard __instance, SpriteBatch sb)
    {
        save_ID = __instance.cardID;
        if (curseIDs.contains(__instance.cardID)) {
            __instance.cardID = Pride.ID;
        }
    }

    public static void Postfix(AbstractCard __instance, SpriteBatch sb)
    {
        __instance.cardID = save_ID;
    }
}
