package com.example.springrestapi.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Arrays;
import java.util.StringJoiner;

@Entity
@Table(name = "images")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "original_file_name")
    private String originalFileName;
    @Column(name = "size")
    private Long size;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "is_preview_image")
    private boolean isPreviewImage;
    @Lob
    @Column(name = "bytes")
    private byte[] bytes;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(mappedBy = "image")
    private User user;


}
