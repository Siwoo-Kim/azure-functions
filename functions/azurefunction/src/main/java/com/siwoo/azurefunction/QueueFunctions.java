package com.siwoo.azurefunction;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

/**
 * QueueTrigger
 *  output -> OutputBinding
 *  input -> QueueTrigger
 */
public class QueueFunctions {

    @FunctionName("QueueFunctions")
    public HttpResponseMessage ping(
            @HttpTrigger(name = "req",
                    route = "queue/ping",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @QueueOutput(name = "message", queueName = "myq", connection = "AzureWebJobsStorage")
                    OutputBinding<String> message,
            final ExecutionContext context) {
        message.setValue("ping");
        context.getLogger().info("Send: " + message.getValue());
        return request.createResponseBuilder(HttpStatus.OK).body("ok").build();
    }

    @FunctionName("QueueProcessor")
    public void run(@QueueTrigger(name = "message", queueName = "myq", connection = "AzureWebJobsStorage") String message,
                    final ExecutionContext context) {
        context.getLogger().info(message);
    }
}
