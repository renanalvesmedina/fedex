<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.testeTagLibService">
	<adsm:form idProperty="idTesteTagLib" action="/municipios/testeTagLib">
<%-- primeiro caso: combo, combo, combo, neste caso quero salvar o idPais, idUnidadeFederativa, idMunicipio--%>
<%--		<adsm:combobox property="pais.idPais" 
				       label="pais" 
					   service="lms.municipios.paisService.find" 
                       optionLabelProperty="nmPais" optionProperty="idPais">
		</adsm:combobox>
		<adsm:combobox property="unidadeFederativa.idUnidadeFederativa" 
				       label="uf" 
					   service="lms.municipios.unidadeFederativaService.find" 
                       optionLabelProperty="nmUnidadeFederativa" optionProperty="idUnidadeFederativa">
     		<adsm:propertyMapping criteriaProperty="pais.idPais" 
							      modelProperty="pais.idPais"
			/>	
		</adsm:combobox>
		<adsm:combobox property="municipio.idMunicipio" 
				       label="municipio" 
					   service="lms.municipios.municipioService.find" 
                       optionLabelProperty="nmMunicipio" optionProperty="idMunicipio">
     		<adsm:propertyMapping criteriaProperty="unidadeFederativa.idUnidadeFederativa" 
							      modelProperty="unidadeFederativa.idUnidadeFederativa"
			/>	
		</adsm:combobox>
--%>
<%-- primeiro caso: combo, combo, combo, neste caso quero salvar o idMunicipio --%>
<%--
		<adsm:combobox property="municipio.unidadeFederativa.pais.idPais" 
				       label="pais" 
					   service="lms.municipios.paisService.find" 
                       optionLabelProperty="nmPais" optionProperty="idPais">
		</adsm:combobox>
		<adsm:combobox property="municipio.unidadeFederativa.idUnidadeFederativa" 
				       label="uf" 
					   service="lms.municipios.unidadeFederativaService.find" 
                       optionLabelProperty="nmUnidadeFederativa" optionProperty="idUnidadeFederativa">
     		<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" 
							      modelProperty="pais.idPais"
			/>	
		</adsm:combobox>
		<adsm:combobox property="municipio.idMunicipio" 
				       label="municipio" 
					   service="lms.municipios.municipioService.find" 
                       optionLabelProperty="nmMunicipio" optionProperty="idMunicipio">
     		<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.idUnidadeFederativa" 
							      modelProperty="unidadeFederativa.idUnidadeFederativa"
			/>	
		</adsm:combobox>
--%>
<%-- terceiro caso, lookup, combo, lookup  PENDENTE (FALTA FAZER FUNCIONAR O RELATED DA COMBO)--%>
<%--		<adsm:lookup property="municipio.unidadeFederativa.pais" idProperty="idPais" criteriaProperty="nmPais" 
					 service="lms.municipios.paisService.findLookup" 
					 dataType="text" label="pais" size="30" required="true" maxLength="60"
					 minLengthForAutoPopUpSearch="3" exactMatch="false"
			         action="/municipios/manterPaises"/>
		
		<adsm:combobox property="municipio.unidadeFederativa.idUnidadeFederativa" 
				       label="uf" 
					   service="lms.municipios.unidadeFederativaService.find" 
                       optionLabelProperty="nmUnidadeFederativa" optionProperty="idUnidadeFederativa">
     		<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" 
							      modelProperty="pais.idPais"
			/>	
		</adsm:combobox>
		<adsm:lookup service="lms.municipios.municipioService.findLookup" dataType="text" 
			property="municipio" criteriaProperty="nrCep" label="capital" size="9" maxLength="8" width="13%"
			action="/municipios/manterMunicipios" idProperty="idMunicipio"
			exactMatch="true">

			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.idUnidadeFederativa"
								  modelProperty="unidadeFederativa.idUnidadeFederativa"	
		    />
