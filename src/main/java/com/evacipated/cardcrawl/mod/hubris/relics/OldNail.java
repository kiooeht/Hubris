package com.evacipated.cardcrawl.mod.hubris.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class OldNail extends HubrisRelic
{
    public static final String ID = "hubris:OldNail";

    public OldNail()
    {
        super(ID, "oldNail.png", RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void setCounter(int value)
    {
        super.setCounter(value);

        if (counter == -42) {
            flash();
            try {
                Field targetField = AbstractRelic.class.getDeclaredField("name");

                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(targetField, targetField.getModifiers() & ~Modifier.FINAL);

                targetField.setAccessible(true);
                targetField.set(this, DESCRIPTIONS[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            flavorText = DESCRIPTIONS[2];
            description = DESCRIPTIONS[3];
            tips.clear();
            tips.add(new PowerTip(name, description));
            initializeTips();
        }
    }

    @Override
    public int onAttackedMonster(DamageInfo info, int damageAmount)
    {
        if (counter == -42) {
            if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS) {
                return damageAmount * 2;
            }
        }
        return damageAmount;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new OldNail();
    }
}
