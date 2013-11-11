<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="text" indent="yes"/>


<xsl:template match="/">
	<xsl:apply-templates select="dispatch"/>
</xsl:template>

<xsl:template match="dispatch">
package <xsl:value-of select="@packageName"/>;

import org.jawin.*;
import org.jawin.constants.*;
import org.jawin.marshal.*;
import org.jawin.io.*;
import java.io.*;
import java.util.Date;

/**
 * Jawin generated code please do not edit
 *
 * Dispatch: <xsl:value-of select="@name"/>
 * Description: <xsl:value-of select="@description"/>
 * Help file:   <xsl:value-of select="@helpFile"/>
 *
 * @author JawinTypeBrowser
 */

public class <xsl:value-of select="@name"/> extends DispatchPtr {
	public static final GUID DIID = new GUID("<xsl:value-of select="@guid"/>");
	public static final int IID_TOKEN;
	static {
		IID_TOKEN = IdentityManager.registerProxy(DIID, <xsl:value-of select="@name"/>.class);
	}

	/**
	 * The required public no arg constructor.
	 * &lt;br&gt;&lt;br&gt;
	 * &lt;b&gt;Important:&lt;/b&gt;Should never be used as this creates an uninitialized
	 * <xsl:value-of select="@name"/> (it is required by Jawin for some internal working though).
	 */
	public <xsl:value-of select="@name"/>() {
		super();
	}

	/**
	 * For creating a new COM-object with the given progid and with 
	 * the <xsl:value-of select="@name"/> interface.
	 * 
	 * @param progid the progid of the COM-object to create.
	 */
	public <xsl:value-of select="@name"/>(String progid) throws COMException {
		super(progid, DIID);
	}

	/**
	 * For creating a new COM-object with the given clsid and with 
	 * the <xsl:value-of select="@name"/> interface.
	 * 
	 * @param clsid the GUID of the COM-object to create.
	 */
	public <xsl:value-of select="@name"/>(GUID clsid) throws COMException {
		super(clsid, DIID);
	}

	/**
	 * For getting the <xsl:value-of select="@name"/> interface on an existing COM-object.
	 * This is an alternative to calling {@link #queryInterface(Class)}
	 * on comObject.
	 * 
	 * @param comObject the COM-object to get the <xsl:value-of select="@name"/> interface on.
	 */
	public <xsl:value-of select="@name"/>(COMPtr comObject) throws COMException {
		super(comObject);
	}

	public int getIIDToken() {
		return IID_TOKEN;
	}
	
