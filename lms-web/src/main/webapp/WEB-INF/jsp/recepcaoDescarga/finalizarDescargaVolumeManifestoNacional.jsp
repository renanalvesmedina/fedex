<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 

<adsm:window service="lms.recepcaodescarga.finalizarDescargaAction">
	<adsm:form idProperty="idManifestoNacionalVolume" action="/recepcaoDescarga/finalizarDescarga"
			   service="lms.recepcaodescarga.finalizarDescargaAction.findByIdManifestoNacionalVolume">
		
		<adsm:masterLink idProperty="idCarregamentoDescarga" showSaveAll="false">
			<adsm:masterLinkItem label="controleCargas" property="controleCarga.nrControleCarga" itemWidth="50" />
			<adsm:masterLinkItem label="filial" property="sgFilial" itemWidth="50" /> 
		</adsm:masterLink>
		
		<adsm:hidden property="tpSituacao" value="A"/>
		
		
 		<adsm:lookup label="numeroIdentificacao" property="volumeNotaFiscal" 
 					 idProperty="idVolumeNotaFiscal" 
 					 action="" 
 					 criteriaProperty="nrVolumeEmbarque"
					 service="lms.recepcaodescarga.finalizarDescargaAction.findLookupVolumeNotaFiscal" 					 
					 dataType="text" size="37" maxLength="35" labelWidth="20%" width="85%" 
					 serializable="true"
					 required="true" 
					 picker="false">
								
				<adsm:propertyMapping relatedProperty="nrIdentificacao" modelProperty="nrIdentificacao" />																  
		</adsm:lookup>
		<adsm:hidden property="nrIdentificacao"/>
	
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvar" service="lms.recepcaodescarga.finalizarDescargaAction.saveManifestoNacionalVolume"/>
			<adsm:resetButton caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idManifestoNacionalVolume" property="manifestoNacionalVolume" selectionMode="check" 
			   gridHeight="200" unique="true" autoSearch="false" detailFrameName="volumes"
   			   service="lms.recepcaodescarga.finalizarDescargaAction.findPaginatedManifestoNacionalVolume" 
			   rowCountService="lms.recepcaodescarga.finalizarDescargaAction.getRowCountManifestoNacionalVolume" >	
		<adsm:gridColumn title="codigoBarrasVolume" property="volumeNotaFiscal.nrVolumeEmbarque" width="40%" align="left"/>
		<adsm:gridColumn title="numeroConhecimento" property="volumeNotaFiscal.nrConhecimento" width="20%" />		
	 	<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="numeroVolumes" property="volumeNotaFiscal.nrSequencia" width="20%" />		
			<adsm:gridColumn title="qtdeVolumes" property="volumeNotaFiscal.qtVolumes" width="20%" />		
        </adsm:gridColumnGroup>
		<adsm:buttonBar>
			<adsm:removeButton caption="excluir" 
							   service="lms.recepcaodescarga.finalizarDescargaAction.removeByIdsManifestoNacionalVolume"/>		
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
		//setElementValue("dispositivoUnitizacao.empresa.idEmpresa", tabDet.getFormProperty("empresa.idEmpresa"));
		//setElementValue("dispositivoUnitizacao.empresa.pessoa.nrIdentificacao", tabDet.getFormProperty("empresa.pessoa.nrIdentificacao"));
		//setElementValue("dispositivoUnitizacao.empresa.pessoa.nmPessoa", tabDet.getFormProperty("empresa.pessoa.nmPessoa"));
	}		
	
</script>
