/*
 * Copyright 2016-2019 the original author or authors.
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
package org.springframework.statemachine.monitor;

import java.util.Iterator;
import java.util.function.Function;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.AbstractCompositeItems;
import org.springframework.statemachine.transition.Transition;

import reactor.core.publisher.Mono;

/**
 * Implementation of a {@link StateMachineMonitor} backed by a multiple monitors.
 *
 * @author Janne Valkealahti
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public class CompositeStateMachineMonitor<S, E> extends AbstractCompositeItems<StateMachineMonitor<S, E>>
		implements StateMachineMonitor<S, E> {

	@Override
	public void transition(StateMachine<S, E> stateMachine, Transition<S, E> transition, long duration) {
		for (Iterator<StateMachineMonitor<S, E>> iterator = getItems().reverse(); iterator.hasNext();) {
			StateMachineMonitor<S, E> monitor = iterator.next();
			monitor.transition(stateMachine, transition, duration);
		}
	}

	@Override
	public void action(StateMachine<S, E> stateMachine, Function<StateContext<S, E>, Mono<Void>> action,
			long duration) {
		for (Iterator<StateMachineMonitor<S, E>> iterator = getItems().reverse(); iterator.hasNext();) {
			StateMachineMonitor<S, E> monitor = iterator.next();
			monitor.action(stateMachine, action, duration);
		}
	}
}
