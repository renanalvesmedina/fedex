<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterRelacaoDocumentosDepositosAction">
	<adsm:form action="/contasReceber/manterFaturas" service="lms.contasreceber.manterRelacaoDocumentosDepositosAction">

		<adsm:lookup label="cliente"                                      
			service="lms.contasreceber.manterRelacaoDocumentosDepositosAction.findLookupClienteResponsavel" 
			dataType="text"
			property="cliente" 
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			exactMatch="true" 
			size="20"
			maxLength="20" 
			width="85%" 
			action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="cliente.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="60"/>  
		</adsm:lookup>

		<adsm:textbox label="numeroProtocolo" dataType="integer" property="idDepositoCcorrenteTmp" size="10" maxLength="10"/>

		<adsm:combobox 
			service="lms.contasreceber.manterRelacaoDocumentosDepositosAction.findComboCedentes" 
			optionLabelProperty="comboText" 
			optionProperty="idCedente" 
			property="cedente.idCedente" 
			onlyActiveValues="false"
			boxWidth="230"
			label="banco"> 
		</adsm:combobox>

		<adsm:range label="dataDeposito">
			<adsm:textbox dataType="JTDate" property="dtDepositoInicial" />
			<adsm:textbox label="ate" dataType="JTDate" property="dtDepositoFinal" />
		</adsm:range>

        <adsm:combobox label="situacao" property="tpSituacaoRelacao" domain="DM_STATUS_RELACAO"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="depositoCcorrente"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idDepositoCcorrente" property="depositoCcorrente" rows="11"
		service="lms.contasreceber.manterRelacaoDocumentosDepositosAction.findPaginatedTela"
		rowCountService="lms.contasreceber.manterRelacaoDocumentosDepositosAction.getRowCountTela">
		<adsm:gridColumn title="banco" property="nmBanco"  />
		<adsm:gridColumn title="agencia" property="nmAgencia"  />
        <adsm:gridColumn title="cliente" property="nmCliente" width="34%" />
        <adsm:gridColumn title="numeroProtocolo" property="idDepositoCcorrenteTmp" dataType="integer"/>
		<adsm:gridColumn title="dataDeposito" property="dtDeposito" dataType="JTDate"/>
		
		<adsm:gridColumn title="valorDeposito" property="siglaSimbolo" width="40" dataType="text"/>			
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">			
			<adsm:gridColumn title="" property="vlDeposito" dataType="currency" width="80"/>
		</adsm:gridColumnGroup>

        <adsm:gridColumn title="situacao" property="tpSituacaoRelacao" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>