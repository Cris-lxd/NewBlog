package com.lxd.dao;

import com.lxd.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Cris on 2020/3/25
 */
public interface TypeRepository extends JpaRepository<Type, Long> {

    Type findByName(String name);

    @Query("select t from t_type t where t.userId = ?1")
    List<Type> findTop(Long userId,Pageable pageable);

    @Query("select count(id) from t_type ")
    int countType();

    @Query("select t from t_type t where t.userId = ?1")
    List<Type> findAllByUserId(Long userId);

    @Query("select t from t_type t where t.userId = ?1")
    Page<Type> findAllByUserIdAndPage(Long userId, Pageable pageable);


}


