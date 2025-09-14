/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier2;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sickle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class DuoHunZhua extends MeleeWeapon {
	
	{
		image = ItemSpriteSheet.DUO_HUN_ZHUA;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1.2f;

		tier = 2;
	}

	@Override
	public int max(int lvl) {
		return 12+3*lvl;
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (Random.Float()<0.35f){
			defender.damage(minAttrib(), new Corruption());
			int amount = minAttrib();
			Barrier b = Buff.affect(attacker, Barrier.class);
			if (b.shielding() <= amount){
				b.setShield(amount);
				b.delay(Math.max(10-b.cooldown(), 0));
			}
		}
		return super.proc(attacker, defender, damage);
	}

	public String statsInfo(){
		if (isIdentified()){
			return Messages.get(this, "stats_desc",  minAttrib());
		} else {
			return Messages.get(this, "typical_stats_desc", 2);
		}
	}
	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		//replaces damage with 15+2.5*lvl bleed, roughly 138% avg base dmg, 125% avg scaling
		int bleedAmt = augment.damageFactor(damageBoost(Math.round(15f + 2.5f*buffedLvl()+2.5f*hero.weaponMastery)));

		Sickle.harvestAbility(hero, target, 0f, bleedAmt, this);
	}

	@Override
	public String abilityInfo() {
		int bleedAmt = damageBoost(Math.round(15f + 2.5f*buffedLvl()+2.5f*Dungeon.hero.weaponMastery));
		if (levelKnown){
			return Messages.get(this, "ability_desc", augment.damageFactor(bleedAmt));
		} else {
			return Messages.get(this, "typical_ability_desc", 15);
		}
	}

	@Override
	public String upgradeAbilityStat(int level) {
		return Integer.toString(augment.damageFactor(Math.round(15f + 2.5f*level)));
	}

}
