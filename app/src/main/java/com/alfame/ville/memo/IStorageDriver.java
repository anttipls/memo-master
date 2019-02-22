package com.alfame.ville.memo;

import java.util.ArrayList;

public interface IStorageDriver {
    public abstract ArrayList loadItemsList();//Should be updated to what you want to get from database for initial data
    public abstract void addItem(Note note);//String item should be Note object
    public abstract void removeItem(Note note);//Ditto, also same should be done in IStorageOperations and StorageOperations and SQLiteDriver should be updated to match
    public abstract void removeAll();
    public abstract void createDatabase();
}
