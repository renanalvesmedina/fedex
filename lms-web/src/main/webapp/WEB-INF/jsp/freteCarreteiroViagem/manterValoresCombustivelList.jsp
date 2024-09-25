<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarValoresCombustivel" service="lms.fretecarreteiroviagem.manterValoresCombustivelAction">
	<adsm:form action="/freteCarreteiroViagem/manterValoresCombustivel">
		<adsm:lookup property="moedaPais.pais" idProperty="idPais" service="lms.fretecarreteiroviagem.manterValoresCombustivelAction.findLookupPais"
					dataType="text" criteriaProperty="nmPais" label="pais" size="35" maxLength="60" action="/municipios/manterPaises"
					exactMatch="false" 	minLengthForAutoPopUpSearch="3" labelWidth="19%" width="81%"/>
		
		<adsm:combobox property="tipoCombustivel.idTipoCombustivel" label="tipoCombustivel" service="lms.fretecarreteiroviagem.manterValoresCombustivelAction.findComboTpCombustivel" 
						optionProperty="idTipoCombustivel" optionLabelProperty="dsTipoCombustivel" labelWidth="19%" width="81%" />
        
		<adsm:range label="vigencia" labelWidth="19%" width="81%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true" >
			<adsm:findButton callbackProperty="ValorCombustivel"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idValorCombustivel" property="ValorCombustivel" unique="true" selectionMode="check" rows="11">
		<adsm:gridColumn title="pais" property="moedaPais.pais.nmPais" width="150"/>
		<adsm:gridColumn title="tipoCombustivel" property="tipoCombustivel.dsTipoCombustivel" width=""/>
		<adsm:gridColumn title="percentualReajuste" property="pcAumento" width="72" align="right" dataType="percent" mask="##0.000"/>			
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" property="moedaPais.moeda.sgMoeda" width="40" />		
			<adsm:gridColumn title="" property="moedaPais.moeda.dsSimbolo" width="40" />
		</adsm:gridColumnGroup> 
		<adsm:gridColumn title="" property="vlValorCombustivel" width="65" align="right" dataType="currency" mask="##0.000"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="100" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="100" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>