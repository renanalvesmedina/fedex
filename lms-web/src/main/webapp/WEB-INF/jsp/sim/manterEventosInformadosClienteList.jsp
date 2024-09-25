<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.manterEventosInformadosClienteAction" >
	<adsm:form action="/sim/manterEventosInformadosCliente" height="100" >
		<adsm:i18nLabels>
                <adsm:include key="LMS-00013"/>
                <adsm:include key="cliente"/>
                <adsm:include key="evento"/>
    	</adsm:i18nLabels>
		<adsm:combobox property="tipoCliente" label="tipoCliente" domain="DM_TIPO_CLIENTE" labelWidth="21%" width="29%"  required="true"/>




		<adsm:lookup property="cliente"  
						idProperty="idCliente" 
						criteriaProperty="pessoa.nrIdentificacao"  
						relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.sim.manterPedidosComprasAction.findLookupCliente" 
					 action="/coleta/cadastrarPedidoColeta" cmd="consultarClientes" dataType="text" 
					 label="cliente"  size="20" maxLength="20" width="75%" labelWidth="21%"  >	
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa"  size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>






		<adsm:combobox property="tpDocumentoCliente" label="documento" domain="DM_TIPO_DOCUMENTO_CLIENTE"  labelWidth="21%"  
		width="29%"	/>


		<adsm:combobox property="evento" label="evento" labelWidth="21%" width="29%" optionLabelProperty="dsEvento"
					service="lms.sim.manterEventosInformadosClienteAction.findEventos"  optionProperty="idEvento" boxWidth="200">
					<adsm:propertyMapping criteriaProperty="tpDocumentoCliente"  modelProperty="tpDocumento"/>
					
		</adsm:combobox>
		
	
	    <adsm:combobox property="tipoAcesso" domain="DM_TIPO_ACESSO_EVENTO" label="tipoAcesso" labelWidth="21%" width="29%" required="true" />
	    <adsm:combobox property="formaComunicacao" domain="DM_MEIO_COMUNICACAO" label="formaComunicacao" labelWidth="21%" width="29%" />

		<adsm:combobox property="servico" optionLabelProperty="dsServico" optionProperty="idServico"  labelWidth="21%" width="29%"
			service="lms.configuracoes.servicoService.find" label="servico" boxWidth="200"/>
			
		<adsm:range label="vigencia" labelWidth="21%" width="79%">
			<adsm:textbox dataType="JTDate" property="dataInicio" size="20"/>
			<adsm:textbox dataType="JTDate" property="dataFim" size="20"/>
		</adsm:range>
	
		<adsm:section caption="comunicacao" />
	
		<adsm:textbox dataType="integer" property="intervaloComunicacao" label="intervaloComunicacao" labelWidth="21%" width="29%" size="4" unit="dias" maxLength="2"/>	
		<adsm:textbox dataType="JTTime" property="horario" label="horario" labelWidth="21%" width="29%"/>	
	
		<adsm:combobox domain="DM_SIM_NAO" property="comunicarCadaEvento" label="comunicarCadaEvento" labelWidth="21%" width="29%" />
		<adsm:combobox domain="DM_SIM_NAO" property="somenteDiasUteis" label="somenteDiasUteis" labelWidth="21%" width="29%" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="configuracaoComunicacao"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idConfiguracaoComunicacao" property="configuracaoComunicacao" gridHeight="160"
	rowCountService="lms.sim.manterEventosInformadosClienteAction.getRowCount"
				  scrollBars="horizontal" rows="8">
		<adsm:gridColumn property="tpCliente" title="tipoCliente" width="80" isDomain="true"/>
		<adsm:gridColumn property="tpI.description" title="identificacao" width="60"  dataType="text"/>
		<adsm:gridColumn property="nrI" title="" width="120" dataType="text" mask="#,###,###,###,###,##0.00"/>
		<adsm:gridColumn property="cliente" title="cliente" width="150" dataType="text"/>
		<adsm:gridColumn property="evento" title="evento" width="210"/>
		<adsm:gridColumn property="outros" title="outros" width="60"/>
		<adsm:gridColumn property="tipoAcesso" title="tipoAcesso" width="100" isDomain="true"/>
		<adsm:gridColumn property="formaComunicacao.description" title="formaComunicacao" width="100" />
		<adsm:gridColumn property="intervalo" title="intervaloComunicacao" unit="dias" width="80"  dataType="integer"/>
		<adsm:gridColumn property="horario" title="horarioDeterminado" width="60" dataType="JTTime" />
		<adsm:gridColumn property="cadaEvento" title="comunicarCadaEvento" width="60" renderMode="image-check"/>
		<adsm:gridColumn  dataType="JTDate" property="vigenciaInicial" title="vigenciaInicial" width="60"/>
		<adsm:gridColumn  dataType="JTDate" property="vigenciaFinal" title="vigenciaFinal" width="60"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
<script>
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (
					(getElementValue("evento") != "") ||
					(getElementValue("cliente.idCliente") != "") ) 
				{
					return true;
				} else {
					
					alert(i18NLabel.getLabel("LMS-00013") 
								+ i18NLabel.getLabel("cliente") + ", "
								+ i18NLabel.getLabel("evento")+ ". "
								);
					return false;
                }
            }
		return false;
    }	
</script>
