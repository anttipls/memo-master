package com.alfame.ville.memo;


import java.util.ArrayList;

public class StorageOperations implements IStorageOperations {

    public IStorageDriver driver = null;

    public StorageOperations(IStorageDriver driver) {
        this.driver = driver;
    }

    @Override
    public ArrayList LoadItemsList() {
        return null;
    }

    @Override
    public void addItem(Note note) {

    }

    @Override
    public void removeItem(Note note) {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public void createDatabase() {

    }
}
