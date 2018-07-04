package com.evacipated.cardcrawl.mod.hubris.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.RareCodexAction;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.ReverseEnlightenmentAction;
import com.evacipated.cardcrawl.mod.hubris.cards.curses.NaturalOne;
import com.evacipated.cardcrawl.mod.hubris.powers.EnergyGainPower;
import com.evacipated.cardcrawl.mod.hubris.powers.FixedDrawPower;
import com.evacipated.cardcrawl.mod.hubris.vfx.combat.ShowRollResult;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.EffectHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.List;

public class Icosahedron extends CustomCard
{
    public static final String ID = "hubris:Icosahedron";
    public static final String IMG = "images/cards/icosahedron.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 0;

    public Icosahedron()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

        purgeOnUse = true;
        retain = true;
    }

    private Icosahedron(int roll)
    {
        super(ID, String.valueOf(roll), IMG, COST, String.valueOf(roll), CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

        rawDescription = name;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int roll = new Random().random(1, 20);

        AbstractRelic relic = p.getRelic(com.evacipated.cardcrawl.mod.hubris.relics.Icosahedron.ID);
        List<AbstractGameAction> actions = new ArrayList<>();

        switch (roll) {
            case 20: // Natural Twenty.
                actions.add(new GainEnergyAction(1));
                if (relic != null) {
                   relic.setCounter(20);
                }
                break;
            case 19: // Heal to full.
                actions.add(new HealAction(p, p, p.maxHealth));
                break;
            case 18: // At the start of each turn, Gain [E].
                actions.add(new ApplyPowerAction(p, p, new EnergyGainPower(p, 1), 1));
                break;
            case 17: // Choose 1 of 3 rare cards to add to your hand.
                actions.add(new RareCodexAction());
                break;
            case 16: // Gain a random rare potion.
                actions.add(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, true)));
                break;
            case 15: // Draw 1 additional card each turn.
                actions.add(new ApplyPowerAction(p, p, new FixedDrawPower(p, 1), 1));
                break;
            case 14: // Gain 1 Strength, 1 Dexterity, and 1 Focus.
                actions.add(new ApplyPowerAction(p, p, new StrengthPower(p, 1), 1));
                actions.add(new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1));
                actions.add(new ApplyPowerAction(p, p, new FocusPower(p, 1), 1));
                break;
            case 13: // Gain 3 Artifact.
                actions.add(new ApplyPowerAction(p, p, new ArtifactPower(p, 3), 3));
                break;
            case 12: // Gain a random common potion.
                actions.add(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.COMMON, true)));
                break;
            case 11: // Gain 100 Gold.
                EffectHelper.gainGold(p, this.current_x, this.current_y, 100);
                break;
            case 10: // Add a Mulligan to your hand.
                actions.add(new MakeTempCardInHandAction(new Mulligan()));
                break;
            case 9: // Lose 50 Gold.
                AbstractDungeon.player.loseGold(50);
                break;
            case 8: // Gain 5 Weak.
                actions.add(new ApplyPowerAction(p, p, new WeakPower(p, 5, false)));
                break;
            case 7: // Gain 5 Frail.
                actions.add(new ApplyPowerAction(p, p, new FrailPower(p, 5, false)));
                break;
            case 6: // Gain 5 Vulnerable.
                actions.add(new ApplyPowerAction(p, p, new VulnerablePower(p, 5, true)));
                break;
            case 5: // Increase the cost of cards in your hand by 1 this combat.
                actions.add(new ReverseEnlightenmentAction());
                break;
            case 4: // Draw 1 less card each turn.
                actions.add(new ApplyPowerAction(p, p, new FixedDrawPower(p, -1), -1));
                break;
            case 3: // Add 3 Void to your draw pile.
                actions.add(new MakeTempCardInDrawPileAction(new com.megacrit.cardcrawl.cards.status.Void(), 3, true, true));
                break;
            case 2: // Lose 10 HP.
                actions.add(new LoseHPAction(p, p, 10));
                break;
            case 1: // Natural One.
                actions.add(new MakeTempCardInHandAction(new NaturalOne()));
                if (relic != null) {
                    relic.setCounter(1);
                }
                break;
        }

        AbstractDungeon.topLevelEffects.add(new ShowRollResult(roll, actions));
    }

    @Override
    public void atTurnStart()
    {
        retain = true;
    }

    @Override
    public void triggerOnEndOfPlayerTurn()
    {
        //AbstractDungeon.actionManager.addToTop(new PurgeCardAction(this, AbstractDungeon.player.hand));
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Icosahedron();
    }
}
