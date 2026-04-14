package io.github.kusoroadeolu.pretty;

import java.util.List;

class Address {
    String street = "123 Main St";
    String city = "Lagos";
    int zip = 100001;
}

class Person {
    String name = "Alice";
    int age = 30;
    boolean active = true;
    Address address = new Address();
    List<String> hobbies = List.of("reading", "coding", "chess");
    MyEnum mine = MyEnum.ADE;
}