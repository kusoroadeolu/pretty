package io.github.kusoroadeolu.pretty;

public enum MyEnum {
    ADE(1, "Ade") {
        @Override
        int age() {
            return ADE.age;
        }

        @Override
        String userName() {
            return ADE.name;
        }
    };

    private final int age;
    private final String name;

    MyEnum(int age, String name) {
        this.age = age;
        this.name = name;
    }

    abstract int age();
    abstract String userName();
}
