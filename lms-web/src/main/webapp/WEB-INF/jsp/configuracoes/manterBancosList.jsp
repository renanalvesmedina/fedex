<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.bancoService">
	<adsm:form action="/configuracoes/manterBancos">

		<adsm:textbox dataType="integer" minValue="0" property="nrBanco" label="numero" width="35%" maxLength="3" size="3"/>
		<adsm:textbox dataType="text" property="nmBanco" label="nome" maxLength="60" size="40"/>

		<adsm:lookup service="lms.municipios.paisService.findLookup" 
					 property="pais" 
					 label="pais"
					 idProperty="idPais"
					 criteriaProperty="nmPais"					 
					 dataType="text" 
					 maxLength="60"
					 action="/municipios/manterPaises"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 size="30"/>

		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="banco"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid idProperty="idBanco" property="banco" defaultOrder="nmBanco" rows="13">	
		<adsm:gridColumn width="15%" title="numero" property="nrBanco" align="right"/>
		<adsm:gridColumn width="50%" title="nome" property="nmBanco" />
		<adsm:gridColumn width="20%" title="pais" property="pais.nmPais" />
		<adsm:gridColumn width="15%" title="situacao" property="tpSituacao" isDomain="true"/>
        <adsm:buttonBar> 
		   <adsm:removeButton/>
    	</adsm:buttonBar>
	</adsm:grid>

</adsm:window>