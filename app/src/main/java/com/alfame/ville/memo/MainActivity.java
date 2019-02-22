package com.alfame.ville.memo;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.ILaunchEditDialogListener, View.OnClickListener {

    private ExpandableListView listView;
    private CategoryAdapter categoryAdapter;
    private List<String> categories;
    private HashMap<String, List<String>> categoryHashMap;//Should be HashMap<String, List<Note>>
    private EditText et;
    IStorageOperations storageOperations;

    //ANTTI ADD
    private ArrayList<Note> notes;//Same as hashmap?
    private String[] cats;//Same as categories?

    private Resources res;
    //private int idCount;//tracks last ID DONT USE
    //END

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res=getResources();


        //Setting up SQLserver as method of operations
        File file = this.getDatabasePath(res.getString(R.string.dbName));
        System.out.println(this.getDatabasePath(res.getString(R.string.dbName)));

        //file.delete() until done testing, is kind of wonky though
        /*
        Boolean result = file.delete();
        if(result){
            System.out.println("database DELETED!");
        }
        else{
            System.out.println("database DOESN'T EXIST");
        }
        */

        //ANTTI need filepath for database!!!
        storageOperations = new StorageOperations(new SQLiteDriver(file));


        listView = findViewById(R.id.noteListView);
        initData();
        categoryAdapter = new CategoryAdapter(this, categories, categoryHashMap);
        listView.setAdapter(categoryAdapter);

        Button addBtn = findViewById(R.id.btnAdd);

        addBtn.setOnClickListener(this);


        CategoryAdapter.setLaunchEditDialogListener(this);

    }
    //ANTTI ADD
    private void getLists() {//gets categories from Strings resources

        cats = getResources().getStringArray(R.array.categoryList);

        for(int i=0;i<cats.length;i++){
            categories.add(cats[i]);
        }

        System.out.println("HEY "+cats[0]);
    }
    //END

    private void initData() {
        categories = new ArrayList<>();
        categoryHashMap = new HashMap<>();

        //categories.add("First");
        //categories.add("Second");

        //ANTTI ADD

        //END
        /* ////For testing
        List<String> firstItems = new ArrayList<>();
        firstItems.add("FirstFirst item");

        List<String> secondItems = new ArrayList<>();
        secondItems.add("SecondFirst item");

        categoryHashMap.put(categories.get(0), firstItems);
        categoryHashMap.put(categories.get(1), secondItems);*/
    }






    private void launchAddDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);

        final View v = inflater.inflate(R.layout.dialog_layout, null);

        //Find all datafields added to dialog layout
        et = v.findViewById(R.id.eTValue);

        //Generate category options from categories for selector inside v

        builder.setTitle(R.string.titlAdd);
        builder.setView(v);
        builder.setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoryHashMap.get(categories.get(0)).add(et.getText().toString());//Replace et.getText().toString with new Note object build from datafields above
                //If new category add category to categories
                //storageOperations.addItem(note, categoria) Needs to be edited to correct form in the interfaces and implementations
                categoryAdapter.notifyDataSetChanged();
            }
        });

        builder.create().show();
    }

    @Override
    public void launchEditDialog(final int groupPosition, final int childPosition) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);

        final View v = inflater.inflate(R.layout.dialog_layout, null);

        //Find all datafields added to dialog layout
        et = v.findViewById(R.id.eTValue);

        //Generate category options from categories for selector inside v

        builder.setTitle(R.string.titlEdit);
        builder.setView(v);
        builder.setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if test if new caterogy
                //categories.add("uusi kategoria");
                //List<String> uusiLista = new ArrayList<>();
                //categoryHashMap.put(categories.get(categories.size() -1), uusiLista);
                categoryHashMap.get(categories.get(groupPosition)).set(childPosition, et.getText().toString());//Replace et.getText().toString with edited Note object
                //if item is changed to different category and old category is left empty, remove category
                //storageOperations.editItem(noteObject); Needs to be added to the interfaces and implementations
                categoryAdapter.notifyDataSetChanged();
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                launchAddDialog();
                break;
            default:
                break;
        }
    }

}
