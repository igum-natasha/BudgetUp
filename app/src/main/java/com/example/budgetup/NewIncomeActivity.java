package com.example.budgetup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewIncomeActivity extends AppCompatActivity {

    String[] paymentItem = {"Cash", "Credit card"};
    String result;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayAdapter;
    EditText entIncomeCount, entNote;
    Button btnCategory;
    ImageButton btnBackspace, btnOkNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_income);
        initViews();
        definePaymentMenu();

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result += entIncomeCount.getText().toString();
                result += "\n";
                result += entNote.getText().toString();

                Toast.makeText(NewIncomeActivity.this, "Result: " + result, Toast.LENGTH_SHORT).show();

            }
        });
        btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entIncomeCount.setText("");
            }
        });

        btnOkNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

    }

    private void definePaymentMenu() {
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, paymentItem);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                result = adapterView.getItemAtPosition(i).toString() + "\n";
//                Toast.makeText(NewIncomeActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        autoCompleteTextView = findViewById(R.id.autoComplete);
        entIncomeCount = findViewById(R.id.entIncomeCount);
        entNote = findViewById(R.id.entNote);
        btnCategory = findViewById(R.id.btnCategory);
        btnBackspace = findViewById(R.id.btnBackspace);
        btnOkNote = findViewById(R.id.btnOk);
    }
}