<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterTaxasSUFRAMAAction">
	<adsm:form action="/tributos/manterTaxasSUFRAMA" >
	
		<adsm:textbox label="valorMercadoria" property="vlMercadoria" dataType="currency" size="22" width="40%"/>

		<adsm:combobox label="indicadorCalculo" property="tpIndicadorCalculo" domain="DM_INDICADOR_CALCULO_SUFRAMA" width="30%"/>

		<adsm:range label="valorLiquido" width="40%">
			<adsm:textbox property="vlLiquidoInicial" dataType="currency" size="22"/>
			<adsm:textbox property="vlLiquidoFinal" dataType="currency" size="22"/>
		</adsm:range>
		
		<adsm:textbox label="vigencia" property="dtVigencia" dataType="JTDate" width="30%"/>		

	<adsm:buttonBar freeLayout="true">
		<adsm:findButton callbackProperty="taxaSuframa"/>
		<adsm:resetButton/>
	</adsm:buttonBar>

	</adsm:form>
	<adsm:grid idProperty="idTaxaSuframa" property="taxaSuframa" rows="12" 
			   defaultOrder="vlMercadoriaInicial, tpIndicadorCalculo, vlLiquido, dtVigenciaInicial">
		<adsm:gridColumn dataType="currency" property="vlMercadoriaInicial" title="valorMercadoriaInicial" width="15%"/>	   
		<adsm:gridColumn dataType="currency" property="vlMercadoriaFinal" title="valorMercadoriaFinal" width="15%"/>		
		<adsm:gridColumn property="tpIndicadorCalculo" title="indicadorCalculo" width="30%"/>		
		<adsm:gridColumn dataType="currency" property="vlLiquido" title="valorLiquido" width="16%"/>		
		<adsm:gridColumn dataType="date" property="dtVigenciaInicial" title="vigenciaInicial" width="12%"/>
		<adsm:gridColumn dataType="date" property="dtVigenciaFinal" title="vigenciaFinal" width="12%"/>		
		<adsm:buttonBar >
			<adsm:removeButton/>
		</adsm:buttonBar>		
	</adsm:grid>

</adsm:window>