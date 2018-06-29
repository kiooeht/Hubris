package com.evacipated.cardcrawl.mod.hubris.actions.monsterOrbs;

import com.evacipated.cardcrawl.mod.hubris.monsters.OrbUsingMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class MonsterLightningOrbEvokeAction extends AbstractGameAction
{
    private OrbUsingMonster owner;
    private DamageInfo info;

    public MonsterLightningOrbEvokeAction(OrbUsingMonster owner, DamageInfo info)
    {
        this.owner = owner;
        this.info = info;
        actionType = ActionType.DAMAGE;
        attackEffect = AttackEffect.NONE;
    }

    @Override
    public void update()
    {
        float speedTime;
        AbstractPlayer p = AbstractDungeon.player;
        speedTime = 0.2F / owner.orbs.size();
        if (Settings.FAST_MODE) {
            speedTime = 0.0F;
        }
        AbstractDungeon.actionManager.addToTop(new DamageAction(p, this.info, AbstractGameAction.AttackEffect.NONE, true));
        AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(p.drawX, p.drawY), speedTime));
        AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        this.isDone = true;
    }
}
