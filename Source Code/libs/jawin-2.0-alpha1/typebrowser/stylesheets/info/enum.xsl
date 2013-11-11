<?xml version="1.0"?> 
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt"
  >

<xsl:output method="html" indent="yes"
  xalan:omit-meta-tag="yes"
  />

<xsl:template match="/">
<html>
  <head>
    <style type="text/css">
      td { font-family: sans-serif; font-size: 12; }
      td.heading { font-size: 14; }
    </style>
  </head>
<body>

<xsl:apply-templates select="/enum"/>

</body>
</html>
</xsl:template>

<xsl:template match="enum">
<table width="100%" cellpadding="2" cellspacing="0" border="0">
<tr bgcolor="#C5DF76">
	<td class="heading" colspan="2">
		Enum: <xsl:value-of select="@name"/>
	</td>
</tr>
<tr bgcolor="#F1F1F1">
	<td>
		Description
	</td>
	<td>
		<xsl:value-of select="@description"/>
	</td>
</tr>
<tr bgcolor="#F1F1F1">
	<td>
		Help file
	</td>
	<td>
		<xsl:value-of select="@helpFile"/>
	</td>
</tr>

<xsl:call-template name="emptyRow"/>

<tr bgcolor="#C5DF76">
	<td class="heading" colspan="2">
		Properties
	</td>
</tr>

<xsl:apply-templates select="property"/>

</table>

</xsl:template>

<!-- match property nodes and write out their values -->
<xsl:template match="property">
<tr bgcolor="#F1F1F1">
	<td>
		<xsl:value-of select="@name"/>
	</td>
	<td>
		<xsl:value-of select="variant/@value"/>
	</td>
</tr>
</xsl:template>

<xsl:template name="emptyRow">
<tr>
	<td colspan="2">&#160;</td>
</tr>
</xsl:template>

</xsl:stylesheet>























