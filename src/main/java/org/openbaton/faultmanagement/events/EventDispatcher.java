/*
 * Copyright (c) 2015 Fraunhofer FOKUS
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

package org.openbaton.faultmanagement.events;


import org.openbaton.catalogue.mano.common.faultmanagement.Alarm;
import org.openbaton.catalogue.mano.common.faultmanagement.PerceivedSeverity;
import org.openbaton.faultmanagement.events.interfaces.AlarmDispatcher;
import org.openbaton.faultmanagement.events.senders.interfaces.EventSender;
import org.openbaton.faultmanagement.model.AlarmEndpoint;
import org.openbaton.faultmanagement.repositories.AlarmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by mob on 28/10/15.
 */

/**
 * This class implements the interface {@Link EventDispatcher} so is in charge
 * of handling the de/registration of a EventEndpoint.
 * <p/>
 * Moreover receives also internal events and dispatches them to the external applications.
 */
@Service
@Scope
@EnableJms
public class EventDispatcher implements ApplicationListener<AbstractVNFAlarm>, AlarmDispatcher{

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private SubscriptionRegister subscriptionRegister;


    @Override
    public void onApplicationEvent(AbstractVNFAlarm event) {
        log.debug("Received event: " + event);
        dispatchAlarm(event);
    }

    @Override
    public void dispatchAlarm(AbstractVNFAlarm abstractAlarm) {
        log.debug("dispatching Alarm to the world!!!");
        log.debug("event is: " + abstractAlarm);

        Iterable<AlarmEndpoint> alarmEndpoints = subscriptionRegister.getSubscriptionRepository().findAll();

        for (AlarmEndpoint alarmEndpoint : alarmEndpoints) {
            log.debug("Checking endpoint: " + alarmEndpoint);
            if(abstractAlarm.getAlarm().getVnfd().getName().equalsIgnoreCase(alarmEndpoint.getVirtualNetworkFunctionId()) &&
                    abstractAlarm.getAlarm().getPerceivedSeverity().ordinal()>= alarmEndpoint.getPerceivedSeverity().ordinal()){
                    log.debug("Dispatching event to endpoint: "+alarmEndpoint.getName());
                    notify(alarmEndpoint, abstractAlarm);
            }
        }

    }

    public void notify(AlarmEndpoint endpoint, AbstractVNFAlarm event) {
        EventSender sender = (EventSender) context.getBean(endpoint.getType().toString().toLowerCase() + "EventSender");
        log.trace("Sender is: " + sender.getClass().getSimpleName());
        try {
            sender.send(endpoint, event);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error while dispatching event " + event);
        }
    }

    public List<Alarm> getAlarmList(String vnfId, PerceivedSeverity perceivedSeverity) {
        Iterable<Alarm> alarms = alarmRepository.findAll();
        for (Alarm a : alarms){
            //if(a.getVnfd().getName().equals(vnfId) && a.getPerceivedSeverity().ordinal()>=perceivedSeverity.ordinal())
                //TODO
        }
        return (List)alarms;
    }

}
