package com.alfame.ville.memo;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.File;
import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

//Home of database code, if methods need to be called from MainActivity add them to the interfaces like these methods
public class SQLiteDriver implements IStorageDriver {

    private String dbName,tableName,idName,titleName,textName,categoryName,struckName;
    private File file;
    private int idCount;
    private Resources res;

    private SQLiteDatabase database;


    SQLiteDriver(File file){
        //this.res=res; //have to add resources object to constructor...
        this.file=file.getAbsoluteFile();//need AbsolutePath so getAbsoluteFile?


        dbName="notes";
        idCount=0;
        tableName="notes";
        idName="ID";
        titleName="Title";
        textName="Text";
        categoryName="Category";
        struckName="Struck";

        System.out.println("Constructor "+ file.getName()+" " +file.getAbsolutePath());
        createDatabase();

    }

    @Override
    public ArrayList loadItemsList() {
        ArrayList<Note> noteList = new ArrayList<>();
        String sql;

        //UH OH HARDCODED
        sql = "SELECT ID, TITLE, TEXT, CATEGORY, STRUCK FROM notes ORDER BY ID";
        Cursor cl = database.rawQuery(sql,null);
        cl.moveToFirst();
        while(!cl.isAfterLast()){
            //checks if struck
            Boolean struckB=false;
            if(!cl.getString(4).equals("FALSE")){
                struckB=true;
            }

            //testing
            System.out.println("loadItemsList :");
            System.out.println("note ID: "+cl.getInt(0)+ " Title: "+cl.getString(1)+" Text: "+cl.getString(2));

            //id,title,text,category,struck
            Note newNote = new Note(cl.getInt(0),
                    cl.getString(1),
                    cl.getString(2),
                    cl.getString(3),
                    struckB);
            noteList.add(newNote);

            //makes sure idCount stays ahead of last id
            if(newNote.getID()>idCount){
                idCount=newNote.getID();
            }
            idCount++;
            cl.moveToNext();
        }
        cl.close();

        //prints out all, for testing
        for(int i=0;i<noteList.size();i++){
            noteList.get(i).printNote();
        }


        return noteList;
    }

    @Override
    public void addItem(Note note) {

        /**WHAT'S THIS GREEN SHIT FOR
         *
         *
         * use storageOperations.getidCount() for id when creating a Note so it don't get mixed up?
         *
         *
         * INSERT INTO notes ('id','Title','Text','Category','Struck')
         * VALUES(int,'String','String','String','String');
         *
         *
         * ' and " etc in strings are going to mess with SQL!
         */

        try{
            String sql = "INSERT INTO "+tableName+" " +
                    "('"+ idName + "', " +
                    "'"+ titleName+"', " +
                    "'"+ textName+"', " +
                    "'"+ categoryName+"', " +
                    "'"+ struckName+"') " +

                    "VALUES ("
                    +note.getID()+","+
                    "'"+note.getTitle()+"'," +
                    "'"+note.getText()+"'," +
                    "'"+note.getCategory()+"'," +
                    "'"+String.valueOf(note.isStruck()).toUpperCase()+"'"+
                    ");";
            database.execSQL(sql);
            idCount++;
            System.out.println("SQL CODE: "+sql);
        }
        catch (SQLiteException e){
            System.out.println("AddItem e: "+e);

        }

    }

    @Override
    public void removeItem(Note note) {
        try{
            String sql = "DELETE FROM "+ tableName +" WHERE "+idName+"= "+note.getID()+";";
            database.execSQL(sql);
            System.out.println("SQL CODE: "+sql);

        }
        catch (SQLException e){

        }


    }

    @Override
    public void removeAll() {

        //DELETE FROM or DELETE * FROM, dunno
        try{
            String sql = "DELETE FROM "+tableName+ ";";
            database.execSQL(sql);
            System.out.println("SQL CODE: "+sql);

        }
        catch (SQLiteException e){
            System.out.println("removeAll e: "+e);
        }


    }

