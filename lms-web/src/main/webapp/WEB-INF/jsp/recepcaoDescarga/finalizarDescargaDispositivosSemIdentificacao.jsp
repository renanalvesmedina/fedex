<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 

<adsm:window service="lms.recepcaodescarga.finalizarDescargaAction">
	<adsm:form idProperty="idDispCarregDescQtde" action="/recepcaoDescarga/finalizarDescarga"
			   service="lms.recepcaodescarga.finalizarDescargaAction.findByIdDispCarregDescQtde">
					
		<adsm:masterLink idProperty="idCarregamentoDescarga" showSaveAll="false">
			<adsm:masterLinkItem label="controleCargas" property="controleCarga.nrControleCarga" itemWidth="50" />
			<adsm:masterLinkItem label="filial" property="sgFilial" itemWidth="50" />
			<adsm:masterLinkItem label="postoAvancado" property="sgPostoAvancado" itemWidth="50" />						
		</adsm:masterLink>
		
		<adsm:combobox label="tipo" property="tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao" 
					   optionProperty="idTipoDispositivoUnitizacao"
					   optionLabelProperty="dsTipoDispositivoUnitizacao" 
					   service="lms.recepcaodescarga.finalizarDescargaAction.findTipoDispositivoUnitizacaoByQuantidade"
					   labelWidth="20%" width="85%" required="true" onlyActiveValues="true"/>

		<adsm:hidden property="tpSituacao" value="A"/>
 		<adsm:lookup label="empresa" property="empresa" 
 					 idProperty="idEmpresa" 
 					 criteriaProperty="pessoa.nrIdentificacao"
 					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.recepcaodescarga.finalizarDescargaAction.findLookupEmpresa" 
					 action="/municipios/manterEmpresas" 
					 dataType="text" size="20" maxLength="20" labelWidth="20%" width="85%" 
					 serializable="true" required="true" exactMatch="true">
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="empresa.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" serializable="false" />
		</adsm:lookup>
					   
		<adsm:textbox label="quantidade" property="qtDispositivo" dataType="integer" size="6" 
					  maxLength="6" labelWidth="20%" width="85%" required="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarDispositivo" service="lms.recepcaodescarga.finalizarDescargaAction.saveDispCarregDescQtde"/>
			<adsm:resetButton caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idDispCarregDescQtde" property="dispCarregDescQtde" selectionMode="check" 			   
			   gridHeight="200" unique="true" autoSearch="false" detailFrameName="dispositivosSemIdentificacao"
   			   service="lms.recepcaodescarga.finalizarDescargaAction.findPaginatedDispCarregDescQtde" 
			   rowCountService="lms.recepcaodescarga.finalizarDescargaAction.getRowCountDispCarregDescQtde" >	
		<adsm:gridColumn title="tipoDispositivo" property="tipoDispositivoUnitizacao.dsTipoDispositivoUnitizacao" width="20%" />
		<adsm:gridColumn title="empresa" property="empresa.pessoa.nmPessoa" width="65%" />		
		<adsm:gridColumn title="quantidade" property="qtDispositivo" width="15%" align="right"/>

		<adsm:buttonBar>
			<adsm:removeButton caption="excluirDispositivo" 
							   service="lms.recepcaodescarga.finalizarDescargaAction.removeByIdsDispCarregDescQtde"/>		
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script type="text/javascript">
	document.getElementById("qtDispositivo").style.textAlign = "right";
	
	// Pega a aba de 'recepcaoDescarga' para pegar os valores dos properties
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("recepcaoDescarga");

	/**
	 * Função chamada ao iniciar a página.
	 */
	function initWindow(eventObj) {
		setElementValue("empresa.idEmpresa", tabDet.getFormProperty("empresa.idEmpresa"));
		setElementValue("empresa.pessoa.nrIdentificacao", tabDet.getFormProperty("empresa.pessoa.nrIdentificacao"));
		setElementValue("empresa.pessoa.nmPessoa", tabDet.getFormProperty("empresa.pessoa.nmPessoa"));
	}		
	
</script>