package ru.yandex.practikum.model;


import org.apache.commons.lang3.RandomStringUtils;

public class Orders {

    public static int maxHoursRent = 24;
    public String firstName;
    public String lastName;
    public String address;
    public String metroStation;
    public String phone;
    public int rentTime;
    public String deliveryDate;
    public String comment;
    public String[] color;

    public Orders(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    public static Orders newOrder(String[] setColor){
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        String address = RandomStringUtils.randomAlphabetic(5);
        String metroStation = RandomStringUtils.randomAlphabetic(5);
        String phone = RandomStringUtils.randomAlphabetic(11);
        int rentTime = (int) (Math.random() * ++maxHoursRent);
        String deliveryDate = RandomStringUtils.randomAlphabetic(5);
        String comment = RandomStringUtils.randomAlphabetic(5);
        String[] color = setColor;
        return new Orders(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment,color);
    }

}