	<xsl:apply-templates select="property|function"/>
}
</xsl:template>

  <xsl:template match="property">
    /**
    *
    * @return <xsl:value-of select="@name"/>
    **/
    public <xsl:value-of select="@typeName"/><xsl:text> get</xsl:text><xsl:value-of select="@name"/><xsl:text>(</xsl:text>) throws COMException
    {
      <xsl:choose>
       <xsl:when test="@typeName='boolean'">
		return ((Boolean) get(&quot;<xsl:value-of select="@name"/>&quot;)).booleanValue();
        </xsl:when>
       <xsl:when test="@typeName='short'">
		return ((Short) get(&quot;<xsl:value-of select="@name"/>&quot;)).shortValue();
        </xsl:when>
       <xsl:when test="@typeName='int'">
		return ((Integer) get(&quot;<xsl:value-of select="@name"/>&quot;)).intValue();
        </xsl:when>
       <xsl:when test="@typeName='float'">
		return ((Float) get(&quot;<xsl:value-of select="@name"/>&quot;)).floatValue();
        </xsl:when>
       <xsl:when test="@typeName='double'">
		return ((Double) get(&quot;<xsl:value-of select="@name"/>&quot;)).doubleValue();
        </xsl:when>
       <xsl:when test="@typeName='Date'">
		return (Date)get(&quot;<xsl:value-of select="@name"/>&quot;);
        </xsl:when>
       <xsl:when test="@typeName='DispatchPtr'">
		return (DispatchPtr) get(&quot;<xsl:value-of select="@name"/>&quot;);
        </xsl:when>
       <xsl:when test="@typeName='String'">
		return (String) get(&quot;<xsl:value-of select="@name"/>&quot;);
        </xsl:when>
       <xsl:when test="@typeName='void'">
		get(&quot;<xsl:value-of select="@name"/>&quot;);
        </xsl:when>
        <xsl:otherwise>
          return null; //<xsl:value-of select="@typeName"/> not implemented
        </xsl:otherwise>
      </xsl:choose>
    }

    /**
    *
    * @param <xsl:value-of select="@name"/>
    **/
    public void set<xsl:value-of select="@name"/><xsl:text>(</xsl:text><xsl:value-of select="@typeName"/><xsl:text> new</xsl:text><xsl:value-of select="@name"/>) throws COMException
    {
      <xsl:choose>
        <xsl:when test="@typeName='boolean'">
		put(&quot;<xsl:value-of select="@name"/>&quot;, new Boolean(new<xsl:value-of select="@name"/>));
        </xsl:when>
        <xsl:when test="@typeName='short'">
		put(&quot;<xsl:value-of select="@name"/>&quot;, new Short(new<xsl:value-of select="@name"/>));
        </xsl:when>
         <xsl:when test="@typeName='int'">
		put(&quot;<xsl:value-of select="@name"/>&quot;, new Integer(new<xsl:value-of select="@name"/>));
        </xsl:when>
        <xsl:when test="@typeName='float'">
		put(&quot;<xsl:value-of select="@name"/>&quot;, new Float(new<xsl:value-of select="@name"/>));
        </xsl:when>
        <xsl:when test="@typeName='double'">
		put(&quot;<xsl:value-of select="@name"/>&quot;, new Double(new<xsl:value-of select="@name"/>));
        </xsl:when>
        <xsl:when test="@typeName='Date'">
		put(&quot;<xsl:value-of select="@name"/>&quot;, new<xsl:value-of select="@name"/>);
        </xsl:when>
        <xsl:when test="@typeName='String'">
		put(&quot;<xsl:value-of select="@name"/>&quot;, new<xsl:value-of select="@name"/>);
        </xsl:when>
        <xsl:when test="@typeName='Variant'">
		put(&quot;<xsl:value-of select="@name"/>&quot;, new<xsl:value-of select="@name"/>);
        </xsl:when>
        <xsl:when test="@typeName='void'">
		put(&quot;<xsl:value-of select="@name"/>&quot;, new<xsl:value-of select="@name"/>);
        </xsl:when>
       <xsl:otherwise>
          //<xsl:value-of select="@typeName"/> not implemented
        </xsl:otherwise>
      </xsl:choose>
    }

  </xsl:template>

