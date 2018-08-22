package com.evacipated.cardcrawl.mod.hubris.patches.monsters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import javassist.*;

@SpirePatch(
        cls="com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_S",
        method=SpirePatch.CONSTRUCTOR
)
public class SpikeSlime_S
{
    public static void Raw(CtBehavior ctMethodToPatch) throws CannotCompileException
    {
        CtClass ctClass = ctMethodToPatch.getDeclaringClass();
        CtConstructor ctor = CtNewConstructor.make(
                "public " + ctClass.getSimpleName() + "(float x, float y) {" +
                        "this(x, y, 0);" +
                        "}",
                ctClass
        );
        ctClass.addConstructor(ctor);
    }
}
