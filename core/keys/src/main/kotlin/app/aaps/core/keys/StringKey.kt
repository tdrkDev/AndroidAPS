package app.aaps.core.keys

enum class StringKey(
    override val key: String,
    override val defaultValue: String,
    override val defaultedBySM: Boolean = false,
    override val showInApsMode: Boolean = true,
    override val showInNsClientMode: Boolean = true,
    override val showInPumpControlMode: Boolean = true,
    override val dependency: BooleanPreferenceKey? = null,
    override val negativeDependency: BooleanPreferenceKey? = null,
    override val hideParentScreenIfHidden: Boolean = false
) : StringPreferenceKey {

    GeneralUnits("units", "mg/dl"),
    GeneralLanguage("language", "default", defaultedBySM = true),
    GeneralPatientName("patient_name", ""),
    GeneralSkin("skin", ""),
    GeneralDarkMode("use_dark_mode", "dark", defaultedBySM = true),

    ProtectionMasterPassword("master_password", ""),
    ProtectionSettingsPassword(key_settings_password, ""),
    ProtectionSettingsPin(key_settings_pin, ""),
    ProtectionApplicationPassword(key_application_password, ""),
    ProtectionApplicationPin(key_application_pin, ""),
    ProtectionBolusPassword(key_bolus_password, ""),
    ProtectionBolusPin(key_bolus_pin, ""),

    SafetyAge(key_safety_age, "adult"),
    LoopApsMode(key_aps_mode, "open" /* ApsMode.OPEN.name */),
    MaintenanceEmail(key_maintenance_logs_email, "logs@aaps.app", defaultedBySM = true),
    MaintenanceIdentification(key_identification_for_crash_report, ""),
    AutomationLocation(key_location, "PASSIVE", hideParentScreenIfHidden = true),

    SmsAllowedNumbers(key_smscommunicator_allowednumbers, ""),
    SmsOtpPassword(key_smscommunicator_otp_password, "", dependency = BooleanKey.SmsAllowRemoteCommands),

    VirtualPumpType(key_virtual_pump_type, "Generic AAPS"),

    NsClientUrl(key_nsclientinternal_url, ""),
    NsClientApiSecret(key_nsclientinternal_api_secret, ""),
    NsClientWifiSsids(key_ns_wifi_ssids, "", dependency = BooleanKey.NsClientUseWifi),
    NsClientAccessToken(key_ns_client_token, ""),
    TidepoolUsername(key_tidepool_username, ""),
    TidepoolPassword(key_tidepool_password, ""),
}