< %--
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.sgUnidadeFederativa"
 								  modelProperty="unidadeFederativa.sgUnidadeFederativa"	
		    />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.nmUnidadeFederativa"
 								  modelProperty="unidadeFederativa.nmUnidadeFederativa"	
		    />
--% >
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio"
						          modelProperty="nmMunicipio"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="municipio.nmMunicipio" width="22%" size="15" disabled="true"/>
--%>
<%-- combo, lookup, combo PENDENTE (AO CLICAR NA LOOKUP E FECHA-LA, NAO ATUALIZA COMBO QUE VEM DEPOIS)--%>
<%--
		<adsm:combobox property="municipio.unidadeFederativa.pais.idPais" 
				       label="pais" 
					   service="lms.municipios.paisService.find" 
                       optionLabelProperty="nmPais" optionProperty="idPais">
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais"
						          modelProperty="nmPais"/>
		</adsm:combobox>
		<adsm:hidden property="municipio.unidadeFederativa.pais.nmPais"/>
		<adsm:hidden property="municipio.unidadeFederativa.tpSituacao" value="A"/>
		<adsm:lookup property="municipio.unidadeFederativa" idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				     service="lms.municipios.unidadeFederativaService.findLookup" 
					 dataType="text" label="uf" size="2" maxLength="2" 
					 action="/municipios/manterUnidadesFederativas" width="13%">
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais"
						          modelProperty="pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais"
					              modelProperty="pais.nmPais" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.tpSituacao"
					              modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa"
						          modelProperty="nmUnidadeFederativa"/>

		</adsm:lookup>
		<adsm:textbox dataType="text" property="municipio.unidadeFederativa.nmUnidadeFederativa" width="22%" size="15" disabled="true" serializable="false"/>

		<adsm:combobox property="municipio.idMunicipio" 
				       label="municipio" 
					   service="lms.municipios.municipioService.find" 
                       optionLabelProperty="nmMunicipio" optionProperty="idMunicipio">
     		<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.idUnidadeFederativa" 
							      modelProperty="unidadeFederativa.idUnidadeFederativa"
			/>	
		</adsm:combobox>
--%>

<%-- caso lookup, lookup, combo, neste caso quero salvar o idMunicipio (PENDENTE, NÃO ATUALIZA COMBO AO ALTERAR O VALOR NA LOOKUP)--%>
<%--
		<adsm:lookup property="municipio.unidadeFederativa.pais" idProperty="idPais" criteriaProperty="nmPais" 
					 service="lms.municipios.paisService.findLookup" 
					 dataType="text" label="pais" size="30" required="true" minLengthForAutoPopUpSearch="3" maxLength="60"  exactMatch="false"
			         action="/municipios/manterPaises">
		</adsm:lookup>

		<adsm:lookup property="municipio.unidadeFederativa" idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				     service="lms.municipios.unidadeFederativaService.findLookup" 
					 dataType="text" label="uf" size="2" maxLength="2" 
					 action="/municipios/manterUnidadesFederativas" width="13%">
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais"
						          modelProperty="pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais"
						          modelProperty="pais.nmPais"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.nmUnidadeFederativa"
						          modelProperty="nmUnidadeFederativa"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.sgUnidadeFederativa"
						          modelProperty="sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa"
						          modelProperty="nmUnidadeFederativa"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="municipio.unidadeFederativa.nmUnidadeFederativa" width="22%" size="15" disabled="true" serializable="false"/>
		
		<adsm:combobox property="municipio.idMunicipio" 
				       label="municipio" 
					   service="lms.municipios.municipioService.find" 
                       optionLabelProperty="nmMunicipio" optionProperty="idMunicipio">
     		<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.idUnidadeFederativa" 
							      modelProperty="unidadeFederativa.idUnidadeFederativa"
			/>	
		</adsm:combobox>
--%>

