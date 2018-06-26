package Language;

import ai.api.AIConfiguration;

public class SimplifiedChinese extends Language {

    String RECONGNIZER_INTENT_LOCALE = "zh";
    AIConfiguration.SupportedLanguages DIALOGFLOW_LANGUAGE = AIConfiguration.SupportedLanguages.ChineseChina;
    String SCAN_FACE_RESPONSE = "让我";
    String VERIFYING_FACE_RESPONSE = "VERIFYING FACE";
    String VERIFYING_FACE_FAIL_RESPONSE = "SORRY, PLEASE TRY AGAIN.";


    @Override
    public String getRECONGNIZER_INTENT_LOCALE() {
        return RECONGNIZER_INTENT_LOCALE;
    }

    @Override
    public AIConfiguration.SupportedLanguages getDIALOGFLOW_LANGUAGE() {
        return DIALOGFLOW_LANGUAGE;
    }

    @Override
    public String getSCAN_FACE_RESPONSE() {
        return SCAN_FACE_RESPONSE;
    }

    @Override
    public String getVERIFYING_FACE_RESPONSE() {
        return VERIFYING_FACE_RESPONSE;
    }

    @Override
    public String getVERIFYING_FACE_FAIL_RESPONSE() {
        return VERIFYING_FACE_FAIL_RESPONSE;
    }

    @Override
    public String getAlertUserResponse(String patientName) {
        return ""
    }

    @Override
    public String getOpenCabinetResponse(String boxNo) {
        return ""
    }

    @Override
    public String getCloseCabinetResponse(String boxNo) {
        return ""
    }

    @Override
    public String getGiveMedInstructionResponse(String boxNo, String medicineName, int Dosage, String metricValue) {
        return ""
    }
}
