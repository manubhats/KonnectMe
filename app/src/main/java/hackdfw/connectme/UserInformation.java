package hackdfw.connectme;

/**
 * Created by Arnav on 4/17/2016.
 */
public class UserInformation {

    public String name;
    public String number;
    public String email;
    public String eventID;
    public String userID;
    public String eventName;
    public String eventDesc;
    public String plan;

    public String toString2() {
        return
                "\"name\":\"" + name +
                        "\", \"number\":\"" + number +
                        "\", \"email\":\"" + email +
                        "\", \"eventID\":\"" + eventID +
                        "\", \"userID\":\"" + userID +
                        "\", \"eventName\":\"" + eventName +
                        "\", \"eventDesc\":\"" + eventDesc +
                        "\"}";
    }

    @Override
    public String toString() {
        return
                "\nName='" + name + '\'' +
                " \nNumber='" + number + '\'' +
                " \nEmail='" + email + '\'' +
                " \nEvent aID='" + eventID + '\'' +
                " \nUser ID='" + userID + '\'' +
                " \nEvent Name='" + eventName + '\'' +
                        " \nEvent Plan='" + plan + '\'' +
                " \nEvent Description='" + eventDesc + '\'';
    }
}
