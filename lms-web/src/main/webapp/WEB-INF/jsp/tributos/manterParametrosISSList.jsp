<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterParametrosISSAction">
	<adsm:form action="/tributos/manterParametrosISS">

		<adsm:lookup property="municipio" 
		             criteriaProperty="nmMunicipio" 
		             idProperty="idMunicipio" 
		             service="lms.municipios.municipioService.findLookup" 
		             dataType="text" 
		             disabled="false" 
		             label="municipio" 
		             size="30" 
		             maxLength="60" 
		             action="/municipios/manterMunicipios" labelWidth="20%" 
		             width="32%" 
		             exactMatch="false"
		             minLengthForAutoPopUpSearch="3"/>
			
		<adsm:textbox label="diaRecolhimento"  
		              dataType="integer" 
		              property="dtDiaRecolhimento" 
		              size="10" 
		              maxLength="2" 
		              labelWidth="18%" 
		              width="30%"/>

		<adsm:combobox label="formaPagamento" 
		               property="tpFormaPagamento" 
			           labelWidth="20%" 
			           width="32%"
			           domain="DM_FORMA_PGTO_ISS" />
			           
		<adsm:combobox label="dispositivoLegal" 
		               property="tpDispositivoLegal" 
			           labelWidth="18%" 
			           domain="DM_TIPO_DISPOSITIVO_LEGAL"
			           width="30%"/>

		<adsm:textbox label="numeroDispositivo"  
		              dataType="text" 
		              property="nrDispositivoLegal" 
		              size="20" 
		              maxLength="20" 
		              labelWidth="20%" 
		              width="32%"/>
		              
		<adsm:textbox label="anoDispositivo" 
		 			  dataType="JTDate" 
		 			  property="dtAnoDispositivoLegal" 
		 			  size="10" 
		 			  mask="yyyy"
		 			  labelWidth="18%" 
		 			  picker="false"
		 			  width="30%"/>

		<adsm:combobox label="livroEletronico" 
		               property="blProcEletronicoLivro" 
		               domain="DM_SIM_NAO"
			           labelWidth="20%" 
			           width="32%"/>
			           
		<adsm:combobox label="emissaoComCTRC" 
		               property="blEmissaoComCtrc" 
		               domain="DM_SIM_NAO"
		               labelWidth="18%" 
		               width="30%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="parametrosISS"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idParametroIssMunicipio" property="parametrosISS" defaultOrder="municipio_.nmMunicipio" selectionMode="check" rows="11" unique="true">
		<adsm:gridColumn title="municipio" property="municipio.nmMunicipio" width="40%" dataType="text" />
		<adsm:gridColumn title="diaRecolhimento" property="dtDiaRecolhimento" width="20%" dataType="integer" />
		<adsm:gridColumn title="livroEletronico" property="blProcEletronicoLivro"  width="20%" renderMode="image-check"/>
		<adsm:gridColumn title="emissaoComCTRC" property="blEmissaoComCtrc"  width="20%" renderMode="image-check" />
        <adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
