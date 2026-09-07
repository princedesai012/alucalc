package com.softellix.alucalc.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object LanguageManager {
    var currentLanguage by mutableStateOf("ENGLISH")

    fun setLanguage(lang: String) {
        currentLanguage = lang.uppercase()
    }

    fun tr(key: String): String {
        return when (currentLanguage) {
            "HINDI" -> hindiStrings[key] ?: englishStrings[key] ?: key
            "GUJARATI" -> gujaratiStrings[key] ?: englishStrings[key] ?: key
            else -> englishStrings[key] ?: key
        }
    }

    private val englishStrings = mapOf(
        "welcome_back" to "Welcome Back",
        "create_account" to "Create Account",
        "login" to "Login",
        "register" to "Register",
        "phone_number" to "PHONE NUMBER",
        "password" to "PASSWORD",
        "new_project" to "New Estimation Project",
        "calculate_sub" to "Calculate dimensions, profiles, and tracks",
        "recent_projects" to "Recent Projects",
        "report_history" to "Report History",
        "all_projects" to "Projects",
        "settings" to "Settings & Profile",
        "select_profile" to "Select Profile",
        "add_windows" to "Add Windows",
        "save_calculate" to "Save & Calculate",
        "add_another_window" to "Add Another Window",
        "back_to_home" to "Back to Home",
        "logout" to "Logout",
        "height" to "HEIGHT",
        "width" to "WIDTH",
        "quantity" to "Quantity",
        "track_type" to "Track Type",
        "home" to "Home",
        "app_language" to "Application Language",
        "download_pdf" to "Download PDF Report",
        "share_report" to "Share Complete Report",
        "next" to "Next",
        "report" to "Report",
        "project_details" to "PROJECT DETAILS",
        "project_name" to "Project Name",
        "contact_info" to "Contact Information",
        "street_address" to "Street Address",
        "choose_profile" to "Choose Profile Type",
        "calc_breakdown" to "DETAILED CALCULATION BREAKDOWN",
        "estimator" to "ESTIMATOR",
        "created_date" to "CREATED DATE",
        "total_windows" to "TOTAL WINDOWS"
    )

    private val hindiStrings = mapOf(
        "welcome_back" to "आपका स्वागत है",
        "create_account" to "खाता बनाएं",
        "login" to "लॉगिन करें",
        "register" to "रजिस्टर करें",
        "phone_number" to "फोन नंबर",
        "password" to "पासवर्ड",
        "new_project" to "नया अनुमान प्रोजेक्ट",
        "calculate_sub" to "आयाम, प्रोफ़ाइल और ट्रैक की गणना करें",
        "recent_projects" to "हाल के प्रोजेक्ट",
        "report_history" to "रिपोर्ट इतिहास",
        "all_projects" to "सभी प्रोजेक्ट",
        "settings" to "सेटिंग्स और प्रोफ़ाइल",
        "select_profile" to "प्रोफ़ाइल चुनें",
        "add_windows" to "खिड़कियां जोड़ें",
        "save_calculate" to "सहेजें और गणना करें",
        "add_another_window" to "एक और खिड़की जोड़ें",
        "back_to_home" to "मुख्य पृष्ठ पर जाएं",
        "logout" to "लॉग आउट करें",
        "height" to "ऊंचाई (Height)",
        "width" to "चौड़ाई (Width)",
        "quantity" to "मात्रा (Quantity)",
        "track_type" to "ट्रैक प्रकार (Track Type)",
        "home" to "होम",
        "app_language" to "एप्लिकेशन भाषा",
        "download_pdf" to "पीडीएफ रिपोर्ट डाउनलोड करें",
        "share_report" to "संपूर्ण रिपोर्ट साझा करें",
        "next" to "आगे बढ़ें",
        "report" to "रिपोर्ट",
        "project_details" to "प्रोजेक्ट विवरण",
        "project_name" to "प्रोजेक्ट का नाम",
        "contact_info" to "संपर्क जानकारी",
        "street_address" to "पता (Street Address)",
        "choose_profile" to "प्रोफ़ाइल प्रकार चुनें",
        "calc_breakdown" to "विस्तृत गणना विवरण",
        "estimator" to "अनुमानक (Estimator)",
        "created_date" to "बनाने की तिथि",
        "total_windows" to "कुल खिड़कियां"
    )

    private val gujaratiStrings = mapOf(
        "welcome_back" to "તમારું સ્વાગત છે",
        "create_account" to "ખાતું બનાવો",
        "login" to "લોગિન કરો",
        "register" to "રજીસ્ટર કરો",
        "phone_number" to "ફોન નંબર",
        "password" to "પાસવર્ડ",
        "new_project" to "નવો અંદાજ પ્રોજેક્ટ",
        "calculate_sub" to "પરિમાણો, પ્રોફાઇલ્સ અને ટ્રેકની ગણતરી કરો",
        "recent_projects" to "તાજેતરના પ્રોજેક્ટ્સ",
        "report_history" to "રિપોર્ટ ઈતિહાસ",
        "all_projects" to "તમામ પ્રોજેક્ટ્સ",
        "settings" to "સેટિંગ્સ અને પ્રોફાઈલ",
        "select_profile" to "પ્રોફાઈલ પસંદ કરો",
        "add_windows" to "બારીઓ ઉમેરો",
        "save_calculate" to "સાચવો અને ગણતરી કરો",
        "add_another_window" to "બીજી બારી ઉમેરો",
        "back_to_home" to "હોમ પર પાછા જાઓ",
        "logout" to "લોગ આઉટ કરો",
        "height" to "ઊંચાઈ (Height)",
        "width" to "પહોળાઈ (Width)",
        "quantity" to "જથ્થો (Quantity)",
        "track_type" to "ટ્રેકનો પ્રકાર (Track Type)",
        "home" to "હોમ",
        "app_language" to "એપ્લિકેશન ભાષા",
        "download_pdf" to "પીડીએફ રિપોર્ટ ડાઉનલોડ કરો",
        "share_report" to "સંપૂર્ણ રિપોર્ટ શેર કરો",
        "next" to "આગળ વધો",
        "report" to "રિપોર્ટ",
        "project_details" to "પ્રોજેક્ટ વિગતો",
        "project_name" to "પ્રોજેક્ટનું નામ",
        "contact_info" to "સંપર્ક માહિતી",
        "street_address" to "સરનામું (Street Address)",
        "choose_profile" to "પ્રોફાઈલનો પ્રકાર પસંદ કરો",
        "calc_breakdown" to "વિગતવાર ગણતરી વિગતો",
        "estimator" to "અંદાજકાર (Estimator)",
        "created_date" to "બનાવ્યા તારીખ",
        "total_windows" to "કુલ બારીઓ"
    )
}
