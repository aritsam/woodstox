package com.ctc.wstx.dom;

import javax.xml.stream.*;
import javax.xml.transform.dom.DOMSource;

import org.codehaus.stax2.ri.dom.DOMWrappingReader;

import com.ctc.wstx.api.ReaderConfig;
import com.ctc.wstx.cfg.ErrorConsts;
import com.ctc.wstx.exc.WstxParsingException;

public class WstxDOMWrappingReader
    extends DOMWrappingReader
{
    protected final ReaderConfig mConfig;

    /*
    ///////////////////////////////////////////////////
    // Life-cycle
    ///////////////////////////////////////////////////
     */

    protected WstxDOMWrappingReader(DOMSource src, ReaderConfig cfg)
        throws XMLStreamException
    {
        super(src, cfg.willSupportNamespaces());
        mConfig = cfg;
    }

    public static WstxDOMWrappingReader createFrom(DOMSource src, ReaderConfig cfg)
        throws XMLStreamException
    {
        return new WstxDOMWrappingReader(src, cfg);
    }

    /*
    ///////////////////////////////////////////////////
    // Defined/Overridden config methods
    ///////////////////////////////////////////////////
     */

    public boolean isPropertySupported(String name)
    {
        // !!! TBI: not all these properties are really supported
        return mConfig.isPropertySupported(name);
    }

    public Object getProperty(String name)
    {
        return mConfig.getProperty(name);
    }

    public boolean setProperty(String name, Object value)
    {
        /* Note: can not call local method, since it'll return false for
         * recognized but non-mutable properties
         */
        return mConfig.setProperty(name, value);
    }

    /*
    ///////////////////////////////////////////////////
    // Defined/Overridden error reporting
    ///////////////////////////////////////////////////
     */

    // @Override
    protected void throwStreamException(String msg, Location loc)
        throws XMLStreamException
    {
        throw new WstxParsingException(msg, loc);
    }

    // @Override
    protected final String findErrorDesc(int errorType, int currEvent)
    {
        String msg = super.findErrorDesc(errorType, currEvent);
        msg += " (current event: "+ErrorConsts.tokenTypeDesc(currEvent)+")";
        return msg;
    }
}