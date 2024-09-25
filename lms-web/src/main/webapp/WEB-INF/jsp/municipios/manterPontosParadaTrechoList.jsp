<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterPontosParadaTrechoAction" >
	<adsm:form action="/municipios/manterPontosParadaTrecho" idProperty="idPontoParadaTrecho" >
		<adsm:hidden property="trechoRotaIdaVolta.idTrechoRotaIdaVolta" value="1" />		
		
				
		<adsm:textbox dataType="integer" label="rota" property="rota.nrRota" disabled="true" mask="0000" size="5" labelWidth="18%" width="32%" serializable="false">
			<adsm:textbox dataType="text" property="rota.dsRota" disabled="true" size="30" serializable="false"/>
		</adsm:textbox>
	
				
		<adsm:textbox dataType="text" label="trecho" property="trechoRotaIdaVolta.dsTrechoRotaIdaVolta"
				size="10" disabled="true" labelWidth="18%" width="82%" serializable="false" />
				
        <adsm:lookup dataType="text" property="pontoParada" idProperty="idPontoParada" criteriaProperty="nmPontoParada"
        	service="lms.municipios.manterPontosParadaTrechoAction.findLookupPontoParada" action="/municipios/manterPontosParadaRota"
        	label="local" labelWidth="18%" width="82%" exactMatch="false" minLengthForAutoPopUpSearch="3" />
        	
		<adsm:range label="vigencia" labelWidth="18%" width="82%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
	<adsm:buttonBar freeLayout="true">
		<adsm:findButton callbackProperty="pontoParadaTrecho" />
		<adsm:resetButton />
	</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idPontoParadaTrecho" property="pontoParadaTrecho" unique="true" rows="10">
		<adsm:gridColumn title="local" property="pontoParada_nmPontoParada" width="16%" />
		<adsm:gridColumn title="municipio" property="pontoParada_municipio_nmMunicipio" width="16%" />
		<adsm:gridColumn title="uf" property="pontoParada_municipio_unidadeFederativa_sgUnidadeFederativa" width="4%" />
		<adsm:gridColumn title="pais" property="pontoParada_municipio_unidadeFederativa_pais_nmPais" width="15%" />
		<adsm:gridColumn title="rodovia" property="pontoParada_rodovia_sgRodovia" width="8%" />
		<adsm:gridColumn title="km" property="pontoParada_nrKm" width="5%" dataType="integer" />
		<adsm:gridColumn title="tempoParada" property="nrTempoParada" unit="h" dataType="text" width="10%" align="center" />
		<adsm:gridColumn title="ordem" property="nrOrdem" dataType="integer" width="6%"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="10%" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>