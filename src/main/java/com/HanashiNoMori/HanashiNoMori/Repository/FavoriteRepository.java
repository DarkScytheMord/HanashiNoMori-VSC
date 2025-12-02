package com.HanashiNoMori.HanashiNoMori.Repository;

import com.HanashiNoMori.HanashiNoMori.Model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndBookId(Long userId, Long bookId);
    Boolean existsByUserIdAndBookId(Long userId, Long bookId);
    
    @Transactional
    void deleteByUserIdAndBookId(Long userId, Long bookId);
}
