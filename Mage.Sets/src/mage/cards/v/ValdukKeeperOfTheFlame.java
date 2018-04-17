/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.v;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.BeginningOfCombatTriggeredAbility;
import mage.abilities.dynamicvalue.common.AuraAttachedCount;
import mage.abilities.dynamicvalue.common.EquipmentAttachedCount;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.SubType;
import mage.constants.SuperType;
import mage.constants.TargetController;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.game.permanent.token.ValdukElementalToken;

/**
 *
 * @author spjspj
 */
public class ValdukKeeperOfTheFlame extends CardImpl {

    public ValdukKeeperOfTheFlame(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{2}{R}");

        addSuperType(SuperType.LEGENDARY);
        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.SHAMAN);
        this.power = new MageInt(3);
        this.toughness = new MageInt(2);

        // At the beginning of combat on your turn, for each Aura and Equipment attached to Valduk, Keeper of the Flame, create a 3/1 red Elemental creature token with trample and haste. Exile those tokens at the beginning of the next end step.
        this.addAbility(new BeginningOfCombatTriggeredAbility(new ValdukKeeperOfTheFlameEffect(), TargetController.YOU, false));
    }

    public ValdukKeeperOfTheFlame(final ValdukKeeperOfTheFlame card) {
        super(card);
    }

    @Override
    public ValdukKeeperOfTheFlame copy() {
        return new ValdukKeeperOfTheFlame(this);
    }
}

class ValdukKeeperOfTheFlameEffect extends OneShotEffect {

    public ValdukKeeperOfTheFlameEffect() {
        super(Outcome.PutCreatureInPlay);
        this.staticText = "for each Aura and Equipment attached to {this}, create a 3/1 red Elemental creature token with trample and haste. Exile those tokens at the beginning of the next end step";
    }

    public ValdukKeeperOfTheFlameEffect(final ValdukKeeperOfTheFlameEffect effect) {
        super(effect);
    }

    @Override
    public ValdukKeeperOfTheFlameEffect copy() {
        return new ValdukKeeperOfTheFlameEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent sourcePermanent = game.getPermanent(source.getSourceId());
        if (sourcePermanent != null) {
            EquipmentAttachedCount eamount = new EquipmentAttachedCount();
            int value = eamount.calculate(game, source, this);
            AuraAttachedCount aamount = new AuraAttachedCount();
            value += aamount.calculate(game, source, this);
            CreateTokenEffect effect = new CreateTokenEffect(new ValdukElementalToken(), value);
            if (effect.apply(game, source)) {
                effect.exileTokensCreatedAtNextEndStep(game, source);
                return true;
            }
        }
        return false;
    }
}