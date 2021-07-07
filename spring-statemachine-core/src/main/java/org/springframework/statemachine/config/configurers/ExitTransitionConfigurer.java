/*
 * Copyright 2016 the original author or authors.
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
package org.springframework.statemachine.config.configurers;

import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.common.annotation.AnnotationConfigurerBuilder;
import org.springframework.statemachine.transition.Transition;

/**
 * {@code TransitionConfigurer} interface for configuring {@link Transition}
 * with an exitpoint pseudo state.
 *
 * @author Janne Valkealahti
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface ExitTransitionConfigurer<S, E>
		extends AnnotationConfigurerBuilder<StateMachineTransitionConfigurer<S, E>> {

	/**
	 * Specify a source state {@code S} for this {@link Transition}.
	 *
	 * @param source the source state {@code S}
	 * @return configurer for chaining
	 */
	ExitTransitionConfigurer<S, E> source(S source);

	/**
	 * Specify a target state {@code S} for this {@link Transition}.
	 *
	 * @param target the target state {@code S}
	 * @return configurer for chaining
	 */
	ExitTransitionConfigurer<S, E> target(S target);
}
