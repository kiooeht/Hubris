package com.evacipated.cardcrawl.mod.hubris.patches;

import basemod.AutoComplete;
import com.evacipated.cardcrawl.mod.hubris.relics.Test447;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

import java.lang.reflect.Field;
import java.util.List;

@SpirePatch(
        cls="basemod.AutoComplete",
        method="createRelicSuggestions",
        optional=true
)
public class BacktickPatch
{
    private static Field suggestions;

    static {
        try {
            suggestions = AutoComplete.class.getDeclaredField("suggestions");
            suggestions.setAccessible(true);
        } catch (NoSuchFieldException ignored) {
        }
    }

    public static void Postfix()
    {
        try {
            ((List)suggestions.get(null)).remove(Test447.ID);
        } catch (Exception ignored) {
        }
    }
}
