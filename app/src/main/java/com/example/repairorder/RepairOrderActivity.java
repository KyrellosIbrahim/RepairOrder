package com.example.repairorder;

import java.util.Locale;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RepairOrderActivity extends AppCompatActivity {

    private EditText inspectionField, partsField, paintField, laborField;
    private TextView subtotalField, taxField, totalField;
    private static final double TAX_RATE = 0.0925;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button submitButton; // step 1: create button variable
        View.OnClickListener buttonListener = view -> finish();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_repair_order);

        Spinner orderTypeSpinner = findViewById(R.id.orderTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.order_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderTypeSpinner.setAdapter(adapter);
        orderTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subtotalField = findViewById(R.id.textSubtotalValue);
        taxField = findViewById(R.id.textTaxValue);
        totalField = findViewById(R.id.textTotalValue);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inspectionField = findViewById(R.id.inspectionPlaceholder);
        partsField = findViewById(R.id.partsPlaceholder);
        paintField = findViewById(R.id.paintPlaceholder);
        laborField = findViewById(R.id.laborPlaceholder);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(buttonListener);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateTotals();
            }
        };
        inspectionField.addTextChangedListener(watcher);
        partsField.addTextChangedListener(watcher);
        paintField.addTextChangedListener(watcher);
        laborField.addTextChangedListener(watcher);
    }

    private void updateTotals() {
        double inspection = getDoubleFromEditText(inspectionField);
        double parts = getDoubleFromEditText(partsField);
        double paint = getDoubleFromEditText(paintField);
        double labor = getDoubleFromEditText(laborField);

        double subtotal = inspection + parts + paint + labor;
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        subtotalField.setText(String.format(Locale.US, "%.2f", subtotal));
        taxField.setText(String.format(Locale.US, "%.2f", tax));
        totalField.setText(String.format(Locale.US, "%.2f", total));
    }

    private double getDoubleFromEditText(EditText editText) {
        String text = editText.getText().toString();
        if (text.isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

}