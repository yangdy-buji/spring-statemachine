/*
 * Copyright 2015-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.statemachine.transition;

import java.util.Collection;
import java.util.function.Function;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.security.SecurityRule;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.trigger.Trigger;

import reactor.core.publisher.Mono;

public class DefaultLocalTransition<S, E> extends AbstractLocalTransition<S, E> {

	/**
	 * Instantiates a new default local transition.
	 *
	 * @param source the source
	 * @param target the target
	 * @param actions the actions
	 * @param event the event
	 * @param guard the guard
	 * @param trigger the trigger
	 */
	public DefaultLocalTransition(State<S, E> source, State<S, E> target,
			Collection<Function<StateContext<S, E>, Mono<Void>>> actions, E event,
			Function<StateContext<S, E>, Mono<Boolean>> guard, Trigger<S, E> trigger) {
		super(source, target, actions, event, guard, trigger);
	}

	/**
	 * Instantiates a new default local transition.
	 *
	 * @param source the source
	 * @param target the target
	 * @param actions the actions
	 * @param event the event
	 * @param guard the guard
	 * @param trigger the trigger
	 * @param securityRule the security rule
	 */
	public DefaultLocalTransition(State<S, E> source, State<S, E> target,
			Collection<Function<StateContext<S, E>, Mono<Void>>> actions, E event,
			Function<StateContext<S, E>, Mono<Boolean>> guard, Trigger<S, E> trigger, SecurityRule securityRule) {
		super(source, target, actions, event, guard, trigger, securityRule);
	}

	@Override
	public String toString() {
		return "DefaultLocalTransition [getSource()=" + getSource() + ", getTarget()=" + getTarget() + "]";
	}

}
