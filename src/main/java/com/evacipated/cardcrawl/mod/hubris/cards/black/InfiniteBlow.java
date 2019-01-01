package com.evacipated.cardcrawl.mod.hubris.cards.black;

import com.evacipated.cardcrawl.mod.hubris.CardIgnore;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SearingBlowEffect;
import infinitespire.abstracts.BlackCard;

import java.io.IOException;

@CardIgnore
public class InfiniteBlow extends BlackCard
{
    public static final String ID = "hubris:InfiniteBlow";
    public static final String IMG = HubrisMod.assetPath("images/cards/infiniteBlow.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;
    private static final int ATTACK_DMG = 12;
    private static final int BASE_UPGRADE = 4;

    private static final String CONFIG_KEY = "infiniteBlow";
    private static int savedUpgrades = 0;

    public InfiniteBlow()
    {
        this(savedUpgrades);
    }

    public InfiniteBlow(int upgrades)
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, CardTarget.ENEMY);
        // Otherwise, BlackCard makes it "infinitespire:ID"
        cardID = ID;

        baseDamage = ATTACK_DMG;
        while (timesUpgraded < upgrades) {
            upgrade();
        }
    }

    @Override
    public void useWithEffect(AbstractPlayer p, AbstractMonster m)
    {
        if (m != null) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new SearingBlowEffect(m.hb.cX, m.hb.cY, timesUpgraded), 0.2F));
        }
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public boolean canUpgrade()
    {
        return true;
    }

    @Override
    public void upgrade()
    {
        upgradeDamage(BASE_UPGRADE + timesUpgraded);
        ++timesUpgraded;
        upgraded = true;
        name = NAME + "+" + timesUpgraded;
        initializeTitle();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new InfiniteBlow();
    }

    public static void save()
    {
        if (AbstractDungeon.player != null && HubrisMod.otherSaveData != null) {
            try {
                int maxUpgrades = -1;
                if (HubrisMod.otherSaveData.has(CONFIG_KEY)) {
                    maxUpgrades = HubrisMod.otherSaveData.getInt(CONFIG_KEY);
                }
                for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                    if (c.cardID.equals(ID)) {
                        if (c.timesUpgraded > maxUpgrades) {
                            maxUpgrades = c.timesUpgraded;
                        }
                    }
                }
                if (maxUpgrades >= 0) {
                    HubrisMod.otherSaveData.setInt(CONFIG_KEY, maxUpgrades);
                    HubrisMod.otherSaveData.save();
                }
            } catch (IOException ignored) {
            }
        }
    }

    public static void load()
    {
        if (HubrisMod.otherSaveData != null && HubrisMod.otherSaveData.has(CONFIG_KEY)) {
            savedUpgrades = HubrisMod.otherSaveData.getInt(CONFIG_KEY);
        }
    }

    public static void clear()
    {
    }
}
