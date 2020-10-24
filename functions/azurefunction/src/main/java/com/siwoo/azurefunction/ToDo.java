package com.siwoo.azurefunction;

import com.microsoft.azure.storage.table.TableServiceEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Delegate;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Getter @Setter
public class ToDo {

    private final String id = UUID.randomUUID().toString();
    private final LocalDateTime createdTime = LocalDateTime.now();
    private String description;
    private boolean completion;

    public AzureToDo toAzure() {
        return new AzureToDo(this) {
            @Override
            public String getPartitionKey() {
                return "todo";
            }
            @Override
            public String getRowKey() {
                return this.getId();
            }
        };
    }

    @ToString
    public static class AzureToDo extends TableServiceEntity {  //진짜 별로다.. 인터페이스도 아니고
        @Delegate(types = ToDo.class)
        private ToDo toDo;

        public AzureToDo(ToDo toDo) {
            this.toDo = toDo;
        }
    }
}
