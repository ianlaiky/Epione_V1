package Language;

import ai.api.AIConfiguration;

public class English implements Language {

    //fixed variables
    String RECONGNIZER_INTENT_LOCALE = "en";
    AIConfiguration.SupportedLanguages DIALOGFLOW_LANGUAGE = AIConfiguration.SupportedLanguages.English;
     String SCAN_FACE_RESPONSE = "LET ME SCAN YOUR FACE";
     String VERIFYING_FACE_RESPONSE = "VERIFYING FACE";
     String VERIFYING_FACE_FAIL_RESPONSE = "SORRY, PLEASE TRY AGAIN.";


    String GIVE_MED_INSTRUCTION_RESPONSE = "";

    String OPEN_CABINET_RESPONSE = " ";
    String CLOSE_CABINET_RESPONSE = "";

    @Override
    public String getAlertUserResponse(String patientName){
        return  "ALERT ALERT "+ patientName + ", TIME TO TAKE MEDICINE";
    }

    @Override
    public String getOpenCabinetResponse(String boxNo) {
        return "OPENING BOX " + boxNo;
    }

    @Override
    public String getCloseCabinetResponse(String boxNo) {
        return "CLOSING BOX " + boxNo;
    }

    @Override
    public String getGiveMedInstructionResponse(String boxNo,String medicineName, int Dosage, String metricValue) {
        return "Please take " + medicineName + " from "+ boxNo +" and take only "+ Dosage + " " + metricValue;
    }


}