    @Override
    public void createDatabase() {

        System.out.println("createDatabase!");

        try{
            //if file exists, only connects to database
            if(file.length()<1){
                System.out.println("createDatabase says file does not exist");
                _createDatabase();
                createTables();
                //databaseTesty(1); //add


            }
            else{
                System.out.println("createDatabase file exists");
                _createDatabase();
                _setidCounter();
                //databaseTesty(1); //add
                //databaseTesty(1); //add
                //databaseTesty(2); //read
                //databaseTesty(3); //read and increment counter, but addItem and _setidCounter
                                    //handle it already

            }

        }
        catch (Exception e){
            System.out.println("createDatabase: "+e);
        }


    }

    private void _setidCounter() {
        String sql = "SELECT ID FROM notes ORDER BY ID";
        Cursor cl = database.rawQuery(sql,null);
        cl.moveToFirst();
        while(!cl.isAfterLast()){
            if(cl.getInt(0)>idCount)idCount=cl.getInt(0)+1;
            cl.moveToNext();
        }
        cl.close();

    }


    private void databaseTesty(int selection) {//for testing
        /** Testy
         1 for testing write
         2 for reading
         3+ for retrieval and idCounter update
         :D
         **/
        String sql;

        Cursor cl;
        switch (selection){
            case 1:
                //WRITING
                Note newNote = new Note(idCount,"NJOICE","I FEEL IT and THINGS","Anttis kaka",false);
                addItem(newNote);
                Note newNote1 = new Note(idCount,"Wwenks","wih wuah wauau ankan kanka","Anttis kaka",false);
                addItem(newNote1);
                Note newNote2 = new Note(idCount,"Hi","How are yeeet","Anttis kaka",false);
                addItem(newNote2);
                break;

            case 2:

                //RETRIEVE
                sql = "SELECT ID, TITLE, TEXT, CATEGORY FROM notes ORDER BY ID";
                cl = database.rawQuery(sql,null);
                cl.moveToFirst();
                while(!cl.isAfterLast()){
                    System.out.println("note ID: "+cl.getInt(0)+ " Title: "+cl.getString(1)+" Text: "+cl.getString(2)+" Category: "+cl.getString(3));
                    cl.moveToNext();
                }
                cl.close();
                break;

            default:
                //RETRIEVE
                sql = "SELECT ID, TITLE, TEXT, CATEGORY FROM notes ORDER BY ID";
                cl = database.rawQuery(sql,null);
                cl.moveToFirst();
                while(!cl.isAfterLast()){
                    System.out.println("note ID: "+cl.getInt(0)+ " Title: "+cl.getString(1)+" Text: "+cl.getString(2)+" Category: "+cl.getString(3));
                    if(cl.getInt(0)>idCount)idCount=cl.getInt(0)+1;
                    cl.moveToNext();
                }
                cl.close();
                break;

        }
    }

    private void createTables() {

        //dropping table for safety/testing
        try{
            StringBuilder sql = new StringBuilder();
            sql.append("DROP TABLE notes;");

            database.execSQL(sql.toString());
            System.out.println("createTables dropped table");
        }
        catch (SQLiteException e){
            System.out.println("createTable dropping e: "+e);
        }

        try{
            StringBuilder sql = new StringBuilder();

            sql.append("CREATE TABLE "+tableName+" (");
            sql.append(idName +" INTEGER AUTO_INCREMENT PRIMARY KEY,");
            sql.append(titleName    +" VARCHAR(25),");
            sql.append(textName  +" VARCHAR(255),");
            sql.append(categoryName     +" VARCHAR(255),");
            //sql.append(res.getString(R.string.noteDate)     +" VARCHAR(15),");
            //sql.append(res.getString(R.string.notePriority) +" INTEGER,");
            sql.append(struckName   +" BOOLEAN );");
            database.execSQL(sql.toString());
            System.out.println("createTable SQL CODE: "+sql);
        }
        catch (SQLiteException e){
            System.out.println("createTables e: "+e);

        }

    }

    private void _createDatabase() {

        if(database==null){
            System.out.println("DATABASE CREATION");
        }

        database= openOrCreateDatabase(file.getAbsolutePath(), null,null);






    }

    //have to retrieve idCount when calling addItem()?? not sure
    public int getidCount(){
        _setidCounter();
        return  idCount;
    }
}
