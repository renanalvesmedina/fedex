<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.manterTiposEquipamentoAction" >
	<adsm:form  action="/vol/manterTiposEquipamento">
		
		<adsm:textbox property="dsNome" dataType="text" label="descricao" maxLength="50" size="60" width="85%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tiposEquipamento"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
				
	</adsm:form>

	<adsm:grid property="tiposEquipamento" idProperty="idTipoEqpto" selectionMode="check" 
			   rows="13" gridHeight="150" defaultOrder="dsNome" 
			   service="lms.vol.manterTiposEquipamentoAction.findPaginatedVolTiposEqpto" 
			   rowCountService="lms.vol.manterTiposEquipamentoAction.getRowCountVolTiposEqpto">
		<adsm:gridColumn property="dsNome" title="descricao" width="100%" />
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>