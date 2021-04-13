package com.example.finalyearproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.finalyearproject.entities.Trip;

import java.util.ArrayList;
import java.util.List;

public class MailAsyncTask extends AsyncTask<Void, Void, Void> {
    public static final String USER = "jenfypemail@gmail.com";
    public static final String PASSWORD = "FinalYearProject21!";

    private Context context;
    private String recipient;
    private String userName;
    private String name;
    private String subject;
    private String body;
    private StringBuilder tripInformation;
    private StringBuilder safetyInformation;
    private EmailTypeEnum mailType;
    private List<String> emailList = new ArrayList<String>();
    private GMailSender sender;

    public MailAsyncTask(Context pContext, EmailTypeEnum pMailType, List<String> pRecipients, String pFirstName) {
        sender = new GMailSender(USER, PASSWORD);
        mailType = pMailType;
        context = pContext;
        emailList = pRecipients;
        name = pFirstName;
        if(pMailType == EmailTypeEnum.TripInformation) {
            tripInformation = new StringBuilder();
        }
        else if(pMailType == EmailTypeEnum.SafetyAlert) {
            safetyInformation = new StringBuilder();
        }
    }

    public void setTripInformation(Trip trip) {
        tripInformation.append(trip.getTitle()).append(" \n");
        tripInformation.append(trip.getDescription()).append(" \n");
        tripInformation.append(trip.getStartDate()).append(" \n");
        tripInformation.append(trip.getEndDate()).append(" \n");
        tripInformation.append(trip.getItinerary()).append(" \n");
    }

    public void setSafetyInformation(String pUserName, String pLocation) {
        userName = pUserName;
        safetyInformation.append(pLocation).append("\n");
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
                    break;
                case TripInformation:
                    addTripInformationDetails();
                    break;
                case WelcomeEmail:
                    addWelcomeEmailDetails();
                    break;
                case SafetyAlert:
                    addSafetyEmailDetails();
                    break;
            }
            for (String email : emailList ) {
                sender.sendMail(subject, body, USER, email);
            }
            Log.d("sendEmail", "done");
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

    private void addContactVerificationEmailDetails() {
        subject = "Added as Contact";
        body = "Hi " + name + ", " + " \n\nWe are notifying you to let you know that a user has added you as a contact on our app. " +
                "\n You may continue to receive emails from us in the future. If you believe this is a mistake, please contact us immediately. \n Thank you.";
    }

    private void addTripInformationDetails() {
        subject = "Added to a Trip";
        body = "Hi " + name + ", " + " \n\nWe are notifying you to let you know that a user has added you as a contact for a trip on our app. " + "\n\n" + tripInformation + "\n" +
                "\nYou may continue to receive emails from us in the future for this trip. If you believe this is a mistake, please contact us immediately. \nThank you.";
    }

    private void addWelcomeEmailDetails() {
        subject = "Welcome!";
        body = "Hello! \nThank you for signing up for our services. We hope you find our app useful on your travels to help you stay safe. If you would like to provide feedback," +
                " don't hesitate to let us know. \nThank you!";
    }

    private void addSafetyEmailDetails() {

        subject = "Safety Alert for " + userName;
        body = "Hello. \n\nThis email has been sent to you because " + userName + " has triggered their safety alert because they feel that their safety has been compromised." +
                "At the time that this email is being sent, this is their last registered location: " +
                "\n\nWe recommend that you get into contact with this person as soon as you can. \n\nThank you.";
    }
}
