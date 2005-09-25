package org.codehaus.stax2.validation;

import java.io.*;
import java.util.Properties;

import javax.xml.stream.FactoryConfigurationError;

/**
 * Defines an abstract factory for constructing {@link XMLValidatorSchema}
 * instances. This abstract base class has methods for instantiating the
 * actual implementation (similar to the way
 * {@link javax.xml.stream.XMLInputFactory} works, and defines the API to
 * use for configuring these instances, as well as factory methods concrete
 * classes implement for actually creating {@link XMLValidatorSchema}
 * instances.
 *<p>
 * Note: this class is part of the second major revision of StAX 2 API
 * (StAX2, v2), and is optional for StAX2 implementations to support.
 *
 * @see javax.xml.stream.XMLInputFactory
 * @see org.codehaus.stax2.validation.XMLValidatorSchema
 * @see org.codehaus.stax2.XMLInputFactory2
 */
public abstract class XMLValidatorFactory
{
    final static String JAXP_PROP_FILENAME = "jaxp.properties";

    /**
     * Defines the system property that can be set to explicitly specify
     * which implementation to use (in case there are multiple StAX2
     * implementations; or the one used does not specify other mechanisms
     * for the loader to find the implementation class).
     */
    public final static String SYSTEM_PROPERTY_FOR_IMPL = "org.codehaus.stax2.validation";

    public final static String SERVICE_DEFINITION_PATH = "META-INF/services/" + SYSTEM_PROPERTY_FOR_IMPL;

    /**
     * Property that determines whether schemas constructed are namespace-aware,
     * in cases where schema supports both namespace-aware and non-namespace
     * aware modes. In general this only applies to DTDs, since namespace
     * support for DTDs is both optional, and not well specified.
     *<p>
     * Default value is TRUE. For schema types for which only one value
     * (usually TRUE) is allowed, this property will be ignored.
     */
    public static final String P_IS_NAMESPACE_AWARE = "org.codehaus2.stax2.validation.isNamespaceAware";

    protected XMLValidatorFactory() { }

    /**
     * Creates a new XMLValidationFactory instance, using the default
     * instance configuration mechanism.
     */
    public static XMLValidatorFactory newInstance()
        throws FactoryConfigurationError
    {
        return newInstance(Thread.currentThread().getContextClassLoader());
    }

    public static XMLValidatorFactory newInstance(ClassLoader classLoader)
        throws FactoryConfigurationError
    {
        SecurityException secEx = null;

        /* First, let's see if there's a system property (overrides other
         * settings)
         */
        try {
            String clsName = System.getProperty(SYSTEM_PROPERTY_FOR_IMPL);
            if (clsName != null && clsName.length() > 0) {
                return newInstance(classLoader, clsName);
            }
        } catch (SecurityException se) {
            // May happen on sandbox envs, like applets?
            secEx = se;
        }

        /* try to read from $java.home/lib/xml.properties (simulating
         * the way XMLInputFactory does this... not sure if this should
         * be done, as this is not [yet?] really jaxp specified)
         */
        try {
            String home = System.getProperty( "java.home" );
            File f = new File(home);
            // Let's not hard-code separators...
            f = new File(f, "lib");
            f = new File(JAXP_PROP_FILENAME);
            if (f.exists()) {
                try {
                    Properties props = new Properties();
                    props.load(new FileInputStream(f));
                    String clsName = props.getProperty(SYSTEM_PROPERTY_FOR_IMPL);
                    if (clsName != null && clsName.length() > 0) {
                        return newInstance(classLoader, clsName);
                    }
                } catch (IOException ioe) {
                    // can also happen quite easily...
                }
            }
        } catch (SecurityException se) {
            // Fine, as above
            secEx = se;
        }

        /* Ok, no match; how about a service def from the impl jar?
         */
        // try to find services in CLASSPATH
        try {
            InputStream is;
            if (classLoader == null) {
                is = ClassLoader.getSystemResourceAsStream(SERVICE_DEFINITION_PATH);
            } else {
                is = classLoader.getResourceAsStream(SERVICE_DEFINITION_PATH);
            }
        
            if (is!=null ) {
                BufferedReader rd =
                    new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String clsName = null;

                try {
                    clsName = rd.readLine();
                    if (clsName != null) {
                        clsName = clsName.trim();
                    }
                } finally {
                    rd.close();
                }

                if (clsName != null && clsName.length() > 0) {
                    return newInstance(classLoader, clsName);
                }
            }
        } catch (SecurityException se) {
            secEx = se;
        } catch (IOException ex) {
            /* Let's assume these are mostly ok, too (missing jar ie.)
             */
        }
        
        String msg = "No XMLValidatoryFactory implementation class specified or accessible (via '"
            +SYSTEM_PROPERTY_FOR_IMPL+"' system property, or service definition under '"+SERVICE_DEFINITION_PATH+"')";
        
        if (secEx != null) {
            throw new FactoryConfigurationError(msg + " (possibly caused by: "+secEx+")", secEx);
        }
        throw new FactoryConfigurationError(msg);
    }

    /*
    ///////////////////////////////////////////////////////
    // Internal methods
    ///////////////////////////////////////////////////////
     */

    private static XMLValidatorFactory newInstance(ClassLoader cloader, String clsName)
        throws FactoryConfigurationError
    {
        try {
            Class factoryClass;

            if (cloader == null) {
                factoryClass = Class.forName(clsName);
            } else {
                factoryClass = cloader.loadClass(clsName);
            }
            return (XMLValidatorFactory) factoryClass.newInstance();
        } catch (ClassNotFoundException x) {
            throw new FactoryConfigurationError
                ("XMLValidatoryFactory implementation '"+clsName+"' not found (missing jar in classpath?)", x);
        } catch (Exception x) {
            throw new FactoryConfigurationError
                ("XMLValidatoryFactory implementation '"+clsName+"' could not be instantiated: "+x, x);
        }
    }
}
