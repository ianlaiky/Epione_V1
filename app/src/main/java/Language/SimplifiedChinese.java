package Language;

import java.util.Locale;

import ai.api.AIConfiguration;

public class SimplifiedChinese extends Language {

    String RECONGNIZER_INTENT_LOCALE = "zh";
    Locale ttsLanguage = Locale.SIMPLIFIED_CHINESE;
    AIConfiguration.SupportedLanguages DIALOGFLOW_LANGUAGE = AIConfiguration.SupportedLanguages.ChineseChina;
    String SCAN_FACE_RESPONSE = "让我扫描你的脸";
    String VERIFYING_FACE_RESPONSE = "现在验证脸部";
    String VERIFYING_FACE_FAIL_RESPONSE = "抱歉，请再试一次";

    public Locale getTtsLanguage() {
        return ttsLanguage;
    }

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
        return
                "警报警报 " + patientName + ", 吃药的时间来了";
    }

    @Override
    public String getOpenCabinetResponse(String boxNo) {
        return "现在开盒子 " + boxNo;
    }

    @Override
    public String getCloseCabinetResponse(String boxNo) {
        return "现在关盒子 " + boxNo;
    }

    @Override
    public String getGiveMedInstructionResponse(String boxNo, String medicineName, int Dosage, String metricValue) {
        return "请拿 " + medicineName + " 从盒子" + boxNo + " 然后请拿 "+ Dosage + " " + metricValue;
    }
}