<%-- CASO LOOKUP,LOOKUP,COMBO, neste caso quero salvar o idMunicipio, idUnidadeFederativa, idPais (PENDENTE, NÃO ATUALIZA COMBO AO ALTERAR O VALOR NA LOOKUP)--%>
<%--
		<adsm:lookup property="pais" idProperty="idPais" criteriaProperty="nmPais" 
					 service="lms.municipios.paisService.findLookup" 
					 dataType="text" label="pais" size="30" required="true" minLengthForAutoPopUpSearch="3" maxLength="60"  exactMatch="false"
			         action="/municipios/manterPaises">
		</adsm:lookup>

		<adsm:lookup property="unidadeFederativa" idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				     service="lms.municipios.unidadeFederativaService.findLookup" 
					 dataType="text" label="uf" size="2" maxLength="2" 
					 action="/municipios/manterUnidadesFederativas" width="13%">
			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="pais.nmPais" modelProperty="pais.nmPais"/>
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa"  modelProperty="nmUnidadeFederativa"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" width="22%" size="15" disabled="true" serializable="false"/>
		
		<adsm:combobox property="municipio.idMunicipio" 
				       label="municipio" 
					   service="lms.municipios.municipioService.find" 
                       optionLabelProperty="nmMunicipio" optionProperty="idMunicipio">
     		<adsm:propertyMapping criteriaProperty="unidadeFederativa.idUnidadeFederativa" 
							      modelProperty="unidadeFederativa.idUnidadeFederativa"
			/>	
		</adsm:combobox>

--%>

<%-- SEGUNDO CASO LOOKUP,LOOKUP,LOOKUP, neste caso quero salvar o idMunicipio --%>
<%--
		<adsm:lookup property="municipio.unidadeFederativa.pais" idProperty="idPais" criteriaProperty="nmPais" 
					 service="lms.municipios.paisService.findLookup" 
					 dataType="text" label="pais" size="30" required="true" minLengthForAutoPopUpSearch="3" maxLength="60"  exactMatch="false"
			         action="/municipios/manterPaises">
		</adsm:lookup>

		<adsm:lookup property="municipio.unidadeFederativa" idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				     service="lms.municipios.unidadeFederativaService.findLookup" 
					 dataType="text" label="uf" size="2" maxLength="2" 
					 action="/municipios/manterUnidadesFederativas" width="13%">
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais"
						          modelProperty="pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais"
						          modelProperty="pais.nmPais"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa"
						          modelProperty="nmUnidadeFederativa"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="municipio.unidadeFederativa.nmUnidadeFederativa" width="22%" size="15" disabled="true" serializable="false"/>

		<adsm:lookup service="lms.municipios.municipioService.findLookup" dataType="text" 
			property="municipio" criteriaProperty="nrCep" label="capital" size="9" maxLength="8" onPopupSetValue="false"
			action="/municipios/manterMunicipios" width="13%" idProperty="idMunicipio">
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.idUnidadeFederativa"
								  modelProperty="unidadeFederativa.idUnidadeFederativa"	
		    />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.sgUnidadeFederativa"
 								  modelProperty="unidadeFederativa.sgUnidadeFederativa"	
		    />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.nmUnidadeFederativa"
 								  modelProperty="unidadeFederativa.nmUnidadeFederativa"	
		    />
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio"
						          modelProperty="nmMunicipio"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="municipio.nmMunicipio" width="22%" size="15" disabled="true"  serializable="false"/>
		
		<adsm:textbox dataType="dateTime" property="dateTimeTest" label="data"/>
		<adsm:textbox dataType="time" property="timeTest" label="hora"/>
		--%>
