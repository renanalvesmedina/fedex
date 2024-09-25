<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contasreceber.manterMotivosOcorrenciasRemessaRetornoBancosAction">

	<adsm:form action="/contasReceber/manterMotivosOcorrenciasRemessaRetornoBancos" idProperty="idMotivoOcorrenciaBanco">
	
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
			property="nrMotivoOcorrenciaBanco" 
			labelWidth="15%" 
			width="85%" 
			label="numero" 
			size="4" 
			maxLength="4"
			required="true"/>

		<adsm:textbox 
			dataType="text" 
			property="dsMotivoOcorrenciaBanco" 
			label="descricao" 
			size="60" 
			maxLength="60" 
			width="85%" 
			labelWidth="15%"
			required="true"/>		
			
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
