package com.log.level.log;

import com.newrelic.api.agent.Insights;
import com.newrelic.api.agent.NewRelic;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class NewRelicUtils {

    private static final String CUSTOM_EVENT = "EVENT_TEST_LOG";

    public void addCustomEvent(Map<String, String> info) {

        Insights newRelicAgent = NewRelic.getAgent().getInsights();
        newRelicAgent.recordCustomEvent(CUSTOM_EVENT, info);
    }

}
