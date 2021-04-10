package com.example.finalyearproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.finalyearproject.entities.Trip;

public class MailAsyncTask extends AsyncTask<Void, Void, Void> {
    public static final String USER = "jenfypemail@gmail.com";
    public static final String PASSWORD = "FinalYearProject21!";

    private Context context;
    private String recipient;
    private String name;
    private String subject;
    private String body;
    private StringBuilder tripInformation;
    private EmailTypeEnum mailType;
    private GMailSender sender;

    public MailAsyncTask(Context pContext, EmailTypeEnum pMailType, String pRecipient, String pFirstName) {
        sender = new GMailSender(USER, PASSWORD);
        mailType = pMailType;
        context = pContext;
        recipient = pRecipient;
        name = pFirstName;
        if(pMailType == EmailTypeEnum.TripInformation) {
            tripInformation = new StringBuilder();
        }
    }

    public void setTripInformation(Trip trip) {
        tripInformation.append(trip.getTitle()).append(" \n");
        tripInformation.append(trip.getDescription()).append(" \n");
        tripInformation.append(trip.getStartDate()).append(" \n");
        tripInformation.append(trip.getEndDate()).append(" \n");
        tripInformation.append(trip.getItinerary()).append(" \n");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //dialog = new ProgressDialog(AddContactActivity.this);
        //dialog.setMessage("Please wait...");
        //dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            switch(mailType)
            {
                case ContactVerification:
                    addContactVerificationEmailDetails();
                    sender.sendMail(subject, body, USER, "lebantij@aston.ac.uk");
                    break;
                case TripInformation:
                    addTripInformationDetails();
                    sender.sendMail(subject, body, USER, "lebantij@aston.ac.uk");
                    break;
            }
            Log.d("send", "done");
        }
        catch(Exception e) {
            Log.d("exceptionsending", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //dialog.cancel();
        Toast.makeText(context, "Contact Notified.", Toast.LENGTH_SHORT).show();
    }

    private void addContactVerificationEmailDetails()
    {
        subject = "Added as Contact";
        body = "Hi " + name + ", " + " \n\nWe are notifying you to let you know that a user has added you as a contact on our app. " +
                "\n You may continue to receive emails from us in the future. If you believe this is a mistake, please contact us immediately. \n Thank you.";
    }

    private void addTripInformationDetails()
    {
        subject = "Added to a Trip";
        body = "Hi " + name + ", " + " \n\nWe are notifying you to let you know that a user has added you as a contact for a trip on our app. " + "\n\n" + tripInformation + "\n" +
                "\nYou may continue to receive emails from us in the future for this trip. If you believe this is a mistake, please contact us immediately. \nThank you.";
    }
}
