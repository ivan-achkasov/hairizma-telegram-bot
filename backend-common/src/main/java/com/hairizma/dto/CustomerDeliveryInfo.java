package com.hairizma.dto;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class CustomerDeliveryInfo {

    @XmlElement
    private long id;

    @XmlElement
    private String name;

    @XmlElement
    private String phone;

    @XmlElement
    private String deliveryAddress;

    public CustomerDeliveryInfo() {

    }

    public CustomerDeliveryInfo(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
