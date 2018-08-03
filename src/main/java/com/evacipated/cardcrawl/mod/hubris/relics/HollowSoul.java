package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EnableEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAndEnableControlsAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.blights.Muzzle;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;

import java.lang.reflect.Field;

public class HollowSoul extends AbstractRelic
{
    public static final String ID = "hubris:HollowSoul";
    private static final int HP_PERCENT = 10;

    private int originalMaxHP = -1;

    public HollowSoul()
    {
        super(ID, "hollowSoul.png", RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + HP_PERCENT + DESCRIPTIONS[1];
    }

    @Override
    public void setCounter(int counter)
    {
        if (counter == -2) {
            img = ImageMaster.loadImage("images/relics/hollowSoulUsed.png");
            this.counter = -2;
        } else if (counter == -1) {
            img = ImageMaster.loadImage("images/relics/hollowSoul.png");
            this.counter = -1;
        }
    }

    public void use()
    {
        beginPulse();
        pulse = true;
        setCounter(-2);
    }

    public void restore()
    {
        pulse = false;
        AbstractDungeon.player.maxHealth = originalMaxHP;
        AbstractDungeon.player.healthBarUpdatedEvent();
        setCounter(-1);
    }

    @Override
    public void onTrigger()
    {
        if (counter != -2) {
            flash();
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            originalMaxHP = AbstractDungeon.player.maxHealth;

            AbstractDungeon.player.maxHealth = Math.round(AbstractDungeon.player.maxHealth * 0.1f);
            if (AbstractDungeon.player.maxHealth < 1) {
                AbstractDungeon.player.maxHealth = 1;
            }
            //AbstractDungeon.player.healthBarUpdatedEvent();

            int healAmt = AbstractDungeon.player.maxHealth;
            if (AbstractDungeon.player.hasBlight(Muzzle.ID)) {
                healAmt /= 2;
            }
            if (healAmt < 1) {
                healAmt = 1;
            }

            AbstractDungeon.player.heal(healAmt, true);
            use();

            restartCombat();
        }
    }

    private void restartCombat()
    {
        System.out.println("Remaking combat vs " + AbstractDungeon.lastCombatMetricKey);
        AbstractRoom room = AbstractDungeon.getCurrRoom();

        // Clear
        AbstractDungeon.fadeIn();
        AbstractDungeon.player.resetControllerValues();
        AbstractDungeon.effectList.clear();
        AbstractDungeon.topLevelEffects.clear();
        AbstractDungeon.topLevelEffectsQueue.clear();
        AbstractDungeon.effectsQueue.clear();
        AbstractDungeon.dungeonMapScreen.dismissable = true;
        AbstractDungeon.dungeonMapScreen.map.legend.isLegendHighlighted = false;

        AbstractDungeon.player.orbs.clear();
        AbstractDungeon.player.animX = 0.0f;
        AbstractDungeon.player.animY = 0.0f;
        AbstractDungeon.player.hideHealthBar();
        AbstractDungeon.player.hand.clear();
        AbstractDungeon.player.powers.clear();
        AbstractDungeon.player.drawPile.clear();
        AbstractDungeon.player.discardPile.clear();
        AbstractDungeon.player.exhaustPile.clear();
        AbstractDungeon.player.limbo.clear();
        AbstractDungeon.player.loseBlock(true);
        AbstractDungeon.player.damagedThisCombat = 0;
        GameActionManager.turn = 1;

        // Stop any currently attack monsters
        AbstractDungeon.actionManager.monsterQueue.clear();
        // Make sure monsters don't try to requeue attacks
        AbstractDungeon.actionManager.monsterAttacksQueued = true;

        // Don't reset RNG, so fight isn't identical

        for (AbstractRelic r : AbstractDungeon.player.relics) {
            r.onEnterRoom(room);
        }

        AbstractDungeon.actionManager.clear();

        // Remake monsters
        room.monsters = MonsterHelper.getEncounter(AbstractDungeon.lastCombatMetricKey);
        room.monsters.init();

        // Prepare monsters
        for (AbstractMonster m : room.monsters.monsters) {
            m.showHealthBar();
            m.usePreBattleAction();
            m.useUniversalPreBattleAction();
        }

        AbstractDungeon.player.preBattlePrep();

        // From AbstractRoom.update(). Most of what happens at start of combat
        AbstractDungeon.actionManager.turnHasEnded = true;
        AbstractDungeon.topLevelEffects.add(new BattleStartEffect(false));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAndEnableControlsAction(AbstractDungeon.player.energy.energyMaster));

        AbstractDungeon.player.applyStartOfCombatPreDrawLogic();
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, AbstractDungeon.player.gameHandSize));

        AbstractDungeon.actionManager.addToBottom(new EnableEndTurnButtonAction());
        AbstractDungeon.overlayMenu.showCombatPanels();
        AbstractDungeon.player.applyStartOfCombatLogic();
        AbstractDungeon.player.applyStartOfTurnRelics();
        AbstractDungeon.player.applyStartOfTurnPostDrawRelics();
        AbstractDungeon.player.applyStartOfTurnCards();
        AbstractDungeon.player.applyStartOfTurnPowers();
        AbstractDungeon.player.applyStartOfTurnOrbs();
    }

    @Override
    public void onVictory()
    {
        if (counter == -2 && originalMaxHP > 0) {
            flash();
            restore();
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new HollowSoul();
    }
}
