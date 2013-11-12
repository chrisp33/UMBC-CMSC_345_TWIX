<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="text" indent="yes"/>
<xsl:strip-space elements="*"/>
    
  <xsl:template match="TypeLib">
    <xsl:apply-templates select="Package"/>

import org.jawin.*;
import org.jawin.constants.*;
import org.jawin.marshal.*;
import org.jawin.io.*;
import java.io.*;

    <xsl:apply-templates select="TypeInfo"/>
  </xsl:template>

  <xsl:template match="TypeInfo">
public class <xsl:value-of select="@Name"/> extends DispatchPtr
{
	public static String clsid = "<xsl:value-of select="./GUID/Value"/>";
	public static GUID guid = new GUID(clsid);
	static {
		int iidToken = IdentityManager.registerProxy(guid, <xsl:value-of select="@Name"/>.class);
	}

	public <xsl:value-of select="@Name"/>() throws COMException
	{
         super();
	}

	public <xsl:value-of select="@Name"/>(String progid) throws COMException { super(progid);}
	public <xsl:value-of select="@Name"/>(IUnknown other) throws COMException { super(other);}

	public <xsl:value-of select="@Name"/>(GUID ClsID) throws COMException { super(ClsID);}

    <xsl:apply-templates select="Property|Function"/>
}
  </xsl:template>

  <xsl:template match="Package">
