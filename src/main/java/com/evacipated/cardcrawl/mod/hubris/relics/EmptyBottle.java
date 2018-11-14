package com.evacipated.cardcrawl.mod.hubris.relics;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.hubris.actions.utility.SetPotionSlotAction;
import com.evacipated.cardcrawl.mod.hubris.patches.potions.AbstractPotion.PotionUseCountField;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnUsePotionRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.List;

public class EmptyBottle extends HubrisRelic implements BetterOnUsePotionRelic, CustomSavable<List<Integer>>
{
    public static final String ID = "hubris:EmptyBottle";
    public static final int POTION_USES = 2;

    private AbstractPotion potion;

    public EmptyBottle()
    {
        super(ID, "emptyBottle.png", RelicTier.SHOP, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public List<Integer> onSave()
    {
        List<Integer> potionCounts = new ArrayList<>();
        for (AbstractPotion p : AbstractDungeon.player.potions) {
            potionCounts.add(PotionUseCountField.useCount.get(p));
        }
        return potionCounts;
    }

    @Override
    public void onLoad(List<Integer> potionCounts)
    {
        if (potionCounts == null) {
            return;
        }
        for (int i = 0; i < AbstractDungeon.player.potions.size(); ++i) {
            int uses = POTION_USES;
            if (potionCounts.size() > i) {
                uses = potionCounts.get(i);
            }
            PotionUseCountField.useCount.set(AbstractDungeon.player.potions.get(i), uses);
        }
    }

    @Override
    public void betterOnUsePotion(AbstractPotion potion)
    {
        int useCount = PotionUseCountField.useCount.get(potion);
        --useCount;
        PotionUseCountField.useCount.set(potion, useCount);

        if (useCount > 0) {
            this.potion = potion;
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (potion != null) {
            flash();
            AbstractDungeon.player.obtainPotion(potion.slot, potion);
            potion = null;
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new EmptyBottle();
    }
}
