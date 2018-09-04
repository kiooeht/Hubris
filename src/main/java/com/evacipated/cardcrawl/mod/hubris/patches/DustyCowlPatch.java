package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.DustyCowl;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;

import java.util.ArrayList;

public class DustyCowlPatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.screens.SingleRelicViewPopup",
            method="open",
            paramtypes={
                    "com.megacrit.cardcrawl.relics.AbstractRelic"
            }
    )
    public static class SingleRelicViewPopupPatch1
    {
        public static void Prefix(SingleRelicViewPopup __instance, AbstractRelic relic)
        {
            if (relic instanceof DustyCowl) {
                ((DustyCowl)relic).rerollFlavorText();
            }
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.screens.SingleRelicViewPopup",
            method="open",
            paramtypes={
                    "com.megacrit.cardcrawl.relics.AbstractRelic",
                    "java.util.ArrayList"
            }
    )
    public static class SingleRelicViewPopupPatch2
    {
        public static void Prefix(SingleRelicViewPopup __instance, AbstractRelic relic, ArrayList<AbstractRelic> group)
        {
            if (relic instanceof DustyCowl) {
                ((DustyCowl)relic).rerollFlavorText();
            }
        }
    }
}
