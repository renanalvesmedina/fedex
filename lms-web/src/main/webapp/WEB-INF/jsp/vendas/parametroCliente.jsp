<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<%-- FRETE PESO SECTION --%>
<adsm:section 
	caption="fretePeso"/>

<%-- MINIMO FRETE PESO COMBO --%>
<adsm:hidden property="minimoFretePesoFlag" value="false"/>
<adsm:combobox 
	domain="DM_INDICADOR_FRETE_MINIMO" 
	label="minimoFretePeso" 
	labelWidth="23%"
	onchange="return validateTpMinimoFretePeso();"
	property="tpIndicadorMinFretePeso" 
	required="true"
	width="29%">
	<adsm:textbox 
		dataType="decimal" 
		property="vlMinFretePeso" 
		mask="###,###,###,###,##0.00"
		onchange="return validateVlMinimoFretePeso();"
		size="18"/>
</adsm:combobox>

<%-- PERCENTUAL MINIMO PROGRESSIVO COMBO --%>
<adsm:combobox 
	domain="DM_ACRESCIMO_DESCONTO"
	label="percentualMinimoProgressivo"
	labelWidth="19%"
	property="tpIndicadorPercMinimoProgr" 
	required="true"
	width="29%">
	<adsm:textbox
		dataType="decimal"
		property="vlPercMinimoProgr"
		mask="###,##0.00"
		onchange="return validateVlPercentualMinimoProgressivo();"
		size="18"/>
</adsm:combobox>

<%-- FRETE PESO COMBO --%>
<adsm:combobox
	domain="DM_INDICADOR_PARAMETRO_CLIENTE"
	label="fretePeso"
	labelWidth="23%"
	onchange="return validateTpFretePeso();"
	property="tpIndicadorFretePeso"
	required="true"
	width="77%">
	<adsm:textbox
		dataType="decimal"
		property="vlFretePeso"
		mask="###,###,###,###,##0.00000"
		onchange="return validateVlFretePeso();"
		size="18"/>
</adsm:combobox>

<%-- VALOR MINIMO FRETE QUILO TEXT --%>
<adsm:textbox
	dataType="decimal"
	property="vlMinimoFreteQuilo"
	label="valorMinimoFreteQuilo"
	mask="###,###,###,###,##0.00"
	size="18"
	labelWidth="23%"
	onchange="return validateVlMinimoFreteQuilo();"
	width="29%"
	required="true"/>

<%-------------------------------%>
<%-- PAGA PESO EXCEDENTE CHECK --%>
<%-------------------------------%>
<adsm:checkbox
	property="blPagaPesoExcedente"
	label="pagaPesoExcedente"
	labelWidth="19%"
	onclick="return validateBlPagaPesoExcedente()"
	width="29%"/>

<%-- TARIFA MINIMA COMBO --%>
<adsm:hidden property="tarifaMinimaFlag" value="false"/>
<adsm:combobox
	domain="DM_INDICADOR_PARAMETRO_CLIENTE"
	label="tarifaMinima"
	labelWidth="23%"
	onchange="return validateTpTarifaMinima();"
	property="tpTarifaMinima"
	required="true"
	width="29%">
	<adsm:textbox
		dataType="decimal"
		property="vlTarifaMinima"
		mask="###,###,###,###,##0.00"
		onchange="return validateVlTarifaMinima();"
		size="18"/>
</adsm:combobox>

<%-- VALOR FRETE VOLUME TEXT --%>
<adsm:textbox
	dataType="decimal"
	property="vlFreteVolume"
	label="valorFreteVolume"
	mask="###,###,###,###,##0.00"
	onchange="return validateVlFreteVolume();"
	size="18"
	labelWidth="19%"
	width="29%"
	required="true"/>

<%-- VALOR TARIFA ESPECIFICA COMBO --%>
<adsm:hidden property="tarifaEspecificaFlag" value="false"/>
<adsm:combobox
	domain="DM_INDICADOR_PARAMETRO_CLIENTE"
	label="valorTarifaEspecifica"
	labelWidth="23%"
	onchange="return validateTpTarifaEspecifica();"
	property="tpIndicVlrTblEspecifica"
	required="true"
	width="29%">
	<adsm:textbox
		dataType="decimal"
		property="vlTblEspecifica"
		mask="###,###,###,###,##0.00"
		onchange="return validateVlTarifaEspecifica();"
		size="18"/>
</adsm:combobox>

<%-- FRETE VALOR SECTION --%>
<adsm:section
	caption="freteValor"/>

