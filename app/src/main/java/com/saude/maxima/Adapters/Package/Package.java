package com.saude.maxima.Adapters.Package;

/**
 * Created by Junnyor on 21/10/2017.
 */

public class Package {

    private int id;
    private String name;
    private String slug;
    private String description;
    private int validity;
    private double value;
    private String img;
    private String img_discount;
    private String begin_discount;
    private String end_discount;
    private double value_discount;
    private int is_active;
    private String created_at;
    private String updated;

    public Package(int id, String name, String description, double value){
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg_discount() {
        return img_discount;
    }

    public void setImg_discount(String img_discount) {
        this.img_discount = img_discount;
    }

    public String getBegin_discount() {
        return begin_discount;
    }

    public void setBegin_discount(String begin_discount) {
        this.begin_discount = begin_discount;
    }

    public String getEnd_discount() {
        return end_discount;
    }

    public void setEnd_discount(String end_discount) {
        this.end_discount = end_discount;
    }

    public double getValue_discount() {
        return value_discount;
    }

    public void setValue_discount(double value_discount) {
        this.value_discount = value_discount;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
