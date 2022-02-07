package ru.yandex.practikum.model;

import org.apache.commons.lang3.RandomStringUtils;


public class Courier {

    public String login;
    public String password;
    public String firstName;


    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Courier (String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Courier (String password) {
        this.password = password;
    }

    public static Courier getRandom(){
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new Courier(login,password,firstName);
    }

    public static Courier getRandomWithoutLogin(){
        String password = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new Courier(password,firstName);
    }

    public static Courier getRandomRequiredFields(){
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        return new Courier(login,password);
    }
}
