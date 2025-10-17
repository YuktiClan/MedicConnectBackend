package com.medicconnect.services;

import com.medicconnect.config.FieldConfig;
import com.medicconnect.models.FormBlock;
import com.medicconnect.models.FormField;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FieldService {

    private final Map<Long, Map<String, Object>> fieldMeta = FieldConfig.getFieldMetadata();

    /**
     * Returns the hospital registration form blocks
     */
    public List<FormBlock> getHospitalRegistrationForm() {
        List<FormBlock> formBlocks = new ArrayList<>();

        Map<String, Long> formMasks = Map.of(
                "organization", FieldConfig.FORM_HOSPITAL_REG_PART1,
                "administrator", FieldConfig.FORM_HOSPITAL_REG_PART2
        );

        for (Map.Entry<String, Long> entry : formMasks.entrySet()) {
            String blockName = entry.getKey();
            long mask = entry.getValue();

            List<FormField> fields = fieldMeta.entrySet().stream()
                    .filter(e -> e.getKey() != null && (e.getKey() & mask) != 0)
                    .map(this::mapToFormField)
                    .collect(Collectors.toList());

            Map<String, Boolean> sameMap = fields.stream()
                    .filter(f -> f.getSameAsOrganizationTarget() != null)
                    .collect(Collectors.toMap(
                            FormField::getSameAsOrganizationTarget,
                            f -> false,
                            (a, b) -> b,
                            LinkedHashMap::new
                    ));

            FormBlock block = new FormBlock();
            block.setBlockName(blockName);
            block.setFields(fields);
            block.setSameAsOrganizationFields(sameMap);
            block.setAgreementChecked(false);

            formBlocks.add(block);
        }

        return formBlocks;
    }

    /**
     * Maps a FieldConfig entry to FormField object (null-safe)
     */
    @SuppressWarnings("unchecked")
    private FormField mapToFormField(Map.Entry<Long, Map<String, Object>> entry) {
        Map<String, Object> meta = entry.getValue() != null ? entry.getValue() : Collections.emptyMap();

        String key = (String) meta.getOrDefault("key", "");
        String label = (String) meta.getOrDefault("label", "");
        String type = (String) meta.getOrDefault("type", "text");
        String path = (String) meta.getOrDefault("path", key);
        boolean required = Boolean.TRUE.equals(meta.get("required"));
        String placeholder = (String) meta.getOrDefault("placeholder", null);
        List<String> options = (List<String>) meta.getOrDefault("options", Collections.emptyList());

        Map<String, Object> extra = new LinkedHashMap<>(meta);
        extra.keySet().removeAll(Set.of("key", "label", "type", "path", "required", "placeholder", "options"));

        FormField field = new FormField(key, label, type, path, required, placeholder, options, extra);

        // Handle personal/admin fields with "Same as Organization"
        switch (path) {
            case "personal.email" -> {
                field.setNote("Always use your personal email here");
                field.setVerifyButton(true);
                field.setSameAsOrganizationSource("organization.email");
                field.setSameAsOrganizationTarget("personal.email");
            }
            case "personal.mobile" -> {
                field.setSameAsOrganizationSource("organization.mobile");
                field.setSameAsOrganizationTarget("personal.mobile");
            }
        }

        return field;
    }
}
