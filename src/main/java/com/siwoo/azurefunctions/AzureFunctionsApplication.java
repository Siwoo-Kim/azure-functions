package com.siwoo.azurefunctions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

@SpringBootApplication
public class AzureFunctionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AzureFunctionsApplication.class, args);
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    @RestController
    public static class MainController {

        @Value("${azure.functions.host}")
        private URI uri;

        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        private ObjectMapper om;

        @GetMapping("/test")
        public void test() throws JsonProcessingException {
            String body = restTemplate.getForObject(uri.resolve("/api/todo/get"), String.class);
            List<ToDo> toDos =  om.readValue(body, new TypeReference<List<ToDo>>() {});
            ToDo updateTodo = null;
            for (ToDo toDo: toDos) {
                if (updateTodo == null)
                    updateTodo = toDo;
                URI reqUri = uri.resolve(String.format("/api/todo/get/%s", toDo.getId()));
                body = restTemplate.getForObject(reqUri, String.class);
                ToDo reqToDo = om.readValue(body, ToDo.class);
                System.out.println(reqToDo);
            }
            if (updateTodo != null) {
                URI reqUri = uri.resolve(String.format("/api/todo/update/%s", updateTodo.getId()));
                updateTodo.setDescription("req task");
                restTemplate.put(reqUri, updateTodo);
            }
        }
    }

    @Getter @Setter @ToString
    private static class ToDo {
        String id;
        String description;
    }



}
