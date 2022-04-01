package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class PojoWriter {

    private final static String INDENT = "  ";
    private final static String DOUBLE_INDENT = "    ";
    private final static String TRIPLE_INDENT = "      ";
    private final Writer writer;
    private final PackageResolver packageResolver;
    private final Message message;

    public PojoWriter(Message message, String location) throws IOException {
        this.writer = new FileWriter(location + "/" + message.getName() + ".java", false);
        this.packageResolver = new PackageResolver();
        this.message = message;
    }

    public void writePojo() {
        try {
            writePackage();
            writeImports();
            writeClassSignature();
            writeFields();
            writeAccessors();
            writeInstanceConstructor();
            writeToString();

            close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writePackage() throws IOException {
        writer.write(String.format("package %s;\n\n", message.getMessageContext().getPackageName()));
    }

    private void writeImports() throws IOException {
        if (message.containsGroup()) {
            writer.write("import java.util.List;\n");
        }
        if (message.hasBase()) {
            writer.write(String.format("import %s;\n", message.getMessageContext().getBase()));
        }
        if (message.hasInterface()) {
            for (String interfaceName : message.getMessageContext().getInterfaces()) {
                writer.write(String.format("import %s;\n", interfaceName));
            }
        }


        for (Field field : message.getFields()) {
            if (packageResolver.hasPackageRegistered(field.getFieldType().repr)) {
                writer.write(String.format("import %s.%s;\n", packageResolver.getPackage(field.getFieldType().repr), field.getFieldType().repr));
            }
        }
        writer.write("\n\n");
    }

    private void writeClassSignature() throws IOException {
        writer.write("public class "+ message.getName());
        if (message.hasBase()) {
            writer.write(String.format(" extends %s", getClassFromPackage(message.getMessageContext().getBase())));
        }
        if (message.hasInterface()) {
            writer.write(" implements");
            for (String interfaceName : message.getMessageContext().getInterfaces()) {
                writer.write(" " + getClassFromPackage(interfaceName));
            }
        }
        writer.write(" {\n");
    }

    private void writeFields() throws IOException {
        for (Field field : message.getFields()) {
            writeField(field);
        }
    }

    private void writeField(Field field) throws IOException {
        writer.write(String.format("\n%sprivate %s %s;", INDENT, field.getFieldType().repr, field.getName()));
    }

    private void writeAccessors() throws IOException {
        for (Field field : message.getFields()) {
            writeGetter(field);
            writeSetter(field);
        }
    }

    private void writeGetter(Field field) throws IOException {
        writer.write(String.format("\n\n%s@Override", INDENT));
        writer.write(String.format("\n%spublic %s %s() {\n", INDENT, field.getFieldType().repr, field.getName()));
        writer.write(String.format("%sreturn this.%s;\n", DOUBLE_INDENT, field.getName()));
        writer.write(String.format("%s}", INDENT));
    }

    private void writeSetter(Field field) throws IOException {
        writer.write(String.format("\n\n%spublic void %s(%s %s) {\n", INDENT, field.getName(), field.getFieldType().repr, field.getName()));
        writer.write(String.format("%sthis.%s = %s;\n", DOUBLE_INDENT, field.getName(), field.getName()));
        writer.write(String.format("%s}", INDENT));
    }

    private void writeInstanceConstructor() throws IOException {

        Field firstField = message.getFields().get(0);
        writer.write(String.format("\n\n%spublic %s newInstance(%s %s,", INDENT, message.getName(), firstField.getFieldType().repr, firstField.getName()));

        for (int i = 1; i < message.getFields().size() - 1; i++) {
            Field field = message.getFields().get(i);
            writer.write(String.format("\n%s%s %s,", TRIPLE_INDENT, field.getFieldType().repr, field.getName()));
        }

        Field lastField = message.getFields().get(message.getFields().size() - 1);

        writer.write(String.format("\n%s%s %s) {", TRIPLE_INDENT, lastField.getFieldType().repr, lastField.getName()));

        for (Field field : message.getFields()) {
            writer.write(String.format("\n%sthis.%s(%s);", DOUBLE_INDENT, field.getName(), field.getName()));
        }

        writer.write(String.format("\n%sreturn this;", INDENT));
        writer.write(String.format("\n%s}", INDENT));
    }

    private void writeToString() throws IOException {
        writer.write(String.format("\n\n%s@Override", INDENT));
        writer.write(String.format("\n%spublic String toString() {", INDENT));
        writer.write(String.format("\n%sreturn \"\"; // todo ", DOUBLE_INDENT));
        writer.write(String.format("\n%s}", INDENT));

    }

    private void close() throws IOException {
        writer.write("\n}");
        writer.flush();
        writer.close();
    }

    private String capitalize(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    private String getClassFromPackage(String packageName) {
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }
}
