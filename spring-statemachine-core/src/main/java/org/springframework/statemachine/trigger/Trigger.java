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
package org.springframework.statemachine.trigger;

import org.springframework.statemachine.transition.Transition;

import reactor.core.publisher.Mono;

/**
 * {@code Trigger} is the cause of the {@link Transition}. Cause is usually an
 * event but can be some other signal or a change in some condition.
 *
 * @author Janne Valkealahti
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface Trigger<S,E> {

	/**
	 * Evaluate trigger.
	 *
	 * @param context the context
	 * @return Mono for completion with true, if trigger is fired, false otherwise
	 */
	Mono<Boolean> evaluate(TriggerContext<S, E> context);

	/**
	 * Adds the trigger listener.
	 *
	 * @param listener the listener
	 */
	void addTriggerListener(TriggerListener listener);

	/**
	 * Gets the event associated with this trigger. It is possible that there
	 * are no event association.
	 *
	 * @return the event
	 */
	E getEvent();

	/**
	 * Arm a trigger. After trigger has been armed a {@link TriggerListener}
	 * may receive events.
	 */
	void arm();

	/**
	 * Disarm a trigger. After trigger has been disarmed a {@link TriggerListener}
	 * will not receive events.
	 */
	void disarm();
}
