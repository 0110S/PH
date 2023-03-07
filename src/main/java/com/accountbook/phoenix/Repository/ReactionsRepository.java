//package com.accountbook.phoenix.Repository;
//
//import com.accountbook.phoenix.Entity.Post;
//import com.accountbook.phoenix.Entity.Reaction;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface ReactionsRepository extends JpaRepository<Reaction,Long> {
//
//    @Query("SELECT r.post FROM Reaction r WHERE r.post.id = :postId")
//    Post findPostByPostId(@Param("postId") int postId);
//}
