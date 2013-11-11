<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="text" indent="yes"/>

<xsl:template match="/">
	<xsl:apply-templates select="interface"/>
</xsl:template>

<xsl:template match="interface">
package <xsl:value-of select="@packageName"/>;

import org.jawin.*;
import org.jawin.constants.*;
import org.jawin.marshal.*;
import org.jawin.io.*;
import java.io.*;

/**
 * Jawin generated code please do not edit
 *
 * Interface: <xsl:value-of select="@name"/>
 * Description: <xsl:value-of select="@description"/>
 * Help file:   <xsl:value-of select="@helpFile"/>
 *
 * @author JawinTypeBrowser
 */

public class <xsl:value-of select="@name"/> extends COMPtr {
	public static final GUID IID = new GUID("<xsl:value-of select="@guid"/>");
	public static final int IID_TOKEN;
	static {
		IID_TOKEN = IdentityManager.registerProxy(IID, <xsl:value-of select="@name"/>.class);
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
		super(progid, IID);
	}

	/**
	 * For creating a new COM-object with the given clsid and with 
	 * the <xsl:value-of select="@name"/> interface.
	 * 
	 * @param clsid the GUID of the COM-object to create.
	 */
	public <xsl:value-of select="@name"/>(GUID clsid) throws COMException {
		super(clsid, IID);
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

	<xsl:apply-templates select="function"/>
}
</xsl:template>

<xsl:template match="function">
<xsl:param name="arraySize" select="0" />
<xsl:param name="totalCount" select="count(parameter)"/>
      <xsl:choose>
        <xsl:when test="@functionKind='FUNC_DISPATCH'">
	public <xsl:value-of select="return/@javaString"/><xsl:text> </xsl:text><xsl:value-of select="@name"/>(<xsl:for-each select="parameter"><xsl:call-template name="Parameter"/><xsl:if test="not(position()=last())"><xsl:text>,</xsl:text></xsl:if></xsl:for-each>) throws COMException
	{
            String marshal="<xsl:for-each select="parameter"><xsl:value-of select="@javaSignature"/></xsl:for-each>:<xsl:value-of select="return/@javaSignature"/>";
            <!--xsl:for-each select="parameter"><xsl:call-template name="ComputeStackAndArraySize"><xsl:with-param name="stackSize"/></xsl:call-template></xsl:for-each-->
            int arraySize=0;
            <!--xsl:if test="count(parameter)=0">
            int stackSize=0;
            </xsl:if-->
            <!--xsl:for-each select="parameter"-->
            //totalCount=<xsl:value-of select="$totalCount"/>
            <xsl:call-template name="recurseStackSize"></xsl:call-template>
            <!--xsl:call-template name="pi"/-->
            <!--/xsl:for-each-->
            byte[] argStream=new byte[<xsl:value-of select="$arraySize" />];
            byte[] result = comInvoke(<xsl:value-of select="@vTableOffset"/>, marshal, stackSize, arraySize, argStream);
	}
        </xsl:when>
        <xsl:when test="@functionKind='FUNC_PUREVIRTUAL'">
	public <xsl:value-of select="return/@javaString"/><xsl:text> </xsl:text><xsl:value-of select="@name"/>(<xsl:for-each select="parameter"><xsl:call-template name="Parameter"/><xsl:if test="not(position()=last())"><xsl:text>,</xsl:text></xsl:if></xsl:for-each>) throws COMException
	{
            //implement your pure virtual code here.
            throw new UnsupportedOperationException();
	}
        </xsl:when>
       <xsl:otherwise>
          //functionKind <xsl:value-of select="@functionKind"/> not implemented <xsl:value-of select="@name"/>
        </xsl:otherwise>
      </xsl:choose>
</xsl:template>

    
  <xsl:template name="recurseStackSize">
  <xsl:param name="i">1</xsl:param>
  <xsl:param name="stackSize">0</xsl:param>
          <xsl:choose>
            <xsl:when test="$i &lt;=count(parameter)">
                  <xsl:choose>
                    <xsl:when test="parameter[$i]/@javaSignature='Z'">
                    <xsl:call-template name="recurseStackSize"><xsl:with-param name="i" select="$i + 1"/><xsl:with-param name="stackSize" select="$stackSize + 1"/></xsl:call-template>
                    </xsl:when>
                    <xsl:when test="parameter[$i]/@javaSignature='B'">
                    <xsl:call-template name="recurseStackSize"><xsl:with-param name="i" select="$i + 1"/><xsl:with-param name="stackSize" select="$stackSize + 1"/></xsl:call-template>
                    </xsl:when>
                    <xsl:when test="parameter[$i]/@javaSignature='I'">
                       <xsl:call-template name="recurseStackSize"><xsl:with-param name="i" select="$i + 1"/><xsl:with-param name="stackSize" select="$stackSize + 4"/></xsl:call-template>
                    </xsl:when>
                    <xsl:when test="parameter[$i]/@javaSignature='S'">
                    <xsl:call-template name="recurseStackSize"><xsl:with-param name="i" select="$i + 1"/><xsl:with-param name="stackSize" select="$stackSize + 4"/></xsl:call-template>
                    </xsl:when>
                    <xsl:when test="parameter[$i]/@javaSignature='F'">
                    <xsl:call-template name="recurseStackSize"><xsl:with-param name="i" select="$i + 1"/><xsl:with-param name="stackSize" select="$stackSize + 4"/></xsl:call-template>
                    </xsl:when>
                    <xsl:when test="parameter[$i]/@javaSignature='D'">
                    <xsl:call-template name="recurseStackSize"><xsl:with-param name="i" select="$i + 1"/><xsl:with-param name="stackSize" select="$stackSize + 8"/></xsl:call-template>
                    </xsl:when>
                    <xsl:when test="parameter[$i]/@javaSignature='V'">
                    <xsl:call-template name="recurseStackSize"><xsl:with-param name="i" select="$i + 1"/><xsl:with-param name="stackSize" select="$stackSize + 4"/></xsl:call-template>
                    </xsl:when>
                   <xsl:otherwise>
                    <xsl:call-template name="recurseStackSize"><xsl:with-param name="i" select="$i + 1"/><xsl:with-param name="stackSize" select="$stackSize"/></xsl:call-template>
                    </xsl:otherwise>
                  </xsl:choose>
           </xsl:when>
           <xsl:otherwise>
           int stackSize=<xsl:value-of select="$stackSize"/>;
            </xsl:otherwise>
          </xsl:choose>
  </xsl:template>

<xsl:variable name="iterations" select="200"/>

<xsl:template name="pi">
  <!-- named template called by main template below -->
  <xsl:param name="i">1</xsl:param>
  <xsl:param name="piValue">0</xsl:param>

  <xsl:choose>
  <!-- If there are more iterations to do, add the passed
       value of pi to another round of calculations. -->
  <xsl:when test="$i &lt;= $iterations">
    <xsl:call-template name="pi">
      <xsl:with-param name="i" select="$i + 4"/>
      <xsl:with-param name="piValue" 
           select="$piValue + (4 div $i) - (4 div ($i + 2))"/>
    </xsl:call-template>
  </xsl:when>

  <!-- If no more iterations to do, add 
       computed value to result tree. -->
  <xsl:otherwise>
   <xsl:value-of select="$piValue"/>
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


</xsl:stylesheet>