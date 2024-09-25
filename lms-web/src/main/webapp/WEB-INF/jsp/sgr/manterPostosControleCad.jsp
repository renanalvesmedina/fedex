<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.postoControleService">
	<adsm:form action="/sgr/manterPostosControle" idProperty="idPostoControle">

		<adsm:textbox dataType="text" property="nmPostoControlePassaporte" label="nomePosto" maxLength="50" width="55%" size="45%" required="true" />

		<adsm:lookup
			dataType="text"
			property="reguladoraSeguro"
			idProperty="idReguladora" 
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.sgr.manterPostosControleAction.findLookupReguladoraSeguro"
			action="/seguros/manterReguladorasSeguro"
			label="reguladoraSeguro"
			maxLength="20" size="20" width="85%" required="true">
			<adsm:propertyMapping 
				modelProperty="pessoa.nmPessoa"
				relatedProperty="reguladoraSeguro.pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="reguladoraSeguro.pessoa.nmPessoa"
				size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox dataType="text" property="nmLocal" label="nomeLocal" maxLength="50" width="55%" size="45%" required="true" />
		<adsm:combobox property="tpBandeiraPosto" domain="DM_BANDEIRA_POSTO" label="bandeira" width="15%" renderOptions="true"/>
		
		<adsm:lookup dataType="text" property="municipio" idProperty="idMunicipio" criteriaProperty="nmMunicipio" 
					 service="lms.municipios.municipioService.findLookup" action="/municipios/manterMunicipios" 
					 label="municipio" maxLength="50" width="55%" size="45%" required="true" exactMatch="false" minLengthForAutoPopUpSearch="4"/>
					 
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao"  labelWidth="15%" width="15%" required="true" renderOptions="true"/>

		<adsm:lookup dataType="text" property="rodovia" idProperty="idRodovia" criteriaProperty="sgRodovia" 
					 service="lms.municipios.rodoviaService.findLookup" action="/municipios/manterRodovias" cmd="list" 
					 label="rodovia" size="5" maxLength="10" width="55%" required="true" exactMatch="false" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping modelProperty="dsRodovia" relatedProperty="rodovia.dsRodovia"/>
			<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox dataType="integer" property="nrKm" label="km" maxLength="6" required="true" width="15%" size="7%"/>
		<adsm:textbox dataType="text" property="nmResponsavel" label="responsavel" maxLength="100" width="55%" size="45%"/>
		<adsm:textbox dataType="text" property="nrTelefone" label="fone" width="15%" size="15%" maxLength="20" />
		<adsm:textbox dataType="email" property="dsCorreioEletronico" label="email" maxLength="60" width="55%" size="45%" />
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>