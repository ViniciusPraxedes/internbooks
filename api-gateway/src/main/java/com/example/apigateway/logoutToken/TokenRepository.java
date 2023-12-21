package com.example.apigateway.logoutToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token>findByToken(String token);



    @Modifying
    @Query("UPDATE Token t " +
            "SET t.revoked = true, t.expired = true " +
            "WHERE t.subject = :subject")
    void revokeAndExpireAllTokensBySubject(@Param("subject") String subject);

    @Modifying
    @Query("UPDATE Token t " +
            "SET t.revoked = true, t.expired = true " +
            "WHERE t.token = :token")
    void revokeAndExpireTokenByToken(@Param("token") String token);



}