package com.home.dbykovskyy.simpletodo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.home.dbykovskyy.simpletodo.database.Item;
import com.home.dbykovskyy.simpletodo.database.ItemsDataBaseHelper;

import java.util.ArrayList;
import java.util.List;


public class ToDoActivity extends Activity{
    static final String TAG = "SQlite>>>>";
    private ArrayList<Item> items;
    private ItemsAdapter itemAdapter;
    private ListView lvItems;
    private EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        items = new ArrayList<Item>();
        readItemsFromDb();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        itemAdapter = new ItemsAdapter(this, items);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemAdapter);
        if(items.size()==0)
           displayToastInCenter("Start adding your Todo items from down below", Toast.LENGTH_LONG);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_do, menu);
        return true;
    }

    private void displayToastInCenter(String msg, final int length){
        Toast toast = Toast.makeText(this,msg, length);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void onAddItem(View v){
        Item tmpUserInput = new Item();
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        tmpUserInput.itemName=etNewItem.getText().toString();
        itemAdapter.add(tmpUserInput);
        etNewItem.setText(null);
        writeItemsToDb();
        displayToastInCenter(tmpUserInput.itemName+" is successfully added", Toast.LENGTH_SHORT);
    }


    private void readItemsFromDb(){
        ItemsDataBaseHelper databaseHelper = ItemsDataBaseHelper.getInstance(this);
        List<Item> itemNames = databaseHelper.getAllItems();
        items = new ArrayList<Item>();
        try{
            for (Item name : itemNames) {
                items.add(name);
            }
        }catch (Exception e){
            Log.d(TAG, "Error while trying to read item from Db "+e.getMessage());
        }
    }

    private void writeItemsToDb(){
        ItemsDataBaseHelper databaseHelper = ItemsDataBaseHelper.getInstance(this);
        databaseHelper.deleteAllItems();
        try{
            for (Item item : items) {
                databaseHelper.addItem(item);
            }
        }catch (Exception e){
            Log.d(TAG, "Error while trying to write items to DB " + e.getMessage());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
