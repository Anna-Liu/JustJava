package me.annaliu.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

//TODO show editText cursor when keyboard is visible
//TODO instant running total of price after each selection

    /**
     * Global variables
     */
    public int quantity = 0; //number of coffees
    public EditText customerNameField;
    private RadioGroup milkOptions1;
    private RadioGroup milkOptions2;
    private RadioGroup sugarOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        milkOptions1 = (RadioGroup) findViewById(R.id.milk1);
        milkOptions2 = (RadioGroup) findViewById(R.id.milk2);
        sugarOption = (RadioGroup) findViewById(R.id.sugar);
        customerNameField = (EditText) findViewById(R.id.nameField);
        milkOptions1.setOnCheckedChangeListener(listener1);
        milkOptions2.setOnCheckedChangeListener(listener2);

    }

    /**
     * This method clears the 2nd milk options radioGroup when options from the 1st group is selected.
     */
    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (i != -1) {
                milkOptions2.setOnCheckedChangeListener(null); //removes listener
                milkOptions2.clearCheck(); //clears the 2nd radio group
                milkOptions2.setOnCheckedChangeListener(listener2); // resets listener
            }
        }
    };

    /**
     * This method clears the 1st milk options radioGroup when options from the 2nd group is selected.
     */
    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (i != -1) {
                milkOptions1.setOnCheckedChangeListener(null); //removes listener
                milkOptions1.clearCheck(); //clears the 1st radio group
                milkOptions1.setOnCheckedChangeListener(listener1); //resets listener
            }
        }
    };


    /**
     * This method is called when the get total button is clicked.
     */
    public void submitOrder(View view) {
        if (customerNameField.getText().toString().matches("")) { //required name field
            Toast.makeText(this, getString(R.string.name_toast), Toast.LENGTH_SHORT).show();
        } else if (quantity == 0) { //required quantity field (cannot be 0)
            Toast.makeText(this, getString(R.string.quantity_toast), Toast.LENGTH_SHORT).show();
        } else if (milkOptions1.getCheckedRadioButtonId() == -1 && milkOptions2.getCheckedRadioButtonId() == -1) { //required milk field
            Toast.makeText(this, getString(R.string.milk_toast), Toast.LENGTH_SHORT).show();
        } else if (sugarOption.getCheckedRadioButtonId() == -1) { //required sugar field
            Toast.makeText(this, getString(R.string.sugar_toast), Toast.LENGTH_SHORT).show();
        } else {
            CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipCream);
            boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
            CheckBox caramelCheckBox = (CheckBox) findViewById(R.id.caramel);
            boolean hasCaramel = caramelCheckBox.isChecked();
            CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate);
            boolean hasChocolate = chocolateCheckBox.isChecked();

            TextView name = (TextView) findViewById(R.id.nameField);
            String customerName = name.getText().toString();
            String sugarChoice = findSugarChoice();
            String milkChoice = findMilkChoice();

            String total = calculatePrice(quantity, hasWhippedCream, hasCaramel, hasChocolate); //calls the calculatePrice method and stores the return value
            String orderSummary = createOrderSummary(total, customerName, sugarChoice, milkChoice, hasWhippedCream, hasCaramel, hasChocolate); //calls the createOrderSummary method
            emailIntent(customerName, orderSummary); //calls the emailIntent method
        }
    }


    /**
     * Calculates the price of the order.
     *
     * @param quantity is the number of cups of coffee ordered
     */
    private String calculatePrice(int quantity, boolean WhippedCream, boolean Caramel, boolean Chocolate) {
        String basePrice = getString(R.string.price); //retrieves string price from string.xml
        Double formattedPrice = Double.parseDouble(basePrice); //converts string to double

        if (WhippedCream) {
            formattedPrice += 0.2; //cost of whip cream = $0.20
        }
        if (Caramel) {
            formattedPrice += 0.2; //cost of caramel = $0.20
        }
        if (Chocolate) {
            formattedPrice += 0.5; //cost of chocolate = $0.50
        }

        double total = quantity * formattedPrice;
        DecimalFormat decimal = new DecimalFormat("0.00"); //declares new obj decimal with two decimal places in String format
        return decimal.format(total);
    }

    /**
     * Creates an order summary
     *
     * @param total        is the order total
     * @param customerName is the customer's name from user input.
     * @param sugar        is the customer's sugar preference.
     * @param milk         is the customer's milk preference.
     * @param addWhipCream shows if customer wants whip cream.
     * @param addCaramel   shows if customer wants caramel.
     * @param addChocolate shows if customer wants chocolate.
     */
    private String createOrderSummary(String total, String customerName, String sugar, String milk, boolean addWhipCream, boolean addCaramel, boolean addChocolate) {
        String summaryMessage = getString(R.string.name, customerName);
        summaryMessage += "\n\n" + getString(R.string.quantity_summary, quantity);
        summaryMessage += "\n\n" + getString(R.string.sugar_summary, sugar);
        summaryMessage += "\n" + getString(R.string.milk_summary, milk);
        summaryMessage += "\n\n" + getString(R.string.whip_cream_summary, addWhipCream);
        summaryMessage += "\n" + getString(R.string.caramel_summary, addCaramel);
        summaryMessage += "\n" + getString(R.string.chocolate_summary, addChocolate);
        summaryMessage += "\n\n" + getString(R.string.total, total);
        return summaryMessage;
    }

    /**
     * Increase quantity
     */
    public void increment(View view) {
        quantity += 1;
        displayQuantity(quantity);
    }

    /**
     * Decrease quantity
     */
    public void decrement(View view) {
        if (quantity > 0) {
            quantity -= 1;
        } else {
            Toast.makeText(this, getString(R.string.quantity_toast), Toast.LENGTH_SHORT).show();
        }
        displayQuantity(quantity);
    }


    /* *
    * Resets quantity to 0, clears name field and selected choices
    * */
    //TODO put checkboxes in an array
    public void Reset(View view) {
       /* Resets quantity to 0 */
        quantity = 0;
        displayQuantity(0);

        /* Clears name field */
        customerNameField.getText().clear();

        /* Create instances of the RadioGroup and clears selected radio buttons */
        RadioGroup milkGroup1 = (RadioGroup) findViewById(R.id.milk1);
        RadioGroup milkGroup2 = (RadioGroup) findViewById(R.id.milk2);
        RadioGroup sugarGroup = (RadioGroup) findViewById(R.id.sugar);
        milkGroup1.clearCheck();
        milkGroup2.clearCheck();
        sugarGroup.clearCheck();

        /* Create instances of the checkBoxes and un-checks them */
        CheckBox caramel = (CheckBox) findViewById(R.id.caramel);
        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate);
        CheckBox whipCream = (CheckBox) findViewById(R.id.whipCream);
        caramel.setChecked(false);
        chocolate.setChecked(false);
        whipCream.setChecked(false);

        //displayMessage("");
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        String numberAsText = Integer.toString(number);//necessary to convert int number into a string because quantity_text_view accepts only strings
        quantityTextView.setText(numberAsText);
    }


    /**
     * This retrieves the selected radio button for the sugar radio group.
     */
    public String findSugarChoice() {
        int selectedId = sugarOption.getCheckedRadioButtonId();
        RadioButton sugarButton = (RadioButton) findViewById(selectedId);
        return sugarButton.getText().toString();
    }


    /**
     * This retrieves the selected radio button for the milk radio group.
     */
    public String findMilkChoice() {
        if (milkOptions1.getCheckedRadioButtonId() != -1) {
            int selectedId = milkOptions1.getCheckedRadioButtonId();
            RadioButton milkButton = (RadioButton) findViewById(selectedId);
            return milkButton.getText().toString();
        } else {
            int selectedId = milkOptions2.getCheckedRadioButtonId();
            RadioButton milkButton = (RadioButton) findViewById(selectedId);
            return milkButton.getText().toString();
        }
    }


    /**
     * Email intent to send order summary by email
     */
    private void emailIntent(String customerName, String orderSummary) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(getString(R.string.email))); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject, customerName));
        intent.putExtra(Intent.EXTRA_TEXT, orderSummary); //populates email body with the order summary
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, getString(R.string.email_chooser)));
        } else {
            Toast.makeText(this, getString(R.string.email_error_toast), Toast.LENGTH_LONG).show();
        }
    }


}