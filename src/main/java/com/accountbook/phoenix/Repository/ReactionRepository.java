package com.accountbook.phoenix.Repository;

import com.accountbook.phoenix.Entity.Reaction;
import com.accountbook.phoenix.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository  extends JpaRepository<Reaction,Integer> {
    Reaction findByRefIdAndUser(int refId, User user);

    @Query("SELECT r FROM Reaction r WHERE r.refId = :id")
    Reaction findByRefId(int id);

    List<Reaction> findALlByRefId(int id);
}
