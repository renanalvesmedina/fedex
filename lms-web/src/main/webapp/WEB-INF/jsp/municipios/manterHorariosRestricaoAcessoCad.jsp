<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterHorariosRestricaoAcessoAction">
	
	<adsm:form action="/municipios/manterHorariosRestricaoAcesso" idProperty="idHorarioTransito">
	
		<adsm:hidden property="rotaIntervaloCep.idRotaIntervaloCep"/>
		
		<adsm:textbox dataType="text" label="filial" property="rotaIntervaloCep.rotaColetaEntrega.filial.pessoa.nmFantasia" size="38" disabled="true" labelWidth="20%" width="80%" /> 
		
		<adsm:textbox dataType="text" label="rotaColetaEntrega2" property="rotaIntervaloCep.rotaColetaEntrega.nrRota" size="38" disabled="true" labelWidth="20%" width="80%" />
		 
        <adsm:textbox dataType="text" property="rotaIntervaloCep.intervaloCep" size="38" label="faixaCep" disabled="true" labelWidth="20%" width="30%"/>

		<adsm:textbox dataType="text" label="cliente" property="cliente.pessoa.nrIdentificacaoFormatado" size="20" disabled="true" labelWidth="20%" width="80%">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="58" disabled="true"/> 
		</adsm:textbox>

       	 <adsm:range label="horario" required="true" labelWidth="20%" width="30%">
             <adsm:textbox dataType="JTTime" property="hrTransitoInicial" picker="true" />
             <adsm:textbox dataType="JTTime" property="hrTransitoFinal" picker="true"/>
        </adsm:range>        
        
	<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>   