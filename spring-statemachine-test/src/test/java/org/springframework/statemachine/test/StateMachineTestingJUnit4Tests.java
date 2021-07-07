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
package org.springframework.statemachine.test;

import static org.hamcrest.CoreMatchers.not;
import static org.springframework.statemachine.TestUtils.resolveMachine;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

public class StateMachineTestingJUnit4Tests extends AbstractStateMachineJUnit4Tests {

	@Test
	public void testSimpleTestingConcept() throws Exception {
		registerAndRefresh(Config1.class);
		StateMachine<String, String> machine =	resolveMachine(context);

		StateMachineTestPlan<String, String> plan =
				StateMachineTestPlanBuilder.<String, String>builder()
					.stateMachine(machine)
					.step().expectState("SI").and()
					.step().sendEvent("E1").expectStateChanged(1).expectState("S1").and()
					.step().sendEvent("E2").expectStateChanged(1).expectState("S2").and()
					.build();

		plan.test();
	}

	@Test
	public void testStarted() throws Exception {
		registerAndRefresh(Config2.class);
		StateMachine<String, String> machine =	resolveMachine(context);

		StateMachineTestPlan<String, String> plan =
				StateMachineTestPlanBuilder.<String, String>builder()
					.stateMachine(machine)
					.step().expectStateMachineStarted(1).and()
					.step().expectState("SI").and()
					.step().sendEvent("E1").expectStateChanged(1).expectState("S1").and()
					.step().sendEvent("E2").expectStateChanged(1).expectState("S2").and()
					.build();

		plan.test();
	}

	@Test
	public void testMultipleEvents() throws Exception {
		registerAndRefresh(Config3.class);
		StateMachine<String, String> machine =	resolveMachine(context);

		StateMachineTestPlan<String, String> plan =
				StateMachineTestPlanBuilder.<String, String>builder()
					.stateMachine(machine)
					.step().expectStateMachineStarted(1).and()
					.step().expectState("READY").and()
					.step()
						.sendEvent("DEPLOY")
						.sendEvent("DEPLOY")
						.expectStateChanged(6)
						.expectState("READY")
						.and()
					.build();

		plan.test();
	}

	@Test
	public void testIntermediate() throws Exception {
		registerAndRefresh(Config4.class);
		StateMachine<String, String> machine =	resolveMachine(context);

		StateMachineTestPlan<String, String> plan =
				StateMachineTestPlanBuilder.<String, String>builder()
					.stateMachine(machine)
					.step().expectStateMachineStarted(1).and()
					.step().expectState("SI").and()
					.step().sendEvent("E1").expectStateChanged(1).expectState("S1").and()
					.step().sendEvent("E2").expectStateChanged(2).expectStateEntered("S2", "S3").expectStateExited("S1", "S2").expectState("S3").and()
					.build();

		plan.test();
	}

	@Test
	public void testVariables() throws Exception {
		registerAndRefresh(Config5.class);
		StateMachine<String, String> machine =	resolveMachine(context);

		StateMachineTestPlan<String, String> plan =
				StateMachineTestPlanBuilder.<String, String>builder()
					.stateMachine(machine)
					.step().expectStateMachineStarted(1).and()
					.step().expectState("SI").and()
					.step()
						.sendEvent("E1")
						.expectStateChanged(1)
						.expectState("S1")
						.expectVariable("V1Key")
						.expectVariable("V1Key", "V1Value")
						.expectVariableWith(IsMapContaining.hasKey("V1Key"))
						.expectVariableWith(IsMapContaining.hasValue("V1Value"))
						.expectVariableWith(IsMapContaining.hasEntry("V1Key", "V1Value"))
						.expectVariableWith(not(IsMapContaining.hasKey("V2Key")))
						.and()
					.step()
						.sendEvent("E2")
						.expectStateChanged(1)
						.expectState("S2")
						.expectVariable("V1Key")
						.expectVariable("V1Key", "V1Value")
						.expectVariable("V2Key")
						.expectVariable("V2Key", "V2Value")
						.expectVariableWith(IsMapContaining.hasKey("V1Key"))
						.expectVariableWith(IsMapContaining.hasValue("V1Value"))
						.expectVariableWith(IsMapContaining.hasEntry("V1Key", "V1Value"))
						.expectVariableWith(IsMapContaining.hasKey("V2Key"))
						.expectVariableWith(IsMapContaining.hasValue("V2Value"))
						.expectVariableWith(IsMapContaining.hasEntry("V2Key", "V2Value"))
						.and()
					.build();

		plan.test();
	}

