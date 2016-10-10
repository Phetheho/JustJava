package com.example.codetribe.justjava;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileOutputStream;
import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends Activity {


    int numOfCoffees = 0;
    int coffee;
    int coffeePrice = 10;
    final int whippedCreamPrice = 5;
    final int chocolatePrice = 7;
    //String priceMessage;
    private boolean hasWhippedCream = false;
    private boolean hasChocolate = false;

    String orderSummary;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public final static String EXTRA_MESSAGE = "com.example.codetribe.justjava.MESSAGE";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display(numOfCoffees);


        //displayMessage(priceMessage);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        int total = calculatePrice(numOfCoffees);


        Intent intent = new Intent(Intent.ACTION_SENDTO);
        // only email apps should handle this
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java Order For " + getCustomerName());
        intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary(total));
        //intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }


        writeOrderOnFile(createOrderSummary(total));

    }

    public void showLocation(View view) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:28.2, -25.7"));
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
     * <p>
     * private void displayPrice(int number) {
     * TextView priceTextView = (TextView) findViewById(R.id.summary_text_view);
     * priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
     * }
     */

    public void addCoffee(View view) {
        if (numOfCoffees >= 0 & numOfCoffees < 100) {
            coffee = 1;
            numOfCoffees = numOfCoffees + coffee;
            display(numOfCoffees);

        }

    }

    public void subtractCoffee(View view) {
        if (numOfCoffees > 0) {

            numOfCoffees = numOfCoffees - coffee;
            display(numOfCoffees);
        } else {

            TextView newQuantityText = (TextView) findViewById(R.id.quantity);
            newQuantityText.setText("No Coffee");
        }

    }

    /**
     * This method displays the given text on the screen.

     /*private void displayMessage(String message) {
     TextView orderSummaryTextView = (TextView) findViewById(R.id.summary_text_view);
     orderSummaryTextView.setText(message);
     }*/

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @return the price
     */
    private int calculatePrice(int quantity) {


        int totPrice = quantity * coffeePrice;

        if (hasWhippedCream == true)
            totPrice = totPrice + whippedCreamPrice;


        if (hasChocolate == true)
            totPrice = totPrice + chocolatePrice;


        return totPrice;
    }


    public String getCustomerName() {
        EditText newText = (EditText) findViewById(R.id.name_edit_view);

        String cusName = newText.getText().toString();

        return cusName;
    }


    public void checkBoxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
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


    public String createOrderSummary(int orderPrice) {

        String customerName = getCustomerName();

        orderSummary = " Name: " + customerName;

        orderSummary = orderSummary + "\n Add whipped cream? " + hasWhippedCream;

        orderSummary = orderSummary + "\n Add Chocolate? " + hasChocolate;

        orderSummary = orderSummary + "\n Quantity: " + numOfCoffees;

        orderPrice = calculatePrice(numOfCoffees);

        orderSummary = orderSummary + "\n Total: R" + orderPrice;

        return orderSummary;

    }


    public void sendMessage(View view) {

        showMyLocation();

    }



    public void showMyLocation(){


        Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class); // Creating a new intent with the name intent, using the Intent costructor
        // The Intent costructor receives 2 paramaters
        // 1. A Context as its first parameter (this is used because the Activity class is a subclass of Context
        // 2. The Class of the app component to which the system should deliver the Intent (in this case, the activity that should be started)


        EditText editText = (EditText) findViewById(R.id.edit_message); // Creating a new editText object


        String message = editText.getText().toString(); // we assigning whatever we're going to get from the editText to the message variable of type String


        intent.putExtra(EXTRA_MESSAGE, message); //The putExtra() method adds the EditText's value to the intent.


        startActivity(intent);

    }



    //Files

    public void writeOrderOnFile(String orderMessage){

        String filename = "myOrder";

        FileOutputStream outputStream;


        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(orderMessage.getBytes());
            outputStream.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}