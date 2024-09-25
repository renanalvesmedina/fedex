<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.restricaoColetaService">
	<adsm:form action="/coleta/manterRestricoesColeta">
		<adsm:combobox 
					autoLoad			="true"
					label				="servico" 
					optionLabelProperty	="dsServico" 
					optionProperty		="idServico" 
					property			="servico.idServico" 
					service				="lms.coleta.restricaoColetaService.findListServico" 
					onlyActiveValues	="false"					
					labelWidth			="19%" 
					width				="81%"
					boxWidth			="225"
		/>
		<adsm:lookup service="lms.municipios.paisService.findLookup" 
					 property="pais" 
					 label="pais"
					 idProperty="idPais"
					 criteriaProperty="nmPais"					 
					 dataType="text"
					 action="/municipios/manterPaises"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 labelWidth="19%"
	                 width="81%"					 
					 maxLength="60"
					 size="35"
		/>
		<adsm:textbox labelWidth="19%" width="81%" dataType="weight" property="psMaximoVolume" label="pesoMaximoPorVolume" maxLength="18" size="10" unit="kg" minValue="0" />
		<adsm:combobox 
					autoLoad			="true"
					label				="produtoProibido" 
					onlyActiveValues	="false"
					optionLabelProperty	="dsProduto" 
					optionProperty		="idProdutoProibido" 
					property			="produtoProibido.idProdutoProibido" 
					service				="lms.coleta.restricaoColetaService.findListProdutoProibido"
					labelWidth="19%"
					width="81%"					 
		/>		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="restricaoColeta"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="restricaoColeta" idProperty="idRestricaoColeta" 
			   service="lms.coleta.manterRestricoesColetaAction.findPaginated"
			   rowCountService="lms.coleta.manterRestricoesColetaAction.getRowCount" 
			   selectionMode="check" unique="true" scrollBars="horizontal" 
			   rows="10" gridHeight="200" defaultOrder="servico_.dsServico" >
		<adsm:gridColumn title="servico" property="servico.dsServico" width="220" />
		<adsm:gridColumn title="pais" property="pais.nmPais" width="145" />
		<adsm:gridColumn title="pesoMaximoPorVolume" property="psMaximoVolume" width="160" align="right" dataType="decimal" mask="###,###,##0.000" unit="kg" />
		<adsm:gridColumn title="produtoProibido" property="produtoProibido.dsProduto" width="400" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
