package com.lxd.dao;

import com.lxd.po.Blog;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Cris on 2020/3/28
 */
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {    //后面这个提供动态组合查询

    @Query(value = "select b from t_blog b where b.recommend = true and b.user.id = ?1")
    Page<Blog> findTop(Long userId,Pageable pageable);

    //获得最新的博客
    @Query(value = "select b from t_blog b where b.user.id = ?1")
    List<Blog> findTop1(Long userId,Pageable pageable);

    //select  b from t_blog b where b.title like '%内容%'
    @Query(value = "select b from t_blog b where (b.title like ?1 or b.content like ?1) and b.user.id = ?3",nativeQuery = true)
    Page<Blog> findByQuerys(@Param("querys") String querys, @Param("pageable") Pageable pageable,@Param("userId") Long userId);

    @Query("select b from t_blog b where b.type.id = ?1")
    List<Blog> getBlogByType(Long id);


    /*@Query("select b from ")
    List<Blog> getBlogByTag(Long id);*/
}
