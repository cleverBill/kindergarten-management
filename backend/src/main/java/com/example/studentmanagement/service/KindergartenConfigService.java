package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.KindergartenConfigDTO;
import com.example.studentmanagement.entity.KindergartenConfig;
import com.example.studentmanagement.repository.KindergartenConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class KindergartenConfigService {

    @Autowired
    private KindergartenConfigRepository configRepository;

    public KindergartenConfigDTO getConfig() {
        KindergartenConfigDTO dto = new KindergartenConfigDTO();
        dto.setName(getConfigValue("name", "田头小学幼儿园"));
        dto.setLogo(getConfigValue("logo", ""));
        dto.setAddress(getConfigValue("address", ""));
        dto.setPhone(getConfigValue("phone", ""));
        dto.setOpeningTime(getConfigValue("openingTime", ""));
        return dto;
    }

    @Transactional
    public KindergartenConfigDTO updateConfig(KindergartenConfigDTO dto) {
        setConfigValue("name", dto.getName());
        setConfigValue("logo", dto.getLogo());
        setConfigValue("address", dto.getAddress());
        setConfigValue("phone", dto.getPhone());
        setConfigValue("openingTime", dto.getOpeningTime());
        return getConfig();
    }

    private String getConfigValue(String key, String defaultValue) {
        return configRepository.findByConfigKey(key)
                .map(KindergartenConfig::getConfigValue)
                .orElse(defaultValue);
    }

    private void setConfigValue(String key, String value) {
        if (value == null) return;
        
        KindergartenConfig config = configRepository.findByConfigKey(key)
                .orElse(new KindergartenConfig());
        
        config.setConfigKey(key);
        config.setConfigValue(value);
        configRepository.save(config);
    }
}
