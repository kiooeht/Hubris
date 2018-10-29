package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.evacipated.cardcrawl.mod.hubris.cards.black.Rewind;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RewindAction extends AbstractGameAction
{
    public RewindAction()
    {
        duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_MED) {
            for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
                if (c.cardID.equals(Rewind.ID)) {
                    continue;
                }

                AbstractMonster targetMonster = AbstractDungeon.getRandomMonster();

                AbstractCard tmp = c.makeSameInstanceOf();
                tmp.current_x = tmp.target_x = Settings.WIDTH / 2.0f - 300.0f * Settings.scale;
                tmp.current_y = tmp.target_y = Settings.HEIGHT / 2.0f;
                tmp.freeToPlayOnce = true;
                if (targetMonster != null) {
                    tmp.calculateCardDamage(targetMonster);
                }
                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, targetMonster));
            }
        }
        tickDuration();
    }
}
