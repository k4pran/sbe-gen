package com.company;

import java.util.ArrayList;
import java.util.List;

public class MessageContext {

    private final String packageName;
    private final List<String> interfaces = new ArrayList<>();
    private String base;

    public MessageContext(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public void addInterface(String interfaceName) {
        this.interfaces.add(interfaceName);
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
