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
                    .filter(e -> (e.getKey() & mask) != 0)
                    .map(this::mapToFormField)
                    .collect(Collectors.toList());

            Map<String, Boolean> sameMap = new HashMap<>();
            for (FormField field : fields) {
                if (field.getSameAsOrganizationSource() != null && field.getSameAsOrganizationTarget() != null) {
                    sameMap.put(field.getSameAsOrganizationTarget(), false);
                }
            }

            FormBlock block = new FormBlock();
            block.setBlockName(blockName);
            block.setFields(fields);
            block.setSameAsOrganizationFields(sameMap);
            block.setAgreementChecked(false);

            formBlocks.add(block);
        }

        return formBlocks;
    }

    @SuppressWarnings("unchecked")
    private FormField mapToFormField(Map.Entry<Long, Map<String, Object>> entry) {
        Map<String, Object> meta = entry.getValue();

        String key = (String) meta.get("key");
        String label = (String) meta.get("label");
        String type = (String) meta.get("type");
        String path = (String) meta.get("path");
        boolean required = Boolean.TRUE.equals(meta.get("required"));
        String placeholder = (String) meta.getOrDefault("placeholder", null);
        List<String> options = (List<String>) meta.getOrDefault("options", List.of());

        // Only keep extra metadata that is not part of standard fields
        Map<String, Object> extra = new LinkedHashMap<>(meta);
        extra.keySet().removeAll(Set.of("key", "label", "type", "path", "required", "placeholder", "options"));

        FormField field = new FormField(key, label, type, path, required, placeholder, options, extra);

        // Only admin/personal fields get verifyButton or sameAsOrganization
        if ("personal.email".equals(path)) {
            field.setNote("Always use your personal email here");
            field.setVerifyButton(true);
            field.setSameAsOrganizationSource("organization.email");
            field.setSameAsOrganizationTarget("personal.email");
        } else if ("personal.mobile".equals(path)) {
            field.setSameAsOrganizationSource("organization.mobile");
            field.setSameAsOrganizationTarget("personal.mobile");
        }

        return field;
    }
}
