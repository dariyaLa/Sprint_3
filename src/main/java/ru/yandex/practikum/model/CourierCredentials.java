package ru.yandex.practikum.model;


public class CourierCredentials{

    public String login;
    public String password;


    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierCredentials(String login) {
        this.login = login;
    }

    public static CourierCredentials from(Courier courier) {
        return new CourierCredentials(courier.login, courier.password);
    }

    public static CourierCredentials fromLogin(Courier courier) {
        return new CourierCredentials(courier.login);
    }

    @Override
    public String toString(){
        return "CourierCredentials login:"+login+",password:"+password;
    }


}
