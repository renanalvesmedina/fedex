<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.pendencia.consultarMDAAction" >
	<adsm:form action="/pendencia/consultarMDA" idProperty="idItemMda" 
			   service="lms.pendencia.consultarMDAAction.findByIdItemMda">
		
		<adsm:masterLink showSaveAll="false" idProperty="idDoctoServico" >
			<adsm:masterLinkItem label="mda" property="siglaNrDoctoServico" itemWidth="50"/>
		</adsm:masterLink>
				
		<adsm:textbox property="doctoServico.tpDocumentoServico" label="documentoServico"
					  labelWidth="19%" width="81%" dataType="text" size="5" maxLength="5"
					  disabled="true" serializable="false">
			<adsm:textbox property="doctoServico.filialByIdFilialOrigem.sgFilial" dataType="text"
					  	  size="4" maxLength="4" disabled="true" serializable="false"/>					  
			<adsm:textbox property="doctoServico.nrDoctoServico" dataType="integer" mask="00000000"
					  	  size="10" maxLength="10" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="naturezaProduto.dsNaturezaProduto" label="naturezaMercadoria" 
					  labelWidth="19%" width="25%" dataType="text" size="30" maxLength="30" 
					  disabled="true" serializable="false"/>
		<adsm:textbox property="qtVolumes" label="volumes" dataType="integer" 
					  size="8%" maxLength="10" labelWidth="19%" width="30%" 
					  disabled="true" serializable="false"/>
					    
		<adsm:textbox label="peso" property="psItem" dataType="weight" 
					  unit="kg" labelWidth="19%" width="25%" 
					  size="16" maxLength="16" disabled="true" serializable="false"/>					    

		<adsm:textbox property="moeda.siglaSimbolo" label="valorMercadoria"  					 
					  labelWidth="19%" width="30%" dataType="text" size="8" maxLength="8" 
					  disabled="true" serializable="false">
			<adsm:textbox property="vlMercadoria" dataType="currency" size="10" maxLength="12"
						  mask="#,###,###,###,###,##0.00" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textarea property="dsMercadoria" label="discriminacaoMercadoria" maxLength="200" 
					   columns="80" rows="3" labelWidth="19%" width="81%" disabled="true" serializable="false"/>

		<adsm:textarea property="obItemMda" label="observacoes" maxLength="200" columns="80" 
					   rows="3" labelWidth="19%" width="81%" disabled="true" serializable="false"/>

		<adsm:buttonBar freeLayout="true">
		</adsm:buttonBar>		
	</adsm:form>
	
	<adsm:grid property="itemMda" idProperty="idItemMda" gridHeight="122" selectionMode="none"
			   scrollBars="vertical" unique="true" detailFrameName="itens" autoSearch="false"
			   service="lms.pendencia.consultarMDAAction.findPaginatedItemMda"
			   rowCountService="lms.pendencia.consultarMDAAction.getRowCountItemMda">
		
		<adsm:gridColumn title="documentoServico" property="doctoServico.tpDocumentoServico" isDomain="true" width="30"/>
        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="" property="doctoServico.filialByIdFilialOrigem.sgFilial" width="30" />
            <adsm:gridColumn title="" property="doctoServico.nrDoctoServico" width="100" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="naturezaProduto" property="naturezaProduto.dsNaturezaProduto" width="150" />		
		<adsm:gridColumn title="volumes" property="qtVolumes" width="80" align="right" />
		<adsm:gridColumn title="peso" property="psItem" dataType="decimal" mask="#,###,##0.000" align="right" unit="kg" />
		<adsm:gridColumn title="valorMercadoria" property="moeda.siglaSimboloMoeda" width="60" />		
		<adsm:gridColumn title="" property="vlMercadoria" dataType="decimal" mask="#,###,###,###,###,##0.00" width="100" align="right"/>				
		
		<adsm:buttonBar/>
	</adsm:grid>
	
</adsm:window>