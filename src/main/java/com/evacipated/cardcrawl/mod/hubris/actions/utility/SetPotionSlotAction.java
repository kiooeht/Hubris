package com.evacipated.cardcrawl.mod.hubris.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.Sozu;

public class SetPotionSlotAction extends AbstractGameAction
{
    private int slot;
    private AbstractPotion potion;
    private boolean ignoreSozu;

    public SetPotionSlotAction(int slot, AbstractPotion potion)
    {
        this(slot, potion, false);
    }

    public SetPotionSlotAction(int slot, AbstractPotion potion, boolean ignoreSozu)
    {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.slot = slot;
        this.potion = potion;
        this.ignoreSozu = ignoreSozu;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_XFAST) {
            if (!ignoreSozu && AbstractDungeon.player.hasRelic(Sozu.ID)) {
                AbstractDungeon.player.getRelic(Sozu.ID).flash();
            } else {
                AbstractDungeon.player.obtainPotion(slot, potion);
            }
        }
        tickDuration();
    }
}
