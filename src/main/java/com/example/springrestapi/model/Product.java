package com.example.springrestapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Integer price;
    @Column(name = "city")
    private String city;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    @ToString.Exclude
    private List<Image> images;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "preview_image_id")
    private Long previewImageId;
    @Column(name = "created")
    private Date created;
    @Column(name = "updated")
    private Date updated;

    @PrePersist
    private void init(){
        created = new Date();
    }


    public void addImageToProduct(Image image){
        image.setProduct(this);
        images.add(image);
    }

    public void deleteImagesFromProduct(List<Image> images){
        this.images.removeAll(images);
    }


}
