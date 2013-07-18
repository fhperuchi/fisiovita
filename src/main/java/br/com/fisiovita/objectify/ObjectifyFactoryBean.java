package br.com.fisiovita.objectify;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;


/**
 * FactoryBean utilizada para registrar as entidades no Objectify Baseada em:
 * http://code.google.com/p/objectify-appengine-spring/
 * 
 */
public class ObjectifyFactoryBean implements FactoryBean<ObjectifyFactory>, InitializingBean {

    /** logger. */
    private final Log logger = LogFactory.getLog(getClass());

    /** base package. */
    private String basePackage;

    /** classes. */
    private List<Class<?>> classes;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    public ObjectifyFactory getObject() throws Exception { // NOSONAR
        return ObjectifyService.factory();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    public Class<? extends ObjectifyFactory> getObjectType() {
        return ObjectifyFactory.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    public boolean isSingleton() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception { //NOSONAR
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Initialization started");
        }
        long startTime = System.currentTimeMillis();

        if (classes == null) {
            classes = new ArrayList<Class<?>>();
        }

        if (basePackage != null && basePackage.length() > 0) {
            classes.addAll(doScan());
        }

        for (Class<?> clazz : classes) {
            ObjectifyService.register(clazz);
            if (this.logger.isInfoEnabled()) {
                this.logger.info("Registered entity class [" + clazz.getName() + "]");
            }
        }

        if (this.logger.isInfoEnabled()) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            this.logger.info("Initialization completed in " + elapsedTime + " ms");
        }
    }

    /**
     * Do scan.
     * 
     * @return list
     */
    protected List<Class<?>> doScan() {
        List<Class<?>> listaClasses = new ArrayList<Class<?>>();
        String[] basePackages = StringUtils.tokenizeToStringArray(this.basePackage,
                        ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        for (String basePkg : basePackages) {
            if (this.logger.isInfoEnabled()) {
                this.logger.info("Scanning package [" + basePkg + "]");
            }
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(com.googlecode.objectify.annotation.Entity.class));
            scanner.addIncludeFilter(new AnnotationTypeFilter(javax.persistence.Entity.class));
            scanner.addIncludeFilter(new AnnotationTypeFilter(com.googlecode.objectify.annotation.Subclass.class));
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePkg);
            for (BeanDefinition candidate : candidates) {
                Class<?> clazz = ClassUtils.resolveClassName(candidate.getBeanClassName(),
                                ClassUtils.getDefaultClassLoader());
                listaClasses.add(clazz);
            }
        }
        return listaClasses;
    }

    /**
     * Define BasePackage.
     * 
     * @param basePackage
     *            BasePackage
     */
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * Define Classes.
     * 
     * @param classes
     *            Classes
     */
    public void setClasses(List<Class<?>> classes) {
        this.classes = classes;
    }
}
