package hackdfw.connectme;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manubhat on 4/17/16.
 */

public class User implements Serializable {


    private String initiator_name;
    private String initiator_phone_number;
    private String event_id;
    private String event_name;
    private String event_message;

    private ArrayList<ContactID> conts;

    public User(String initiator_name, String initiator_phone_number, String event_id,
                String event_name, String event_message,ArrayList<ContactID> conts) {

        this.setInitiator_name(initiator_name);
        this.setInitiator_phone_number(initiator_phone_number);
        this.setEvent_id(event_id);
        this.setEvent_name(event_name);
        this.setEvent_message(event_message);
        //ArrayList of contacts
        this.setConts(conts);
    }

    public String getInitiator_name() {
        return initiator_name;
    }

    public void setInitiator_name(String initiator_name) {
        this.initiator_name = initiator_name;
    }

    public String getInitiator_phone_number() {
        return initiator_phone_number;
    }

    public void setInitiator_phone_number(String initiator_phone_number) {
        this.initiator_phone_number = initiator_phone_number;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_message() {
        return event_message;
    }

    public void setEvent_message(String event_message) {
        this.event_message = event_message;
    }



    public ArrayList<ContactID> getConts() {
        return conts;
    }

    public void setConts(ArrayList<ContactID> conts) {
        this.conts = conts;
    }

}

class ContactID{

    String contact_id,contact_name,contact_number;
    ContactID(String contact_id,String contact_name,String contact_number){
        this.contact_id = contact_id;
        this.contact_name = contact_name;
        this.contact_number = contact_number;
    }
}