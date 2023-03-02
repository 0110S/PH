package com.accountbook.phoenix.Repository;

import com.accountbook.phoenix.Entity.Post;
import com.accountbook.phoenix.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {


    @Query("SELECT u.post FROM Post u WHERE u.user = :user")
     public List<Object> findAllByUser (User user);
}
