package com.medicconnect.config;

import java.util.*;

public final class FieldConfig {

    private FieldConfig() {}

    // ----------------------
    // Field bits
    // ----------------------
    public static final long F_NAME = 1L << 0;
    public static final long F_DOB = 1L << 1;
    public static final long F_GENDER = 1L << 2;
    public static final long F_BLOOD_GROUP = 1L << 3;
    public static final long F_ORG_NAME = 1L << 4;
    public static final long F_CATEGORY = 1L << 5;
    public static final long F_REG_NO = 1L << 6;
    public static final long F_YEAR_EST = 1L << 11;
    public static final long F_OWNERSHIP_TYPE = 1L << 12;

    public static final long F_ADDRESS_FULL_ORG = 1L << 14;
    public static final long F_ADDRESS_COUNTRY_ORG = 1L << 15;
    public static final long F_ADDRESS_STATE_ORG = 1L << 16;
    public static final long F_ADDRESS_CITY_ORG = 1L << 17;
    public static final long F_ADDRESS_PIN_ORG = 1L << 18;

    public static final long F_ADDRESS_FULL_PERSONAL = 1L << 19;
    public static final long F_ADDRESS_COUNTRY_PERSONAL = 1L << 20;
    public static final long F_ADDRESS_STATE_PERSONAL = 1L << 21;
    public static final long F_ADDRESS_CITY_PERSONAL = 1L << 22;
    public static final long F_ADDRESS_PIN_PERSONAL = 1L << 23;

    public static final long F_EMAIL_PERSONAL = 1L << 24;
    public static final long F_MOBILE_PERSONAL = 1L << 25;

    public static final long F_LANDLINE_COUNTRY = 1L << 26;
    public static final long F_LANDLINE_AREA = 1L << 27;
    public static final long F_LANDLINE_LOCAL = 1L << 28;
    public static final long F_DOCUMENTS_ORG = 1L << 29;
    public static final long F_DOCUMENTS_PERSONAL = 1L << 30;

    public static final long F_CREATE_PASSWORD = 1L << 31;
    public static final long F_CONFIRM_PASSWORD = 1L << 32;
    public static final long F_AGREEMENT = 1L << 33;

    public static final long F_ORG_EMAIL = 1L << 34;
    public static final long F_ORG_MOBILE = 1L << 35;

    // ----------------------
    // "Same as Organization" checkboxes
    // ----------------------
    public static final long F_SAME_AS_ORG_EMAIL = 1L << 36;
    public static final long F_SAME_AS_ORG_MOBILE = 1L << 37;

    // ----------------------
    // Pre-built form masks
    // ----------------------
    public static final long FORM_HOSPITAL_REG_PART1 =
            F_ORG_NAME | F_CATEGORY | F_REG_NO | F_YEAR_EST | F_OWNERSHIP_TYPE |
            F_ADDRESS_FULL_ORG | F_ADDRESS_COUNTRY_ORG | F_ADDRESS_STATE_ORG |
            F_ADDRESS_CITY_ORG | F_ADDRESS_PIN_ORG |
            F_ORG_EMAIL | F_ORG_MOBILE | F_LANDLINE_COUNTRY | F_LANDLINE_AREA |
            F_LANDLINE_LOCAL | F_DOCUMENTS_ORG;

    public static final long FORM_HOSPITAL_REG_PART2 =
            F_NAME | F_DOB | F_GENDER | F_BLOOD_GROUP |
            F_EMAIL_PERSONAL | F_SAME_AS_ORG_EMAIL |
            F_MOBILE_PERSONAL | F_SAME_AS_ORG_MOBILE |
            F_CREATE_PASSWORD | F_CONFIRM_PASSWORD |
            F_ADDRESS_FULL_PERSONAL | F_ADDRESS_COUNTRY_PERSONAL | F_ADDRESS_STATE_PERSONAL |
            F_ADDRESS_CITY_PERSONAL | F_ADDRESS_PIN_PERSONAL |
            F_DOCUMENTS_PERSONAL | F_AGREEMENT;

    public static final long FORM_HOSPITAL_REGISTRATION =
            FORM_HOSPITAL_REG_PART1 | FORM_HOSPITAL_REG_PART2;

    // ----------------------
    // Form masks map
    // ----------------------
    public static Map<String, Long> getFormMasks() {
        Map<String, Long> m = new LinkedHashMap<>();
        m.put("hospital_registration_part1", FORM_HOSPITAL_REG_PART1);
        m.put("hospital_registration_part2", FORM_HOSPITAL_REG_PART2);
        m.put("hospital_registration", FORM_HOSPITAL_REGISTRATION);
        return m;
    }

