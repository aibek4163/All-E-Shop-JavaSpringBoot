package eshop.Home_Task_7_spring.repositories;

import eshop.Home_Task_7_spring.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByItem_Id(Long id);
}
