package com.siwoo.azurefunction;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Function 컴포넌트.
 *  1. Function Name
 *  2. Trigger
 *      HttpTrigger
 *          구성요소 -> auth level, http methods, route
 *          사용처 -> webhook, rest api
 *
 *  3. Logger (ExecutionContext)
 *
 * 실행 -
 *  mvn clean package
 *  mvn azure-functions:run
 *
 *  azure-functions-core-tools 3.0 -> 자바 지원 안함.
 *
 * local.settings.json (로컬 환경) = app settings (배포 환경)
 *  배포 -> 매뉴얼 디플로이, 깃, zip (with cli)
 */
public class ServerlessFunction1 {

    /**
     * This function listens at endpoint "/api/ServerlessFunction1". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/ServerlessFunction1
     * 2. curl {your host}/api/ServerlessFunction1?name=HTTP%20Query
     */
    @FunctionName("ServerlessFunction1")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.GET, HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "")
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String query = request.getQueryParameters().get("name");
        String name = request.getBody().orElse(query);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }
}
