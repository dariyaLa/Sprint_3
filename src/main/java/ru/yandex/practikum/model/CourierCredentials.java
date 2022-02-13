package ru.yandex.practikum.model;


public class CourierCredentials{

    public String login;
    public String password;


    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }


    public CourierCredentials() {

    }


    public static CourierCredentials from(Courier courier) {
        return new CourierCredentials(courier.login, courier.password);
    }


    @Override
    public String toString(){
        return "CourierCredentials login:"+login+",password:"+password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
