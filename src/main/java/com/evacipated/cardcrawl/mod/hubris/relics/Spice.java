package com.evacipated.cardcrawl.mod.hubris.relics;

import basemod.Pair;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.cards.curses.SpiceAddiction;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;

public class Spice extends HubrisRelic
{
    public static final String ID = "hubris:Spice";
    private static final int HP_AMT = 8;

    private int updateDescriptions = 0;

    public Spice()
    {
        this(-1);
    }

    public Spice(int c)
    {
        super(ID, "spice.png", RelicTier.RARE, LandingSound.MAGICAL);
        setCounter(c);
    }

    @Override
    public String getUpdatedDescription()
    {
        AbstractRelic spice = null;
        if (AbstractDungeon.player != null) {
            spice = AbstractDungeon.player.getRelic(Spice.ID);
        }

        if (AbstractDungeon.player == null || spice == null) {
            return DESCRIPTIONS[0];
        } else if (spice == this) {
            // Spice player has
            String ret = "";
            if (counter <= 4) {
                ret += DESCRIPTIONS[4]; // Max HP +
            } else {
                ret += DESCRIPTIONS[5]; // Max HP -
            }
            if (counter <= 1) {
                ret += HP_AMT;
            } else if (counter == 2) {
                ret += HP_AMT * 2;
            } else if (counter == 3) {
                ret += HP_AMT;
            } else {
                ret += -HP_AMT * (counter - 4);
            }
            ret += DESCRIPTIONS[6]; // Draw
            if (counter >= 2) {
                ret += DESCRIPTIONS[7]; // Start combat with
            }
            Pair<Integer, Integer> strDex = getStartingStrDex();
            int str = strDex.getKey();
            int dex = strDex.getValue();
            if (str > 0) {
                ret += str + DESCRIPTIONS[8]; // Strength
            }
            if (dex > 0) {
                ret += dex + DESCRIPTIONS[9]; // Dexterity
            }
            ret += DESCRIPTIONS[10]; // Warning
            return ret;
        } else {
            // Other Spice. Rewards, shop, etc.
            if (spice.counter <= 1) {
                return DESCRIPTIONS[1];
            } else if (spice.counter >= 3) {
                return DESCRIPTIONS[3];
            } else {
                return DESCRIPTIONS[spice.counter];
            }
        }
    }

    @Override
    public void setCounter(int c)
    {
        super.setCounter(c);
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    private void updateOtherDescriptions()
    {
        Spice spice = (Spice) AbstractDungeon.player.getRelic(Spice.ID);
        if (spice != null) {
            if (spice.updateDescriptions > 0) {
                if (this == spice) {
                    --updateDescriptions;
                } else {
                    description = getUpdatedDescription();
                    tips.clear();
                    tips.add(new PowerTip(name, description));
                    initializeTips();
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        updateOtherDescriptions();

        super.render(sb);
    }

    @Override
    public void renderWithoutAmount(SpriteBatch sb, Color c)
    {
        updateOtherDescriptions();

        super.renderWithoutAmount(sb, c);
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.increaseMaxHp(HP_AMT, true);
        AbstractDungeon.player.masterHandSize += 1;
    }

    @Override
    public void onUnequip()
    {
        AbstractDungeon.player.masterHandSize -= 1;
    }

    private Pair<Integer, Integer> getStartingStrDex()
    {
        int str = 0;
        int dex = 0;
        switch (counter) {
            default:
            case 4:
                str += counter - 3;
            case 3:
                str += 1;
                dex += 1;
            case 2:
                dex += 1;
            case 1:
            case 0:
            case -1:
                break;
        }
        return new Pair<>(str, dex);
    }

    @Override
    public void atPreBattle()
    {
        flash();
        Pair<Integer, Integer> strDex = getStartingStrDex();
        if (strDex.getKey() > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, strDex.getKey()), strDex.getKey()));
        }
        if (strDex.getValue() > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, strDex.getValue()), strDex.getValue()));
        }
    }

    public void increment()
    {
        if (counter <= 0) {
            counter = 1;
        }
        setCounter(counter + 1);

        if (counter <= 2) {
            AbstractDungeon.player.increaseMaxHp(HP_AMT, true);
        } else {
            AbstractDungeon.player.decreaseMaxHealth(HP_AMT);
            AbstractPlayer p = AbstractDungeon.player;
            String text = AbstractCreature.TEXT[2].substring(0, AbstractCreature.TEXT[2].length()-1) + "-";
            AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(p.hb.cX - p.animX, p.hb.cY, text + HP_AMT, Settings.RED_TEXT_COLOR));
        }

        updateDescriptions = 2;
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void instantObtain()
    {
        if (AbstractDungeon.player.hasRelic(Spice.ID)) {
            Spice spice = (Spice) AbstractDungeon.player.getRelic(Spice.ID);
            spice.increment();
            spice.flash();
        } else {
            super.instantObtain();
        }
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip)
    {
        if (AbstractDungeon.player.hasRelic(Spice.ID)) {
            Spice spice = (Spice) AbstractDungeon.player.getRelic(Spice.ID);
            spice.increment();
            spice.flash();

            isDone = true;
            isObtained = true;
            discarded = true;
        } else {
            super.instantObtain(p, slot, callOnEquip);
        }
    }

    @Override
    public void obtain()
    {
        if (AbstractDungeon.player.hasRelic(Spice.ID)) {
            Spice spice = (Spice) AbstractDungeon.player.getRelic(Spice.ID);
            spice.increment();
            spice.flash();
        } else {
            super.obtain();
        }
    }

    @Override
    public void onObtainCard(AbstractCard c)
    {
        if (c.cardID.equals(SpiceAddiction.ID)) {
            flash();
            increment();
        }
    }

    @Override
    public void onMasterDeckChange()
    {
        boolean remove = false;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.cardID.equals(SpiceAddiction.ID)) {
                remove = true;
                break;
            }
        }
        if (remove) {
            AbstractDungeon.player.masterDeck.removeCard(SpiceAddiction.ID);
        }
    }

    private static int replaceWithSpiceChance()
    {
        // Happens because of other mods
        if (AbstractDungeon.player == null) {
            return 0;
        }

        AbstractRelic spice = AbstractDungeon.player.getRelic(Spice.ID);

        if (spice == null) {
            return 0;
        }

        int threshold;
        if (spice.counter >= 4) {
            threshold = 50;
        } else {
            switch (spice.counter) {
                case 3:
                    threshold = 45;
                    break;
                case 2:
                    threshold = 35;
                    break;
                default:
                    threshold = 20;
                    break;
            }
        }

        return threshold;
    }

    public static boolean replaceWithSpice()
    {
        int chance = replaceWithSpiceChance();

        return AbstractDungeon.relicRng.random(0, 99) < chance;
    }

    public static boolean replaceCardWithSpice()
    {
        int chance = replaceWithSpiceChance();
        chance /= 2;
        chance -= 10;

        return AbstractDungeon.relicRng.random(0, 99) < chance;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Spice(counter);
    }
}
