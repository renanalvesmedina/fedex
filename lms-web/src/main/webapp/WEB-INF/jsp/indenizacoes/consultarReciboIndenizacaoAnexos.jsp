<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.indenizacoes.consultarReciboIndenizacaoAction" >
	
	<adsm:form action="/indenizacoes/consultarReciboIndenizacao" 
			   service="lms.indenizacoes.consultarReciboIndenizacaoAction.findByIdAnexoRim"			
			   idProperty="idAnexoRim"
			   height="60">
							  
		<adsm:masterLink idProperty="idReciboIndenizacao" showSaveAll="false">						 
			<adsm:masterLinkItem property="nrReciboComposto" label="numeroRIM" itemWidth="50" />			
		</adsm:masterLink>
		
		<adsm:textbox label="descricao" 
					  dataType="text"
					  property="descAnexo"
					  disabled="true"
					  width="93%"
					  labelWidth="7%" 
					  maxLength="120" 
					  size="120" 
					  required="true" />
					  
		<adsm:textbox label="arquivo" 
					  dataType="file"
					  property="dcArquivo"
					  blobColumnName="DC_ARQUIVO"
					  tableName="ANEXO_RIM"
			          primaryKeyColumnName="ID_ANEXO_RIM"
			          primaryKeyValueProperty="idAnexoRim"
					   width="93%"
					  labelWidth="7%"
					  size="100" 
					  serializable="true" />
							  
	</adsm:form>
		
	<adsm:grid idProperty="idAnexoRim"
			   property="anexoRim" 
			   service="lms.indenizacoes.consultarReciboIndenizacaoAction.findPaginatedAnexoRim"
			   rowCountService="lms.indenizacoes.consultarReciboIndenizacaoAction.getRowCountAnexoRim"
			   selectionMode="none" 			  
			   detailFrameName="anexo"		
			   scrollBars="none" 		
			   unique="true" 
			   rows="13"
			   gridHeight="290" >			  
	
		<adsm:gridColumn title="nomeArquivo" property="nomeArquivo" width="25%"/>
		<adsm:gridColumn title="descricao" property="descricaoAnexo" width="40%" />
		<adsm:gridColumn title="dataHoraDeInclusao" property="dtHoraCriacao" width="15%" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="usuario" property="nomeUsuario" width="20%" />		
	</adsm:grid>
	<adsm:buttonBar/>
</adsm:window>

<script>
	
	function onTabShow(fromTab) {
		resetValue(document);		
	}
	
</script>	