<%-- ADVALOREM1 COMBO --%>
<adsm:combobox
	domain="DM_INDICADOR_ADVALOREM"
	label="advalorem1"
	labelWidth="28%"
	onchange="return validateTpAdvalorem1();"
	property="tpIndicadorAdvalorem"
	required="true"
	width="62%">
	<adsm:textbox
		dataType="decimal"
		property="vlAdvalorem"
		maxLength="15"
		onchange="return validateVlAdvalorem1();"
		size="15"/>
</adsm:combobox>
<%-- ADVALOREM2 COMBO --%>
<adsm:combobox 
	domain="DM_INDICADOR_ADVALOREM" 
	label="advalorem2" 
	labelWidth="28%"
	onchange="return validateTpAdvalorem2();"
	property="tpIndicadorAdvalorem2" 
	required="true"
	width="72%">
	<adsm:textbox 
		dataType="decimal" 
		property="vlAdvalorem2" 
		mask="###,###,###,###,##0.00"
		onchange="return validateVlAdvalorem2();"
		size="18"/>
</adsm:combobox>

<%-- VALOR REFERENCIA COMBO --%>
<adsm:hidden property="valorReferenciaFlag" value="false"/>
<adsm:combobox
	domain="DM_INDICADOR_PARAMETRO_CLIENTE"
	label="valorReferencia"
	labelWidth="28%"
	onchange="return validateTpValorReferencia();"
	property="tpIndicadorValorReferencia"
	required="true"
	width="72%">
	<adsm:textbox
		dataType="decimal"
		property="vlValorReferencia"
		mask="###,###,###,###,##0.00"
		onchange="return validateVlValorReferencia();"
		size="18"/>
</adsm:combobox>

<%-- FRETE PERCENTUAL SECTION --%>
<adsm:section 
	caption="fretePercentual"/>

<%-- PERCENTUAL FRETE PERCENTUAL TEXT --%>
<adsm:textbox 
	dataType="decimal" 
	label="percentualFretePercentual"
	labelWidth="28%"
	property="pcFretePercentual" 
	required="true"
	mask="##0.00"
	onchange="return validateVlPercentualFretePercentual();"
	size="5" 
	width="72%"/>
<%-- VALOR MINIMO FRETE PERCENTUAL TEXT --%>
<adsm:textbox 
	dataType="decimal" 
	label="valorMinimoFretePercentual"
	labelWidth="28%"
	property="vlMinimoFretePercentual" 
	required="true"
	mask="###,###,###,###,##0.00"
	onchange="validateVlMinimoFretePercentual();"
	size="18" 
	width="72%"/>

<%-- VALOR TONELADA FRETE PERCENTUAL TEXT --%>
<adsm:textbox
	dataType="decimal"
	label="valorToneladaFretePercentual"
	labelWidth="28%"
	property="vlToneladaFretePercentual"
	required="true"
	mask="###,###,###,###,##0.00"
	onchange="return validateVlToneladaFretePercentual();"
	size="18"
	width="72%"/>

<%-- PESO REFERENCIA FRETE PERCENTUAL TEXT --%>
<adsm:textbox
	dataType="weight"
	label="pesoReferenciaFretePercentual"
	labelWidth="28%"
	property="psFretePercentual"
	maxLength="15"
	onchange="return validatePsReferenciaFretePercentual();"
	required="true"
	size="15"
	unit="kg"
	width="72%"/>

<%-- GRIS SECTION --%>
<adsm:section
	caption="gris"/>
<%-- PERCENTUAL GRIS COMBO --%>
<adsm:combobox
	property="tpIndicadorPercentualGris"
	required="true"
	domain="DM_INDICADOR_ADVALOREM"
	label="percentualGris"
	labelWidth="28%"
	onchange="return validateTpPercentualGris();"
	width="72%">
	<adsm:textbox
		dataType="decimal"
		property="vlPercentualGris"
		mask="###,###,###,###,##0.00"
		onchange="return validateVlPercentualGris();"
		size="18"/>
</adsm:combobox>

<%-- MINIMO GRIS COMBO --%>
<adsm:combobox 
	domain="DM_INDICADOR_PARAMETRO_CLIENTE" 
	label="minimoGris" 
	labelWidth="28%"
	onchange="return validateTpMinimoGris();"
	property="tpIndicadorMinimoGris"
	required="true"
	width="72%">
	<adsm:textbox
		dataType="decimal"
		property="vlMinimoGris"
		mask="###,###,###,###,##0.00"
		onchange="return validateVlMinimoGris();"
		size="18"/>
