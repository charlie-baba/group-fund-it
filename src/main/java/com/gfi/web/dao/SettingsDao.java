/**
 * 
 */
package com.gfi.web.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.Settings;
import com.gfi.web.dao.base.StandardJpaEntityBaseDao;
import com.gfi.web.enums.SettingsEnum;

/**
 * @author Obi
 *
 */
@Repository
public class SettingsDao extends StandardJpaEntityBaseDao<Settings> {
	
	@PostConstruct
    public void init() {
        super.setClazz(Settings.class);
    }

	@Transactional
	public String getSetting(SettingsEnum settingEnum) {
        return getSettingObj(settingEnum).getValue();
	}
	
	@Transactional
	public boolean getSettingBoolean(SettingsEnum settingEnum) {
        return Boolean.parseBoolean(getSettingObj(settingEnum).getValue());
	}
	
	@Transactional
	public Settings getSettingObj(SettingsEnum settingEnum) {
		String query = "select s from Settings s where upper(s.name) = :name";
        Map<String, Object> map = new HashMap<>();
        map.put("name", settingEnum.name().toUpperCase());
        Settings settings = executeQueryUniqueResult(query, map);

        if (settings != null) {
            return settings;
        }

        settings = new Settings();
        settings.setName(settingEnum.name());
        settings.setDescription(settingEnum.getDescription());
        settings.setValue(settingEnum.getDefaultValue() == null ? "" : settingEnum.getDefaultValue());
        create(settings);
        
        return settings;
	}
}
