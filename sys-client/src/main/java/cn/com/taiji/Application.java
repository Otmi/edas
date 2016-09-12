package cn.com.taiji;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@SpringBootApplication
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class Application extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setShowBanner(false);
		app.run(args);
	}

	// @PostConstruct
	// public void init() {
//		// @formatter:off
//		jacksonObjectMapper
//				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//				.configure(SerializationFeature.INDENT_OUTPUT, true)
//				.setDateFormat(new ISO8601DateFormat())
//				.registerModule(jacksonJodaModule) 
//				.registerModule(new SysModule()); // @TODO add more Module
//		// @formatter:on
	// }
	//
	// @Autowired
	// ObjectMapper jacksonObjectMapper;

	// @Autowired
	// JodaModule jacksonJodaModule;

	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;

	@Autowired
	Environment environment;

	// @Bean
	// public LiteDeviceDelegatingViewResolver deviceDelegatingViewResolver() {
	//
	// RelaxedPropertyResolver env = new RelaxedPropertyResolver(environment,
	// "spring.mobile.devicedelegatingviewresolver.");
	// LiteDeviceDelegatingViewResolver resolver = new
	// LiteDeviceDelegatingViewResolver(
	// thymeleafViewResolver);
	// resolver.setNormalPrefix(env.getProperty("normal-prefix", ""));
	// resolver.setNormalSuffix(env.getProperty("normal-suffix", ""));
	// resolver.setMobilePrefix(env.getProperty("mobile-prefix", "mobile/"));
	// resolver.setMobileSuffix(env.getProperty("mobile-suffix", ""));
	// resolver.setTabletPrefix(env.getProperty("tablet-prefix", "tablet/"));
	// resolver.setTabletSuffix(env.getProperty("tablet-suffix", ""));
	// resolver.setOrder(thymeleafViewResolver.getOrder());
	// resolver.setEnableFallback(true);
	// return resolver;
	// }

	@Bean
	@Primary
	public ObjectMapper jacksonObjectMapper() {
		// @formatter:off
		return new ObjectMapper()
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				.configure(SerializationFeature.INDENT_OUTPUT, true)
				.setDateFormat(new ISO8601DateFormat())
				.registerModule(new JodaModule()); // add more Module
			//.registerModule(new SysModule());
		// @formatter:on
	}

	@Bean
	public MappingJackson2JsonView mappingJackson2JsonView() {
		MappingJackson2JsonView v = new org.springframework.web.servlet.view.json.MappingJackson2JsonView();
		v.setObjectMapper(jacksonObjectMapper());
		v.setPrettyPrint(true);
		return v;
	}

	protected class MappingJackson2JsonpView extends MappingJackson2JsonView {
		public static final String DEFAULT_CONTENT_TYPE = "application/javascript";

		@Override
		public String getContentType() {
			return DEFAULT_CONTENT_TYPE;
		}

		@Override
		public void render(Map<String, ?> model, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			Map<String, String[]> params = request.getParameterMap();
			if (params.containsKey("callback")) {
				response.getOutputStream().write(
						new String(params.get("callback")[0] + "(").getBytes());
				super.render(model, request, response);
				response.getOutputStream().write(new String(");").getBytes());
				response.setContentType(DEFAULT_CONTENT_TYPE);
			} else {
				super.render(model, request, response);
			}
		}
	}

	@Bean
	public MappingJackson2JsonpView mappingJackson2JsonpView() {
		MappingJackson2JsonpView v = new MappingJackson2JsonpView();
		v.setObjectMapper(jacksonObjectMapper());
		v.setPrettyPrint(false);
		return v;
	}

	// @Override
	public void configureContentNegotiation(
			ContentNegotiationConfigurer configurer) {
		configurer.favorParameter(true).ignoreAcceptHeader(false)
				.defaultContentType(MediaType.TEXT_HTML)
				.mediaType("json", MediaType.APPLICATION_JSON)
				.mediaType("jsonp", MediaType.valueOf("application/javascript"));
	}

	@Bean
	public ViewResolver contentNegotiatingViewResolver(
			ContentNegotiationManager manager) {
		List<ViewResolver> resolvers = new ArrayList<ViewResolver>();

		resolvers.add(thymeleafViewResolver);
		// resolvers.add(deviceDelegatingViewResolver());
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setViewResolvers(resolvers);
		resolver.setContentNegotiationManager(manager);

		List<View> views = new ArrayList<View>();
		views.add(mappingJackson2JsonView());
		views.add(mappingJackson2JsonpView());
		resolver.setDefaultViews(views);
		return resolver;

	}

	// see
	// org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration.ThymeleafDefaultConfiguration

	// @Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/access").setViewName("access");
	}


/*	<bean id="UserService" class="com.taobao.hsf.app.spring.util.HSFSpringConsumerBean" init-method="init">
		<property name="interfaceName" value="com.example.demo.service.UserService" />
		<property name="version" value="1.0.0.daily" />
		<property name="group">
			<value>reconcile</value>
		</property>
	</bean>
   */
	

@Bean
public com.taobao.hsf.app.spring.util.HSFSpringConsumerBean LoginService(){
		com.taobao.hsf.app.spring.util.HSFSpringConsumerBean hsf=new com.taobao.hsf.app.spring.util.HSFSpringConsumerBean();
	try {
		hsf.init();
		hsf.setInterfaceName("cn.com.taiji.LoginService");
		hsf.setVersion("1.0.0.aaa");
		hsf.setGroup("taiji-reconcile");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return hsf;
}
}