</adsm:combobox>

<%-- PEDAGIO SECTION--%>
<adsm:section
	caption="pedagio"/>
<adsm:combobox 
	domain="DM_INDICADOR_PEDAGIO" 
	label="valorPedagio" 
	labelWidth="28%"
	onchange="return validateTpPedagio();"
	property="tpIndicadorPedagio" 
	required="true"
	width="72%">

	<adsm:textbox 
		dataType="decimal" 
		property="vlPedagio" 
		mask="###,###,###,###,##0.00000"
		onchange="return validateVlPedagio();"
		size="18"/>
</adsm:combobox>

<%-- GRIS SECTION --%>
<adsm:section
	caption="trt"/>
<%-- PERCENTUAL TRT COMBO --%>
<adsm:combobox
	property="tpIndicadorPercentualTrt"
	required="true"
	domain="DM_INDICADOR_ADVALOREM"
	label="percentualTrt"
	labelWidth="28%"
	onchange="return validateTpPercentualTrt();"
	width="72%">
	<adsm:textbox
		dataType="decimal"
		property="vlPercentualTrt"
		mask="###,###,###,###,##0.00"
		onchange="return validateVlPercentualTrt();"
		size="18"/>
</adsm:combobox>

<%-- MINIMO TRT COMBO --%>
<adsm:combobox 
	domain="DM_INDICADOR_PARAMETRO_CLIENTE" 
	label="minimoTrt" 
	labelWidth="28%"
	onchange="return validateTpMinimoTrt();"
	property="tpIndicadorMinimoTrt"
	required="true"
	width="72%">
	<adsm:textbox
		dataType="decimal"
		property="vlMinimoTrt"
		mask="###,###,###,###,##0.00"
		onchange="return validateVlMinimoTrt();"
		size="18"/>
</adsm:combobox>

<%-- TDE SECTION --%>
<adsm:section
	caption="tde"/>

<%--------------------------%>
<%-- PERCENTUAL TDE COMBO --%>
<%--------------------------%>
<adsm:combobox
	property="tpIndicadorPercentualTde"
	required="true"
	domain="DM_INDICADOR_ADVALOREM"
	label="percentualTde"
	labelWidth="28%"
	defaultValue="T"
	onchange="return validateTpPercentualTde();"
	width="72%">

	<adsm:textbox 
		dataType="decimal" 
		property="vlPercentualTde" 
		mask="###,###,###,###,##0.00"
		onchange="return validateVlPercentualTde();"
		size="18"/>

</adsm:combobox>

<%-- MINIMO TDE COMBO --%>
<adsm:combobox
	domain="DM_INDICADOR_PARAMETRO_CLIENTE"
	label="minimoTde"
	labelWidth="28%"
	onchange="return validateTpMinimoTde();"
	property="tpIndicadorMinimoTde"
	required="true"
	defaultValue="T"
	width="72%">

	<adsm:textbox
		dataType="decimal"
		property="vlMinimoTde"
		mask="###,###,###,###,##0.00"
		onchange="return validateVlMinimoTde();"
		size="18"/>
</adsm:combobox>

<%-- TOTAL FRETE SECTION --%>
<adsm:section
	caption="totalFrete" />

<%-- PERCENTUAL DESCONTO FRETE TOTAL TEXT --%>
<adsm:textbox
	dataType="decimal"
	label="percentualDescontoFreteTotal"
	labelWidth="28%"
	property="pcDescontoFreteTotal"
	mask="##0.00"
	onchange="return validateVlPercentualDescontoFreteTotal();"
	size="5"
	required="true"
	width="72%"	/>
<%-- PERCENTUAL COBRANCA DE REENTREGA TEXT --%>
<adsm:textbox 
	dataType="decimal" 
	label="percentualCobrancaReentrega"
	labelWidth="28%"
	property="pcCobrancaReentrega" 
	mask="##0.00"
	onchange="return validateVlPercentualCobrancaReentrega()"
	size="5" 
	required="true"
	width="72%"/>
<%-- PERCENTUAL COBRANCA DE DEVOLUCOES TEXT --%>
<adsm:textbox 
	dataType="decimal" 
	label="percentualCobrancaDevolucoes"
	labelWidth="28%"
	property="pcCobrancaDevolucoes" 
	mask="##0.00"
	onchange="return validateVlPercentualCobrancaDevolucoes()"
	size="5" 
	required="true"
	width="72%"/>