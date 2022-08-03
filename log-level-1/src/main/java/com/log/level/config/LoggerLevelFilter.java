package com.log.level.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.Setter;

@Setter
public class LoggerLevelFilter extends Filter<ILoggingEvent> {

    private Level level;
    private boolean launcher;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return launcher ? FilterReply.NEUTRAL : FilterReply.DENY;
        }

        return event.getLevel().equals(level) ?
            FilterReply.NEUTRAL :
            FilterReply.DENY;

    }

    public void start() {
        if (this.level != null) {
            super.start();
        }
    }

}
