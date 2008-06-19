package org.codehaus.stax2.ri.typed;

import javax.xml.namespace.QName;

/**
 * Helper class used for serializing typed values to String.
 *
 * @author Tatu Saloranta
 */
public class DefaultValueEncoder
{
    public DefaultValueEncoder() { }


    /**
     * @param leadingSpace If true, lexical value will start with a
     *   space; if false, spaces are only used between values but not
     *   before the first value
     */
    public String encodeAsString(boolean leadingSpace, int[] value, int from, int length)
    {
        // !!! TODO: with Java 5, use StringBuilder instead

        /* let's guestimate that we'll have 3 chars per int...
         * just trying to allocate enough but not too much room
         */
        int estLen = 16 + (length << 2);
        StringBuffer sb = new StringBuffer(estLen);
        length += from;

        for (int i = from; i < length; ++from) {
            if (i > from || leadingSpace) {
                sb.append(' ');
            }
            sb.append(value[i]);
        }
        return sb.toString();
    }
}