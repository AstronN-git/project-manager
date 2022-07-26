package com.max.projectmanager.repository;

import com.max.projectmanager.entity.Item;
import com.max.projectmanager.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemsByProject(Project project);
    List<Item> findItemsByProjectAndDone(Project project, Boolean isDone);
}
