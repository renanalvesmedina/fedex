<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.coleta.manterMilkRunAction">
	<adsm:form action="/coleta/manterMilkRun" idProperty="idMilkRun">
	
		<adsm:lookup label="destinatario" width="85%" size="20" maxLength="20"  serializable="true"
					 service="lms.coleta.manterMilkRunAction.findLookupCliente" 
					 action="/vendas/manterDadosIdentificacao"
					 dataType="text" 
					 property="cliente" 
					 idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="false"
					 disabled="false">
			<adsm:propertyMapping relatedProperty="nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="nmPessoa" size="50" maxLength="50" 
						  disabled="true" serializable="true"/>
		</adsm:lookup>
	
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="milkRun"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="milkRun" idProperty="idMilkRun" selectionMode="check" gridHeight="250" unique="true"
	           service="lms.coleta.manterMilkRunAction.findPaginatedMilkRun" rows="14"
	           rowCountService="lms.coleta.manterMilkRunAction.getRowCountMilkRun"
	           defaultOrder="cliente_pessoa_.nmPessoa" onRowClick="populaForm">
		<adsm:gridColumn width="80" title="identificacao" property="cliente.pessoa.tpIdentificacao" isDomain="true" align="left" />
		<adsm:gridColumn width="150" title="" property="cliente.pessoa.nrIdentificacao" align="right" />
		<adsm:gridColumn title="destinatario" property="cliente.pessoa.nmPessoa"/>
		<adsm:gridColumn width="25%" title="coletasInterdependentes" property="blColetasInterdependentes" renderMode="image-check"/>
		
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
	/**
	 * Responsável por habilitar/desabilitar uma tab
	 */
	function desabilitaTab(aba, disabled) {
		var tabGroup = getTabGroup(this.document);	
		tabGroup.setDisabledTab(aba, disabled);
	}	
	
	function populaForm(id) {
		var tabGroup = getTabGroup(this.document);		
		if(tabGroup != null) {			
			desabilitaTab("remetentes", false);
			var tabCad = tabGroup.getTab("cad");
			var telaCad = tabCad.tabOwnerFrame;		
			telaCad.onDataLoad(id);
		}
	}
	
</script>