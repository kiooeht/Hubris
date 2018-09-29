package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.BetterOnLoseHpRelic;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.BetterOnUsePotionRelic;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CrystalStatue extends HubrisRelic implements BetterOnLoseHpRelic
{
    public static final String ID = "hubris:CrystalStatue";
    private static final int AMT = 20;

    public CrystalStatue()
    {
        super(ID, "crystalStatue.png", RelicTier.UNCOMMON, LandingSound.MAGICAL);

        setCounter(AMT);
    }

    @Override
    public String getUpdatedDescription()
    {
        if (counter > 0) {
            return DESCRIPTIONS[3] + DESCRIPTIONS[1] + counter + DESCRIPTIONS[2];
        }
        return DESCRIPTIONS[0];
    }

    @Override
    public void setCounter(int c)
    {
        if (c < 0) {
            c = 0;
        }
        counter = c;
        if (counter == 0) {
            stopPulse();

            img = ImageMaster.loadImage(HubrisMod.assetPath("images/relics/crystalStatueBROKEN.png"));
            outlineImg = ImageMaster.loadImage(HubrisMod.assetPath("images/relics/outline/crystalStatueBROKEN.png"));
        }

        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void atBattleStart()
    {
        if (counter > 0) {
            beginLongPulse();
        } else {
            stopPulse();
        }
    }

    @Override
    public void atTurnStart()
    {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                if (CrystalStatue.this.counter > 0) {
                    CrystalStatue.this.flash();
                    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, CrystalStatue.this));
                    AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
                }
                isDone = true;
            }
        });
    }

    @Override
    public int betterOnLoseHp(DamageInfo info, int damageAmount)
    {
        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 0) {
            if (counter > 0) {
                flash();
                setCounter(counter - damageAmount);
            }
        }

        return damageAmount;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new CrystalStatue();
    }
}
