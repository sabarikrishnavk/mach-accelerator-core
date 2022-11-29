package com.galaxy.foundation.logger

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import org.apache.logging.log4j.Level
import java.util.regex.Pattern

interface EventType




var BlaskListProperties = "(.*address.*|.*cardnumber.*|password|cvv|cvc|pin|pinnumber|.*name*.)"


class LogEntry(val eventType: EventType, val level: Level, val message:String?, vararg val params:Any? )



class BlackListLogExclusion : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false;
    }
    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        return Pattern.compile(BlaskListProperties).matcher(f?.name?.toLowerCase()).matches();
    }
}