<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPromotoresClienteAction">
	<adsm:form action="/vendas/manterPromotoresCliente">

        <adsm:hidden property="cliente.idCliente"/>
	    <adsm:textbox
	    	dataType="text"
	    	property="cliente.pessoa.nrIdentificacao"
	    	label="cliente"
	    	size="20"
	    	maxLength="20"
	    	disabled="true"
	    	labelWidth="20%"
	    	width="80%"
	    	serializable="false" >
	        <adsm:textbox
	        	dataType="text"
	        	property="cliente.pessoa.nmPessoa"
	        	size="60"
	        	maxLength="50"
	        	disabled="true"
	        	serializable="false"/>
	    </adsm:textbox>

		<adsm:lookup
	   		property="usuario" 
	   		idProperty="idUsuario" 
	   		criteriaProperty="nrMatricula" 
            dataType="text" 
            label="promotor" 
            size="16" 
            maxLength="16" 
            labelWidth="20%" 
            width="80%" 
            exactMatch="false"
            service="lms.vendas.manterPromotoresClienteAction.findLookupFuncionarioPromotor" 
			action="/configuracoes/consultarFuncionarios"
			cmd="promotor">
            <adsm:propertyMapping
            	relatedProperty="usuario.nmUsuario"
            	modelProperty="nmUsuario"
            	inlineQuery="true"/>
            <adsm:textbox
            	dataType="text"
            	property="usuario.nmUsuario"
            	size="30"
            	maxLength="45"
            	disabled="true"
            	serializable="false"/>
		</adsm:lookup>

		<adsm:combobox
			property="tpModal"
			label="modal"
			domain="DM_MODAL"
			labelWidth="20%"
			width="40%"/>
		<adsm:combobox
			property="tpAbrangencia"
			label="abrangencia"
			domain="DM_ABRANGENCIA"
			labelWidth="20%"
			width="20%"/>
		<adsm:textbox
			label="dataInicioPromotor"
			width="80%"
			labelWidth="20%"
			dataType="JTDate"
			property="dtInicioPromotor" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton
				callbackProperty="promotores"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid
		idProperty="idPromotorCliente"
		property="promotores"
		rowCountService="lms.vendas.manterPromotoresClienteAction.getRowCountOtimizado"
		service="lms.vendas.manterPromotoresClienteAction.findPaginatedOtimizado"
		gridHeight="200"
		unique="true">
		<adsm:gridColumn title="promotor" property="nomePromotor" width="45%" />
		<adsm:gridColumn title="dataInicio" property="dtInicioPromotor" width="15%" dataType="JTDate"/>
		<adsm:gridColumn title="modal" property="tpModal" isDomain="true" width="20%" />
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" isDomain="true" width="20%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>