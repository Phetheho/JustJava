package com.example.codetribe.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends ActionBarActivity {


    int numOfCoffees = 0;
    int coffee;
    int coffeePrice = 10;
    final int whippedCreamPrice = 5;
    final int chocolatePrice = 7;
    String priceMessage;
    private boolean hasWhippedCream = false;
    private boolean hasChocolate = false;

    String orderSummary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display(numOfCoffees);

        priceMessage = "Free";
        displayMessage(priceMessage);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        // only email apps should handle this
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java Order For " + getCustomerName());
        intent.putExtra(Intent.EXTRA_TEXT, orderSummary);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity);
        quantityTextView.setText("" + number);
    }


    /**
     * This method displays the given price on the screen.
     */
    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.summary_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    public void addCoffee(View view)
    {
        if (numOfCoffees >= 0 & numOfCoffees < 100) {
            coffee = 1;
            numOfCoffees = numOfCoffees + coffee;
            display(numOfCoffees);

        }

    }

    public void subtractCoffee(View view)
    {
        if (numOfCoffees > 0) {

            numOfCoffees = numOfCoffees - coffee;
            display(numOfCoffees);
        }
        else {

            TextView newQuantityText = (TextView) findViewById(R.id.quantity);
            newQuantityText.setText("No Coffee");
        }

    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @return the price
     */
    private int calculatePrice(int quantity) {


        int totPrice;

        if (hasWhippedCream == true)
            totPrice = quantity * (coffeePrice + whippedCreamPrice);
        else
            totPrice = quantity * coffeePrice;

        if (hasChocolate == true)
            totPrice = quantity * (coffeePrice + chocolatePrice);
        else
            totPrice = quantity * coffeePrice;


        if (hasWhippedCream == true & hasChocolate == true)
            totPrice = quantity * (coffeePrice + whippedCreamPrice + chocolatePrice);
        else
            totPrice = quantity * coffeePrice;
        return totPrice;
    }



    public String getCustomerName ()
    {
        EditText newText = (EditText) findViewById(R.id.name_edit_view);

        String cusName = newText.getText().toString();

        return cusName;
    }


    public void checkBoxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.whipped_cream_checkbox:
                if (checked)
                    hasWhippedCream = true;
                else
                    hasWhippedCream = false;
                break;
            case R.id.chocolate_checkbox:
                if (checked)
                    hasChocolate = true;
                else
                    hasChocolate = false;
                break;

        }
    }



    public String createOrderSummary(int orderPrice)
    {

        String customerName = getCustomerName();

        orderSummary = "Name: " + customerName;

        orderSummary = orderSummary + "\n Add whipped cream? " + hasWhippedCream;

        orderSummary = orderSummary + "\n Add Chocolate? " + hasChocolate;

        orderSummary = orderSummary + "\n Quantity: " + numOfCoffees;

        orderPrice = calculatePrice(numOfCoffees);

        orderSummary = orderSummary + "\n Total: R" + orderPrice;

        orderSummary = orderSummary + "\n Thank You, " + customerName;

        return orderSummary;

    }








}