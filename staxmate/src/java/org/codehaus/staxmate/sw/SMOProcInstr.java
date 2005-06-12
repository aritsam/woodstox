package org.codehaus.staxmate.sw;

import javax.xml.stream.XMLStreamException;

public class SMOProcInstr
    extends SMSimpleOutput
{
    final String mTarget;
    final String mData;

    public SMOProcInstr(String target, String data) {
        super();
        mTarget = target;
        mData = data;
    }
    
    protected boolean doOutput(SMOutputContext ctxt, boolean canClose)
        throws XMLStreamException
    {
        ctxt.writeProcessingInstruction(mTarget, mData);
        return true;
    }
}
