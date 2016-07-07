package me.annaliu.justjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0; //global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        String price = getString(R.string.price); //retrieves string price from string.xml
        Double formattedPrice = Double.parseDouble(price); //converts string to double
        Double total = formattedPrice * quantity;
        DecimalFormat decimal = new DecimalFormat("0.00"); //declares new obj decimal with two decimal places
        String formattedTotal = decimal.format(total); //stores price as a string and formats it according to the decimal object
        String message = "$" + formattedTotal;
        displayMessage(message);
        displayThanks("Thank you!");
    }

    /* Increase quantity */
    public void increment(View view) {
        quantity += 1;
        display(quantity);
    }

    /*Resets quantity and price to 0*/
    public void Reset(View view) {
        display(0);
        displayMessage("");
    }

    /* Decrease quantity */
    public void decrement(View view) {
        if (quantity > 0) {
            quantity -= 1;
        }
        display(quantity);
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }


    /**
     * This method displays the given price on the screen.
     */
    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(message);
    }

    /**
     * This method displays the thank you message on the screen.
     */
    private void displayThanks(String thanks) {
        TextView thanksTextView = (TextView) findViewById(R.id.thanks);
        thanksTextView.setText(thanks);
    }
}