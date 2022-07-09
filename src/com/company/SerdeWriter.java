package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SerdeWriter {

    private final static String INDENT = "  ";
    private final static String DOUBLE_INDENT = "    ";
    private final static String TRIPLE_INDENT = "      ";
    private final static String QUAD_INDENT = "        ";
    private final static String SERDE_SUFFIX = "Serde";
    private final static String SERDE_INTERFACE_PACKAGE = "com.company.Serde";
    private final static String BUFFER_CLASS = "MutableDirectBuffer";
    private final static String MESSAGE_HEADER_ENCODER_CLASS = "MessageHeaderEncoder";
    private final static String MESSAGE_HEADER_DECODER_CLASS = "MessageHeaderDecoder";

    private final static String GENERIC_ENCODE_TEMPLATE = "encoder.%s(%s);";

    private final Writer writer;
    private final PackageResolver packageResolver;
    private final Message message;

    public SerdeWriter(Message message, String location) throws IOException {
        this.writer = new FileWriter(location + "/" + message.getName() + SERDE_SUFFIX + ".java", false);
        this.packageResolver = new PackageResolver();
        this.message = message;
    }

    public void writeSerde() {
        try {
            writePackage();
            writeImports();
            writeClassSignature();

            writePublicEncoder();
            writePublicDecoder();

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
        writer.write(String.format("import %s;\n", SERDE_INTERFACE_PACKAGE));


        for (Field field : message.getFields()) {
            if (packageResolver.hasPackageRegistered(field.getFieldType().repr)) {
                writer.write(String.format("import %s.%s;\n", packageResolver.getPackage(field.getFieldType().repr), field.getFieldType().repr));
            }
        }
        writer.write("\n\n");
    }

    private void writeClassSignature() throws IOException {
        writer.write("public class "+ message.getName() + SERDE_SUFFIX);
        writer.write(String.format(" implements Serde<%s>", message.getName()));
        writer.write(" {\n");
    }

    private void writePublicEncoder() throws IOException {
        writer.write(String.format("\n%s@Override", INDENT));
        writer.write(String.format("\n%spublic int encode(", INDENT));
        writer.write(String.format("\n%s%s buffer,", TRIPLE_INDENT, BUFFER_CLASS));
        writer.write(String.format("\n%s int offset,", TRIPLE_INDENT));
        writer.write(String.format("\n%s%s headerEncoder,", TRIPLE_INDENT, MESSAGE_HEADER_ENCODER_CLASS));
        writer.write(String.format("\n%s%s value) {", TRIPLE_INDENT, message.getName()));
        writer.write(String.format("\n%sencoder.wrapAndApplyHeader(buffer, offset, headerEncoder);", DOUBLE_INDENT));
        writer.write(String.format("\n%sencode(value);", DOUBLE_INDENT));
        writer.write(String.format("\n%sreturn encodedLength;", DOUBLE_INDENT));
        writer.write(String.format("\n%s}", INDENT));
    }

    private void writePublicDecoder() throws IOException {
        writer.write(String.format("\n\n%s@Override", INDENT));
        writer.write(String.format("\n%spublic %s decode(", INDENT, message.getName()));
        writer.write(String.format("\n%s%s buffer, int offset, %s headerDecoder) {", TRIPLE_INDENT, BUFFER_CLASS, MESSAGE_HEADER_DECODER_CLASS));
        writer.write(String.format("\n%sdecoder.wrap(", DOUBLE_INDENT));
        writer.write(String.format("\n%sbuffer,", QUAD_INDENT));
        writer.write(String.format("\n%sheaderDecoder.encodedLength() + offset,", QUAD_INDENT));
        writer.write(String.format("\n%sheaderDecoder.blockLength(),", QUAD_INDENT));
        writer.write(String.format("\n%sheaderDecoder.version());", QUAD_INDENT));
        writer.write(String.format("\n%sreturn decode();", DOUBLE_INDENT));
        writer.write(String.format("\n%s}", INDENT));
    }

    private void writePrivateEncoder() throws IOException {
        writer.write(String.format("\n%sprivate void encode(%s source) {", message.getName(), INDENT));
        writer.write(String.format("\n%s}", INDENT));
    }

    private void close() throws IOException {
        writer.write("\n}");
        writer.flush();
        writer.close();
    }


    private String getClassFromPackage(String packageName) {
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }
}
