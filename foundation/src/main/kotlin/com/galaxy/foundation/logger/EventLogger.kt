package com.galaxy.foundation.logger

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Component
import java.util.*

@Component
class EventLogger {

    private val LOGGER: Logger = LogManager.getLogger(EventLogger::class.qualifiedName)

    val gson: Gson = GsonBuilder().setExclusionStrategies(BlackListLogExclusion()  ).create()

    fun log(level: Level, message:String?, eventType: EventType, vararg args: Any?){
        val logmap: MutableMap<String, Any> = HashMap()

        if(message !=null) logmap["message"] = message
        if(message !=null) logmap["event"] = eventType.toString()

        for (obj in args){
            logmap["" + obj?.javaClass?.name] = gson.toJson(obj)
        }
        LOGGER.log(level,gson?.toJson(logmap)?.toString()?:"")
    }
    fun log( eventType: EventType, message:String?, vararg args: Any?) {
        log(Level.INFO,message,eventType,*args)
    }
    fun logMessage(logEntry: LogEntry ){
       log(logEntry.level, logEntry.message,logEntry.eventType, *logEntry.params )
    }
}