    // ----------------------
    // Field metadata
    // ----------------------
    public static Map<Long, Map<String, Object>> getFieldMetadata() {
        Map<Long, Map<String, Object>> map = new LinkedHashMap<>();

        // Organization fields
        map.put(F_ORG_NAME, createBasicField("organizationName", "Name of the Organization", "text", "organization.name", true, "e.g., City Hospital"));
        map.put(F_CATEGORY, createFieldWithOptions("category", "Category", "select", "organization.category", true, List.of("Hospital", "Clinic")));
        map.put(F_REG_NO, createBasicField("registrationNumber", "Registration Number", "text", "organization.registration_number", false, null));
        map.put(F_YEAR_EST, createBasicField("yearOfEstablishment", "Year of Establishment", "number", "organization.year_of_establishment", false, "YYYY"));
        map.put(F_OWNERSHIP_TYPE, createFieldWithOptions("ownershipType", "Ownership Type", "select", "organization.ownership_type", false, List.of("Private","Trust","Government","Corporation")));

        // Organization address
        map.put(F_ADDRESS_FULL_ORG, createBasicField("fullAddress", "Full Address", "textarea", "organization.address.full_address", false, "Street, landmark, etc."));
        map.put(F_ADDRESS_COUNTRY_ORG, createBasicField("country", "Country", "text", "organization.address.country", false, "Select country"));
        map.put(F_ADDRESS_STATE_ORG, createBasicField("state", "State", "text", "organization.address.state", false, "Select state"));
        map.put(F_ADDRESS_CITY_ORG, createBasicField("city", "City", "text", "organization.address.city", false, "Select city"));
        map.put(F_ADDRESS_PIN_ORG, createBasicField("pincode", "Pin Code", "text", "organization.address.pincode", false, null));

        // Organization contact
        Map<String, Object> orgEmail = createBasicField("email", "Email ID", "email", "organization.email", true, "example@domain.com");
        orgEmail.put("verifyButton", true);
        map.put(F_ORG_EMAIL, orgEmail);

        Map<String, Object> orgMobile = createBasicField("mobile", "Mobile", "phone", "organization.mobile", true, "Enter mobile");
        orgMobile.put("verifyButton", true); // Added phone verification
        map.put(F_ORG_MOBILE, orgMobile);

        // Landline
        map.put(F_LANDLINE_COUNTRY, createBasicField("landline.countryCode", "Landline Country Code", "text", "landline.country_code", false, "Country code"));
        map.put(F_LANDLINE_AREA, createBasicField("landline.areaCode", "Landline Area Code", "text", "landline.area_code", false, null));
        map.put(F_LANDLINE_LOCAL, createBasicField("landline.localNumber", "Landline Local Number", "text", "landline.local_number", false, null));

        // Documents
        map.put(F_DOCUMENTS_ORG, createBasicField("documents", "Documents", "file", "organization.documents", false, null));

        // Personal/Admin fields
        map.put(F_NAME, createBasicField("name", "Name", "text", "personal.name", true, null));
        map.put(F_DOB, createBasicField("dob", "Date Of Birth", "date", "personal.dob", false, "DD/MM/YYYY"));
        map.put(F_GENDER, createFieldWithOptions("gender", "Gender", "select", "personal.gender", false, List.of("Male","Female","Others", "Prefer not to say")));
        map.put(F_BLOOD_GROUP, createFieldWithOptions("bloodGroup", "Blood Group", "select", "personal.bloodGroup", false, List.of("A+","A-","B+","B-","AB+","AB-","O+","O-")));

        // Personal contact
        Map<String, Object> personalEmail = createBasicField("email", "Email ID", "email", "personal.email", true, "example@domain.com");
        personalEmail.put("note", "Always use your personal email here");
        personalEmail.put("verifyButton", true);
        map.put(F_EMAIL_PERSONAL, personalEmail);

        Map<String, Object> personalMobile = createBasicField("mobile", "Mobile", "phone", "personal.mobile", true, "Enter mobile");
        personalMobile.put("verifyButton", true); // Added phone verification
        map.put(F_MOBILE_PERSONAL, personalMobile);

        map.put(F_SAME_AS_ORG_EMAIL, createCheckbox("sameAsOrgEmail", "Same as Organization", "organization.email", "personal.email"));
        map.put(F_SAME_AS_ORG_MOBILE, createCheckbox("sameAsOrgMobile", "Same as Organization", "organization.mobile", "personal.mobile"));

        // Personal address
        map.put(F_ADDRESS_FULL_PERSONAL, createBasicField("fullAddress", "Full Address", "textarea", "personal.address.full_address", false, "Street, landmark, etc."));
        map.put(F_ADDRESS_COUNTRY_PERSONAL, createBasicField("country", "Country", "text", "personal.address.country", false, "Select country"));
        map.put(F_ADDRESS_STATE_PERSONAL, createBasicField("state", "State", "text", "personal.address.state", false, "Select state"));
        map.put(F_ADDRESS_CITY_PERSONAL, createBasicField("city", "City", "text", "personal.address.city", false, "Select city"));
        map.put(F_ADDRESS_PIN_PERSONAL, createBasicField("pincode", "Pin Code", "text", "personal.address.pincode", false, null));

        // Personal documents
        map.put(F_DOCUMENTS_PERSONAL, createBasicField("documents", "Documents", "file", "personal.documents", false, null));

        // Password & agreement
        map.put(F_CREATE_PASSWORD, createBasicField("password", "Create Password", "password", "auth.password", false, null));
        map.put(F_CONFIRM_PASSWORD, createBasicField("confirmPassword", "Confirm Password", "password", "auth.confirmPassword", false, null));
        map.put(F_AGREEMENT, createBasicField("agreement", "I hereby declare that all the information provided above is true and correct.", "checkbox", "auth.agreement", true, null));

        return map;
    }

    // ----------------------
    // Helper methods
    // ----------------------
    private static Map<String, Object> createBasicField(String key, String label, String type, String path, boolean required, String placeholder) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("key", key);
        m.put("label", label);
        m.put("type", type);
        m.put("path", path);
        m.put("required", required);
        if (placeholder != null) m.put("placeholder", placeholder);

        // Calendar widget for DOB
        if ("date".equals(type)) {
            m.put("widget", "calendar");
        }

        // Email verification button
        if ("email".equals(type)) {
            m.put("verifyButton", true);
        }

        return m;
    }

    private static Map<String, Object> createFieldWithOptions(String key, String label, String type, String path, boolean required, List<String> options) {
        Map<String, Object> m = createBasicField(key, label, type, path, required, null);
        m.put("options", options);
        return m;
    }

    private static Map<String, Object> createCheckbox(String key, String label, String sourceField, String targetField) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("key", key);
        m.put("label", label);
        m.put("type", "checkbox");
        m.put("sourceField", sourceField);
        m.put("targetField", targetField);
        return m;
    }
}
