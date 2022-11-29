package com.galaxy.foundation.logger

import org.apache.logging.log4j.Level
import org.junit.jupiter.api.Test


class EventLoggerTest {
    val eventLogger = EventLogger()
    @Test
    fun testLogger(){
        val test1 =SampleObject("test","test","4121212112");
        val test2= "Stringtest"

        eventLogger.log(Level.INFO,"test", TestType.TEST_SAVE, test1,test2  );

        var logEntry= LogEntry(TestType.TEST_SAVE, Level.INFO,"Success " , test1,test2 )

        eventLogger.logMessage(logEntry);
    }

}
class SampleObject(val eventType: String,val level: String,val cardNumber:String)
enum class TestType: EventType{
    TEST_SAVE ,
    TEST_UPDATE

}