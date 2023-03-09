package com.accountbook.phoenix.Repository;

import com.accountbook.phoenix.Entity.FriendRequest;
import com.accountbook.phoenix.Entity.Post;
import com.accountbook.phoenix.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {


    @Query("SELECT u.post  FROM Post u WHERE u.user = :user")
    public List<Object> findAllByUser(User user);

    long count();

    Post findByUser(User user);

    List<Post> findAllPostsByUser(User user);
}

