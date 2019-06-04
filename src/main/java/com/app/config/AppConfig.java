package com.app.config; 

import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement ;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.app.model.Document;
import com.app.model.Item;
import com.app.model.OrderMethod;
import com.app.model.Purchase;
import com.app.model.PurchaseDtl;
import com.app.model.SaleOrder;
import com.app.model.ShipmentType;
import com.app.model.Uom;
import com.app.model.User;
import com.app.model.WhUserType; 

@EnableWebMvc //it is Spring WEB MVC AppConfig 
@EnableTransactionManagement // enable commit/rollback 
@Configuration 
@PropertySource("classpath:app.properties") 
@ComponentScan(basePackages="com.app") //<context:component-scan 
public class AppConfig implements WebMvcConfigurer{  
	@Autowired 
	private Environment env; 

	//1. DataSource 
	@Bean  
	public BasicDataSource dsObj() {   
		BasicDataSource ds=new BasicDataSource();   
		ds.setDriverClassName(env.getProperty("dc"));   
		ds.setUrl(env.getProperty("url"));  
		ds.setUsername(env.getProperty("un"));  
		ds.setPassword(env.getProperty("pwd"));  
		ds.setInitialSize(1);   
		ds.setMaxIdle(1);   
		ds.setMinIdle(1);  
		ds.setMaxTotal(5);   
		return ds;  
	}  
	//2. SessionFactory  
	@Bean 
	public LocalSessionFactoryBean sfObj() {   
		LocalSessionFactoryBean sf=new LocalSessionFactoryBean();  
		sf.setDataSource(dsObj());   sf.setHibernateProperties(props());   
		/* TODO pass model classes */  
		sf.setAnnotatedClasses(ShipmentType.class,Uom.class,
				OrderMethod.class,WhUserType.class,User.class,
				Document.class,Item.class,Purchase.class,SaleOrder.class,
				PurchaseDtl.class
				); //Model class names  
		return sf;  
	}   
	private Properties props() {   
		Properties p=new Properties();  
		p.put("hibernate.dialect", env.getProperty("dialect"));  
		p.put("hibernate.show_sql", env.getProperty("showsql"));  
		p.put("hibernate.format_sql", env.getProperty("fmtsql"));  
		p.put("hibernate.hbm2ddl.auto", env.getProperty("ddlauto"));  
		return p;  
	}  
	//3. HibernateTemplate  
	@Bean  
	public HibernateTemplate htObj() {   
		HibernateTemplate ht=new HibernateTemplate();  
		ht.setSessionFactory(sfObj().getObject());   
		return ht;  
	}
	//4. Transaction Manager 
	@Bean  
	public HibernateTransactionManager htmObj() { 
		HibernateTransactionManager htm=new HibernateTransactionManager();   
		htm.setSessionFactory(sfObj().getObject());  
		return htm;  
	}   
	//5. View Resolver 
	@Bean  
	public InternalResourceViewResolver ivr() {   
		InternalResourceViewResolver v=new InternalResourceViewResolver();   
		v.setPrefix(env.getProperty("mvc.prefix")); //location of UI file  
		v.setSuffix(env.getProperty("mvc.suffix")); //extension of UI file   
		return v;  
	} 

	//6.
	@Bean
	public  CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver cm=new CommonsMultipartResolver();
		return cm;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry reg) {
		reg.addResourceHandler("/resources/**").addResourceLocations("/resources/");

	}
}
