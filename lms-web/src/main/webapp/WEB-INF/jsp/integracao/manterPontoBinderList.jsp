<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="com.mercurio.adsmmanager.integracao.action.manterPontoBinderAction">

	<adsm:form
		action="/integracao/manterPontoBinder"> 		

		<adsm:textbox
			dataType="text"
			property="nome" 
			label="nome"
			maxLength="70"
			size="40"
			labelWidth="12%"
			width="38%"/>

		<adsm:textbox
			dataType="text"
			property="descricao" 
			label="descricao"
			size="40"
			maxLength="70"
			labelWidth="12%"
			width="38%"/>

		<adsm:combobox
			property="tipo"
			label="tipo"
			domain="DM_TIPO_PONTO_INTEGRACAO"
			labelWidth="12%"
			width="38%"/>

		<adsm:combobox
			property="tpOrigem"
			label="origem"
			domain="DM_TIPO_ORIGEM_LAYOUT"
			labelWidth="12%"
			width="38%"/>

		<adsm:buttonBar
			freeLayout="true">
			<adsm:findButton
				callbackProperty="pontosIntegracaoGD"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		idProperty="id"
		property="pontosIntegracaoGD"
		defaultOrder="nome"
		rows="10"
		selectionMode="check"
		gridHeight="200"
		unique="true">

		<adsm:gridColumn
			title="nome"
			property="nome"
			align="center"
			width="20%"/>

		<adsm:gridColumn
			title="tipo"
			property="tipo.description"
			align="center"
			width="20%"/>

		<adsm:gridColumn
			title="origem"
			property="tpOrigem.description"
			align="center"
			width="20%"/>

		<adsm:gridColumn
			title="descricao"
			property="descricao"
			align="center"
			width="40%"/>

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar> 
	</adsm:grid>
</adsm:window>