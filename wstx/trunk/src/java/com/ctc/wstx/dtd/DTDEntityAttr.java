package com.ctc.wstx.dtd;

import java.util.Map;

import javax.xml.stream.Location;

import com.ctc.wstx.cfg.ErrorConsts;
import com.ctc.wstx.ent.EntityDecl;
import com.ctc.wstx.exc.WstxException;
import com.ctc.wstx.sr.AttributeCollector;
import com.ctc.wstx.sr.InputProblemReporter;
import com.ctc.wstx.util.SymbolTable;
import com.ctc.wstx.util.TextBuilder;
import com.ctc.wstx.util.WordResolver;

import com.ctc.wstx.sr.StreamScanner;

/**
 * Specific attribute class for attributes that contain (unique)
 * identifiers.
 */
public final class DTDEntityAttr
    extends DTDAttribute
{
    /*
    ///////////////////////////////////////////////////
    // Life-cycle
    ///////////////////////////////////////////////////
     */

    /**
     * Main constructor. Note that id attributes can never have
     * default values.
     */
    public DTDEntityAttr(NameKey name, int defValueType, String defValue,
                         int specIndex)
    {
        super(name, defValueType, defValue, specIndex);
    }

    public DTDAttribute cloneWith(int specIndex)
    {
        return new DTDEntityAttr(mName, mDefValueType, mDefValue, specIndex);
    }

    /*
    ///////////////////////////////////////////////////
    // Public API
    ///////////////////////////////////////////////////
     */

    public int getValueType() {
        return TYPE_ENTITY;
    }

    /*
    ///////////////////////////////////////////////////
    // Public API, validation
    ///////////////////////////////////////////////////
     */

    /**
     * Method called by the {@link ElementValidator}
     * to let the attribute do necessary normalization and/or validation
     * for the value.
     * 
     */
    public void validate(ElementValidator v, boolean normalize, AttributeCollector ac,
                         int index)
        throws WstxException
    {
        TextBuilder tb = ac.getAttrBuilder();
        char[] ch = tb.getCharBuffer();
        int start = tb.getOffset(index);
        int last = tb.getOffset(index+1) - 1;

        while (start <= last && StreamScanner.isSpaceChar(ch[start])) {
            ++start;
        }

        // Empty value?
        if (start > last) {
            reportParseError(v, "Empty ENTITY value");
        }
        while (last > start && StreamScanner.isSpaceChar(ch[last])) {
            --last;
        }

        // Ok, need to check char validity, and also calc hash code:
        char c = ch[start];
        if (!StreamScanner.isNameStartChar(c) && c != ':') {
            reportInvalidChar(v, c, "not valid as the first ID character");
        }
        int hash = (int) c;

        for (int i = start+1; i <= last; ++i) {
            c = ch[i];
            if (!StreamScanner.isNameChar(c)) {
                reportInvalidChar(v, c, "not valid as an ID character");
            }
            hash = (hash * 31) + (int) c;
        }

        EntityDecl ent = findEntityDecl(v, ch, start, (last - start + 1), hash);
        // only returns if it succeeded...

        if (normalize) {
            ac.setNormalizedValue(index, ent.getName());
        }
    }

    /**
     * Method called by the {@link ElementValidator}
     * to ask attribute to verify that the default it has (if any) is
     * valid for such type.
     */
    public void validateDefault(InputProblemReporter rep, boolean normalize)
        throws WstxException
    {
        mDefValue = validateDefaultName(rep, normalize);
    }

    /*
    ///////////////////////////////////////////////////
    // Internal methods
    ///////////////////////////////////////////////////
     */

}
