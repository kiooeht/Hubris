package com.evacipated.cardcrawl.mod.hubris.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.DrawPower;

public class FixedDrawPower extends DrawPower
{
    public static final String POWER_ID = "hubris:FixedDraw";
    private Color redColor = new Color(1.0F, 0.0F, 0.0F, 1.0F);

    public FixedDrawPower(AbstractCreature owner, int amount)
    {
        super(owner, amount);
        ID = POWER_ID;
        canGoNegative = true;
    }

    @Override
    public void stackPower(int stackAmount)
    {
        fontScale = 8.0f;
        amount += stackAmount;
    }

    @Override
    public void updateDescription()
    {
        if (amount > 0) {
            if (amount == 1) {
                description = DrawPower.DESCRIPTIONS[0] + amount + DrawPower.DESCRIPTIONS[1];
            } else {
                description = DrawPower.DESCRIPTIONS[0] + amount + DrawPower.DESCRIPTIONS[3];
            }
            type = PowerType.BUFF;
        } else {
            if (amount == -1) {
                description = DrawPower.DESCRIPTIONS[0] + Math.abs(amount) + DrawPower.DESCRIPTIONS[2];
            } else {
                description = DrawPower.DESCRIPTIONS[0] + Math.abs(amount) + DrawPower.DESCRIPTIONS[4];
            }
            type = PowerType.DEBUFF;
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (amount > 0) {
            super.renderAmount(sb, x, y, c);
        } else if (amount < 0 && canGoNegative) {
            redColor.a = c.a;
            c = redColor;

            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(Math.abs(amount)), x, y, fontScale, c);
        }
    }
}
