package stax2.typed;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import javax.xml.stream.*;

import org.codehaus.stax2.*;
import org.codehaus.stax2.typed.*;

import stax2.BaseStax2Test;

/**
 * Set of simple unit tests to verify implementation
 * of {@link TypedXMLStreamWriter}.
 *
 * @author Tatu Saloranta
 */
public class TestTypedWriter
    extends BaseStax2Test
{
    /*
    ////////////////////////////////////////
    // Tests for numeric/enum types
    ////////////////////////////////////////
     */

    public void testSimpleBooleanElem()
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 w = getTypedWriter(bos);
        writeBooleanElem(w, true);
        w.close();
        checkBooleanElem(bos, true);

        w = getTypedWriter(bos);
        writeBooleanElem(w, false);
        w.close();
        checkBooleanElem(bos, false);
    }

    public void testSimpleBooleanAttr()
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 w = getTypedWriter(bos);
        writeBooleanAttr(w, true);
        w.close();
        checkBooleanAttr(bos, true);

        w = getTypedWriter(bos);
        writeBooleanAttr(w, false);
        w.close();
        checkBooleanAttr(bos, false);
    }

    public void testMultipleBooleanAttr()
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 w = getTypedWriter(bos);

        w.writeStartDocument();
        w.writeStartElement("root");

        w.writeBooleanAttribute(null, null, "a1", true);
        w.writeBooleanAttribute(null, null, "xyz", false);
        w.writeBooleanAttribute(null, null, "_attr3", true);

        w.writeEndElement();
        w.writeEndDocument();

        XMLStreamReader2 sr = getRootReader(bos);
        assertEquals(3, sr.getAttributeCount());
        int ix1 = sr.getAttributeIndex("", "a1");
        int ix2 = sr.getAttributeIndex("", "xyz");
        int ix3 = sr.getAttributeIndex("", "_attr3");
        if (ix1 < 0 || ix2 < 0 || ix3 < 0) {
            fail("Couldn't find indexes of attributes: a1="+ix1+", xyz="+ix2+", _attr3="+ix3);
        }
        assertTrue(sr.getAttributeAsBoolean(ix1));
        assertFalse(sr.getAttributeAsBoolean(ix2));
        assertTrue(sr.getAttributeAsBoolean(ix3));

        sr.close();
    }

    public void testSimpleIntElem()
        throws Exception
    {
        int[] values = new int[] {
            0, 3, -9, 999, -77, 1000000000, -1000000000,
            Integer.MIN_VALUE, Integer.MAX_VALUE
        };
        for (int i = 0; i < values.length; ++i) {
            int value = values[i];
            assertEquals("<root>"+value+"</root>", writeIntElemDoc("root", value));
        }
    }

    public void testSimpleIntAttr()
        throws Exception
    {
        int[] values = new int[] {
            0, 3, -7, 123, -102, 1000000, -999999,
            Integer.MIN_VALUE, Integer.MAX_VALUE
        };
        for (int i = 0; i < values.length; ++i) {
            int value = values[i];
            assertEquals("<a attr='"+value+"'></a>", writeIntAttrDoc("a", "attr", value));
        }
    }

    public void testMultipleIntAttr()
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 w = getTypedWriter(bos);

        w.writeStartDocument();
        w.writeStartElement("root");

        w.writeIntAttribute(null, null, "a", 0);
        w.writeIntAttribute(null, null, "bz", Integer.MAX_VALUE);
        w.writeIntAttribute(null, null, "___", -1200300400);

        w.writeEndElement();
        w.writeEndDocument();

        XMLStreamReader2 sr = getRootReader(bos);
        assertEquals(3, sr.getAttributeCount());
        int ix1 = sr.getAttributeIndex("", "a");
        int ix2 = sr.getAttributeIndex("", "bz");
        int ix3 = sr.getAttributeIndex("", "___");
        if (ix1 < 0 || ix2 < 0 || ix3 < 0) {
            fail("Couldn't find indexes of attributes: a="+ix1+", bz="+ix2+", ___="+ix3);
        }
        assertEquals(0, sr.getAttributeAsInt(ix1));
        assertEquals(Integer.MAX_VALUE, sr.getAttributeAsInt(ix2));
        assertEquals(-1200300400, sr.getAttributeAsInt(ix3));

        sr.close();
    }

    public void testSimpleLongElem()
        throws Exception
    {
        long[] values = new long[] {
            0, 3, -9, 999, -77, 1000000000, -1000000000,
            123456789012345678L, 
            -987654321098765423L,
            Long.MIN_VALUE, Long.MAX_VALUE
        };
        for (int i = 0; i < values.length; ++i) {
            long value = values[i];
            assertEquals("<root>"+value+"</root>", writeLongElemDoc("root", value));
        }
    }

    public void testSimpleLongAttr()
        throws Exception
    {
        long[] values = new long[] {
            0, 3, -9, 999, -77, 1000000002, -2000000004,
            123456789012345678L, 
            -987654321098765423L,
            Long.MIN_VALUE, Long.MAX_VALUE
        };
        for (int i = 0; i < values.length; ++i) {
            long value = values[i];
            assertEquals("<a attr='"+value+"'></a>", writeLongAttrDoc("a", "attr", value));
        }
    }

    public void testMultipleLongAttr()
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 w = getTypedWriter(bos);

        w.writeStartDocument();
        w.writeStartElement("root");

        w.writeLongAttribute(null, null, "a", 0L);
        w.writeLongAttribute(null, null, "bz", Long.MAX_VALUE);
        w.writeLongAttribute(null, null, "___", -1200300400L);

        w.writeEndElement();
        w.writeEndDocument();

        XMLStreamReader2 sr = getRootReader(bos);
        assertEquals(3, sr.getAttributeCount());
        int ix1 = sr.getAttributeIndex("", "a");
        int ix2 = sr.getAttributeIndex("", "bz");
        int ix3 = sr.getAttributeIndex("", "___");
        if (ix1 < 0 || ix2 < 0 || ix3 < 0) {
            fail("Couldn't find indexes of attributes: a="+ix1+", bz="+ix2+", ___="+ix3);
        }
        assertEquals(0L, sr.getAttributeAsLong(ix1));
        assertEquals(Long.MAX_VALUE, sr.getAttributeAsLong(ix2));
        assertEquals(-1200300400L, sr.getAttributeAsLong(ix3));

        sr.close();
    }

    public void testSimpleFloatElem()
        throws Exception
    {
        float[] values = new float[] {
            0.0f,  10.47f, (float) (1.0 / 3.0), -0.25f,
            Float.MIN_VALUE, Float.MAX_VALUE,
            Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY
        };
        for (int i = 0; i < values.length; ++i) {
            float value = values[i];
            assertEquals("<root>"+value+"</root>", writeFloatElemDoc("root", value));
        }
    }

    public void testSimpleFloatAttr()
        throws Exception
    {
        float[] values = new float[] {
            0.0f,  10.47f, (float) (1.0 / 3.0), -0.25f,
            Float.MIN_VALUE, Float.MAX_VALUE,
            Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY
        };
        for (int i = 0; i < values.length; ++i) {
            float value = values[i];
            assertEquals("<a attr='"+value+"'></a>", writeFloatAttrDoc("a", "attr", value));
        }
    }

    public void testSimpleDoubleElem()
        throws Exception
    {
        double[] values = new double[] {
            0.0f,  10.47f, (double) (1.0 / 3.0), -0.25f,
            Double.MIN_VALUE, Double.MAX_VALUE,
            Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY
        };
        for (int i = 0; i < values.length; ++i) {
            double value = values[i];
            assertEquals("<root>"+value+"</root>", writeDoubleElemDoc("root", value));
        }
    }

    public void testSimpleDoubleAttr()
        throws Exception
    {
        double[] values = new double[] {
            0.0f,  10.47f, (double) (1.0 / 3.0), -0.25f,
            Double.MIN_VALUE, Double.MAX_VALUE,
            Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY
        };
        for (int i = 0; i < values.length; ++i) {
            double value = values[i];
            assertEquals("<a attr='"+value+"'></a>", writeDoubleAttrDoc("a", "attr", value));
        }
    }

    public void testBigInteger()
        throws Exception
    {
        BigInteger I = BigInteger.valueOf(3);
        Random rnd = new Random(2);
        for (int i = 1; i < 200; ++i) {
            assertEquals("<root>"+I+"</root>", writeIntegerElemDoc("root", I));
            assertEquals("<a attr='"+I+"'></a>", writeIntegerAttrDoc("a", "attr", I));
            I = I.multiply(BigInteger.valueOf(10)).add(BigInteger.valueOf(rnd.nextInt() & 0xF));
        }
    }

    public void testBigDecimal()
        throws Exception
    {
        BigDecimal D = BigDecimal.valueOf(1L);
        Random rnd = new Random(9);
        for (int i = 1; i < 200; ++i) {
            assertEquals("<root>"+D+"</root>", writeDecimalElemDoc("root", D));
            assertEquals("<a attr='"+D+"'></a>", writeDecimalAttrDoc("a", "attr", D));
            // Ok, then, add a small integer, divide by 10 to generate digits
            D = D.add(BigDecimal.valueOf(rnd.nextInt() & 0xF)).divide(BigDecimal.valueOf(10L));
        }
    }

    /*
    ////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////
     */

    private void checkBooleanElem(ByteArrayOutputStream src, boolean expState)
        throws XMLStreamException
    {
        XMLStreamReader2 sr = getRootReader(src);
        assertEquals(expState, sr.getElementAsBoolean());
        sr.close();
    }

    private void checkBooleanAttr(ByteArrayOutputStream src, boolean expState)
        throws XMLStreamException
    {
        XMLStreamReader2 sr = getRootReader(src);
        assertEquals(expState, sr.getAttributeAsBoolean(0));
        sr.close();
    }

    private void writeBooleanElem(TypedXMLStreamWriter sw, boolean b)
        throws XMLStreamException
    {
        sw.writeStartDocument();
        sw.writeStartElement("root");
        sw.writeBoolean(b);
        sw.writeEndElement();
        sw.writeEndDocument();
    }

    private void writeBooleanAttr(TypedXMLStreamWriter sw, boolean b)
        throws XMLStreamException
    {
        sw.writeStartDocument();
        sw.writeStartElement("root");
        sw.writeBooleanAttribute(null, null, "attr", b);
        sw.writeEndElement();
        sw.writeEndDocument();
    }

    private String writeIntElemDoc(String elem, int value)
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 sw = getTypedWriter(bos);
        // Let's not write start doc, to avoid getting xml declaration
        //sw.writeStartDocument();
        sw.writeStartElement(elem);
        sw.writeInt(value);
        sw.writeEndElement();
        sw.writeEndDocument();
        return bos.toString("UTF-8");
    }

    private String writeIntAttrDoc(String elem, String attr, int value)
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 sw = getTypedWriter(bos);
        // Let's not write start doc, to avoid getting xml declaration
        //sw.writeStartDocument();
        sw.writeStartElement(elem);
        sw.writeIntAttribute(null, null, attr, value);
        sw.writeCharacters(""); // to avoid empty elem
        sw.writeEndElement();
        sw.writeEndDocument();
        String str = bos.toString("UTF-8");
        // One twist: need to ensure quotes are single-quotes (for the test)
        return str.replace('"', '\'');
    }

    private String writeLongElemDoc(String elem, long value)
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 sw = getTypedWriter(bos);
        // Let's not write start doc, to avoid getting xml declaration
        //sw.writeStartDocument();
        sw.writeStartElement(elem);
        sw.writeLong(value);
        sw.writeEndElement();
        sw.writeEndDocument();
        return bos.toString("UTF-8");
    }

    private String writeLongAttrDoc(String elem, String attr, long value)
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 sw = getTypedWriter(bos);
        // Let's not write start doc, to avoid getting xml declaration
        //sw.writeStartDocument();
        sw.writeStartElement(elem);
        sw.writeLongAttribute(null, null, attr, value);
        sw.writeCharacters(""); // to avoid empty elem
        sw.writeEndElement();
        sw.writeEndDocument();
        String str = bos.toString("UTF-8");
        // One twist: need to ensure quotes are single-quotes (for the test)
        return str.replace('"', '\'');
    }

    private String writeFloatElemDoc(String elem, float value)
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 sw = getTypedWriter(bos);
        // Let's not write start doc, to avoid getting xml declaration
        //sw.writeStartDocument();
        sw.writeStartElement(elem);
        sw.writeFloat(value);
        sw.writeEndElement();
        sw.writeEndDocument();
        return bos.toString("UTF-8");
    }

    private String writeFloatAttrDoc(String elem, String attr, float value)
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 sw = getTypedWriter(bos);
        // Let's not write start doc, to avoid getting xml declaration
        //sw.writeStartDocument();
        sw.writeStartElement(elem);
        sw.writeFloatAttribute(null, null, attr, value);
        sw.writeCharacters(""); // to avoid empty elem
        sw.writeEndElement();
        sw.writeEndDocument();
        String str = bos.toString("UTF-8");
        // One twist: need to ensure quotes are single-quotes (for the test)
        return str.replace('"', '\'');
    }

    private String writeDoubleElemDoc(String elem, double value)
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 sw = getTypedWriter(bos);
        sw.writeStartElement(elem);
        sw.writeDouble(value);
        sw.writeEndElement();
        sw.writeEndDocument();
        return bos.toString("UTF-8");
    }

    private String writeDoubleAttrDoc(String elem, String attr, double value)
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 sw = getTypedWriter(bos);
        sw.writeStartElement(elem);
        sw.writeDoubleAttribute(null, null, attr, value);
        sw.writeCharacters(""); // to avoid empty elem
        sw.writeEndElement();
        sw.writeEndDocument();
        String str = bos.toString("UTF-8");
        // One twist: need to ensure quotes are single-quotes (for the test)
        return str.replace('"', '\'');
    }

    private String writeIntegerElemDoc(String elem, BigInteger value)
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 sw = getTypedWriter(bos);
        sw.writeStartElement(elem);
        sw.writeInteger(value);
        sw.writeEndElement();
        sw.writeEndDocument();
        return bos.toString("UTF-8");
    }

    private String writeIntegerAttrDoc(String elem, String attr, BigInteger value)
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 sw = getTypedWriter(bos);
        sw.writeStartElement(elem);
        sw.writeIntegerAttribute(null, null, attr, value);
        sw.writeCharacters(""); // to avoid empty elem
        sw.writeEndElement();
        sw.writeEndDocument();
        String str = bos.toString("UTF-8");
        // One twist: need to ensure quotes are single-quotes (for the test)
        return str.replace('"', '\'');
    }

    private String writeDecimalElemDoc(String elem, BigDecimal value)
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 sw = getTypedWriter(bos);
        sw.writeStartElement(elem);
        sw.writeDecimal(value);
        sw.writeEndElement();
        sw.writeEndDocument();
        return bos.toString("UTF-8");
    }

    private String writeDecimalAttrDoc(String elem, String attr, BigDecimal value)
        throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter2 sw = getTypedWriter(bos);
        sw.writeStartElement(elem);
        sw.writeDecimalAttribute(null, null, attr, value);
        sw.writeCharacters(""); // to avoid empty elem
        sw.writeEndElement();
        sw.writeEndDocument();
        String str = bos.toString("UTF-8");
        // One twist: need to ensure quotes are single-quotes (for the test)
        return str.replace('"', '\'');
    }

    private XMLStreamWriter2 getTypedWriter(ByteArrayOutputStream out)
        throws XMLStreamException
    {
        out.reset();
        XMLOutputFactory outf = getOutputFactory();
        return (XMLStreamWriter2) outf.createXMLStreamWriter(out, "UTF-8");
    }

    // XMLStreamReader2 extends TypedXMLStreamReader
    private XMLStreamReader2 getRootReader(ByteArrayOutputStream src)
        throws XMLStreamException
    {
        XMLStreamReader2 sr = getReader(src.toByteArray());
        assertTokenType(START_DOCUMENT, sr.getEventType());
        while (sr.next() != START_ELEMENT) { }
        assertTokenType(START_ELEMENT, sr.getEventType());
        return sr;
    }

    private XMLStreamReader2 getReader(byte[] data)
        throws XMLStreamException
    {
        XMLInputFactory f = getInputFactory();
        setCoalescing(f, false); // shouldn't really matter
        setNamespaceAware(f, true);
        return (XMLStreamReader2) f.createXMLStreamReader(new ByteArrayInputStream(data));
    }
}
