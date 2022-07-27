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

    public List<Item> findItemsByProjectAndIsDone(Project project, Boolean isDone) {
        return itemRepository.findItemsByProjectAndIsDone(project, isDone);
    }

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public Item findItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public void deleteItem(Item item) {
        itemRepository.delete(item);
    }
}
