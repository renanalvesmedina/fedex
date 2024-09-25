<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script>
function carregaPagina(){
	onPageLoad();
}
</script>
<adsm:window service="lms.recepcaodescarga.finalizarDescargaAction" onPageLoad="carregaPagina" onPageLoadCallBack="retornoCarregaPagina">
	<adsm:form idProperty="idDispCarregIdentificado" action="/recepcaoDescarga/finalizarDescarga"
			   service="lms.recepcaodescarga.finalizarDescargaAction.findByIdDispCarregIdentificado">
		
		<adsm:masterLink idProperty="idCarregamentoDescarga" showSaveAll="false">
			<adsm:masterLinkItem label="controleCargas" property="controleCarga.nrControleCarga" itemWidth="50" />
			<adsm:masterLinkItem label="filial" property="sgFilial" itemWidth="50" />
			<adsm:masterLinkItem label="postoAvancado" property="sgPostoAvancado" itemWidth="50" />			
		</adsm:masterLink>
		
		<adsm:combobox label="tipo" property="dispositivoUnitizacao.tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao" 
					   optionProperty="idTipoDispositivoUnitizacao"
					   optionLabelProperty="dsTipoDispositivoUnitizacao" 
					   service="lms.recepcaodescarga.finalizarDescargaAction.findTipoDispositivoUnitizacaoByIdentificacao"
					   labelWidth="20%" width="85%" required="true" onlyActiveValues="true"/>

		<adsm:hidden property="tpSituacao" value="A"/>
 		<adsm:lookup label="empresa" property="dispositivoUnitizacao.empresa" 
 					 idProperty="idEmpresa" 
 					 criteriaProperty="pessoa.nrIdentificacao"
 					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.recepcaodescarga.finalizarDescargaAction.findLookupEmpresa" 
					 action="/municipios/manterEmpresas" 
					 dataType="text" size="20" maxLength="20" labelWidth="20%" width="85%" 
					 serializable="true" required="true" exactMatch="true">
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>			
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="dispositivoUnitizacao.empresa.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="dispositivoUnitizacao.empresa.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" serializable="false" />
		</adsm:lookup>
		
 		<adsm:lookup label="numeroIdentificacao" property="dispositivoUnitizacao" 
 					 idProperty="idDispositivoUnitizacao" 
 					 action="" 
 					 criteriaProperty="nrIdentificacao"
					 service="lms.recepcaodescarga.finalizarDescargaAction.findLookupDispositivoUnitizacao" 					 
					 dataType="text" size="12" maxLength="12" labelWidth="20%" width="85%" 
					 serializable="true"
					 required="true" 
					 picker="false">
								
				<adsm:propertyMapping criteriaProperty="dispositivoUnitizacao.tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao" 
									  modelProperty="tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao" />
				<adsm:propertyMapping criteriaProperty="dispositivoUnitizacao.empresa.idEmpresa" 
									  modelProperty="empresa.idEmpresa" />
				<adsm:propertyMapping relatedProperty="nrIdentificacao" 
									  modelProperty="nrIdentificacao" />																  
		</adsm:lookup>
		<adsm:hidden property="nrIdentificacao"/>
	
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarDispositivo" service="lms.recepcaodescarga.finalizarDescargaAction.saveDispCarregIdentificado"/>
			<adsm:resetButton caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idDispCarregIdentificado" property="dispCarregIdentificado" selectionMode="check" 
			   gridHeight="200" unique="true" autoSearch="true" detailFrameName="dispositivosComIdentificacao"
   			   service="lms.recepcaodescarga.finalizarDescargaAction.findPaginatedDispCarregIdentificado" 
			   rowCountService="lms.recepcaodescarga.finalizarDescargaAction.getRowCountDispCarregIdentificado" >	
		<adsm:gridColumn title="tipoDispositivo" property="dispositivoUnitizacao.tipoDispositivoUnitizacao.dsTipoDispositivoUnitizacao" width="20%" />
		<adsm:gridColumn title="empresa" property="dispositivoUnitizacao.empresa.pessoa.nmPessoa" width="65%" />		
		<adsm:gridColumn title="numeroIdentificacao" property="dispositivoUnitizacao.nrIdentificacao" width="15%" align="left"/>
	
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirDispositivo" 
							   service="lms.recepcaodescarga.finalizarDescargaAction.removeByIdsDispCarregIdentificado"/>		
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
<script type="text/javascript">
	// Pega a aba de 'recepcaoDescarga' para pegar os valores dos properties
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("recepcaoDescarga");

	/**
	 * Função chamada ao iniciar a página.
	 */
	function initWindow(eventObj) {
		setElementValue("dispositivoUnitizacao.empresa.idEmpresa", tabDet.getFormProperty("empresa.idEmpresa"));
		setElementValue("dispositivoUnitizacao.empresa.pessoa.nrIdentificacao", tabDet.getFormProperty("empresa.pessoa.nrIdentificacao"));
		setElementValue("dispositivoUnitizacao.empresa.pessoa.nmPessoa", tabDet.getFormProperty("empresa.pessoa.nmPessoa"));

	}		
	
	function retornoCarregaPagina_cb(data, error){
		onPageLoad_cb(data,error);
	}
	
</script>

