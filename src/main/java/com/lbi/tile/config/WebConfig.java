package com.lbi.tile.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
		while(iterator.hasNext()){
			HttpMessageConverter<?> converter = iterator.next();
			if(converter instanceof MappingJackson2HttpMessageConverter){
				iterator.remove();
			}
		}
		//WebMvcConfigurer.super.configureMessageConverters(converters);
		//创建fastjson转换器实例
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		//配置对象
		FastJsonConfig config = new FastJsonConfig();
		List<MediaType> mediaTypes = new ArrayList<>();
		//中文编码
		MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
		mediaTypes.add(mediaType);
		config.setSerializerFeatures(SerializerFeature.PrettyFormat);
		converter.setSupportedMediaTypes(mediaTypes);
		converter.setFastJsonConfig(config);
		converters.add(converter);
	}
	/*
	 * Configure ContentNegotiationManager
	 */
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		//configurer.ignoreAcceptHeader(true).defaultContentType(MediaType.TEXT_HTML);
		configurer.favorPathExtension(false);
	}
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(true);
	}

}
