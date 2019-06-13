package net.explorviz.shared.config.annotations.injection;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Singleton;
import javax.ws.rs.InternalServerErrorException;
import net.explorviz.shared.config.annotations.Config;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InjectionResolver for {@code @Config} annotation. You must bind it in your implemented
 * {@link org.glassfish.hk2.utilities.binding.AbstractBinder}}, e.g.:
 *
 * <pre>
 * {@code this.bind(new ConfigInjectionResolver())
 * .to(new TypeLiteral<InjectionResolver<Config>>() {});}
 * </pre>
 *
 * @see net.explorviz.shared.config.annotations.Config
 */

@Singleton
public class ConfigInjectionResolver implements InjectionResolver<Config> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigInjectionResolver.class);

  private static final String PROPERTIES_DEFAULT_FILENAME = "explorviz.properties";
  private static final String PROPERTIES_CUSTOM_FILENAME = "explorviz-custom.properties";
  private static final String PROPERTIES_TEST_FILENAME = "explorviz-test.properties";


  private static final Properties PROP = new Properties();

  private static Properties passedProperties = null;

  private final InternalServerErrorException exception = new InternalServerErrorException(
      "An internal server error occured. Contact your administrator.");

  private AtomicBoolean wasUpdatedViaPassedProperties = new AtomicBoolean(false);

  /**
   * Creates a ConfigInjectionResolver that is used to load injectable configuration properties from
   * the explorviz.properties file. Will be automatically created and registered in the CDI context
   * at application startup.
   */
  public ConfigInjectionResolver() {

    final ClassLoader loader = Thread.currentThread().getContextClassLoader();

    if (passedProperties == null) {

      InputStream input = loader.getResourceAsStream(PROPERTIES_TEST_FILENAME);

      if (input == null) {
        // no testing environment, use custom or default file

        input = loader.getResourceAsStream(PROPERTIES_CUSTOM_FILENAME);

        if (input == null) {
          // use default properties
          input = loader.getResourceAsStream(PROPERTIES_DEFAULT_FILENAME);

          if (input == null) {
            LOGGER.error("Couldn't load default property file.");
            throw this.exception;
          }

          LOGGER.info("Using default property file.");
        } else {
          LOGGER.info("Using custom property file.");
        }
      } else {
        LOGGER.info("Using test property file.");
      }

      try {
        PROP.load(input);
      } catch (final IOException e) {
        LOGGER.error("Couldn't load property file.");
        throw this.exception;
      }

    } else {
      LOGGER.info("Using passed properties.");
      // use passed properties (e.g. for testing)
      PROP.putAll(passedProperties);
    }
  }

  @Override
  public Object resolve(final Injectee injectee, final ServiceHandle<?> root) {

    if (!wasUpdatedViaPassedProperties.get() && passedProperties != null) {
      PROP.putAll(passedProperties);
      wasUpdatedViaPassedProperties.set(true);
      LOGGER.info("Updated config injection due to passed properties.");
    }

    final Type t = injectee.getRequiredType();

    if (String.class == t) {
      return this.handlePropertyLoading(injectee);
    }

    if ("int".equals(t.toString())) {
      try {
        return Integer.valueOf(this.handlePropertyLoading(injectee));
      } catch (final NumberFormatException e) {
        LOGGER.error("Property injection for type 'int' failed. Stacktrace:", e);
        throw this.exception;
      }
    }

    if ("boolean".equals(t.toString())) {
      return Boolean.valueOf(this.handlePropertyLoading(injectee));
    }

    if (LOGGER.isErrorEnabled()) {
      LOGGER.error("Property injection failed: {}",
          "Type '" + t + "' for property injection is not valid. Use String, int or boolean.");
    }
    throw this.exception;

  }

  private String handlePropertyLoading(final Injectee injectee) {

    // @Config annotation at method, field or constructor level
    Config annotation = injectee.getParent().getAnnotation(Config.class);

    if (annotation == null) {

      // Check if @Config annotation is at constructor parameter level
      if (injectee.getParent() instanceof Constructor) {
        Constructor<?> ctor = (Constructor<?>) injectee.getParent();
        annotation = (Config) ctor.getParameterAnnotations()[injectee.getPosition()][0];
      }
      // Check if @Config annotation is at method parameter level
      else if (injectee.getParent() instanceof Method) {
        Method ctor = (Method) injectee.getParent();
        annotation = (Config) ctor.getParameterAnnotations()[injectee.getPosition()][0];
      }
    }

    // Finally resolve @Config annotation value and
    // return the related property
    if (annotation != null) {

      final String propName = annotation.value();
      
      // try to read environment variable
      String environmentVariableName = propName.toUpperCase().replace('.', '_');
      String potentialEnvironmentalValue = System.getenv(environmentVariableName);

      if (potentialEnvironmentalValue != null) {
        return potentialEnvironmentalValue;
      } else {
        // else try to read property in properties file
        Object resolvedProp = PROP.get(propName);

        if (resolvedProp == null) {
          LOGGER.error("Couldn't resolve property with key {}", propName);
        }

        return String.valueOf(resolvedProp);
      }
    }

    LOGGER.error(
        "@Config property injection failed. Annotation for property injection is not present.");
    throw this.exception;
  }

  @Override
  public boolean isConstructorParameterIndicator() {
    return true;
  }

  @Override
  public boolean isMethodParameterIndicator() {
    return true;
  }

  public static Properties getPassedProperties() {
    return passedProperties;
  }

  public static void setPassedProperties(Properties passedProperties) {
    ConfigInjectionResolver.passedProperties = passedProperties;
  }

}
