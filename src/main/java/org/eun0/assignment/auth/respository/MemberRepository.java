package org.eun0.assignment.auth.respository;

import org.eun0.assignment.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUsername(String username);
    Optional<Member> findByUsername(String username);

}
