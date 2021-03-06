/*
 * Copyright 2015 Kejun Xia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shipdream.lib.android.mvc.samples.simple.controller.internal;

import com.shipdream.lib.android.mvc.MvcGraph;
import com.shipdream.lib.android.mvc.controller.NavigationController;
import com.shipdream.lib.android.mvc.event.bus.EventBus;
import com.shipdream.lib.android.mvc.event.bus.internal.EventBusImpl;
import com.shipdream.lib.android.mvc.samples.simple.controller.CounterController;
import com.shipdream.lib.android.mvc.samples.simple.model.CounterModel;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Random;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestCounterController {
    @BeforeClass
    public static void beforeClass() {
        ConsoleAppender console = new ConsoleAppender(); //create appender
        //configure the appender
        String PATTERN = "%d [%p] %C{1}: %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(Level.DEBUG);
        console.activateOptions();
        //add appender to any Logger (here is root)
        Logger.getRootLogger().addAppender(console);
    }

    //Dependencies of base controllers
    protected EventBus eventBusC2C;
    protected EventBus eventBusC2V;
    protected ExecutorService executorService;
    //The graph used to inject
    private MvcGraph mvcGraph;

    private CounterController counterController;

    @Before
    public void setUp() throws Exception {
        //create instance of CounterController
        counterController = new CounterControllerImpl();

        //Prepare dependencies for injection
        eventBusC2C = new EventBusImpl();
        eventBusC2V = new EventBusImpl();
        mvcGraph = new MvcGraph(new MvcGraph.BaseDependencies() {
            @Override
            public EventBus createEventBusC2C() {
                return eventBusC2C;
            }

            @Override
            public EventBus createEventBusC2V() {
                return eventBusC2V;
            }

            @Override
            public ExecutorService createExecutorService() {
                return executorService;
            }
        });
        executorService = mock(ExecutorService.class);

        //inject dependencies into controller
        mvcGraph.inject(counterController);

        //init controller
        counterController.init();
    }

    @Test
    public void increment_should_post_counter_update_event_with_incremented_value() {
        //1. Prepare
        //prepare event monitor
        class Monitor {
            void onEvent(CounterController.EventC2V.OnCounterUpdated event) {
            }
        }
        Monitor monitor = mock(Monitor.class);
        eventBusC2V.register(monitor);

        //mock controller model for count value
        int value = new Random().nextInt();
        CounterModel counterModel = mock(CounterModel.class);
        when(counterModel.getCount()).thenReturn(value);
        //Bind the mock model to the controller
        counterController.bindModel(this, counterModel);

        //2. Act
        counterController.increment(this);

        //3. Verify
        ArgumentCaptor<CounterController.EventC2V.OnCounterUpdated> updateEvent
                = ArgumentCaptor.forClass(CounterController.EventC2V.OnCounterUpdated.class);
        //event should be fired once
        verify(monitor, times(1)).onEvent(updateEvent.capture());
        //event should carry incremented value
        Assert.assertEquals(value + 1, updateEvent.getValue().getCount());
    }

    @Test
    public void should_navigate_to_locationB_when_go_to_advance_view_and_back_to_locationA_after_go_to_basic_view() {
        //Prepare
        NavigationController navigationController = ((CounterControllerImpl) counterController).navigationController;
        NavigationController.Model navModel = navigationController.getModel();
        //App has not navigated to anywhere, current location should be null
        Assert.assertNull(navModel.getCurrentLocation());
        //Simulate navigating to location A
        navigationController.navigateTo(this, "LocationA");
        //Verify: location should be changed to LocationA
        Assert.assertEquals(navModel.getCurrentLocation().getLocationId(), "LocationA");

        //Act: CounterController now goes to advanced view underlining logic is navigating to locationB
        counterController.goToAdvancedView(this);

        //Verify: Current location should be LocationB
        Assert.assertEquals(navModel.getCurrentLocation().getLocationId(), "LocationB");

        //Act: CounterController now goes back to basic view underlining logic is navigating back to locationA
        counterController.goBackToBasicView(this);

        //Verify: Current location should be back to LocationA
        Assert.assertEquals(navModel.getCurrentLocation().getLocationId(), "LocationA");
    }

}
