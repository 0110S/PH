package com.accountbook.phoenix.Repository;

import com.accountbook.phoenix.Entity.FriendRequest;
import com.accountbook.phoenix.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {

    List<FriendRequest> findAllBySender(User sender);

    FriendRequest findBySenderAndReceiver(User sender, User receiver);



    List<FriendRequest> findAllByReceiverAndFollowingTrue(User receiver);
}
