package com.accountbook.phoenix.Repository;

import com.accountbook.phoenix.Entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionsRepository extends JpaRepository<Reaction,Long> {
}