package <xsl:value-of select="@Name"/>;
  </xsl:template>

  <xsl:template match="Property">
    public <xsl:value-of select="@Type"/><xsl:text> get</xsl:text><xsl:value-of select="@Name"/><xsl:text>(</xsl:text>) throws COMException
    {
      <xsl:choose>
       <xsl:when test="@Type='boolean'">
		return ((Boolean) get(&quot;<xsl:value-of select="@Name"/>&quot;)).booleanValue();
        </xsl:when>
       <xsl:when test="@Type='short'">
		return ((Short) get(&quot;<xsl:value-of select="@Name"/>&quot;)).shortValue();
        </xsl:when>
       <xsl:when test="@Type='int'">
		return ((Integer) get(&quot;<xsl:value-of select="@Name"/>&quot;)).intValue();
        </xsl:when>
       <xsl:when test="@Type='float'">
		return ((Float) get(&quot;<xsl:value-of select="@Name"/>&quot;)).floatValue();
        </xsl:when>
       <xsl:when test="@Type='double'">
		return ((Double) get(&quot;<xsl:value-of select="@Name"/>&quot;)).doubleValue();
        </xsl:when>
       <xsl:when test="@Type='String'">
		return (String) get(&quot;<xsl:value-of select="@Name"/>&quot;);
        </xsl:when>
        <xsl:otherwise>
          return null; //<xsl:value-of select="@Type"/> not implemented
        </xsl:otherwise>
      </xsl:choose>
    }

    public void set<xsl:value-of select="@Name"/><xsl:text>(</xsl:text><xsl:value-of select="@Type"/><xsl:text> new</xsl:text><xsl:value-of select="@Name"/>) throws COMException
    {
      <xsl:choose>
        <xsl:when test="@Type='boolean'">
		put(&quot;<xsl:value-of select="@Name"/>&quot;, new Boolean(new<xsl:value-of select="@Name"/>));
        </xsl:when>
        <xsl:when test="@Type='short'">
		put(&quot;<xsl:value-of select="@Name"/>&quot;, new Short(new<xsl:value-of select="@Name"/>));
        </xsl:when>
         <xsl:when test="@Type='int'">
		put(&quot;<xsl:value-of select="@Name"/>&quot;, new Integer(new<xsl:value-of select="@Name"/>));
        </xsl:when>
        <xsl:when test="@Type='float'">
		put(&quot;<xsl:value-of select="@Name"/>&quot;, new Float(new<xsl:value-of select="@Name"/>));
        </xsl:when>
        <xsl:when test="@Type='double'">
		put(&quot;<xsl:value-of select="@Name"/>&quot;, new Double(new<xsl:value-of select="@Name"/>));
        </xsl:when>
        <xsl:when test="@Type='String'">
		put(&quot;<xsl:value-of select="@Name"/>&quot;, new<xsl:value-of select="@Name"/>);
        </xsl:when>
       <xsl:otherwise>
          //<xsl:value-of select="@Type"/> not implemented
        </xsl:otherwise>
      </xsl:choose>
    }

  </xsl:template>

  <xsl:template match="Function">
      <xsl:choose>
        <xsl:when test="@InvokeKind='DISPATCH_PROPERTYGET'">
    public <xsl:value-of select="@ReturnType"/><xsl:text> get</xsl:text><xsl:value-of select="@Name"/><xsl:text>(</xsl:text><xsl:for-each select="Parameter"><xsl:apply-templates select="."/><xsl:if test="not(position()=last())"><xsl:text>,</xsl:text></xsl:if></xsl:for-each>) throws COMException
    {
      <xsl:choose>
        <xsl:when test="@ReturnType='boolean'">
		return ((Boolean)get(&quot;<xsl:value-of select="@Name"/>&quot;)).booleanValue();
        </xsl:when>
        <xsl:when test="@ReturnType='short'">
		return ((Short)get(&quot;<xsl:value-of select="@Name"/>&quot;)).shortValue();
        </xsl:when>
        <xsl:when test="@ReturnType='int'">
		return ((Integer)get(&quot;<xsl:value-of select="@Name"/>&quot;)).intValue();
        </xsl:when>
        <xsl:when test="@ReturnType='float'">
		return ((Float)get(&quot;<xsl:value-of select="@Name"/>&quot;)).floatValue();
        </xsl:when>
        <xsl:when test="@ReturnType='double'">
		return ((Double)get(&quot;<xsl:value-of select="@Name"/>&quot;)).doubleValue();
        </xsl:when>
        <xsl:when test="@ReturnType='String'">
		return (String)get(&quot;<xsl:value-of select="@Name"/>&quot;);
        </xsl:when>
       <xsl:otherwise>
          return null;  //<xsl:value-of select="@ReturnType"/> not implemented
        </xsl:otherwise>
      </xsl:choose>
     }
        </xsl:when>
        <xsl:when test="@InvokeKind='DISPATCH_PROPERTYPUT'">
    public <xsl:value-of select="@ReturnType"/><xsl:text> set</xsl:text><xsl:value-of select="@Name"/><xsl:text>(</xsl:text><xsl:for-each select="Parameter"><xsl:apply-templates select="."/><xsl:if test="not(position()=last())"><xsl:text>,</xsl:text></xsl:if></xsl:for-each>) throws COMException
    {
         <xsl:choose>
            <xsl:when test="@Type='boolean'">
              put("<xsl:value-of select="@Name"/>", <xsl:text>new Boolean(</xsl:text><xsl:value-of select="@Name"/><xsl:text>)</xsl:text>);
            </xsl:when>
            <xsl:when test="@Type='short'">
              put("<xsl:value-of select="@Name"/>", <xsl:text>new Short(</xsl:text><xsl:value-of select="@Name"/><xsl:text>)</xsl:text>);
            </xsl:when>
            <xsl:when test="@Type='int'">
              put("<xsl:value-of select="@Name"/>", <xsl:text>new Integer(</xsl:text><xsl:value-of select="@Name"/><xsl:text>)</xsl:text>);
            </xsl:when>
            <xsl:when test="@Type='float'">
              put("<xsl:value-of select="@Name"/>", <xsl:text>new Float(</xsl:text><xsl:value-of select="@Name"/><xsl:text>)</xsl:text>);
            </xsl:when>
            <xsl:when test="@Type='double'">
              put("<xsl:value-of select="@Name"/>", <xsl:text>new Double(</xsl:text><xsl:value-of select="@Name"/><xsl:text>)</xsl:text>);
            </xsl:when>
            <xsl:when test="@Type='String'">
              put("<xsl:value-of select="@Name"/>", <xsl:text></xsl:text><xsl:value-of select="@Name"/><xsl:text></xsl:text>);
            </xsl:when>
           <xsl:otherwise>
             put("<xsl:value-of select="@Name"/>", <xsl:value-of select="@Name"/>);
            </xsl:otherwise>
        </xsl:choose>
     }
        </xsl:when>
        <xsl:when test="@InvokeKind='DISPATCH_PROPERTYPUTREF'">
          //for <xsl:value-of select="@Name"/> DISPATCH_PROPERTYPUTREF not implemented
        </xsl:when>
       <xsl:otherwise>
    public <xsl:value-of select="@ReturnType"/><xsl:text> </xsl:text><xsl:value-of select="@Name"/><xsl:text>(</xsl:text><xsl:for-each select="Parameter"><xsl:apply-templates select="."/><xsl:if test="not(position()=last())"><xsl:text>,</xsl:text></xsl:if></xsl:for-each>) throws COMException
    {
      <xsl:choose>
        <xsl:when test="@ReturnType='boolean'">
		return ((Boolean)invokeN(&quot;<xsl:value-of select="@Name"/>&quot;, new Object[] {<xsl:for-each select="Parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>})).booleanValue();
        </xsl:when>
        <xsl:when test="@ReturnType='short'">
		return ((Short)invokeN(&quot;<xsl:value-of select="@Name"/>&quot;, new Object[] {<xsl:for-each select="Parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>})).shortValue();
        </xsl:when>
        <xsl:when test="@ReturnType='int'">
		return ((Integer)invokeN(&quot;<xsl:value-of select="@Name"/>&quot;, new Object[] {<xsl:for-each select="Parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>})).intValue();
        </xsl:when>
        <xsl:when test="@ReturnType='float'">
		return ((Float)invokeN(&quot;<xsl:value-of select="@Name"/>&quot;, new Object[] {<xsl:for-each select="Parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>})).floatValue();
        </xsl:when>
        <xsl:when test="@ReturnType='double'">
		return ((Double)invokeN(&quot;<xsl:value-of select="@Name"/>&quot;, new Object[] {<xsl:for-each select="Parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>})).doubleValue();
        </xsl:when>
        <xsl:when test="@ReturnType='String'">
		return (String)invokeN(&quot;<xsl:value-of select="@Name"/>&quot;, new Object[] {<xsl:for-each select="Parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>});
        </xsl:when>
        <xsl:when test="@ReturnType='void'">
		invokeN(&quot;<xsl:value-of select="@Name"/>&quot;, new Object[] {<xsl:for-each select="Parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>});
        </xsl:when>
       <xsl:otherwise>
          return null;  //<xsl:value-of select="@ReturnType"/> not implemented
        </xsl:otherwise>
      </xsl:choose>
    }
        </xsl:otherwise>
      </xsl:choose>
    
  </xsl:template>

  <xsl:template match="Parameter">
    <xsl:value-of select="@Type"/><xsl:text> </xsl:text><xsl:value-of select="@Name"/>
  </xsl:template>

  <xsl:template name="InvokeParameter">
      <xsl:choose>
        <xsl:when test="@Type='boolean'">
          <xsl:text>new Boolean(</xsl:text><xsl:value-of select="@Name"/><xsl:text>)</xsl:text>
        </xsl:when>
        <xsl:when test="@Type='short'">
          <xsl:text>new Short(</xsl:text><xsl:value-of select="@Name"/><xsl:text>)</xsl:text>
        </xsl:when>
        <xsl:when test="@Type='int'">
          <xsl:text>new Integer(</xsl:text><xsl:value-of select="@Name"/><xsl:text>)</xsl:text>
        </xsl:when>
        <xsl:when test="@Type='float'">
          <xsl:text>new Float(</xsl:text><xsl:value-of select="@Name"/><xsl:text>)</xsl:text>
        </xsl:when>
        <xsl:when test="@Type='double'">
          <xsl:text>new Double(</xsl:text><xsl:value-of select="@Name"/><xsl:text>)</xsl:text>
        </xsl:when>
        <xsl:when test="@Type='String'">
          <xsl:text></xsl:text><xsl:value-of select="@Name"/><xsl:text></xsl:text>
        </xsl:when>
       <xsl:otherwise>
         <xsl:value-of select="@Name"/>
        </xsl:otherwise>
      </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>