package com.company;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private final String name;
    private final FieldType fieldType;
    private final boolean varLength;
    private final List<Field> children;

    public Field(String name, FieldType fieldType, boolean varLength) {
        this.name = name;
        this.fieldType = fieldType;
        this.varLength = varLength;
        children = new ArrayList<>();
    }

    public Field(String name, FieldType fieldType, boolean varLength, List<Field> children) {
        this.name = name;
        this.fieldType = fieldType;
        this.varLength = varLength;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public boolean isVarLength() {
        return varLength;
    }

    public List<Field> getChildren() {
        return children;
    }

    public void addChildren(List<Field> fields) {
        this.children.addAll(fields);
    }
}
