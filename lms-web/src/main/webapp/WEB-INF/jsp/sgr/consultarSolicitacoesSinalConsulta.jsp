<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.sgr.consultarSolicitacoesSinalAction">
	<adsm:form action="/sgr/consultarSolicitacoesSinal" >
		<adsm:i18nLabels>
			<adsm:include key="LMS-11013"/>
		</adsm:i18nLabels>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>

		<adsm:lookup label="filialOrigem" dataType="text" size="3" maxLength="3" labelWidth="25%" width="75%"
					 property="filial" idProperty="idFilial" required="true"
					 service="lms.sgr.consultarSolicitacoesSinalAction.findLookupFilial" 
                     action="/municipios/manterFiliais"                     
                     criteriaProperty="sgFilial"
                     exactMatch="true" 
                     serializable="true">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" serializable="false" 
            			  size="50" maxLength="50" disabled="true"/>
        </adsm:lookup>	

		<adsm:textbox label="numeroSolicitacaoSinal" property="nrSolicitacaoSinal" dataType="integer" labelWidth="25%" width="30%" size="9" maxLength="8" mask="00000000" />

		<adsm:range label="periodo" labelWidth="14%" width="28%">
			<adsm:textbox property="dataInicial" dataType="JTDate" size="10" maxLength="10" picker="true"/>
			<adsm:textbox property="dataFinal" dataType="JTDate" size="10" maxLength="10" picker="true"/>
		</adsm:range>
		
		<adsm:textbox label="numeroRastreador" property="nrRastreador" 
					  dataType="integer" labelWidth="25%" width="30%" maxLength="10"/>

		<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte"
				service="lms.sgr.consultarSolicitacoesSinalAction.findLookupMeioTransporte" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrFrota" 
				label="meioTransporte" labelWidth="14%" width="28%" size="8" serializable="true" maxLength="6" >
                  <adsm:propertyMapping criteriaProperty="meioTransporte2.nrIdentificador" modelProperty="nrIdentificador" />
                  <adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte" modelProperty="idMeioTransporte" /> 
                  <adsm:propertyMapping relatedProperty="meioTransporte2.nrIdentificador" modelProperty="nrIdentificador" />				
                  
					<adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte" 
							service="lms.sgr.consultarSolicitacoesSinalAction.findLookupMeioTransporte" picker="true" maxLength="25"
							action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrIdentificador"
							size="20" required="false"  >
			                  <adsm:propertyMapping criteriaProperty="meioTransporte.nrFrota" modelProperty="nrFrota" />
			                  <adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte" />      
			                  <adsm:propertyMapping relatedProperty="meioTransporte.nrFrota" modelProperty="nrFrota" />				
					</adsm:lookup>
		</adsm:lookup>

		<adsm:lookup label="operadoraMCT" dataType="text" size="20" maxLength="20" labelWidth="25%" width="75%" popupLabel="pesquisarOperadoraMCT"
					 property="operadoraMct" 
					 idProperty="idOperadoraMct"  
					 service="lms.sgr.consultarSolicitacoesSinalAction.findLookupOperadoraMCT" 
					 action="/contratacaoVeiculos/manterOperadorasMCT"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
        	<adsm:propertyMapping relatedProperty="operadoraMct.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
            <adsm:textbox dataType="text" property="operadoraMct.pessoa.nmPessoa" serializable="false"
            			  size="50" maxLength="40" disabled="true"/>
        </adsm:lookup>

		<adsm:lookup label="proprietario" dataType="text" size="20" maxLength="20" labelWidth="25%" width="75%"
					 property="proprietario" 
					 idProperty="idProprietario"
					 service="lms.sgr.consultarSolicitacoesSinalAction.findLookupProprietario"
					 action="/contratacaoVeiculos/manterProprietarios" 
					 criteriaProperty="pessoa.nrIdentificacao"
				 	 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
			<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />				 	 
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" 
						  size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpStatusSolicitacao" label="situacao" labelWidth="25%" width="75%" domain="DM_STATUS_SOLICITACAO_SINAL" defaultValue="GE" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton caption="consultar" callbackProperty="solicitacaoSinal"/>
			<adsm:resetButton caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idSolicitacaoSinal" property="solicitacaoSinal" rows="8" gridHeight="160"
			   selectionMode="none" scrollBars="horizontal" onRowClick="rowClickNone"
			   service="lms.sgr.consultarSolicitacoesSinalAction.findPaginatedBySolicitacaoSinal"
			   rowCountService="lms.sgr.consultarSolicitacoesSinalAction.getRowCountBySolicitacaoSinal">
		
		<adsm:gridColumnGroup customSeparator=" ">
		<adsm:gridColumn title="solicitacaoSinal" property="filial.sgFilial" width="35" />	   
		<adsm:gridColumn title="" property="nrSolicitacaoSinal" width="90" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>	   
		<adsm:gridColumn title="dataHora" property="dhGeracao" dataType="JTDateTimeZone" align="center" width="150"/>
		<adsm:gridColumn title="filialOrigem"  property="sgFilialOrigem" width="80"/>
		<adsm:gridColumn title="operadoraMCT" property="operadoraMct.pessoa.nmPessoa" width="200"/>
		<adsm:gridColumn title="numeroRastreador" property="nrRastreador" width="160" align="right" mask="0"/>
		<adsm:gridColumn title="veiculo" property="meioTransporte.nrFrota" width="50"/>
		<adsm:gridColumn title="" property="meioTransporte.nrIdentificador" width="70"/>
		<adsm:gridColumn title="proprietario" property="proprietario.pessoa.nmPessoa" width="180"/>
		<adsm:gridColumn title="situacao" property="tpStatusSolicitacao" isDomain="true" width="80"/>
		<adsm:gridColumn title="imprimir" property="imprimir" image="/images/printer.gif" width="70" align="center" link="javascript:onReportClick"  />
		<adsm:buttonBar/>			
	</adsm:grid>

</adsm:window>

<script type="text/javascript">
	function rowClickNone() {
		return false;
	}
	
	function onReportClick(id) {
		var row = solicitacaoSinalGridDef.findById(id);
		if (row.tpStatusSolicitacao.value=="CA") {
			alert(i18NLabel.getLabel("LMS-11013"));
		} else {
			var remoteCall = new Object();
			remoteCall.serviceDataObjects = new Array();
			var gridCall = createServiceDataObject('lms.sgr.consultarSolicitacoesSinalAction.execute', 'openReportWithLocator', {idSolicitacaoSinal: id}); 
			remoteCall.serviceDataObjects.push(gridCall);
			remoteCall.handleCallbackUserMessages = false;
			xmit(remoteCall);
		}
	}
</script>