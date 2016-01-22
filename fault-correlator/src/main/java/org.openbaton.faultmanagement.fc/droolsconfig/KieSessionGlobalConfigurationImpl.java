package org.openbaton.faultmanagement.fc.droolsconfig;

import org.kie.api.runtime.KieSession;
import org.openbaton.catalogue.mano.common.monitoring.AlarmState;
import org.openbaton.catalogue.mano.common.monitoring.PerceivedSeverity;
import org.openbaton.catalogue.mano.common.monitoring.VRAlarm;
import org.openbaton.faultmanagement.fc.interfaces.KieSessionGlobalConfiguration;
import org.openbaton.faultmanagement.fc.interfaces.NSRManager;
import org.openbaton.faultmanagement.fc.policymanagement.interfaces.PolicyManager;
import org.openbaton.faultmanagement.fc.repositories.AlarmRepository;
import org.openbaton.faultmanagement.ha.HighAvailabilityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by mob on 21.01.16.
 */
@Service
public class KieSessionGlobalConfigurationImpl implements KieSessionGlobalConfiguration {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private HighAvailabilityManager highAvailabilityManager;

    @Autowired
    private KieSession kieSession;

    @Autowired
    private NSRManager nsrManager;

    @Autowired
    private PolicyManager policyManager;

    private static final Logger log = LoggerFactory.getLogger(KieSessionGlobalConfigurationImpl.class);

    @PostConstruct
    public void init() {

        log.debug("\n................................................................................\n" +
                "................................................................................\n" +
                "................................................................................\n" +
                "................................................................................\n" +
                "................................................................................\n" +
                "................................................................................\n" +
                "................................................................................\n" +
                "................................................................................\n" +
                "................................................................................\n" +
                "................................................................................\n" +
                "................................................................................\n" +
                "........=.............................................................+.........\n" +
                "........$.............................................................$.........\n" +
                "........?.............................................................?.........\n" +
                "........M.............................................................M.........\n" +
                "................................................................................\n" +
                ".......I...............................................................I........\n" +
                "......M.................................................................M.......\n" +
                ".....M...................................................................M......\n" +
                "....M.............................:,........:.............................M.....\n" +
                "...$...............................M.......M...............................Z....\n" +
                "....................................M.....M.....................................\n" +
                "..M..................................M,..M..................................M...\n" +
                ".....................................8...O......................................\n" +
                ".7....................................$.Z....................................7..\n" +
                ".N....................................7.7,...................................N..\n" +
                ".M...........................................................................M..\n" +
                ".Z.......N....+D...............................................,+....N......,Z..\n" +
                ".,......N.~Z....7.............................................7:...$=.M......,..\n" +
                "..O,.....N...I..M.............................................M..I...M.,....O...\n" +
                ".,Z.,...$M...~..D.............................................D..~...MZ.....$...\n" +
                "...M.....M....?D......................M.M......................,?...,M...,.M....\n" +
                "....D................................M...N................................D,....\n" +
                ".....Z..............................O.....O.............................,Z.,,...\n" +
                "...,..M............................N.......M............................M..,,...\n" +
                "...,...+:........................~~.........=~.,......................:+...,,...\n" +
                ".........~$....................O:.............:M....................Z~......,...\n" +
                "..,,........?N.............~M.....................M~.............N?.........,,..\n" +
                "..,,..........,.=DMDO8NM8~...........................~8MN8ODMD=.............,,..\n" +
                ".,,..........................................................................,,.\n" +
                ".,,..........................................................................,,,\n" +
                ".,...........................................................................,,,\n" +
                ",,............................................................................,,\n" +
                ",,..................................ESCILE....................................,,\n" +
                ",.............................................................................,,\n" +
                ",..............................................................................,\n" +
                ",..............................................................................,\n" +
                "................................................................................\n" +
                "................................................................................\n" +
                "................................................................................\n" +
                "................................................................................");

        VRAlarm vrAlarm = new VRAlarm();
        vrAlarm.setPerceivedSeverity(PerceivedSeverity.CRITICAL);
        vrAlarm.setManagedObject("blabla");
        vrAlarm.setAlarmState(AlarmState.FIRED);


        kieSession.setGlobal("policyManager", policyManager);
        kieSession.setGlobal("logger", log);
        kieSession.setGlobal("alarmRepository", alarmRepository);
        kieSession.setGlobal("highAvailabilityManager", highAvailabilityManager);
        kieSession.setGlobal("nsrManager", nsrManager);

        //kieSession.insert(vrAlarm);
        //kieSession.fireAllRules();
    }
}