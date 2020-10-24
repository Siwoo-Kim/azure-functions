package com.siwoo.azurefunction;

import java.nio.charset.StandardCharsets;
import java.time.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * TimerTrigger
 *  schedule = 크론 표현식으로 시간 주기 지정.
 */
public class TimerFunction {
    /**
     * This function will be invoked periodically according to the specified schedule.
     */
    @FunctionName("TimerFunction")
    public void run(
        @TimerTrigger(name = "timerInfo", schedule = "0 */1 * * * *") String timerInfo,
        @BlobInput(name = "test", dataType = "binary", path = "webcontent/root=001/2015-01/test111.txt", connection = "AzureWebJobsStorage") byte[] bytes,
        final ExecutionContext context
    ) {
        context.getLogger().info(timerInfo + ": " + new String(bytes, StandardCharsets.UTF_8));
    }
}
