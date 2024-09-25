<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.recepcaodescarga.iniciarDescargaAction">

	<adsm:form idProperty="idLacreControleCarga" action="/recepcaoDescarga/iniciarDescarga"
			   service="lms.recepcaodescarga.iniciarDescargaAction.findByIdLacreControleCarga">
		
		<adsm:masterLink idProperty="idEquipeOperacao" showSaveAll="false">			
			<adsm:masterLinkItem label="controleCargas" property="controleCarga.nrControleCarga" itemWidth="50" />
			<adsm:masterLinkItem label="filial" property="sgFilial" itemWidth="50" />
			<adsm:masterLinkItem label="postoAvancado" property="sgPostoAvancado" itemWidth="50" />
		</adsm:masterLink>		
		
		<adsm:textbox label="numeroLacre" property="nrLacres" dataType="text" 
					  size="8" maxLength="8" width="85%" required="true" />
					  
		<adsm:combobox label="conferencia" property="tpStatusLacre" 
					   optionProperty="value"
					   optionLabelProperty="description"
					   service="lms.recepcaodescarga.iniciarDescargaAction.findStatusLacre"
					   width="85%" required="true" onlyActiveValues="true"/>
					   
		<adsm:textarea label="observacaoConferencia" property="obConferenciaLacre" maxLength="200" 
					   rows="3" columns="80" width="85%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarLacre" service="lms.recepcaodescarga.iniciarDescargaAction.saveLacreControleCarga"/>
			<adsm:resetButton caption="limpar"/>
		</adsm:buttonBar>		
	</adsm:form>
	
	<adsm:grid idProperty="idLacreControleCarga" property="lacreControleCarga" selectionMode="check" 
			   title="lacres" gridHeight="200" unique="true" autoSearch="false" rows="8"
   			   service="lms.recepcaodescarga.iniciarDescargaAction.findPaginatedLacreControleCarga" 
			   rowCountService="lms.recepcaodescarga.iniciarDescargaAction.getRowCountLacreControleCarga"
			   detailFrameName="lacres">
		<adsm:gridColumn title="lacre" property="nrLacres" width="15%"/>
		<adsm:gridColumn title="conferencia" property="tpStatusLacre" isDomain="true" width="25%" />
		<adsm:gridColumn title="observacaoConferencia" property="obConferenciaLacre" />
		
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirLacre" 
							   service="lms.recepcaodescarga.iniciarDescargaAction.removeByIdsLacreControleCarga"/>
		</adsm:buttonBar>	
	</adsm:grid>
	
</adsm:window>