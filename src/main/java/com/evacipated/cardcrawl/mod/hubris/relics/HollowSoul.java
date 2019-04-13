package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.actions.utility.ForceWaitAction;
import com.evacipated.cardcrawl.mod.hubris.monsters.MerchantMonster;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnPlayerDeathRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EnableEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAndEnableControlsAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.blights.Muzzle;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;

public class HollowSoul extends HubrisRelic implements OnPlayerDeathRelic
{
    public static final String ID = "hubris:HollowSoul";
    private static final int HP_PERCENT = 20;
    private static final int VICTORY_HEAL_PERCENT = 50;

    private int originalMaxHP = -1;

    public HollowSoul()
    {
        super(ID, "hollowSoul.png", RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + HP_PERCENT + DESCRIPTIONS[1] + VICTORY_HEAL_PERCENT + DESCRIPTIONS[2];
    }

    @Override
    public void setCounter(int counter)
    {
        if (counter == -2) {
            img = ImageMaster.loadImage(HubrisMod.assetPath("images/relics/hollowSoulUsed.png"));
            this.counter = -2;
        } else if (counter == -1) {
            img = ImageMaster.loadImage(HubrisMod.assetPath("images/relics/hollowSoul.png"));
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
    public boolean onPlayerDeath(AbstractPlayer p, DamageInfo info)
    {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !p.hasRelic(MarkOfTheBloom.ID)) {
            if (counter != -2) {
                p.currentHealth = 0;
                flash();
                AbstractDungeon.actionManager.addToTop(new HollowSoulReviveAction());
                AbstractDungeon.actionManager.addToTop(new ForceWaitAction(2.0f));
                return false;
            } else {
                // Reset Max HP to normal to stop losing out on Stuffed points
                restore();
                setCounter(-2);
            }
        }

        return true;
    }

    private class HollowSoulReviveAction extends AbstractGameAction
    {
        @Override
        public void update()
        {
            flash();
            originalMaxHP = AbstractDungeon.player.maxHealth;

            AbstractDungeon.player.maxHealth = (int) (AbstractDungeon.player.maxHealth * ((float) HP_PERCENT / 100.0f));
            if (AbstractDungeon.player.maxHealth < 1) {
                AbstractDungeon.player.maxHealth = 1;
            }
            //AbstractDungeon.player.healthBarUpdatedEvent();

            int healAmt = AbstractDungeon.player.maxHealth;
            AbstractDungeon.player.heal(healAmt, true);
            use();

            restartCombat();

            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, HollowSoul.this));
            isDone = true;
        }
    }


    private void restartCombat()
    {
        System.out.println("Remaking combat vs " + AbstractDungeon.lastCombatMetricKey);
        AbstractRoom room = AbstractDungeon.getCurrRoom();

        // Clear
        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();

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
            int healAmt = (int) (AbstractDungeon.player.maxHealth * ((float) VICTORY_HEAL_PERCENT / 100.0f));
            if (healAmt < 1) {
                healAmt = 1;
            }
            AbstractDungeon.player.currentHealth = 0;
            AbstractDungeon.player.heal(healAmt, true);
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new HollowSoul();
    }
}
