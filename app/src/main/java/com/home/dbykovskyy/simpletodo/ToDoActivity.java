package com.home.dbykovskyy.simpletodo;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.home.dbykovskyy.simpletodo.DialogFragment.MyAlertDialogFragment;
import com.home.dbykovskyy.simpletodo.Model.Item;
import com.home.dbykovskyy.simpletodo.database.ItemsDataBaseHelper;

import java.util.ArrayList;
import java.util.List;


public class ToDoActivity extends AppCompatActivity
{
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
        setUpListViewListener();
        if(items.size()==0)
           displayToastInCenter("Start adding your Todo items from down below", Toast.LENGTH_LONG);

    }

    /*
    * Setting priority for Todo items
    * */
    private void  setUpListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, final View item, int pos, long id) {

                final MyAlertDialogFragment fragment = MyAlertDialogFragment.newInstance("Set priority");

                fragment.setOnChoiceClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String priority = ItemPriority.priority[which];
                        setViewBackground(priority, item);
                        Toast.makeText(getApplicationContext(),
                                "You Choose : " + priority,
                                Toast.LENGTH_LONG).show();
                    }
                });

                fragment.setPositiveListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                fragment.setCancelClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        displayToastInCenter("I just clicked Cancel", Toast.LENGTH_SHORT);
                        item.setBackgroundColor(Color.TRANSPARENT);
                        fragment.dismiss();
                    }

                });

                fragment.show(getSupportFragmentManager(), "fragment");
                return true;
            }

        });
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
        String userInput = etNewItem.getText().toString();
        tmpUserInput.itemName=userInput;
        if(userInput.isEmpty()){
            displayToastInCenter("Please give your Todo item a name", Toast.LENGTH_SHORT);
        }else {
            itemAdapter.add(tmpUserInput);
            etNewItem.setText(null);
            writeItemsToDb();
            displayToastInCenter(tmpUserInput.itemName+" is successfully added", Toast.LENGTH_SHORT);
        }
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

    private void setViewBackground(String priority, View v){
        switch (priority) {
            case "High":
                v.setBackgroundColor(Color.RED);
                break;
            case "Medium":
                v.setBackgroundColor(Color.YELLOW);
                break;
            case "Low":
                v.setBackgroundColor(Color.GREEN);
                break;
        }
        v.getBackground().setAlpha(30);
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