<%-- SEGUNDO CASO LOOKUP, LOOKUP, LOOKUP, neste caso quero salvar o idMunicipio,  idPais, idUnidadeFederativa --%>
<%--
		<adsm:lookup property="pais" idProperty="idPais" criteriaProperty="nmPais" 
					 service="lms.municipios.paisService.findLookup" 
					 dataType="text" label="pais" size="30" required="true" minLengthForAutoPopUpSearch="3" maxLength="60"  exactMatch="false"
			         action="/municipios/manterPaises">
		</adsm:lookup>

		<adsm:lookup property="unidadeFederativa" idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				     service="lms.municipios.unidadeFederativaService.findLookup" 
					 dataType="text" label="uf" size="2" maxLength="2" 
					 action="/municipios/manterUnidadesFederativas" width="13%">
			<adsm:propertyMapping criteriaProperty="pais.idPais"
						          modelProperty="pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="pais.nmPais"
						          modelProperty="pais.nmPais"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa"
						          modelProperty="nmUnidadeFederativa"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" width="22%" size="15" disabled="true" serializable="false"/>

		<adsm:lookup service="lms.municipios.municipioService.findLookup" dataType="text" 
			property="municipio" criteriaProperty="nrCep" label="capital" size="9" maxLength="8"
			action="/municipios/manterMunicipios" width="13%" idProperty="idMunicipio">
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.idUnidadeFederativa"
								  modelProperty="unidadeFederativa.idUnidadeFederativa"	
		    />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.sgUnidadeFederativa" 
 								  modelProperty="unidadeFederativa.sgUnidadeFederativa"	
		    />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.nmUnidadeFederativa"
 								  modelProperty="unidadeFederativa.nmUnidadeFederativa"	
		    />
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio"
						          modelProperty="nmMunicipio"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="municipio.nmMunicipio" width="22%" size="15" disabled="true"  serializable="false"/>
--%>
<%--		<adsm:range label="vigencia">
			<adsm:textbox dataType="date" property="dtVigenciaInicial"  required="true"/>
			<adsm:textbox dataType="date" property="dtVigenciaFinal"/>
		</adsm:range>
		<adsm:combobox property="tpSituacao" domain="TP_SITUACAO" label="situacao"  required="true"/>
--%>		
<%--
		<adsm:checkbox label="obrigaPreenchimentoKm" property="blTeste" width="75%"/>
		<adsm:multicheckbox property="blDom|blSeg|blTer|blQua|blQui|blSex|blSab|" label="frequencia" texts="dom|seg|ter|qua|qui|sex|sab|" align="top"/>
--%> 	


		<adsm:lookup label="filial" dataType="text" size="6" maxLength="3" width="10%"
		  	         service="lms.municipios.filialService.findLookup" property="filial" 
		  	         idProperty="idFilial" 
		  	         onPopupSetValue="sugereVigenciaPopUp" 
		  	         onDataLoadCallBack="sugereVigenciaDataLoad"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais" cellStyle="vertical-align:bottom;">
                  <adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="filialByIdFilial.pessoa.nmPessoa"/>
        </adsm:lookup>
      	<adsm:textbox dataType="text" serializable="false" property="filialByIdFilial.pessoa.nmPessoa" size="30" disabled="true" width="25%" cellStyle="vertical-align:bottom;" required="true"/>
		
	
	<adsm:buttonBar>
			<adsm:newButton />
			<adsm:storeButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">

	function sugereVigenciaPopUp(data) {
	
		var idFilial = getNestedBeanPropertyValue(data,"idFilial");
		sugereVigencia(idFilial);
	}
	
	function sugereVigenciaDataLoad_cb(data, erro) {
		filial_pessoa_sgFilial_exactMatch_cb(data);
			var idFilial = getNestedBeanPropertyValue(data,":0.idFilial");
		sugereVigencia(idFilial);
	}
	
	function sugereVigencia(idFilial){
		var sdo = createServiceDataObject("lms.municipios.filialService.findVigenciaFutura", "sugereVigencia",
			{idFilial:idFilial});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function sugereVigencia_cb(data, erro) {
	
	    setElementValue("dtVigenciaInicial", data.dtVigenciaSugerida);
	}
	
</script>