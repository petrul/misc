<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
    <xsl:apply-templates select="story" />
</xsl:template>

<xsl:template match="story">
    <div>
    Story #<xsl:value-of select="id"/> (vdm:<xsl:value-of select="vdmid"/>)
    </div>
    <div>
    Categories : <xsl:apply-templates select="categories/category" />
    </div>
    <div>
    Original : <xsl:value-of select="original"/>
    </div>
    <div id="alternatives">
    Alternatives :
        <ol>
        <xsl:apply-templates select="alternatives/alt" />
        </ol>
     </div>
</xsl:template>

<xsl:template match="categories/category">
    <xsl:value-of select="."/>,
</xsl:template>


<xsl:template match="alt">
    <li><xsl:value-of select="text"/></li>
</xsl:template>

</xsl:stylesheet>
