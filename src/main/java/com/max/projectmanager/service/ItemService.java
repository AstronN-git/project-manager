package com.max.projectmanager.service;

import com.max.projectmanager.entity.Item;
import com.max.projectmanager.entity.Project;
import com.max.projectmanager.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getItemsByProject(Project project) {
        return itemRepository.findItemsByProject(project);
    }

    public List<Item> getItemsByProjectAndIsDone(Project project, Boolean isDone) {
        return itemRepository.findItemsByProjectAndIsDone(project, isDone);
    }
}
