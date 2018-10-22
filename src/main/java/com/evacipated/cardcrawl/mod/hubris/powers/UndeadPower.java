package com.evacipated.cardcrawl.mod.hubris.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.hubris.actions.common.AlwaysApplyPowerAction;
import com.evacipated.cardcrawl.mod.hubris.actions.common.AlwaysRemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.vfx.TintEffect;

public class UndeadPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:Undead";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private AbstractCreature parent;
    public float shaderTimer;

    public UndeadPower(AbstractCreature owner, AbstractCreature parent, int amount)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.parent = parent;
        this.amount = amount;
        type = PowerType.BUFF;
        updateDescription();
        priority = -99;
        loadRegion("minion");
        //region48 = HubrisMod.powerAtlas.findRegion("48/championShield");
        //region128 = HubrisMod.powerAtlas.findRegion("128/championShield");
        shaderTimer = 0.0f;
    }

    public UndeadPower(AbstractCreature owner, UndeadPower other)
    {
        this(owner, other.parent, other.amount);
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + FontHelper.colorString(parent.name, "y") + DESCRIPTIONS[2];
    }

    @Override
    public void onInitialApplication()
    {
        int poison = (int)(owner.maxHealth * (1.0f / 5.0f));
        owner.addPower(new PoisonPower(owner, owner, poison));
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.owner != null) {
            AbstractDungeon.actionManager.addToBottom(new AlwaysApplyPowerAction(info.owner, owner, new PoisonPower(info.owner, owner, amount), amount, true));
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void onDeath()
    {
        if (!parent.isDeadOrEscaped()) {
            owner.isDying = false;
            owner.halfDead = true;
            // Stop the fade out on death
            owner.tint = new TintEffect() {
                @Override
                public void fadeOut() {}
            };
            // Stop death animation shake
            ReflectionHacks.setPrivate(owner, AbstractCreature.class, "animationTimer", 0.0f);
            // Remove all debuffs
            for (AbstractPower p : owner.powers) {
                if (p.type == PowerType.DEBUFF) {
                    AbstractDungeon.actionManager.addToTop(new AlwaysRemoveSpecificPowerAction(owner, owner, p));
                }
            }

            ((AbstractMonster) owner).setMove((byte) -72, AbstractMonster.Intent.BUFF);
            ((AbstractMonster) owner).createIntent();
            AbstractDungeon.actionManager.addToBottom(new SetMoveAction((AbstractMonster) owner, (byte)-72, AbstractMonster.Intent.BUFF));
        }
    }

    @Override
    public void onSpecificTrigger()
    {
        onInitialApplication();
        //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);

        if (shaderTimer < 1.0f) {
            shaderTimer += Gdx.graphics.getDeltaTime();
            if (shaderTimer > 1.0f) {
                shaderTimer = 1.0f;
            }
        }
    }
}
