<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterMotivosPontoParadaTrechoAction" >
	<adsm:form action="/municipios/manterMotivosPontoParadaTrecho" idProperty="idMotivoParadaPontoTrecho" >
		<adsm:hidden property="pontoParadaTrecho.idPontoParadaTrecho" value="1" />
		
		<adsm:textbox dataType="integer" label="rota" property="rota.nrRota" disabled="true" mask="0000" size="5" labelWidth="22%" width="32%" serializable="false">
			<adsm:textbox dataType="text" property="rota.dsRota" disabled="true" size="30" serializable="false"/>
		</adsm:textbox>
	
		<adsm:textbox dataType="text" label="trecho" property="trechoRotaIdaVolta.dsTrechoRotaIdaVolta"
				size="10" disabled="true" labelWidth="22%" width="60%" serializable="false" />
      	<adsm:textbox dataType="text" property="pontoParadaTrecho.pessoa.nmPessoa" label="pontoParadaTrecho"
      			labelWidth="22%" width="28%" size="30" disabled="true" serializable="false" />
      			
	   	<adsm:combobox property="motivoParada.idMotivoParada" optionProperty="idMotivoParada" optionLabelProperty="dsMotivoParada"
	   			service="lms.municipios.manterMotivosPontoParadaTrechoAction.findMotivoParadaCombo"
	   			label="motivoParada" width="35%" boxWidth="200" />
	   	
       	<adsm:range label="vigencia" labelWidth="22%" width="45%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
        </adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivoParadaPontoTrecho" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="motivoParadaPontoTrecho" idProperty="idMotivoParadaPontoTrecho" unique="true" rows="11"
			defaultOrder="motivoParada_.dsMotivoParada,dtVigenciaInicial" >
		<adsm:gridColumn title="motivoParada" property="motivoParada.dsMotivoParada" width="70%" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="15%" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="15%" />
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
