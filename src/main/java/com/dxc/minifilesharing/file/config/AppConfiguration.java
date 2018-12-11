package com.dxc.minifilesharing.file.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * ApplicationConfiguration will define:
 * <ul>
 * <li>{@code MessageSourceAccessor} that enable message localization.</li>
 * <li>timezone used in proposal json date</li>
 * <li>proposal json schema & Jackson Object Mapper</li>
 * <li>Json Path configuration</li>
 * </ul>
 *
 * @author hpham21
 */
@Configuration
class AppConfiguration {

	/**
	 * Create {@link ReloadableResourceBundleMessageSource} bean.
	 *
	 * @return {@link ReloadableResourceBundleMessageSource} instance
	 */
	@Bean
    ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:locale/messages");
		messageSource.setCacheSeconds(3600); // refresh cache once per hour
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
    MessageSourceAccessor messageSourceAccessor() {
		return new MessageSourceAccessor(messageSource());
	}

	/**
	 * CORS configuration.
	 *
	 * @return WebMvcConfigurer
	 */
	@Bean
    WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurerAdapter() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// @formatter:off
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedHeaders("*")
						.allowedMethods("*")
						.exposedHeaders("Cache-Control", "Pragma", "Origin",
								"Authorization", "Content-Type", "X-Requested-With");
				// @formatter:on
			}
		};
	}

    /**
     * For file uploading
     * @return
     */
    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }}
