/*
 * Copyright 2015 the original author or authors.
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
package org.springframework.statemachine.ensemble;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.support.LifecycleObjectSupport;

/**
 * Support class for implementing {@link StateMachineEnsemble}s.
 *
 * @author Janne Valkealahti
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public abstract class StateMachineEnsembleObjectSupport<S, E> extends LifecycleObjectSupport implements StateMachineEnsemble<S, E> {

	private final static Log log = LogFactory.getLog(StateMachineEnsembleObjectSupport.class);

	private final CompositeEnsembleListener<S, E> ensembleListener = new CompositeEnsembleListener<S, E>();

	@Override
	public abstract void join(StateMachine<S, E> stateMachine);

	@Override
	public abstract void leave(StateMachine<S, E> stateMachine);

	@Override
	public void addEnsembleListener(EnsembleListener<S, E> listener) {
		ensembleListener.register(listener);
	}

	@Override
	public void removeEnsembleListener(EnsembleListener<S, E> listener) {
		ensembleListener.unregister(listener);
	}

	protected void notifyJoined(StateMachine<S, E> stateMachine, StateMachineContext<S, E> context) {
		ensembleListener.stateMachineJoined(stateMachine, context);
	}

	protected void notifyLeft(StateMachine<S, E> stateMachine, StateMachineContext<S, E> context) {
		ensembleListener.stateMachineLeft(stateMachine, context);
	}

	protected void notifyError(StateMachineEnsembleException exception) {
		ensembleListener.ensembleError(exception);
	}

	protected void notifyGranted(StateMachine<S, E> stateMachine) {
		ensembleListener.ensembleLeaderGranted(stateMachine);
	}

	protected void notifyRevoked(StateMachine<S, E> stateMachine) {
		ensembleListener.ensembleLeaderRevoked(stateMachine);
	}

	protected void notifyStateChanged(StateMachineContext<S, E> context) {
		if (log.isTraceEnabled()) {
			log.trace("Notify notifyStateChanged " + context);
		}
		ensembleListener.stateChanged(context);
	}
}
