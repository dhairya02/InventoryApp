package com.example.abhis.inventoryapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.abhis.inventoryapp.data.InventoryContract.InventoryEntry;

public class CursorAdapterActivity extends CursorAdapter {

    public CursorAdapterActivity(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        
        TextView NameTextView = view.findViewById(R.id.name_text_view);
        TextView PriceTextView = view.findViewById(R.id.price_text_view);
        TextView QuantityTextView = view.findViewById(R.id.quantity_text_view);

        final int columnIdIndex = cursor.getColumnIndex(InventoryEntry._ID);
        int NameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME);
        int PriceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
        int QuantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);

        final String medicineID = cursor.getString(columnIdIndex);
        String Name = cursor.getString(NameColumnIndex);
        String Price = cursor.getString(PriceColumnIndex);
        final String Quantity = cursor.getString(QuantityColumnIndex);


        NameTextView.setText(medicineID + " ) " + Name);
        PriceTextView.setText(context.getString(R.string.medicine_price) + " : " + Price);
        QuantityTextView.setText(context.getString(R.string.medicine_quantity) + " : " + Quantity);

        Button EditButton = view.findViewById(R.id.edit_button);
        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), EditorActivity.class);
                Uri currentProdcuttUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, Long.parseLong(medicineID));
                intent.setData(currentProdcuttUri);
                context.startActivity(intent);
            }
        });

    }

}
