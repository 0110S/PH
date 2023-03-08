package com.accountbook.phoenix.Repository;

import com.accountbook.phoenix.Entity.FriendRequest;
import com.accountbook.phoenix.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest,Integer> {

    List<FriendRequest> findByUser(User user);

    Optional<FriendRequest> findByUserAndFriend(User user, User friend);
}
