package com.siwoo.azurefunction;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * HttpTrigger
 *  FunctionName =>
 *  route => /api/{route}
 *      => {"/somepath/{path}"} path 변수 지원 => @BindingName 으로 맵핑해야 됨.
 *
 *  Function State => 메모리를 사용하지 마라. 운영 중 shutdown 가능 & 여러 인스턴스
 *
 * Azure Table Storage => NoSql
 *  => partition key ?, row key ?
 *  테이블 데이터 맵핑을 위한 엔티티는 TableServiceEntity 을 상속해야 함.
 *
 * TableInput, TableOutput
 *  name => 파라미터 이름.
 *  tableName => 테이블 이름.
 *  partitionKey, rowKey => 테이블의 어느 섹션에서 읽어올지 지.
 *  connection => 커넥션. local.settings.json 에 정의
 *
 *  ==> 안됨 (클래스 디자인 문제인듯) => 예외도 안던짐..
 *
 *  보안.
 *      - HttpRequestMessage 을 이용한 bearer 토큰 인증.
 *      - Function key, Host key 을 이용한 보안 인증.
 *      - Authentication Provider feature (3자 인증) 이용.
 *
*/
public class ToDoFunction {

    private final ResourceManager resourceManager = ResourceManager.INSTANCE;

    @FunctionName("todocreate2")
    public HttpResponseMessage create2(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.POST},
                    route = "todo2/create",
                    authLevel = AuthorizationLevel.FUNCTION)
                    HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) throws JsonProcessingException {
        context.getLogger().info("TODO create.");
        ToDo toDo = resourceManager.getObjectMapper().readValue(request.getBody().orElseThrow(RuntimeException::new), ToDo.class);
        context.getLogger().info(toDo.toString());
        resourceManager.getToDoRepository().save(toDo);
        context.getLogger().info("TODO create." + toDo);
        return request.createResponseBuilder(HttpStatus.CREATED).build();
    }

    @FunctionName("todocreate")
    public HttpResponseMessage create(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.POST},
                    route = "todo/create",
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @TableOutput(name = "todos", tableName = "todo", connection = "AzureWebJobsStorage")
                    OutputBinding<ToDo.AzureToDo> todo,
            final ExecutionContext context) throws JsonProcessingException {
        context.getLogger().info("TODO create.");
        ToDo toDo = resourceManager.getObjectMapper().readValue(request.getBody().orElseThrow(RuntimeException::new), ToDo.class);
        context.getLogger().info(toDo.toString());
        todo.setValue(toDo.toAzure());
//        resourceManager.getToDoRepository().save(toDo);
        context.getLogger().info("TODO create." + todo.getValue());
        return request.createResponseBuilder(HttpStatus.CREATED).build();
    }

    @FunctionName("todogetall")
    public HttpResponseMessage getall(
            @HttpTrigger(name = "req",
                    methods = HttpMethod.GET,
                    route = "todo/get",
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @TableInput(name = "todos", tableName = "todo", connection = "AzureWebJobsStorage") ToDo.AzureToDo[] todos,
            final ExecutionContext context) {
        context.getLogger().info("TODO get all.");
        return request.createResponseBuilder(HttpStatus.CREATED).body(todos).build();
    }

    @FunctionName("todoget")
    public HttpResponseMessage get(
            @HttpTrigger(name = "req",
                    methods = HttpMethod.GET,
                    route = "todo/get/{id}",
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @TableInput(name = "todo", tableName = "todo", partitionKey = "todo", rowKey = "{id}", connection = "AzureWebJobsStorage") ToDo.AzureToDo todo,
            final ExecutionContext context,
            @BindingName("id") String id) {
        context.getLogger().info("TODO get by id.");
        ToDo toDo = resourceManager.getToDoRepository().getById(id);
        return request.createResponseBuilder(HttpStatus.CREATED).body(toDo).build();
    }

// 정
}
