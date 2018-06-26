package Language;

import java.util.Locale;

import ai.api.AIConfiguration;

public class English extends Language {

    //fixed variables
    String RECONGNIZER_INTENT_LOCALE = "en";
    Locale ttsLanguage = Locale.ENGLISH;
    AIConfiguration.SupportedLanguages DIALOGFLOW_LANGUAGE = AIConfiguration.SupportedLanguages.English;
    String SCAN_FACE_RESPONSE = "LET ME SCAN YOUR FACE";
    String VERIFYING_FACE_RESPONSE = "VERIFYING FACE";
    String VERIFYING_FACE_FAIL_RESPONSE = "SORRY, PLEASE TRY AGAIN.";


    public Locale getTtsLanguage() {
        return ttsLanguage;
    }

    public String getRECONGNIZER_INTENT_LOCALE(){ return RECONGNIZER_INTENT_LOCALE;}

    public AIConfiguration.SupportedLanguages getDIALOGFLOW_LANGUAGE() {
        return DIALOGFLOW_LANGUAGE;
    }

    public String getSCAN_FACE_RESPONSE() {
        return SCAN_FACE_RESPONSE;
    }

    public String getVERIFYING_FACE_RESPONSE() {
        return VERIFYING_FACE_RESPONSE;
    }

    public String getVERIFYING_FACE_FAIL_RESPONSE() {
        return VERIFYING_FACE_FAIL_RESPONSE;
    }

    @Override
    public String getAlertUserResponse(String patientName){
        return  "ALERT ALERT "+ patientName + ", TIME TO TAKE MEDICINE";
    }

    @Override
    public String getOpenCabinetResponse(String boxNo) {
        return "OPENING BOX " + boxNo;
    }

    @Override
    //TODO: NEED TO ADD AN VARIABLE TO SAY CLOSING BOX 2 FOR EXAMPLE
    //      FOR NOW IS JUST ONE BOX SO WILL HAVE THE LAST SENTENCE
    public String getCloseCabinetResponse(String boxNo) {
        return "Okay,Closing Cabinet. I will remind you for when's its time for your next medicine";
    }

    @Override
    public String getGiveMedInstructionResponse(String boxNo,String medicineName, int Dosage, String metricValue) {
        return "Please take " + medicineName + " from "+ boxNo +" and take only "+ Dosage + " " + metricValue;
    }


}
