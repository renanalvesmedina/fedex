<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.tabelaprecos.simularNovosPrecosCiasAereasAction">
	<adsm:form action="/tabelaPrecos/simularNovosPrecosCiasAereas">
	
		<adsm:hidden property="geral.tpSituacao" value="A" serializable="false" />
	
		<adsm:listbox
			label="aeroportoDestino"
			optionProperty="idAeroporto"
			property="aeroportos"
			boxWidth="223"
			labelWidth="13%"
			width="37%"
			size="5">
			
			<%------------------------------%>
			<%-- AEROPORTO DESTINO LOOKUP --%>
			<%------------------------------%>
			<adsm:lookup 
				property="aeroportoByIdAeroportoDestino"
				idProperty="idAeroporto" 
				dataType="text" 
				service="lms.tabelaprecos.simularNovosPrecosCiasAereasAction.findLookupAeroporto" 
				action="/municipios/manterAeroportos" 
				criteriaProperty="sgAeroporto"
				size="5" 
				maxLength="3">
				
				<adsm:propertyMapping 
					criteriaProperty="geral.tpSituacao" 
					modelProperty="tpSituacao" />
					
			   	<adsm:propertyMapping 
			   		relatedProperty="aeroportos_aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
					modelProperty="pessoa.nmPessoa" />				
				
					
	            <adsm:textbox 
	            	dataType="text" 
	            	serializable="false" 
	            	size="27"
					property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
					maxLength="30" 
					disabled="true" />
					
	        </adsm:lookup>
			
		</adsm:listbox>
		
		<adsm:listbox
			label="produtoEspecifico"
			optionProperty="idProdutoEspecifico"
			property="produtosEspecificos"
			optionLabelProperty="nmTarifaEspecifica"
			boxWidth="223"
			labelWidth="13%"
			width="37%"
			size="5">
			
			<adsm:combobox 
				boxWidth="223"
				service="lms.tabelaprecos.simularNovosPrecosCiasAereasAction.findLookupProdutoEspecifico"
				optionProperty="idProdutoEspecifico"
				optionLabelProperty="nmTarifaEspecifica"/>
			
		</adsm:listbox>
		
		<adsm:buttonBar/>
	</adsm:form>
</adsm:window> 