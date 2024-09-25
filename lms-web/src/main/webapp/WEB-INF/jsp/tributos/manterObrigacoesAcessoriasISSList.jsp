<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.tributos.manterObrigacoesAcessoriasISSAction"
>
	<adsm:form 
		action="/tributos/manterObrigacoesAcessoriasISS"
		idProperty="idObrigacaoAcessoriaIssMun"
	>

		<adsm:lookup 
			service="lms.tributos.manterObrigacoesAcessoriasISSAction.findLookupMunicipio" 
			action="/municipios/manterMunicipios" 
			property="municipio" 
			idProperty="idMunicipio" 
			criteriaProperty="nmMunicipio" 

			exactMatch="false"
			minLengthForAutoPopUpSearch="2"
			label="municipio" 
			dataType="text" 

			width="85%" 
			maxLength="60" 
			size="30"
		/>

		<adsm:textbox 
			label="descricao" 
			dataType="text" 
			property="dsObrigacaoAcessoriaIssMun" 
			size="80" 
			maxLength="60" 
			width="85%" 
		/>

        <adsm:combobox 
        	labelWidth="15%"
        	width="35%"
	        property="tpPeriodicidade" 
	        label="periodicidade" 
	        domain="DM_PERIODICIDADE_OBRIGACOES_MUNICIPIO" 
        />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="obrigacaoAcessoriaIssMun"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idObrigacaoAcessoriaIssMun" defaultOrder="municipio_.nmMunicipio" 
			 	rows="12"
			 	property="obrigacaoAcessoriaIssMun" unique="true" >
		<adsm:gridColumn title="municipio" property="municipio.nmMunicipio" width="40%" />
		<adsm:gridColumn title="descricao" property="dsObrigacaoAcessoriaIssMun" width="60%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

