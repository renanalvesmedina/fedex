<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterMotivosOcorrenciasRemessaRetornoBancosAction">

	<adsm:form action="/contasReceber/manterMotivosOcorrenciasRemessaRetornoBancos">
	
		<adsm:hidden property="ocorrenciaBanco.idOcorrenciaBanco" serializable="true"/>
		
		<adsm:textbox 
			dataType="integer" 
			property="ocorrenciaBanco.nrOcorrenciaBanco" 
			labelWidth="15%" 
			width="6%" 
			label="ocorrencia" 
			size="4" 
			maxLength="4">
			
				<adsm:textbox 
					dataType="text" 
					property="ocorrenciaBanco.dsOcorrenciaBanco" 
					size="60" 
					maxLength="60" 
					width="79%"/>	

		</adsm:textbox>
		
		<adsm:textbox 
			dataType="integer" 
			property="motivoOcorrenciaBanco.nrMotivoOcorrenciaBanco" 
			labelWidth="15%" 
			width="85%" 
			label="numero" 
			size="4" 
			maxLength="4"  />
			
		<adsm:textbox 
			dataType="text" 
			property="motivoOcorrenciaBanco.dsMotivoOcorrenciaBanco" 
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
				onclick="validateMotivoOcorrencia(this.form)"/>
				
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid 
		selectionMode="check" 
		idProperty="idMotivoOcorrenciaBanco" 
		property="motivoOcorrenciaBancos" 
		service="lms.contasreceber.manterMotivosOcorrenciasRemessaRetornoBancosAction.findPaginatedByMotivoOcorrenciaRemessaRetornoBancos"
		rowCountService="lms.contasreceber.manterMotivosOcorrenciasRemessaRetornoBancosAction.getRowCountByMotivoOcorrenciaRemessaRetornoBancos"
		gridHeight="200" 
		unique="true" 
		rows="12">
		
		<adsm:gridColumn 
			width="20%" 
			title="numero" 
			property="nrMotivoOcorrenciaBanco" 
			align="right"/>
			
		<adsm:gridColumn 
			width="80%" 
			title="descricao" 
			property="dsMotivoOcorrenciaBanco"/>
		
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>
	
	
</adsm:window>


<script language="javascript">

function validateMotivoOcorrencia(form){
	findButtonScript('motivoOcorrenciaBancos', form);
}

</script>