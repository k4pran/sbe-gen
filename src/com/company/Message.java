package com.company;

import java.util.List;

public class Message {

    private final String name;
    private final List<Field> fields;
    private final MessageContext messageContext;
    private boolean containsGroup;

    public Message(String name, List<Field> fields, MessageContext messageContext) {
        this.name = name;
        this.fields = fields;
        this.messageContext = messageContext;
        setContainsGroup();
    }

    public boolean hasInterface() {
        return messageContext.getInterfaces().size() > 0;
    }

    public boolean hasBase() {
        return messageContext.getBase() != null;
    }


    public String getName() {
        return name;
    }

    public List<Field> getFields() {
        return fields;
    }

    public MessageContext getMessageContext() {
        return messageContext;
    }

    public boolean containsGroup() {
        return containsGroup;
    }

    public void setContainsGroup() {
        for (Field field : fields) {
            if (field.getFieldType() == FieldType.GROUP) {
                containsGroup = true;
                break;
            }
        }
    }
}
