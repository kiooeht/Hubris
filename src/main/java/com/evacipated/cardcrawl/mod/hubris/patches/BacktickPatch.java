package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.Test447;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

import java.util.ArrayList;

@SpirePatch(
        cls="basemod.devcommands.ConsoleCommand",
        method="getRelicOptions",
        optional=true
)
public class BacktickPatch
{
    public static ArrayList<String> Postfix(ArrayList<String> __result)
    {
        __result.remove(Test447.ID);
        return __result;
    }
}
