<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="text" indent="yes"/>


<xsl:template match="/">
	<xsl:apply-templates select="alias"/>
</xsl:template>

<xsl:template match="alias">
  <xsl:choose>
    <xsl:when test="@typeName=''">
      <xsl:text>// Skipping </xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:text> maps to an unknown type</xsl:text>
    </xsl:when>
    <xsl:when test="@typeName='boolean' or
                    @typeName='byte' or
                    @typeName='char' or
                    @typeName='short' or
                    @typeName='int' or
                    @typeName='long' or
                    @typeName='float' or
                    @typeName='double'">
      <xsl:text>// Skipping </xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:text> maps to primitive type </xsl:text>
      <xsl:value-of select="@typeName"/>
    </xsl:when>
    <xsl:otherwise>
package <xsl:value-of select="@packageName"/>;

/**
 * Jawin generated code please do not edit
 *
 * Alias: <xsl:value-of select="@name"/>
 * Description: <xsl:value-of select="@description"/>
 *
 * @author jawin_gen
 */

public class <xsl:value-of select="@name"/> extends <xsl:value-of select="@typeName"/>
{
	<xsl:apply-templates select="function"/>
}
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="function">
	public <xsl:for-each select="return"><xsl:call-template name="return"/></xsl:for-each> <xsl:value-of select="@name"/>(<xsl:for-each select="parameter"><xsl:call-template name="Parameter"/><xsl:if test="not(position()=last())"><xsl:text>,</xsl:text></xsl:if></xsl:for-each>) throws COMException
	{
            return super.<xsl:value-of select="@name"/>(<xsl:for-each select="parameter"><xsl:call-template name="InvokeParameter"/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each>);
	}
</xsl:template>

  <xsl:template name="return">
    <xsl:value-of select="@javaString"/>
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
          <xsl:text></xsl:text><xsl:value-of select="@name"/><xsl:text></xsl:text>
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
</xsl:stylesheet>