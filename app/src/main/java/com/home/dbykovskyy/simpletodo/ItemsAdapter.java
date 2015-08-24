package com.home.dbykovskyy.simpletodo;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.home.dbykovskyy.simpletodo.Model.Item;
import com.home.dbykovskyy.simpletodo.database.ItemsDataBaseHelper;

import java.util.ArrayList;

/**
 * Created by dbykovskyy on 8/20/15.
 */
public class ItemsAdapter extends ArrayAdapter<Item> {

    private static class ViewHolder {
        TextView itemName;
        ImageView editItem;
        ImageView deleteItem;
    }



    public ItemsAdapter(Context context, ArrayList<Item> items) {
        super(context,0, items);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder=  new ViewHolder();


            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);

        // Lookup view for data population
        viewHolder.itemName =(TextView) convertView.findViewById(R.id.itemView);
        viewHolder.editItem = (ImageView) convertView.findViewById(R.id.editIcon);
        viewHolder.deleteItem = (ImageView) convertView.findViewById(R.id.deleteIcon);
        convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemName.setText(item.itemName);
        viewHolder.editItem.setImageResource(R.drawable.edit_trans);
        viewHolder.deleteItem.setImageResource(R.drawable.x_icon);


       /*
       * Set listener for X icon to delete an item
       * */
        viewHolder.deleteItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //creating the dialog that asks user if she wants to remove the item
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.delete_dialog);
                dialog.setTitle("Do you want to delete item:");

                final Button yes = (Button) dialog.findViewById(R.id.btnYesDelete);
                final Button no = (Button) dialog.findViewById(R.id.btnNoDelete);
                final TextView itemToDelete = (TextView)dialog.findViewById(R.id.deleteItemText);
                itemToDelete.setText(item.itemName+"?");
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String itemNameToBeRemoved = "\"" + item.itemName + "\" is successfully removed";
                        remove(item);
                        notifyDataSetChanged();
                        removeFromDb(item);
                        displayToastInCenter(itemNameToBeRemoved, Toast.LENGTH_SHORT);
                        dialog.dismiss();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        /*
        * Set listener for pencil icon to edit an item
        * */
        viewHolder.editItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.edit_dialog_box);
                dialog.setTitle("Edit your list item");

                final EditText text = (EditText) dialog.findViewById(R.id.editTextField);
                final String retreivedItemName = item.itemName;
                text.setText(retreivedItemName);

                final Button yes = (Button) dialog.findViewById(R.id.btnApply);
                final Button no = (Button) dialog.findViewById(R.id.btnCancel);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Item temNewItem = new Item();
                        ItemsDataBaseHelper databaseHelper = ItemsDataBaseHelper.getInstance(getContext());
                        String itemText = text.getText().toString();
                        temNewItem.itemName = itemText;
                        remove(item);
                        insert(temNewItem, position);
                        notifyDataSetChanged();
                        text.setText(null);
                        databaseHelper.updateIteme(item, temNewItem);
                        dialog.dismiss();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text.setText(null);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    private void removeFromDb(Item i){
        ItemsDataBaseHelper databaseHelper = ItemsDataBaseHelper.getInstance(getContext());
        databaseHelper.deleteItem(i);
    }

    private void displayToastInCenter(String msg, final int length){
        Toast toast = Toast.makeText(getContext(), msg, length);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
