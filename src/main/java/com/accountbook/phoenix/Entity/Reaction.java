package com.accountbook.phoenix.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int refId;
    private String refType;
    private boolean like;
    private LocalDateTime likedTime;

    @JoinColumn(name = "reacted_user")
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    private User user;

}
