package com.example.demo;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Item")
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Cantity")
    private Integer cantity;

    @Column(name = "Category")
    private String category;

    @Column(name = "Price")
    private float price;

    @Column(name = "Poster")
    private String poster;

    @Column(name = "TrailerLink")
    private String trailerLink;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCantity() {
        return cantity;
    }

    public void setCantity(Integer cantity) {
        this.cantity = cantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String categorie) {
        this.category = categorie;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }
}