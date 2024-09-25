<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarHorariosRestricaoAcesso" service="lms.municipios.manterHorariosRestricaoAcessoAction">
	
	<adsm:form action="/municipios/manterHorariosRestricaoAcesso" idProperty="idHorarioTransito">
		
		<adsm:hidden property="rotaIntervaloCep.idRotaIntervaloCep"/>
	
		<adsm:textbox dataType="text" label="filial" property="rotaIntervaloCep.rotaColetaEntrega.filial.pessoa.nmFantasia" serializable="false" size="38" disabled="true" labelWidth="20%" width="80%" /> 
		
		<adsm:textbox dataType="text" label="rotaColetaEntrega2" property="rotaIntervaloCep.rotaColetaEntrega.nrRota" serializable="false" size="38" disabled="true" labelWidth="20%" width="80%" />
		 
         <adsm:textbox dataType="text" property="rotaIntervaloCep.intervaloCep" serializable="false" size="38" label="faixaCep" disabled="true" labelWidth="20%" width="30%"/>
         
		<adsm:textbox dataType="text" label="cliente" property="cliente.pessoa.nrIdentificacaoFormatado" size="20" disabled="true" labelWidth="20%" width="80%" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="58" disabled="true" serializable="false"/> 
		</adsm:textbox>
        
       	 <adsm:range label="horario" labelWidth="20%" width="30%">
             <adsm:textbox dataType="JTTime" property="hrTransitoInicial" picker="true" />
             <adsm:textbox dataType="JTTime" property="hrTransitoFinal" picker="true"/>
        </adsm:range>
    
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="horarioTransito"/>
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idHorarioTransito" defaultOrder="hrTransitoInicial" rows="10" property="horarioTransito" selectionMode="check" gridHeight="200" unique="true">
		<adsm:gridColumn title="horaInicial" property="hrTransitoInicial" dataType="JTTime" align="center" />
		<adsm:gridColumn title="horaFinal" property="hrTransitoFinal" dataType="JTTime" align="center" />
	
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
		
	</adsm:grid>
	
</adsm:window>
