<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterOcorrenciasRemessaRetornoBancosAction">

	<adsm:form action="/contasReceber/manterOcorrenciasRemessaRetornoBancos">

		<adsm:lookup 
     		service="lms.configuracoes.bancoService.findLookup" 
			idProperty="idBanco"
			dataType="integer" 
			property="banco" 					 
			criteriaProperty="nrBanco"					 
			label="banco" 
			size="5" 
			maxLength="3" 
			width="30%"
			action="/configuracoes/manterBancos"
			exactMatch="false" minLengthForAutoPopUpSearch="1">
					 
			<adsm:propertyMapping 
				modelProperty="nmBanco" 
				relatedProperty="banco.nmBanco"/>		
						
			<adsm:textbox 
				dataType="text" 
				property="banco.nmBanco" 
				disabled="true" />
			
		</adsm:lookup>		

		<adsm:combobox 
			label="tipo" 
			property="ocorrenciaBancos.tpOcorrenciaBanco" 
			domain="DM_TIPO_OCOR_BANCO"
			width="85%" 
			labelWidth="15%"/>
	
		<adsm:textbox 
			dataType="integer" 
			property="ocorrenciaBancos.nrOcorrenciaBanco" 
			labelWidth="15%" 
			width="85%" 
			label="numero" 
			size="3" 
			maxLength="3"/>

		<adsm:textbox 
			dataType="text" 
			property="ocorrenciaBancos.dsOcorrenciaBanco" 
			label="descricao" 
			size="60" 
			maxLength="60" 
			width="85%" 
			labelWidth="15%"/>		

		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				disabled="false"
				buttonType="findButton" 
				caption="consultar" 
				onclick="validateOcorrencia(this.form)"/>
				
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid 
		selectionMode="check" 
		idProperty="idOcorrenciaBanco" 
		property="ocorrenciaBancos" 
		service="lms.contasreceber.manterOcorrenciasRemessaRetornoBancosAction.findPaginatedByOcorrenciaRemessaRetornoBancos"
		rowCountService="lms.contasreceber.manterOcorrenciasRemessaRetornoBancosAction.getRowCountByOcorrenciaRemessaRetornoBancos"
		gridHeight="200" 
		unique="true" 
		rows="11">
		
		<adsm:gridColumn 
			width="15%" 
			title="banco" 
			property="nmBanco" 
			dataType="text"/>
			
		<adsm:gridColumn 
			isDomain="true"
			width="15%"
			title="tipo" 
			property="tpOcorrenciaBanco"/>
			
		<adsm:gridColumn 
			width="10%" 
			title="numero" 
			property="nrOcorrenciaBanco" 
			align="right"/>
			
		<adsm:gridColumn 
			width="60%" 
			title="descricao" 
			property="dsOcorrenciaBanco"/>
		
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>
	
	
</adsm:window>


<script language="javascript">

function validateOcorrencia(form){
	findButtonScript('ocorrenciaBancos', form);
}

</script>