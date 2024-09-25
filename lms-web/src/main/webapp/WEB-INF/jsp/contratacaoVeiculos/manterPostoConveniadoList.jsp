<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contratacaoveiculos.manterPostoConveniadoAction" >
	<adsm:form action="/contratacaoVeiculos/manterPostoConveniado">
        <adsm:combobox property="pessoa.tpPessoa" onlyActiveValues="true" labelWidth="15%" width="35%" label="tipoPessoa" domain="DM_TIPO_PESSOA" definition="TIPO_PESSOA.list" defaultValue="J" disabled="true" />
        
		<adsm:complement label="identificacao" >
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list" />
			<adsm:textbox definition="IDENTIFICACAO_PESSOA" serializable="true"/>
		</adsm:complement>	

		<adsm:textbox dataType="text" size="95%" labelWidth="15%" width="82%" property="pessoa.nmPessoa" label="nome" maxLength="50"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="postoConveniado"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idPostoConveniado" property="postoConveniado" gridHeight="200" rows="13" defaultOrder="pessoa_.nmPessoa" unique="true" >
		<adsm:gridColumn title="identificacao"	property="pessoa.tpIdentificacao" isDomain="true" width="55"/>
		<adsm:gridColumn title="" 			   	property="pessoa.nrIdentificacaoFormatado" width="120" align="right"/>
		<adsm:gridColumn title="nome" 			property="pessoa.nmPessoa" width="62%" />
		<adsm:gridColumn title="tipoPessoa" 	property="pessoa.tpPessoa" width="" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
