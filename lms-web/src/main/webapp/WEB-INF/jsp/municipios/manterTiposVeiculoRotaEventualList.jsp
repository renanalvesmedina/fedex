<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterTiposVeiculoRotaEventualAction" >
	<adsm:form action="/municipios/manterTiposVeiculoRotaEventual" idProperty="idTipoMeioTranspRotaEvent" >
	    <adsm:hidden property="rotaIdaVolta.idRotaIdaVolta" value="1" />
	    <adsm:hidden property="rotaIdaVolta.idMoedaPais" serializable="true" />
		<adsm:hidden property="rotaIdaVolta.siglaSimbolo" serializable="true" />
	    
	    <adsm:textbox dataType="text" label="rota" property="rotaIdaVolta.dsRota"
	    		size="35" disabled="true" labelWidth="20%" width="80%" serializable="false" />
		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte"
				optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte"
				service="lms.municipios.manterTiposVeiculoRotaEventualAction.findTipoMeioTransporteCombo"
				label="tipoMeioTransporte" labelWidth="20%" width="80%" boxWidth="198" />
				
		<adsm:range label="vigencia" labelWidth="20%" width="80%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
        </adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoMeioTranspRotaEvent" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTipoMeioTranspRotaEvent" property="tipoMeioTranspRotaEvent" unique="true" rows="12"
			defaultOrder="tipoMeioTransporte_.dsTipoMeioTransporte,dtVigenciaInicial" >
		<adsm:gridColumn title="tipoMeioTransporte" property="tipoMeioTransporte.dsTipoMeioTransporte" />
		<adsm:gridColumn title="valorPedagio" property="dsMoeda_01" width="60" />
		<adsm:gridColumn title="" property="vlPedagio" dataType="currency" width="80" />
		<adsm:gridColumn title="valorFrete" property="dsMoeda_02" width="60" />
		<adsm:gridColumn title="" property="vlFrete" dataType="currency" width="80" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="90"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="80"/>
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
