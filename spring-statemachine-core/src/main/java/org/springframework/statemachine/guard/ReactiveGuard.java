/*
 * Copyright 2019 the original author or authors.
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
package org.springframework.statemachine.guard;

import java.util.function.Function;

import org.springframework.statemachine.StateContext;

import reactor.core.publisher.Mono;

/**
 * Reactive counterpart of a {@link Guard} being simply a {@link Function} of a
 * return type of a {@link Mono} wrapping {@link Boolean}.
 *
 * @author Janne Valkealahti
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface ReactiveGuard<S, E> extends Function<StateContext<S, E>, Mono<Boolean>> {
}
