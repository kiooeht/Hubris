package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.DisguiseKit;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class DisguiseKitPatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.rewards.RewardItem",
            method="claimReward"
    )
    public static class ClaimReward
    {
        public static void Postfix(RewardItem __instance)
        {
            if (__instance.type == RewardItem.RewardType.CARD && AbstractDungeon.player.hasRelic(DisguiseKit.ID)) {
                AbstractDungeon.player.getRelic(DisguiseKit.ID).flash();
            }
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon",
            method="getRewardCards"
    )
    public static class AddCardReward
    {
        public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> __result)
        {
            if (AbstractDungeon.player.hasRelic(DisguiseKit.ID)) {
                DisguiseKit relic = (DisguiseKit) AbstractDungeon.player.getRelic(DisguiseKit.ID);

                AbstractCard.CardRarity rarity = AbstractDungeon.rollRarity();
                AbstractCard card = relic.getRewardCard(rarity);

                if (card != null) {
                    __result.add(card.makeCopy());
                }
            }

            return __result;
        }
    }
}
