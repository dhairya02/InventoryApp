package com.example.abhis.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.abhis.inventoryapp.data.InventoryContract.InventoryEntry;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentMedicineUri;

    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private Spinner mSupplieNameSpinner;
    private EditText mSupplierPhoneNumberEditText;

    private int mSupplieName = InventoryEntry.SUPPLIER_UNKNOWN;

    private boolean mHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        
        Intent intent = getIntent();
        mCurrentMedicineUri = intent.getData();

        if (mCurrentMedicineUri == null) {
            setTitle(getString(R.string.add));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_medicine));
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        mNameEditText = findViewById(R.id.medicine_name_edit_text);
        mPriceEditText = findViewById(R.id.medicine_price_edit_text);
        mQuantityEditText = findViewById(R.id.medicine_quantity_edit_text);
        mSupplieNameSpinner = findViewById(R.id.medicine_supplier_name_spinner);
        mSupplierPhoneNumberEditText = findViewById(R.id.medicine_supplier_phone_number_edit_text);

        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplieNameSpinner.setOnTouchListener(mTouchListener);
        mSupplierPhoneNumberEditText.setOnTouchListener(mTouchListener);

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter medicineSupplieNameSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.supplier_options, android.R.layout.simple_spinner_item);

        medicineSupplieNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSupplieNameSpinner.setAdapter(medicineSupplieNameSpinnerAdapter);

        mSupplieNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_ranbaxy))) {
                        mSupplieName = InventoryEntry.SUPPLIER_RANBAXY;
                    } else if (selection.equals(getString(R.string.supplier_sun_pharma))) {
                        mSupplieName = InventoryEntry.SUPPLIER_SUN_PHARMA;
                    } else if (selection.equals(getString(R.string.supplier_cipla))) {
                        mSupplieName = InventoryEntry.SUPPLIER_CIPLA;
                    } else if (selection.equals(getString(R.string.supplier_ajanta))) {
                        mSupplieName = InventoryEntry.SUPPLIER_AJANTA;
                    } else {
                        mSupplieName = InventoryEntry.SUPPLIER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplieName = InventoryEntry.SUPPLIER_UNKNOWN;
            }
        });
    }


    private void saveMedicine() {
        String medicineNameString = mNameEditText.getText().toString().trim();
        String medicinePriceString = mPriceEditText.getText().toString().trim();
        String medicineQuantityString = mQuantityEditText.getText().toString().trim();
        String medicineSupplierPhoneNumberString = mSupplierPhoneNumberEditText.getText().toString().trim();
        if (mCurrentMedicineUri == null) {
            if (TextUtils.isEmpty(medicineNameString)) {
                Toast.makeText(this, getString(R.string.medicine_name_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(medicinePriceString)) {
                Toast.makeText(this, getString(R.string.price_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(medicineQuantityString)) {
                Toast.makeText(this, getString(R.string.quantity_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mSupplieName == InventoryEntry.SUPPLIER_UNKNOWN) {
                Toast.makeText(this, getString(R.string.supplier_name_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(medicineSupplierPhoneNumberString)) {
                Toast.makeText(this, getString(R.string.supplier_phone_required), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(InventoryEntry.COLUMN_NAME, medicineNameString);
            values.put(InventoryEntry.COLUMN_PRICE, medicinePriceString);
            values.put(InventoryEntry.COLUMN_QUANTITY, medicineQuantityString);
            values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, mSupplieName);
            values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER, medicineSupplierPhoneNumberString);

            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{

            if (TextUtils.isEmpty(medicineNameString)) {
                Toast.makeText(this, getString(R.string.medicine_name_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(medicinePriceString)) {
                Toast.makeText(this, getString(R.string.price_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(medicineQuantityString)) {
                Toast.makeText(this, getString(R.string.quantity_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mSupplieName == InventoryEntry.SUPPLIER_UNKNOWN) {
                Toast.makeText(this, getString(R.string.supplier_name_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(medicineSupplierPhoneNumberString)) {
                Toast.makeText(this, getString(R.string.supplier_phone_required), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(InventoryEntry.COLUMN_NAME, medicineNameString);
            values.put(InventoryEntry.COLUMN_PRICE, medicinePriceString);
            values.put(InventoryEntry.COLUMN_QUANTITY, medicineQuantityString);
            values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, mSupplieName);
            values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER, medicineSupplierPhoneNumberString);


            int rowsAffected = getContentResolver().update(mCurrentMedicineUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveMedicine();
                return true;
            case android.R.id.home:
                if (!mHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };
        return new CursorLoader(this,
                mCurrentMedicineUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            String currentName = cursor.getString(nameColumnIndex);
            int currentPrice = cursor.getInt(priceColumnIndex);
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            int currentSupplierName = cursor.getInt(supplierNameColumnIndex);
            int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

            mNameEditText.setText(currentName);
            mPriceEditText.setText(Integer.toString(currentPrice));
            mQuantityEditText.setText(Integer.toString(currentQuantity));
            mSupplierPhoneNumberEditText.setText(Integer.toString(currentSupplierPhone));

            switch (currentSupplierName) {
                case InventoryEntry.SUPPLIER_RANBAXY:
                    mSupplieNameSpinner.setSelection(1);
                    break;
                case InventoryEntry.SUPPLIER_SUN_PHARMA:
                    mSupplieNameSpinner.setSelection(2);
                    break;
                case InventoryEntry.SUPPLIER_CIPLA:
                    mSupplieNameSpinner.setSelection(3);
                    break;
                case InventoryEntry.SUPPLIER_AJANTA:
                    mSupplieNameSpinner.setSelection(4);
                    break;
                default:
                    mSupplieNameSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierPhoneNumberEditText.setText("");
        mSupplieNameSpinner.setSelection(0);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
