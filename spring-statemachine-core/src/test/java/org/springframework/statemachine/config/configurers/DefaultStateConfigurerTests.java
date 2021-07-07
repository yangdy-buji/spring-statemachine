/*
 * Copyright 2015-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.statemachine.config.configurers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.statemachine.AbstractStateMachineTests.TestEntryAction;
import org.springframework.statemachine.AbstractStateMachineTests.TestEvents;
import org.springframework.statemachine.AbstractStateMachineTests.TestExitAction;
import org.springframework.statemachine.AbstractStateMachineTests.TestStateAction;
import org.springframework.statemachine.AbstractStateMachineTests.TestStates;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.builders.StateMachineStateBuilder;
import org.springframework.statemachine.config.model.StateData;
import org.springframework.statemachine.state.PseudoStateKind;

import reactor.core.publisher.Mono;

public class DefaultStateConfigurerTests {

	@Test
	public void testInitialWithoutState() throws Exception {
		DefaultStateConfigurer<TestStates, TestEvents> configurer = new DefaultStateConfigurer<TestStates, TestEvents>();
		TestStateMachineStateBuilder builder = new TestStateMachineStateBuilder();
		configurer.initial(TestStates.SI);
		configurer.configure(builder);
		assertThat(builder.data).isNotNull();
		assertThat(builder.data).hasSize(1);
		assertThat(builder.data.iterator().next().getState()).isEqualTo(TestStates.SI);
	}

	@Test
	public void testInitialWithState() throws Exception {
		DefaultStateConfigurer<TestStates, TestEvents> configurer = new DefaultStateConfigurer<TestStates, TestEvents>();
		TestStateMachineStateBuilder builder = new TestStateMachineStateBuilder();
		configurer.initial(TestStates.SI);
		configurer.state(TestStates.SI);
		configurer.configure(builder);
		assertThat(builder.data).isNotNull();
		assertThat(builder.data).hasSize(1);
		assertThat(builder.data.iterator().next().getState()).isEqualTo(TestStates.SI);
	}

	@Test
	public void testSameStateShouldResultOneState() throws Exception {
		DefaultStateConfigurer<TestStates, TestEvents> configurer = new DefaultStateConfigurer<TestStates, TestEvents>();
		TestStateMachineStateBuilder builder = new TestStateMachineStateBuilder();
		configurer.state(TestStates.SI);
		configurer.state(TestStates.SI);
		configurer.configure(builder);
		assertThat(builder.data).isNotNull();
		assertThat(builder.data).hasSize(1);
		assertThat(builder.data.iterator().next().getState()).isEqualTo(TestStates.SI);
	}

	@Test
	public void testParentSet() throws Exception {
		DefaultStateConfigurer<TestStates, TestEvents> configurer = new DefaultStateConfigurer<TestStates, TestEvents>();
		TestStateMachineStateBuilder builder = new TestStateMachineStateBuilder();
		configurer.parent(TestStates.SI);
		configurer.state(TestStates.S1);
		configurer.configure(builder);
		assertThat(builder.data).isNotNull();
		assertThat(builder.data).hasSize(1);
		assertThat(builder.data.iterator().next().getState()).isEqualTo(TestStates.S1);
		assertThat((TestStates)builder.data.iterator().next().getParent()).isEqualTo(TestStates.SI);
	}

	@Test
	public void testActionsInitialFirst() throws Exception {
		Collection<Action<TestStates, TestEvents>> exitActions = Arrays.asList(testExitAction());

		DefaultStateConfigurer<TestStates, TestEvents> configurer = new DefaultStateConfigurer<TestStates, TestEvents>();
		TestStateMachineStateBuilder builder = new TestStateMachineStateBuilder();
		configurer.initial(TestStates.S1);
		configurer.state(TestStates.S1, null, exitActions);
		configurer.configure(builder);
		assertThat(builder.data).isNotNull();
		assertThat(builder.data).hasSize(1);

		assertThat(builder.data.iterator().next().getState()).isEqualTo(TestStates.S1);
		assertThat(builder.data.iterator().next().getEntryActions()).isNull();
		assertThat(builder.data.iterator().next().getStateActions()).isNull();
		assertThat(builder.data.iterator().next().getExitActions()).isNotNull();
	}

	@Test
	public void testActionsJustState() throws Exception {
		Collection<Action<TestStates, TestEvents>> entryActions = Arrays.asList(testEntryAction());

		DefaultStateConfigurer<TestStates, TestEvents> configurer = new DefaultStateConfigurer<TestStates, TestEvents>();
		TestStateMachineStateBuilder builder = new TestStateMachineStateBuilder();
		configurer.state(TestStates.S2, entryActions, null);
		configurer.configure(builder);
		assertThat(builder.data).isNotNull();
		assertThat(builder.data).hasSize(1);

		assertThat(builder.data.iterator().next().getState()).isEqualTo(TestStates.S2);
		assertThat(builder.data.iterator().next().getExitActions()).isNull();
		assertThat(builder.data.iterator().next().getStateActions()).isNull();
		assertThat(builder.data.iterator().next().getEntryActions()).isNotNull();
	}

	@Test
	public void testStateActions() throws Exception {
		Collection<Action<TestStates, TestEvents>> stateActions = Arrays.asList(testStateAction());

		DefaultStateConfigurer<TestStates, TestEvents> configurer = new DefaultStateConfigurer<TestStates, TestEvents>();
		TestStateMachineStateBuilder builder = new TestStateMachineStateBuilder();
		configurer.state(TestStates.S2, stateActions);
		configurer.configure(builder);
		assertThat(builder.data).isNotNull();
		assertThat(builder.data).hasSize(1);

		assertThat(builder.data.iterator().next().getState()).isEqualTo(TestStates.S2);
		assertThat(builder.data.iterator().next().getExitActions()).isNull();
		assertThat(builder.data.iterator().next().getStateActions()).isNotNull();
		assertThat(builder.data.iterator().next().getEntryActions()).isNull();
	}

	@Test
	public void testStateActionFunctions() throws Exception {
		DefaultStateConfigurer<TestStates, TestEvents> configurer = new DefaultStateConfigurer<TestStates, TestEvents>();
		TestStateMachineStateBuilder builder = new TestStateMachineStateBuilder();
		configurer.stateDoFunction(TestStates.S2, context -> Mono.empty());
		configurer.stateEntryFunction(TestStates.S2, context -> Mono.empty());
		configurer.stateExitFunction(TestStates.S2, context -> Mono.empty());
		configurer.configure(builder);
		assertThat(builder.data).isNotNull();
		assertThat(builder.data).hasSize(1);

		assertThat(builder.data.iterator().next().getState()).isEqualTo(TestStates.S2);
		assertThat(builder.data.iterator().next().getExitActions()).isNotNull();
		assertThat(builder.data.iterator().next().getExitActions()).hasSize(1);
		assertThat(builder.data.iterator().next().getStateActions()).isNotNull();
		assertThat(builder.data.iterator().next().getStateActions()).hasSize(1);
		assertThat(builder.data.iterator().next().getEntryActions()).isNotNull();
		assertThat(builder.data.iterator().next().getEntryActions()).hasSize(1);
	}

	@Test
	public void testEndStateNoState() throws Exception {
		DefaultStateConfigurer<TestStates, TestEvents> configurer = new DefaultStateConfigurer<TestStates, TestEvents>();
		TestStateMachineStateBuilder builder = new TestStateMachineStateBuilder();
		configurer.end(TestStates.SF);
		configurer.configure(builder);
		assertThat(builder.data).isNotNull();
		assertThat(builder.data).hasSize(1);
		assertThat(builder.data.iterator().next().getState()).isEqualTo(TestStates.SF);
	}

	@Test
	public void testEndStateAsState() throws Exception {
		DefaultStateConfigurer<TestStates, TestEvents> configurer = new DefaultStateConfigurer<TestStates, TestEvents>();
		TestStateMachineStateBuilder builder = new TestStateMachineStateBuilder();
		configurer.state(TestStates.SF);
		configurer.end(TestStates.SF);
		configurer.configure(builder);
		assertThat(builder.data).isNotNull();
		assertThat(builder.data).hasSize(1);
		assertThat(builder.data.iterator().next().getState()).isEqualTo(TestStates.SF);
	}

	@Test
	public void testChoiceStateNoState() throws Exception {
		DefaultStateConfigurer<TestStates, TestEvents> configurer = new DefaultStateConfigurer<TestStates, TestEvents>();
		TestStateMachineStateBuilder builder = new TestStateMachineStateBuilder();
		configurer.choice(TestStates.S1);
		configurer.configure(builder);
		assertThat(builder.data).isNotNull();
		assertThat(builder.data).hasSize(1);
		assertThat(builder.data.iterator().next().getState()).isEqualTo(TestStates.S1);
		assertThat(builder.data.iterator().next().getPseudoStateKind()).isEqualTo(PseudoStateKind.CHOICE);
	}

	private static class TestStateMachineStateBuilder extends StateMachineStateBuilder<TestStates, TestEvents> {

		Collection<StateData<TestStates, TestEvents>> data;

		@Override
		public void addStateData(Collection<StateData<TestStates, TestEvents>> stateDatas) {
			this.data = stateDatas;
		}
	}

	private Action<TestStates, TestEvents> testEntryAction() {
		return new TestEntryAction();
	}

	private Action<TestStates, TestEvents> testExitAction() {
		return new TestExitAction();
	}

	private Action<TestStates, TestEvents> testStateAction() {
		return new TestStateAction();
	}
}
