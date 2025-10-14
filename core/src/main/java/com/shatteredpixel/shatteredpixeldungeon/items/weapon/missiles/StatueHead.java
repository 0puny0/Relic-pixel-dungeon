package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class StatueHead extends MissileWeapon{
    {
        image = ItemSpriteSheet.STATUE_HEAD;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;
        tier = 3;
        baseUses = 1000;
        sticky = false;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Ballistica trajectory = new Ballistica(attacker.pos, defender.pos, Ballistica.STOP_TARGET);
        trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
        WandOfBlastWave.throwChar(defender, trajectory, 3, false, true, this);
        return super.proc(attacker, defender, damage);
    }
}
