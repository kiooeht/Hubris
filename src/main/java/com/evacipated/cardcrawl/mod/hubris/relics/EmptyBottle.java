package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.actions.utility.SetPotionSlotAction;
import com.evacipated.cardcrawl.mod.hubris.patches.potions.AbstractPotion.PotionUseCountField;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnUsePotionRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class EmptyBottle extends HubrisRelic implements BetterOnUsePotionRelic
{
    public static final String ID = "hubris:EmptyBottle";
    public static final int POTION_USES = 2;

    private static final String CONFIG_KEY = "potionUse_";

    public EmptyBottle()
    {
        super(ID, "emptyBottle.png", RelicTier.SHOP, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    public static void save(SpireConfig config)
    {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
            for (int i=0; i<AbstractDungeon.player.potions.size(); ++i) {
                config.setInt(CONFIG_KEY + i, PotionUseCountField.useCount.get(AbstractDungeon.player.potions.get(i)));
            }
        } else {
            config.remove(CONFIG_KEY);
        }
    }

    public static void load(SpireConfig config)
    {
        if (AbstractDungeon.player.hasRelic(ID)) {
            for (int i = 0; i < AbstractDungeon.player.potions.size(); ++i) {
                int uses = POTION_USES;
                if (config.has(CONFIG_KEY + i)) {
                    uses = config.getInt(CONFIG_KEY + i);
                }
                PotionUseCountField.useCount.set(AbstractDungeon.player.potions.get(i), uses);
            }
        }
    }

    public static void clear()
    {
    }

    @Override
    public void betterOnUsePotion(AbstractPotion potion)
    {
        int useCount = PotionUseCountField.useCount.get(potion);
        --useCount;
        PotionUseCountField.useCount.set(potion, useCount);

        if (useCount > 0) {
            flash();
            AbstractDungeon.actionManager.addToTop(new SetPotionSlotAction(potion.slot, potion, true));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new EmptyBottle();
    }
}
