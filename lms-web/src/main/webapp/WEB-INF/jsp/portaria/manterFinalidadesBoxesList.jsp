<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.portaria.manterFinalidadesBoxesAction">
	<adsm:form action="/portaria/manterFinalidadesBoxes" height="106">
				
		<adsm:textbox dataType="text" property="box.doca.terminal.filial.sgFilial" size="3" label="filial" labelWidth="22%" width="38%" disabled="true">
			<adsm:textbox dataType="text"property="box.doca.terminal.filial.pessoa.nmFantasia" size="27" disabled="true"/>
		</adsm:textbox>
				
		<adsm:textbox dataType="text" property="box.doca.terminal.pessoa.nmPessoa" label="terminal" labelWidth="22%" width="28%" disabled="true" />
		
		<adsm:textbox dataType="text" property="box.doca.numeroDescricaoDoca" serializable="false" label="doca" labelWidth="22%" width="28%" disabled="true" />
		
		<adsm:textbox dataType="text" property="box.nrBox" label="box" labelWidth="22%" width="28%" disabled="true" />
		<adsm:hidden property="box.idBox"/>
		
	    <adsm:combobox property="finalidade.idFinalidade" optionLabelProperty="dsFinalidade" optionProperty="idFinalidade" boxWidth="200"
	    				service="lms.portaria.manterFinalidadesBoxesAction.findFinalidade" label="finalidade" labelWidth="22%" width="68%" />
	    
		<adsm:combobox property="servico.idServico" optionLabelProperty="dsServico" optionProperty="idServico" boxWidth="200"
						service="lms.portaria.manterFinalidadesBoxesAction.findServico" label="servico" labelWidth="22%" width="68%" />
		
		<adsm:range label="vigencia" labelWidth="22%" width="68%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton caption="consultar" callbackProperty="boxFinalidade"/>
			<adsm:resetButton caption="limpar"/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idBoxFinalidade" defaultOrder="finalidade_.dsFinalidade,hrInicial, dtVigenciaInicial" property="boxFinalidade" gridHeight="200" unique="true" rows="10">
		<adsm:gridColumn title="finalidade" property="finalidade.dsFinalidade" width="180"/>
		<adsm:gridColumn title="servico" property="servico.dsServico" width="150"/>
		<adsm:gridColumn title="intervaloAtendimento" dataType="JTTime" property="hrInicial" align="center" width="80" />
		<adsm:gridColumn title="" property="hrFinal" dataType="JTTime" align="center" width="80" />
		<adsm:gridColumn title="vigenciaInicial" dataType="JTDate" property="dtVigenciaInicial" width="120" />
		<adsm:gridColumn title="vigenciaFinal" dataType="JTDate" property="dtVigenciaFinal" width="120" />
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
