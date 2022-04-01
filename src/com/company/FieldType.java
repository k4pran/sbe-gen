package com.company;

public enum FieldType {

    INT("int"),
    LONG("long"),
    BOOL("boolean"),
    STRING("String"),
    GROUP("List<String>"),
    MODEL_YEAR("ModelYear"),
    UNKNOWN("unknown");

    public final String repr;

    FieldType(String repr) {
        this.repr = repr;
    }

    public static FieldType findByRepr(String repr) {
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType.repr.equals(repr)) {
                return fieldType;
            }
        }
        return FieldType.UNKNOWN;
    }
}
