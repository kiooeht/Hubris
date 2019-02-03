package com.evacipated.cardcrawl.mod.hubris.powers;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.lang.reflect.Field;

public class SpecializedCircuitryPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:SpecializedCircuitry";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private AbstractOrb orbType;

    public SpecializedCircuitryPower(AbstractCreature owner, AbstractOrb orbType)
    {
        name = DESCRIPTIONS[2] + orbType.name + NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        amount = -1;
        setOrbType(orbType);
        updateDescription();
    }

    private static Texture resize(Texture tex)
    {
        TextureData textureData = tex.getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }
        Pixmap pixmap = textureData.consumePixmap();
        Pixmap small = new Pixmap(32, 32, pixmap.getFormat());
        small.drawPixmap(pixmap,
                0, 0, pixmap.getWidth(), pixmap.getHeight(),
                0, 0, small.getWidth(), small.getHeight());
        Texture ret = new Texture(small);
        ret.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();
        small.dispose();
        return ret;
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + orbType.name + DESCRIPTIONS[1];
    }

    public void setOrbType(AbstractOrb orbType)
    {
        this.orbType = orbType;
        Texture tex = null;
        try {
            if (orbType instanceof Frost) {
                Field f = Frost.class.getDeclaredField("img3");
                f.setAccessible(true);
                tex = (Texture) f.get(orbType);
            } else {
                Field f = AbstractOrb.class.getDeclaredField("img");
                f.setAccessible(true);
                tex = (Texture) f.get(orbType);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (tex == null) {
            tex = ImageMaster.ORB_PLASMA;
        }
        img = resize(tex);
    }

    public AbstractOrb getOrb()
    {
        return orbType.makeCopy();
    }
}
