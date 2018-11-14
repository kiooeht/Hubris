package com.evacipated.cardcrawl.mod.hubris.rewards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class LinkedRewardItem extends RewardItem
{
    public LinkedRewardItem(RewardItem original)
    {
        type = original.type;
        outlineImg = original.outlineImg;
        img = original.img;
        goldAmt = original.goldAmt;
        bonusGold = original.bonusGold;
        text = original.text;
        relicLink = original.relicLink; // TODO?
        relic = original.relic;
        potion = original.potion;
        cards = original.cards;
        //effects
        //isBoss
        hb = original.hb;
        y = original.y;
        flashTimer = original.flashTimer;
        isDone = original.isDone;
        ignoreReward = original.ignoreReward;
        redText = original.redText;
    }

    public LinkedRewardItem(RewardItem setRelicLink, AbstractRelic relic)
    {
        super(relic);

        this.relicLink = setRelicLink;
        setRelicLink.relicLink = this;
    }

    @Override
    public void render(SpriteBatch sb)
    {
        super.render(sb);

        if (relicLink != null && type != RewardType.SAPPHIRE_KEY) {
            if (hb.hovered) {
                // Make TipHelper think we haven't tried to render a tip this frame
                try {
                    Field f = TipHelper.class.getDeclaredField("renderedTipThisFrame");
                    f.setAccessible(true);
                    f.setBoolean(null, false);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }

                ArrayList<PowerTip> tips = new ArrayList<>();
                tips.add(new PowerTip(relic.name, relic.description));
                tips.add(new PowerTip(TEXT[7], TEXT[8] + FontHelper.colorString(relicLink.relic.name, "y") + TEXT[9]));
                TipHelper.queuePowerTips(360.0f * Settings.scale, InputHelper.mY + 50.0f * Settings.scale, tips);
            }

            if (AbstractDungeon.getCurrRoom().rewards.indexOf(this) > AbstractDungeon.getCurrRoom().rewards.indexOf(relicLink)) {
                renderRelicLink(sb);
            }
        }

        hb.render(sb);
    }

    @SpireOverride
    protected void renderRelicLink(SpriteBatch sb)
    {
        SpireSuper.call(sb);
    }
}
