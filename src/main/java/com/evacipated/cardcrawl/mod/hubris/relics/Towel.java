package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.hubris.rewards.LinkedRewardItem;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.List;

public class Towel extends HubrisRelic
{
    public static final String ID = "hubris:Towel";

    public Towel()
    {
        super(ID, "towel.png", RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onChestOpenAfter(boolean bossChest)
    {
        if (!bossChest) {
            onTrigger();
        }
    }

    @Override
    public void onTrigger()
    {
        List<RewardItem> relicRewards = new ArrayList<>();
        for (RewardItem reward : AbstractDungeon.getCurrRoom().rewards) {
            if (reward.type == RewardItem.RewardType.RELIC && reward.relicLink == null) {
                relicRewards.add(reward);
            }
        }

        boolean doFlash = false;
        for (RewardItem reward : relicRewards) {
            RelicTier tier = reward.relic.tier;
            if (tier != RelicTier.SPECIAL && tier != RelicTier.DEPRECATED && tier != RelicTier.STARTER) {
                AbstractRelic newRelic = AbstractDungeon.returnRandomRelic(tier);
                if (newRelic != null) {
                    doFlash = true;
                    RewardItem replaceReward = new LinkedRewardItem(reward);
                    RewardItem newReward = new LinkedRewardItem(replaceReward, newRelic);
                    int indexOf = AbstractDungeon.getCurrRoom().rewards.indexOf(reward);
                    // Insert after existing reward
                    AbstractDungeon.getCurrRoom().rewards.add(indexOf + 1, newReward);
                    // Replace original
                    AbstractDungeon.getCurrRoom().rewards.set(indexOf, replaceReward);
                }
            }
        }

        if (doFlash) {
            flash();
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player,  this));

        }
    }

    // Elite relic rewards are done in TowelPatch

    @Override
    public AbstractRelic makeCopy()
    {
        return new Towel();
    }
}
