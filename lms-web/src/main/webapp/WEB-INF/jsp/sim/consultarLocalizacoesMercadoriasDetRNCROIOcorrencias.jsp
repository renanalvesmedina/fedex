<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="185">

		<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="numero" label="numero" disabled="true"/>
		<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="motivoAbertura" label="motivoAbertura" disabled="true"/>

		<adsm:textbox width="82%" labelWidth="18%" size="15" dataType="text" property="dataHoraAbertura" label="dataHoraAbertura" disabled="true"/>

		<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="setorResponsavel" label="setorResponsavel" disabled="true"/>
		<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="funcionario" label="funcionario" disabled="true"/>

		<adsm:textarea property="descricao" maxLength="50" label="descricao" columns="100" labelWidth="18%" width="82%" disabled="true"/>

		<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="volumes" label="volumes" disabled="true"/>
		<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="valorOcorrencia" label="valorOcorrencia" disabled="true"/>

		<adsm:textbox width="82%" labelWidth="18%" size="25" dataType="text" property="situacao" label="situacao" disabled="true"/>

		<adsm:grid paramId="id" paramProperty="id" showCheckbox="false" unique="false" gridHeight="22"  scrollBars="vertical" showPaging="false">
			<adsm:gridColumn width="20%" title="numero" property="numero" align="right"/>
			<adsm:gridColumn width="20%" title="motivoAbertura" property="motivo" align="left"/>
			<adsm:gridColumn width="20%" title="volumes" property="volume" align="right"/>
			<adsm:gridColumn width="20%" title="valorOcorrencia" property="valor" align="right"/>
			<adsm:gridColumn width="20%" title="situacao" property="situacao" align="left"/>
		</adsm:grid>
		<adsm:buttonBar freeLayout="true">
				<adsm:button caption="negociacoes" onClick="showModal:/sim/consultarLocalizacoesMercadorias.do?cmd=RNCROIOcorrenciasNegociacao"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>   