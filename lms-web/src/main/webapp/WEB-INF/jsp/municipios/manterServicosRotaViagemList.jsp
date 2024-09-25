<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterServicosRotaViagemAction" >
	<adsm:form action="/municipios/manterServicosRotaViagem" idProperty="idServicoRotaViagem" >
		<adsm:hidden property="rotaViagem.idRotaViagem" value="1" />
		<adsm:hidden property="rotaViagem.versao" value="-1" />

		<adsm:textbox dataType="text" label="tipoRota" property="rotaViagem.tipoRota" size="35" width="85%" disabled="true" serializable="false" />

		<adsm:textbox dataType="integer" label="rotaIda" property="rotaIda.nrRota" disabled="true" mask="0000" size="5" serializable="false">
			<adsm:textbox dataType="text" property="rotaIda.dsRota" disabled="true" size="30" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox dataType="integer" label="rotaVolta" property="rotaVolta.nrRota" disabled="true" mask="0000" size="5" serializable="false" >
			<adsm:textbox dataType="text" property="rotaVolta.dsRota" disabled="true" size="30" serializable="false"/>
		</adsm:textbox>

		<adsm:combobox property="servico.idServico" label="servico"
			optionProperty="idServico" optionLabelProperty="dsServico"
			service="lms.municipios.manterServicosRotaViagemAction.findServicoCombo" boxWidth="200" width="85%"
		>
		</adsm:combobox>

		<adsm:range label="vigencia" width="85%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="servicoRotaViagem" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="servicoRotaViagem" idProperty="idServicoRotaViagem" unique="true" rows="11"
			defaultOrder="servico_.dsServico,dtVigenciaInicial">
		<adsm:gridColumn title="servico" property="servico.dsServico"/>
		<adsm:gridColumn title="situacao" property="servico.tpSituacao" isDomain="true" width="10%"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="15%"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="15%"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>