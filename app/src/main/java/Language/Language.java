package Language;

import java.util.Locale;

import ai.api.AIConfiguration;

public class Language {

    //fixed variables
    //default
    String RECONGNIZER_INTENT_LOCALE = "en";
    Locale ttsLanguage = Locale.ENGLISH;
    AIConfiguration.SupportedLanguages DIALOGFLOW_LANGUAGE = AIConfiguration.SupportedLanguages.English;


    String SCAN_FACE_RESPONSE = "LET ME SCAN YOUR FACE";
    String VERIFYING_FACE_RESPONSE = "VERIFYING FACE";
    String VERIFYING_FACE_FAIL_RESPONSE = "SORRY, PLEASE TRY AGAIN.";


    public String getRECONGNIZER_INTENT_LOCALE(){ return RECONGNIZER_INTENT_LOCALE;}

    public Locale getTtsLanguage() {
        return ttsLanguage;
    }

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

    public String getAlertUserResponse(String patientName){
        return  "ALERT ALERT "+ patientName + ", TIME TO TAKE MEDICINE";
    }

    public String getOpenCabinetResponse(String boxNo) {
        return "OPENING BOX " + boxNo;
    }

    public String getCloseCabinetResponse(String boxNo) {
        return "CLOSING BOX " + boxNo;
    }

    public String getGiveMedInstructionResponse(String boxNo,String medicineName, int Dosage, String metricValue) {
        return "Please take " + medicineName + " from "+ boxNo +" and take only "+ Dosage + " " + metricValue;
    }

}
