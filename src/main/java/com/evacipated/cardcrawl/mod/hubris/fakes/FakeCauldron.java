package com.evacipated.cardcrawl.mod.hubris.fakes;

import basemod.patches.whatmod.WhatMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Cauldron;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.List;

public class FakeCauldron extends Cauldron
{
    private List<RewardItem> toAdd = null;

    @Override
    protected void initializeTips()
    {
        boolean save = WhatMod.enabled;
        WhatMod.enabled = false;
        super.initializeTips();
        WhatMod.enabled = save;
    }

    @Override
    public void onEquip()
    {
        toAdd = new ArrayList<>();
        for (int i=0; i<5; ++i) {
            toAdd.add(new RewardItem(PotionHelper.getRandomPotion()));
        }
    }

    @Override
    public void update()
    {
        super.update();

        // Replace this with a real Cauldron in the player's relics
        int index = AbstractDungeon.player.relics.indexOf(this);
        RelicLibrary.getRelic(Cauldron.ID).makeCopy().instantObtain(AbstractDungeon.player, index, false);

        // Add the potions to the rewards screen
        if (toAdd != null) {
            AbstractDungeon.combatRewardScreen.rewards.addAll(0, toAdd);
            toAdd = null;
        }
        AbstractDungeon.combatRewardScreen.positionRewards();
    }
}
