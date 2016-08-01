package me.annaliu.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
//TODO change checked checkbox and radiobutton colors
//TODO change milk choices into two lines
//TODO make icon transparent
//TODO make name field static after input
//TODO instant running total of price after each selection
//TODO make milk and sugar option mandatory

    /**
     * Global variables
     */
    int quantity = 0; //number of coffees

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the get total button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipCream);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        CheckBox caramelCheckBox = (CheckBox) findViewById(R.id.caramel);
        boolean hasCaramel = caramelCheckBox.isChecked();
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        TextView name = (TextView) findViewById(R.id.nameField);
        String customerName = name.getText().toString();

        String total = calculatePrice(quantity, hasWhippedCream, hasCaramel, hasChocolate); //calls the calculatePrice method and stores the return value
        String orderSummary = createOrderSummary(total, customerName, hasWhippedCream, hasCaramel, hasChocolate);
//        displayMessage(orderSummary);

        /**
         * Email intent to send order summary by email
         */
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:test@example.com")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java Order for " + customerName);
        intent.putExtra(Intent.EXTRA_TEXT, orderSummary); //populates email body with the order summary
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Send Order By:"));
        } else {
            Toast.makeText(this, "Error: There are no email clients installed.", Toast.LENGTH_LONG).show();
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
            formattedPrice += 0.2;
        }
        if (Caramel) {
            formattedPrice += 0.2;
        }
        if (Chocolate) {
            formattedPrice += 0.5;
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
     * @param addWhipCream shows if customer wants whip cream.
     * @param addCaramel   shows if customer wants caramel.
     * @param addChocolate shows if customer wants chocolate.
     */
    private String createOrderSummary(String total, String customerName, boolean addWhipCream, boolean addCaramel, boolean addChocolate) {
        String summaryMessage = "NAME: " + customerName;
        summaryMessage += "\n\nQUANTITY: " + quantity;
        summaryMessage += "\n\nWhipped Cream: " + addWhipCream;
        summaryMessage += "\nCaramel: " + addCaramel;
        summaryMessage += "\nChocolate: " + addChocolate;
        summaryMessage += "\n\nTOTAL: $" + total;
        return summaryMessage;
    }

    /* Increase quantity */
    public void increment(View view) {
        quantity += 1;
        displayQuantity(quantity);
    }

    /*Resets quantity and price to 0*/
    public void Reset(View view) { //TODO clear choices when reset button is hit
        quantity = 0;
        displayQuantity(0);
        displayMessage("");
    }

    /* Decrease quantity */
    public void decrement(View view) {
        if (quantity > 0) {
            quantity -= 1;
        } else {
            Toast.makeText(this, "Please select a valid quantity", Toast.LENGTH_SHORT).show();
        }
        displayQuantity(quantity);
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number); // "" necessary to turn int number into a string (quantity_text_view accepts only strings)
    }


    /**
     * This method displays the order summary on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

}