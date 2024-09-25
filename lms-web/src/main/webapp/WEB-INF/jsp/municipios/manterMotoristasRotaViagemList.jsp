<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterMotoristasRotaViagemAction" >
	<adsm:form action="/municipios/manterMotoristasRotaViagem" idProperty="idMotoristaRotaViagem" >
		<adsm:hidden property="rotaViagem.idRotaViagem" value="1" />

		<adsm:textbox dataType="integer" label="rotaIda" property="rotaIda.nrRota" disabled="true" mask="0000" size="5" serializable="false">
			<adsm:textbox dataType="text" property="rotaIda.dsRota" disabled="true" size="30" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox dataType="integer" label="rotaVolta" property="rotaVolta.nrRota" disabled="true" mask="0000" size="5" serializable="false" >
			<adsm:textbox dataType="text" property="rotaVolta.dsRota" disabled="true" size="30" serializable="false"/>
		</adsm:textbox>

		<adsm:lookup
			label="motorista"
			property="motorista"
			idProperty="idMotorista"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.municipios.manterMotoristasRotaViagemAction.findMotoristaLookup"
			action="/contratacaoVeiculos/manterMotoristas"
			dataType="text"
			size="15"
			maxLength="20"
			width="85%"
			exactMatch="false"
			minLengthForAutoPopUpSearch="5"
		>
			<adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="30" disabled="true"/>
		</adsm:lookup>

		<adsm:range label="vigencia" width="85%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motoristaRotaViagem" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="motoristaRotaViagem" idProperty="idMotoristaRotaViagem" unique="true" rows="12"
			defaultOrder="motorista_pessoa_.nmPessoa,dtVigenciaInicial" >
		<adsm:gridColumn title="identificacao" property="motorista.pessoa.tpIdentificacao" isDomain="true" width="50" />
		<adsm:gridColumn title="" property="motorista.pessoa.nrIdentificacaoFormatado" width="100" align="right" />
		<adsm:gridColumn title="motorista" property="motorista.pessoa.nmPessoa" />	
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="90"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="80" />

		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
