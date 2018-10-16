package com.evacipated.cardcrawl.mod.hubris.cards.black;

import com.evacipated.cardcrawl.mod.hubris.CardIgnore;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.abstracts.BlackCard;

import java.io.IOException;

@CardIgnore
public class InfiniteBlow extends BlackCard
{
    public static final String ID = "hubris:InfiniteBlow";
    public static final String IMG = HubrisMod.BETA_ATTACK;
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
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
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
        if (AbstractDungeon.player != null) {
            try {
                SpireConfig config = new SpireConfig("Hubris", "OtherSaveData");
                int maxUpgrades = -1;
                if (config.has(CONFIG_KEY)) {
                    maxUpgrades = config.getInt(CONFIG_KEY);
                }
                for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                    if (c.cardID.equals(ID)) {
                        if (c.timesUpgraded > maxUpgrades) {
                            maxUpgrades = c.timesUpgraded;
                        }
                    }
                }
                if (maxUpgrades >= 0) {
                    config.setInt(CONFIG_KEY, maxUpgrades);
                    config.save();
                }
            } catch (IOException ignored) {
            }
        }
    }

    public static void load()
    {
        try {
            SpireConfig config = new SpireConfig("Hubris", "OtherSaveData");
            if (config.has(CONFIG_KEY)) {
                savedUpgrades = config.getInt(CONFIG_KEY);
                System.out.println("GOLD:" + Settings.GOLD_COLOR.toString());
            }
        } catch (IOException ignored) {
        }
    }

    public static void clear()
    {
    }
}
