package com.company;

import java.util.HashMap;
import java.util.Map;

public class PackageResolver {

    private Map<String, String> classToPackage = new HashMap<>();

    public PackageResolver() {
        classToPackage.put("ModelYear", "com.company.custom");
    }

    public boolean hasPackageRegistered(String className) {
        return classToPackage.containsKey(className);
    }

    public String getPackage(String className) {
        return classToPackage.get(className);
    }
}