	@Override
	protected AnnotationConfigApplicationContext buildContext() {
		return new AnnotationConfigApplicationContext();
	}

	@Configuration
	@EnableStateMachine
	static class Config1 extends StateMachineConfigurerAdapter<String, String> {

		@Override
		public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
			states
				.withStates()
					.initial("SI")
					.state("S1")
					.state("S2");
		}

		@Override
		public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
			transitions
				.withExternal()
					.source("SI")
					.target("S1")
					.event("E1")
					.and()
				.withExternal()
					.source("S1")
					.target("S2")
					.event("E2");
		}

	}

	@Configuration
	@EnableStateMachine
	static class Config2 extends StateMachineConfigurerAdapter<String, String> {

		@Override
		public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
			states
				.withStates()
					.initial("SI")
					.state("S1")
					.state("S2");
		}

		@Override
		public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
			transitions
				.withExternal()
					.source("SI")
					.target("S1")
					.event("E1")
					.and()
				.withExternal()
					.source("S1")
					.target("S2")
					.event("E2");
		}

		@Bean
		public TaskExecutor taskExecutor() {
			ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
			taskExecutor.setCorePoolSize(1);
			return taskExecutor;
		}

	}

	@Configuration
	@EnableStateMachine
	static class Config3 extends StateMachineConfigurerAdapter<String, String> {

		@Override
		public void configure(StateMachineStateConfigurer<String, String> states)
				throws Exception {
			states
				.withStates()
					.initial("READY")
					.state("DEPLOYPREPARE", "DEPLOY")
					.state("DEPLOYEXECUTE", "DEPLOY");
		}

		@Override
		public void configure(StateMachineTransitionConfigurer<String, String> transitions)
				throws Exception {
			transitions
				.withExternal()
					.source("READY").target("DEPLOYPREPARE")
					.event("DEPLOY")
					.and()
				.withExternal()
					.source("DEPLOYPREPARE").target("DEPLOYEXECUTE")
					.and()
				.withExternal()
					.source("DEPLOYEXECUTE").target("READY");
		}
	}

	@Configuration
	@EnableStateMachine
	static class Config4 extends StateMachineConfigurerAdapter<String, String> {

		@Override
		public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
			states
				.withStates()
					.initial("SI")
					.state("S1")
					.state("S2")
					.state("S3");
		}

		@Override
		public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
			transitions
				.withExternal()
					.source("SI")
					.target("S1")
					.event("E1")
					.and()
				.withExternal()
					.source("S1")
					.target("S2")
					.event("E2")
					.and()
				.withExternal()
					.source("S2")
					.target("S3");
		}

	}

	@Configuration
	@EnableStateMachine
	static class Config5 extends StateMachineConfigurerAdapter<String, String> {

		@Override
		public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
			states
				.withStates()
					.initial("SI")
					.state("S1")
					.state("S2");
		}

		@Override
		public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
			transitions
				.withExternal()
					.source("SI")
					.target("S1")
					.event("E1")
					.action(c -> {
						c.getExtendedState().getVariables().put("V1Key", "V1Value");
					})
					.and()
				.withExternal()
					.source("S1")
					.target("S2")
					.event("E2")
					.action(c -> {
						c.getExtendedState().getVariables().put("V2Key", "V2Value");
					});
		}
	}
}
