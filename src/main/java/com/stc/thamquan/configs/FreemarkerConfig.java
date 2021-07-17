package com.stc.thamquan.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 13:33
 * Filename  : FreemarkerConfig
 */
public class FreemarkerConfig {
    @Bean
    public FreeMarkerConfigurationFactoryBean getFreemarkerConfig() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("/templates/");
        return bean;
    }
}
