package com.evacipated.cardcrawl.mod.hubris.cards.blue;

import basemod.abstracts.CustomCard;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.characters.FakePlayer;
import com.evacipated.cardcrawl.mod.hubris.powers.SpecializedCircuitryPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpecializedCircuitry extends CustomCard implements ModalChoice.Callback
{
    public static final String ID = "hubris:SpecializedCircuitry";
    public static final String IMG = null;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 2;

    private static List<AbstractOrb> allowedOrbs = new ArrayList<>();

    private ModalChoice modal;

    static
    {
        AbstractPlayer realPlayer = AbstractDungeon.player;
        AbstractDungeon.player = new FakePlayer();

        // Base game orbs
        allowedOrbs.add(new Lightning());
        allowedOrbs.add(new Frost());
        allowedOrbs.add(new Dark());

        // Replay the Spire orbs
        if (HubrisMod.hasReplayTheSpire) {
            allowedOrbs.add(new HellFireOrb());
        }

        AbstractDungeon.player = realPlayer;
    }

    public SpecializedCircuitry()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.POWER, CardColor.BLUE, CardRarity.RARE, CardTarget.SELF);

        ModalChoiceBuilder builder = new ModalChoiceBuilder()
                .setCallback(this)
                .setColor(CardColor.BLUE)
                .setType(CardType.POWER);

        for (AbstractOrb orb : allowedOrbs) {
                builder.addOption(orb.name, "All orbs you Channel are " + orb.name + " orbs.", CardTarget.SELF);
        }

        modal = builder.create();
    }

    @Override
    public List<TooltipInfo> getCustomTooltips()
    {
        String description = "Choose ";
        for (int i=0; i<allowedOrbs.size()-1; ++i) {
            description += " #y" + allowedOrbs.get(i).name + ",";
        }
        description += " or #y" + allowedOrbs.get(allowedOrbs.size()-1).name + " orbs.";

        return Arrays.asList(
                new TooltipInfo("Orb Choices", description)
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        modal.open();
    }

    @Override
    public void optionSelected(AbstractPlayer p, AbstractMonster m, int i)
    {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpecializedCircuitryPower(p, allowedOrbs.get(i).makeCopy())));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            isInnate = true;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new SpecializedCircuitry();
    }

    public static void addOrbType(AbstractOrb orb)
    {
        allowedOrbs.add(orb);
    }
}
