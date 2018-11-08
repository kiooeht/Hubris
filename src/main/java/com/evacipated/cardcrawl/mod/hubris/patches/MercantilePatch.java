package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.cards.merchant.Defend_Merchant;
import com.evacipated.cardcrawl.mod.hubris.cards.merchant.MerchantsHand;
import com.evacipated.cardcrawl.mod.hubris.cards.merchant.Strike_Merchant;
import com.evacipated.cardcrawl.mod.hubris.relics.NiceRug;
import com.evacipated.cardcrawl.mod.hubris.trials.MercantileTrial;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.blue.Defend_Blue;
import com.megacrit.cardcrawl.cards.blue.Strike_Blue;
import com.megacrit.cardcrawl.cards.green.Defend_Green;
import com.megacrit.cardcrawl.cards.green.Strike_Green;
import com.megacrit.cardcrawl.cards.red.Defend_Red;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.mods.ColorlessCards;
import com.megacrit.cardcrawl.daily.mods.Diverse;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.SmilingMask;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import com.megacrit.cardcrawl.trials.CustomTrial;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Arrays;

@SpirePatch(
        clz=CustomModeScreen.class,
        method="addNonDailyMods"
)
public class MercantilePatch
{
    @SpireInsertPatch(
            rloc=1,
            localvars={"modId"}
    )
    public static void Insert(CustomModeScreen __instance, CustomTrial trial, ArrayList<String> modIds, String modId)
    {
        if ("hubris:Mercantile".equals(modId)) {
            // Set starting hp
            trial.setMaxHpOverride(75);
            // Set starting gold to 0
            MercantileTrial.MercantileOverrides.startingGold.set(trial, 0);
            // Set orb slots
            MercantileTrial.MercantileOverrides.startingOrbSlots.set(trial, 2);
            // Replace starter relic with Smiling Mask and Nice Rug
            trial.addStarterRelics(Arrays.asList(SmilingMask.ID, NiceRug.ID));
            trial.setShouldKeepStarterRelic(false);
            // Add Hand of Greed to starting deck
            trial.setStarterCards(Arrays.asList(
                    Strike_Red.ID,
                    Strike_Green.ID,
                    Strike_Blue.ID,
                    Strike_Merchant.ID,
                    Defend_Red.ID,
                    Defend_Green.ID,
                    Defend_Blue.ID,
                    Defend_Merchant.ID,
                    MerchantsHand.ID,
                    MerchantsHand.ID
            ));
            // Set Diverse and Colorless Cards
            trial.addDailyMods(Arrays.asList(Diverse.ID, ColorlessCards.ID));
        }
    }

    @SpirePatch(
            clz=AbstractPlayer.class,
            method="initializeStarterDeck"
    )
    public static class ClearBaseStarterDeck
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"addBaseCards"}
        )
        public static void Insert(AbstractPlayer __instance, @ByRef boolean[] addBaseCards)
        {
            if (Settings.isTrial && CardCrawlGame.trial != null && !CardCrawlGame.trial.keepsStarterCards()) {
                addBaseCards[0] = false;
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ModHelper.class, "isModEnabled");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
