<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.filialMercurioFilialCiaService" >
	<adsm:form action="/municipios/manterFiliaisCiaAereaFiliais" idProperty="idFilialMercurioFilialCia" >
		<adsm:hidden property="flag" serializable="false" value="01"/>

		<adsm:lookup dataType="text" property="filialCiaAerea" service="lms.municipios.filialCiaAereaService.findLookup"
				action="/municipios/manterFiliaisCiaAerea" idProperty="idFilialCiaAerea" criteriaProperty="pessoa.nrIdentificacao"
				label="filialCiaAerea2" size="20" maxLength="20" width="35%" exactMatch="true" onDataLoadCallBack="filiaCiaAereaLookup" onPopupSetValue="filiaCiaAereaPopup" >
			<adsm:propertyMapping relatedProperty="filialCiaAerea.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping criteriaProperty="filialCiaAerea.aeroporto.idAeroporto" modelProperty="aeroporto.idAeroporto"/>
			<adsm:propertyMapping criteriaProperty="filialCiaAerea.aeroporto.sgAeroporto" modelProperty="aeroporto.sgAeroporto"/>
			<adsm:propertyMapping criteriaProperty="filialCiaAerea.aeroporto.pessoa.nmPessoa" modelProperty="aeroporto.pessoa.nmPessoa"/>
			<adsm:propertyMapping criteriaProperty="ciaFilialMercurio.empresa.idEmpresa" modelProperty="empresa.idEmpresa" />
			<adsm:textbox dataType="text" property="filialCiaAerea.pessoa.nmPessoa" size="20" disabled="true" serializable="false" />
	    </adsm:lookup>
	     
	    <adsm:combobox property="ciaFilialMercurio.empresa.idEmpresa" optionLabelProperty="pessoa.nmPessoa" optionProperty="idEmpresa"
				label="ciaAerea" service="lms.municipios.empresaService.findCiaAerea" boxWidth="229" />		
	    
		
		<adsm:lookup dataType="text" property="filialCiaAerea.aeroporto" service="lms.municipios.aeroportoService.findLookup"
				action="/municipios/manterAeroportos" idProperty="idAeroporto" criteriaProperty="sgAeroporto"
				label="aeroporto" size="3" maxLength="3" width="35%" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="filialCiaAerea.aeroporto.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
	    	<adsm:textbox dataType="text" property="filialCiaAerea.aeroporto.pessoa.nmPessoa"
					size="37" disabled="true" serializable="false" />
	    </adsm:lookup>
		
		
		<adsm:lookup dataType="text" property="ciaFilialMercurio" service="lms.municipios.ciaFilialMercurioService.findLookup"
				action="/municipios/manterCiasAereasFiliais" idProperty="idCiaFilialMercurio" criteriaProperty="filial.sgFilial"
				label="filial" size="3" maxLength="3" width="35%" minLengthForAutoPopUpSearch="3" exactMatch="false" >
			<adsm:propertyMapping relatedProperty="ciaFilialMercurio.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="ciaFilialMercurio.empresa.idEmpresa" modelProperty="empresa.idEmpresa"/>
	    	<adsm:textbox dataType="text" property="ciaFilialMercurio.filial.pessoa.nmFantasia"
	    			size="30" disabled="true" serializable="false" />
	    </adsm:lookup>
	  			
       <adsm:range label="vigencia" labelWidth="15%" width="85%" >
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="filialMercurioFilialCia" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="filialMercurioFilialCia" idProperty="idFilialMercurioFilialCia"
			unique="true" gridHeight="220" rows="11" scrollBars="horizontal"
			defaultOrder="filialCiaAerea_aeroporto_pessoa_.nmPessoa,nrOrdemUso,dtVigenciaInicial" >
		<adsm:gridColumn title="identificacao" property="filialCiaAerea.pessoa.tpIdentificacao" isDomain="true" width="60"  />		
		<adsm:gridColumn title="" property="filialCiaAerea.pessoa.nrIdentificacaoFormatado" width="120" align="right" />
		<adsm:gridColumn title="filialCiaAerea2" property="filialCiaAerea.pessoa.nmPessoa" width="150" />
		<adsm:gridColumn title="ciaAerea" property="ciaFilialMercurio.empresa.pessoa.nmPessoa" width="150" />
		
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="aeroporto" property="filialCiaAerea.aeroporto.sgAeroporto" width="100" />
			<adsm:gridColumn title="" property="filialCiaAerea.aeroporto.pessoa.nmPessoa" width="100" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filial" property="ciaFilialMercurio.filial.sgFilial" width="20" />
			<adsm:gridColumn title="" property="ciaFilialMercurio.filial.pessoa.nmFantasia" width="130" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="ordemUsoAeroporto" property="nrOrdemUso" width="170" dataType="integer" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="100"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="100"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	function filiaCiaAereaLookup_cb(data) {
		var isFilialCiaAereaEmpty = (getNestedBeanPropertyValue(data,":0.idFilialCiaAerea") == undefined);
		if (isFilialCiaAereaEmpty)
			document.getElementById("ciaFilialMercurio.idCiaFilialMercurio").value = "";
		if(document.getElementById("ciaFilialMercurio.idCiaFilialMercurio").masterLink == "false")		
			setDisabled("ciaFilialMercurio.idCiaFilialMercurio",isFilialCiaAereaEmpty);
		
		if (data != undefined && data.length == 1) {
			var nrFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			setNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacao", nrFormatado);
		}
		
		return lookupExactMatch({e:document.getElementById("filialCiaAerea.idFilialCiaAerea"), data:data, callBack:'setaNrIdentificacaoLikeEnd'});
		
	}
	
	function filiaCiaAereaPopup(data) {
		var isFilialCiaAereaEmpty = (getNestedBeanPropertyValue(data,"idFilialCiaAerea") == undefined);
		if (isFilialCiaAereaEmpty)
			document.getElementById("ciaFilialMercurio.idCiaFilialMercurio").value = "";
		if(document.getElementById("ciaFilialMercurio.idCiaFilialMercurio").masterLink == "false")		
			setDisabled("ciaFilialMercurio.idCiaFilialMercurio",isFilialCiaAereaEmpty);

		var nrFormatado = getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado");
		setNestedBeanPropertyValue(data, "pessoa.nrIdentificacao", nrFormatado);
		
	}
	
	function setaNrIdentificacaoLikeEnd_cb(data) {
	    if (data != undefined && data.length == 1) {
			var nrFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			setNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacao", nrFormatado);
		}
		
		return empresa_pessoa_nrIdentificacao_likeEndMatch_cb(data);
	}	

</script>