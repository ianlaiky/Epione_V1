package Language;

import Model.Patient;
import ai.api.AIConfiguration;

public interface Language {

    //default languages
      String RECONGNIZER_INTENT_LOCALE = "";
      AIConfiguration.SupportedLanguages DIALOGFLOW_LANGUAGE = AIConfiguration.SupportedLanguages.English;
     String SCAN_FACE_RESPONSE = "";
     String VERIFYING_FACE_RESPONSE = "";
     String VERIFYING_FACE_FAIL_RESPONSE = "";



     String GIVE_MED_INSTRUCTION_RESPONSE = "";
     String OPEN_CABINET_RESPONSE = "";
     String CLOSE_CABINET_RESPONSE = "";
    String ALERT_USER_RESPONSE = "";


     public String getAlertUserResponse(String patientName);
     public String getOpenCabinetResponse(String boxNo);
    public String getCloseCabinetResponse(String boxNo);
    public String getGiveMedInstructionResponse(String BoxNo,String medicineName,int Dosage,String metricValue);




}
