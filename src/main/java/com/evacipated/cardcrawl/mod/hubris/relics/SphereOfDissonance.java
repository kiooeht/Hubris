package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.JuzuBracelet;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;

public class SphereOfDissonance extends HubrisRelic
{
    public static final String ID = "hubris:SphereOfDissonance";
    private static final int AMT = 3;
    private static final int JUZU_HEAL = 3;

    public SphereOfDissonance()
    {
        super(ID, "sphereOfDissonance.png", RelicTier.UNCOMMON, LandingSound.MAGICAL);

        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip("Synergy", DESCRIPTIONS[2] + FontHelper.colorString(new JuzuBracelet().name, "y") + DESCRIPTIONS[3] + JUZU_HEAL + DESCRIPTIONS[4]));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + AMT + DESCRIPTIONS[1] + new JuzuBracelet().name;
    }

    @Override
    public void atBattleStart()
    {
        flash();
        AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(m, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, AMT, false), AMT, true));
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        if (room instanceof EventRoom && AbstractDungeon.player.hasRelic(JuzuBracelet.ID)) {
            flash();
            AbstractDungeon.player.heal(JUZU_HEAL);
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new SphereOfDissonance();
    }
}