<xsl:template match="function">
      <xsl:choose>
        <xsl:when test="@invokeKind='DISPATCH_PROPERTYGET'">
    /**
    *
    * @return <xsl:value-of select="return/@javaString"/>
    **/
    public <xsl:for-each select="return"><xsl:call-template name="return"/></xsl:for-each><xsl:text> get</xsl:text><xsl:value-of select="@name"/><xsl:text>(</xsl:text><xsl:for-each select="parameter"><xsl:call-template name="Parameter"/><xsl:if test="not(position()=last())"><xsl:text>,</xsl:text></xsl:if></xsl:for-each>) throws COMException
    {
        <xsl:call-template name="ConvertPrefix"/><xsl:call-template name="CallPropertyGet"/><xsl:call-template name="ConvertSuffix"/>;
    }
        </xsl:when>
        <xsl:when test="@invokeKind='DISPATCH_PROPERTYPUT'">
    /**
    *
    * @param <xsl:value-of select="@name"/>
    **/
    public <xsl:for-each select="return"><xsl:call-template name="return"/></xsl:for-each><xsl:text> set</xsl:text><xsl:value-of select="@name"/><xsl:text>(</xsl:text><xsl:for-each select="parameter"><xsl:call-template name="Parameter"/><xsl:if test="not(position()=last())"><xsl:text>,</xsl:text></xsl:if></xsl:for-each>) throws COMException
    {
        <xsl:call-template name="CallPropertyPut"/>;
    }
        </xsl:when>
        <xsl:when test="@invokeKind='DISPATCH_PROPERTYPUTREF'">
          //for <xsl:value-of select="@Name"/> DISPATCH_PROPERTYPUTREF not implemented
        </xsl:when>
       <xsl:otherwise>
    /**
    *
    <xsl:for-each select="parameter">
        <xsl:choose>
        <xsl:when test="pointer[last()=1]">
        <!--xsl:for-each select="pointer"><xsl:call-template name="Pointer"/></xsl:for-each>[] <xsl:value-of select="@name"/-->
        </xsl:when>
       <xsl:otherwise>
    * @param <xsl:value-of select="@name"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
    * @return <xsl:value-of select="return/@javaString"/>
    **/
    <xsl:call-template name="comment-out"><xsl:with-param name="text" select="'/*'"/></xsl:call-template>public <xsl:for-each select="return"><xsl:call-template name="return"/></xsl:for-each><xsl:text> </xsl:text><xsl:value-of select="@name"/><xsl:text>(</xsl:text><xsl:for-each select="parameter"><xsl:call-template name="Parameter"/><xsl:if test="not(position()=last())"><xsl:text>,</xsl:text></xsl:if></xsl:for-each>) throws COMException
    {
      <xsl:choose>
        <xsl:when test="return[@javaString='boolean']">
		return ((Boolean)invokeN(&quot;<xsl:value-of select="@name"/>&quot;, new Object[] {<xsl:for-each select="parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>})).booleanValue();
        </xsl:when>
        <xsl:when test="return[@javaString='short']">
		return ((Short)invokeN(&quot;<xsl:value-of select="@name"/>&quot;, new Object[] {<xsl:for-each select="parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>})).shortValue();
        </xsl:when>
        <xsl:when test="return[@javaString='int']">
		return ((Integer)invokeN(&quot;<xsl:value-of select="@name"/>&quot;, new Object[] {<xsl:for-each select="parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>})).intValue();
        </xsl:when>
        <xsl:when test="return[@javaString='float']">
		return ((Float)invokeN(&quot;<xsl:value-of select="@name"/>&quot;, new Object[] {<xsl:for-each select="parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>})).floatValue();
        </xsl:when>
        <xsl:when test="return[@javaString='double']">
		return ((Double)invokeN(&quot;<xsl:value-of select="@name"/>&quot;, new Object[] {<xsl:for-each select="parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>})).doubleValue();
        </xsl:when>
        <xsl:when test="return[@javaString='String']">
		return (String)invokeN(&quot;<xsl:value-of select="@name"/>&quot;, new Object[] {<xsl:for-each select="parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>});
        </xsl:when>
        <xsl:when test="return[@javaString='Variant']">
		return invokeN(&quot;<xsl:value-of select="@name"/>&quot;, new Object[] {<xsl:for-each select="parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>});
        </xsl:when>
        <xsl:when test="return[@typeString='VT_PTR'] and return/pointer[@typeString='VT_USERDEFINED']">
          <xsl:value-of select="return/@typeName"/> res = new <xsl:value-of select="return/@typeName"/>();
          DispatchPtr dispPtr = (DispatchPtr)invokeN(&quot;<xsl:value-of select="@name"/>&quot;, new Object[] {<xsl:for-each select="parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>});
          res.stealUnknown(dispPtr);
          return res;
        </xsl:when>
        <xsl:when test="return[@javaString='void']">
		invokeN(&quot;<xsl:value-of select="@name"/>&quot;, new Object[] {<xsl:for-each select="parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>});
        </xsl:when>
        <xsl:when test="return[@typeString='VT_USERDEFINED']">
          return (<xsl:value-of select="return/@typeName"/>)invokeN(&quot;<xsl:value-of select="@name"/>&quot;, new Object[] {<xsl:for-each select="parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>});
        </xsl:when>
       <xsl:otherwise>
          return null;  //<xsl:for-each select="return"><xsl:call-template name="return"/></xsl:for-each> not implemented
        </xsl:otherwise>
      </xsl:choose>
    }<xsl:call-template name="comment-out"><xsl:with-param name="text" select="'*/'"/></xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    
</xsl:template>

  <xsl:template name="return">
    <xsl:choose>
      <xsl:when test="@typeString='VT_PTR' and pointer[@typeString='VT_USERDEFINED']"><xsl:value-of select="@typeName"/></xsl:when>
      <xsl:when test="@typeString='VT_VARIANT'">Object</xsl:when>
      <xsl:otherwise><xsl:value-of select="@javaString"/></xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="comment-out">
  <xsl:param name="text" select="'*/'"/>
        <xsl:choose>
        <xsl:when test="@name='QueryInterface' or @name='AddRef' or @name='Release' or @name='GetTypeInfoCount' or @name='GetTypeInfo' or @name='GetIDsOfNames' or @name='Invoke'">
        <xsl:value-of select="$text"/>
        </xsl:when>
       <xsl:otherwise>
        </xsl:otherwise>
      </xsl:choose>
  </xsl:template>

  <xsl:template name="wrap-primitive">
    <xsl:param name="type" select="'int'"/>
    <xsl:param name="name" select="'name'"/>
     <xsl:choose>
	<xsl:when test="$type='boolean'">
	  <xsl:text>new Boolean(</xsl:text><xsl:value-of select="$name"/><xsl:text>)</xsl:text>
	</xsl:when>
	<xsl:when test="$type='short'">
	  <xsl:text>new Short(</xsl:text><xsl:value-of select="$name"/><xsl:text>)</xsl:text>
	</xsl:when>
	<xsl:when test="$type='int'">
	  <xsl:text>new Integer(</xsl:text><xsl:value-of select="$name"/><xsl:text>)</xsl:text>
	</xsl:when>
	<xsl:when test="$type='float'">
	  <xsl:text>new Float(</xsl:text><xsl:value-of select="$name"/><xsl:text>)</xsl:text>
	</xsl:when>
	<xsl:when test="$type='double'">
	  <xsl:text>new Double(</xsl:text><xsl:value-of select="$name"/><xsl:text>)</xsl:text>
	</xsl:when>
	<xsl:when test="$type='String'">
	  <xsl:text></xsl:text><xsl:value-of select="$name"/><xsl:text></xsl:text>
	</xsl:when>
       <xsl:otherwise>
	 <xsl:value-of select="@name"/>
	</xsl:otherwise>
     </xsl:choose>
  </xsl:template>
  
  <xsl:template name="Parameter">
      <xsl:choose>
        <xsl:when test="pointer[last()=1]">
        <xsl:for-each select="pointer"><xsl:call-template name="Pointer"/></xsl:for-each>[] <xsl:value-of select="@name"/>
        </xsl:when>
       <xsl:otherwise>
        <xsl:value-of select="@javaString"/><xsl:text> </xsl:text><xsl:value-of select="@name"/>
        </xsl:otherwise>
      </xsl:choose>
  </xsl:template>

  <xsl:template name="Pointer">
      <xsl:choose>
        <xsl:when test="pointer[last()=1]">
        <xsl:for-each select="pointer"><xsl:call-template name="Pointer"/></xsl:for-each>[] 
        </xsl:when>
       <xsl:otherwise>
        <xsl:value-of select="@javaString"/>
        </xsl:otherwise>
      </xsl:choose>
  </xsl:template>

  <xsl:template name="InvokeParameter">
         <xsl:choose>
            <xsl:when test="@javaString='boolean'">
              <xsl:text>new Boolean(</xsl:text><xsl:value-of select="@name"/><xsl:text>)</xsl:text>
            </xsl:when>
            <xsl:when test="@javaString='short'">
              <xsl:text>new Short(</xsl:text><xsl:value-of select="@name"/><xsl:text>)</xsl:text>
            </xsl:when>
            <xsl:when test="@javaString='int'">
              <xsl:text>new Integer(</xsl:text><xsl:value-of select="@name"/><xsl:text>)</xsl:text>
            </xsl:when>
            <xsl:when test="@javaString='float'">
              <xsl:text>new Float(</xsl:text><xsl:value-of select="@name"/><xsl:text>)</xsl:text>
            </xsl:when>
            <xsl:when test="@javaString='double'">
              <xsl:text>new Double(</xsl:text><xsl:value-of select="@name"/><xsl:text>)</xsl:text>
            </xsl:when>
            <xsl:when test="@javaString='String'">
              <xsl:text></xsl:text><xsl:value-of select="@name"/><xsl:text></xsl:text>
            </xsl:when>
           <xsl:otherwise>
          <xsl:text></xsl:text><xsl:value-of select="@name"/><xsl:text></xsl:text>
            </xsl:otherwise>
        </xsl:choose>
      <!--xsl:choose>
        <xsl:when test="pointer[@javaString='boolean']">
          <xsl:text></xsl:text><xsl:value-of select="@name"/><xsl:text></xsl:text>
        </xsl:when>
        <xsl:when test="@javaString='boolean'">
          <xsl:text>new Boolean(</xsl:text><xsl:value-of select="@name"/><xsl:text>)</xsl:text>
        </xsl:when>
        <xsl:when test="pointer[@javaString='short']">
          <xsl:text></xsl:text><xsl:value-of select="@name"/><xsl:text></xsl:text>
        </xsl:when>
        <xsl:when test="@javaString='short'">
          <xsl:text>new Short(</xsl:text><xsl:value-of select="@name"/><xsl:text>)</xsl:text>
        </xsl:when>
        <xsl:when test="pointer[@javaString='int']">
          <xsl:text></xsl:text><xsl:value-of select="@name"/><xsl:text></xsl:text>
        </xsl:when>
        <xsl:when test="@javaString='int'">
          <xsl:text>new Integer(</xsl:text><xsl:value-of select="@name"/><xsl:text>)</xsl:text>
        </xsl:when>
        <xsl:when test="@javaString='float'">
          <xsl:text>new Float(</xsl:text><xsl:value-of select="@name"/><xsl:text>)</xsl:text>
        </xsl:when>
        <xsl:when test="@javaString='double'">
          <xsl:text>new Double(</xsl:text><xsl:value-of select="@name"/><xsl:text>)</xsl:text>
        </xsl:when>
        <xsl:when test="@javaString='String'">
          <xsl:text></xsl:text><xsl:value-of select="@name"/><xsl:text></xsl:text>
        </xsl:when>
       <xsl:otherwise>
         <xsl:value-of select="@name"/>
        </xsl:otherwise>
      </xsl:choose-->
  </xsl:template>

  <xsl:template name="CallPropertyGet">
    <xsl:choose>
      <xsl:when test="count(parameter)=0">
        <xsl:text>get("</xsl:text><xsl:value-of select="@name"/><xsl:text>")</xsl:text>
      </xsl:when>
      <xsl:when test="count(parameter)=1">
        <xsl:text>get("</xsl:text><xsl:value-of select="@name"/>
        <xsl:text>", </xsl:text>
        <xsl:for-each select="parameter">
          <xsl:call-template name="wrap-primitive">
            <xsl:with-param name="type" select="@javaString"/>
            <xsl:with-param name="name" select="@name"/>
          </xsl:call-template>
        </xsl:for-each>
        <xsl:text>)</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>getN("</xsl:text><xsl:value-of select="@name"/>
        <xsl:text>", new Object[] {</xsl:text>
        <xsl:for-each select="parameter">
          <xsl:call-template name="wrap-primitive">
            <xsl:with-param name="type" select="@javaString"/>
            <xsl:with-param name="name" select="@name"/>
          </xsl:call-template>
          <xsl:if test="not(position()=last())">
            <xsl:text>,</xsl:text>
          </xsl:if>
        </xsl:for-each>
        <xsl:text>})</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template name="ConvertPrefix">
    <xsl:choose>
      <xsl:when test="return[@javaString='boolean']">return ((Boolean)</xsl:when>
      <xsl:when test="return[@javaString='short']">return ((Short)</xsl:when>
      <xsl:when test="return[@javaString='int']">return ((Integer)</xsl:when>
      <xsl:when test="return[@javaString='float']"> return ((Float)</xsl:when>
      <xsl:when test="return[@javaString='double']"> return ((Double)</xsl:when>
      <xsl:when test="return[@javaString='Date']"> return (Date)</xsl:when>
      <xsl:when test="return[@javaString='DispatchPtr']"> return (DispatchPtr)</xsl:when>
      <xsl:when test="return[@javaString='String']"> return (String)</xsl:when>
      <xsl:when test="return[@javaString='Variant']"> return </xsl:when>
      <xsl:when test="return[@typeString='VT_PTR'] and return/pointer[@typeString='VT_USERDEFINED']">
          <xsl:value-of select="return/@typeName"/> res = new <xsl:value-of select="return/@typeName"/>();
          DispatchPtr dispPtr = (DispatchPtr)</xsl:when>
      <xsl:when test="return[@javaString='void']"></xsl:when>
      <xsl:when test="return[@javaString='IUnknown']"> return (IUnknown)</xsl:when>
      <xsl:otherwise>return null; // not implemented</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template name="ConvertSuffix">
    <xsl:choose>
      <xsl:when test="return[@javaString='boolean']">).booleanValue()</xsl:when>
      <xsl:when test="return[@javaString='short']">).shortValue()</xsl:when>
      <xsl:when test="return[@javaString='int']">).intValue()</xsl:when>
      <xsl:when test="return[@javaString='float']">).floatValue()</xsl:when>
      <xsl:when test="return[@javaString='double']">).doubleValue()</xsl:when>
      <xsl:when test="return[@typeString='VT_PTR'] and return/pointer[@typeString='VT_USERDEFINED']">;
          res.stealUnknown(dispPtr);
          return res</xsl:when>
      <xsl:otherwise/>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="CallPropertyPut">
    <xsl:choose>
      <xsl:when test="count(parameter)=0">
        <xsl:text>throw new UnsupportedOperationException()</xsl:text>
      </xsl:when>
      <xsl:when test="count(parameter)=1">
        <xsl:text>put("</xsl:text><xsl:value-of select="@name"/><xsl:text>", </xsl:text>
        <xsl:value-of select="parameter[1]/@name"/>
        <xsl:text>)</xsl:text>
      </xsl:when>
      <xsl:when test="count(parameter)=2">
        <xsl:text>put("</xsl:text><xsl:value-of select="@name"/><xsl:text>", </xsl:text>
        <xsl:call-template name="wrap-primitive">
          <xsl:with-param name="type" select="parameter[1]/@javaString"/>
          <xsl:with-param name="name" select="parameter[1]/@name"/>
        </xsl:call-template>
        <xsl:text>, </xsl:text>
        <xsl:value-of select="parameter[2]/@name"/>
        <xsl:text>)</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>putN("</xsl:text><xsl:value-of select="@name"/>
        <xsl:text>", new Object[] {</xsl:text>
        <xsl:for-each select="parameter">
          <xsl:if test="position()=last()">} </xsl:if>
          <xsl:if test="not(position()=1)">, </xsl:if>
          <xsl:call-template name="wrap-primitive">
            <xsl:with-param name="type" select="@javaString"/>
            <xsl:with-param name="name" select="@name"/>
          </xsl:call-template>
        </xsl:for-each>
        <xsl:text>)</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>