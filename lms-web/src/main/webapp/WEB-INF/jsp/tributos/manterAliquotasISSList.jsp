<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>
	
	function myOnPageLoadCallBack_cb(){
		
		onPageLoad_cb();
		
		var id = getElementValue("issMunicipioServico.idIssMunicipioServico");
		
		var dados = new Array();
		
		setNestedBeanPropertyValue(dados, "idIssMunicipioServico", id);
		
		var sdo = createServiceDataObject("lms.tributos.manterAliquotasISSAction.findIssMunicipioServicoById",
										  "carregaDadosFilhos",
										  dados);
		xmit({serviceDataObjects:[sdo]});		
	}
	
	function carregaDadosFilhos_cb(data, erro){
		
		if(erro != undefined){
			alert(erro);
			return false;		
		}
		
		setElementValue("issMunicipioServico.municipio.nmMunicipio", 
						getNestedBeanPropertyValue(data, "municipio.nmMunicipio" ));		
		
		/** Testa se os dois atributos são diferentes de undefined, se ambos testes retornar true, 
		 *	seta o campo "issMunicipioServico.servicoMunicipio.dsServicoMunicipio".
		 */
		if(getNestedBeanPropertyValue(data, "servicoMunicipio.nrServicoMunicipio" ) != undefined 
			&& getNestedBeanPropertyValue(data, "servicoMunicipio.dsServicoMunicipio") != undefined) {
			
			setElementValue("issMunicipioServico.servicoMunicipio.dsServicoMunicipio", 
						getNestedBeanPropertyValue(data, "servicoMunicipio.nrServicoMunicipio" ) + " - " + getNestedBeanPropertyValue(data, "servicoMunicipio.dsServicoMunicipio" ));		
		}		
		
		setElementValue("issMunicipioServico.servicoAdicional.dsServicoAdicional", 
						getNestedBeanPropertyValue(data, "servicoAdicional.dsServicoAdicional" ));		
						
		setElementValue("issMunicipioServico.servicoTributo.dsServicoTributo", 
						getNestedBeanPropertyValue(data, "servicoTributo.dsServicoTributo" ));		
				
	}
	
</script>
<adsm:window service="lms.tributos.manterAliquotasISSAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/tributos/manterAliquotasISS">

		<adsm:hidden property="issMunicipioServico.idIssMunicipioServico" serializable="true" />
		<adsm:hidden property="issMunicipioServico.municipio.idMunicipio" serializable="true" />	
		<adsm:hidden property="issMunicipioServico.servicoMunicipio.idServicoMunicipio" serializable="true" />	
		<adsm:hidden property="issMunicipioServico.servicoAdicional.idServicoAdicional" serializable="true" />	
		<adsm:hidden property="issMunicipioServico.servicoTributo.idServicoTributo"     serializable="true" />				 					 					 				 

		<adsm:textbox dataType="text"
					  property="issMunicipioServico.municipio.nmMunicipio"
					  label="municipio" size="45" maxLength="60" width="35%" disabled="true"
					  serializable="false" />

		<adsm:textbox dataType="text"
			          property="issMunicipioServico.servicoMunicipio.dsServicoMunicipio"
					  label="servicoMunicipio" size="50" maxLength="60" width="35%" disabled="true"
					  serializable="false"/>

		<adsm:textbox dataType="text"
					  property="issMunicipioServico.servicoAdicional.dsServicoAdicional"
					  label="servicoAdicional" size="45" maxLength="60" width="35%" disabled="true"
					  serializable="false"/>
					  
		<adsm:textbox dataType="text"
					  property="issMunicipioServico.servicoTributo.dsServicoTributo"
					  label="outroServico" size="50" maxLength="60" width="35%" disabled="true"
					  serializable="false"/>

		<adsm:range label="vigencia">
		    <adsm:textbox label="vigencia" dataType="JTDate" property="dtVigencia"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="aliquotaIssMunicipioServ"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idAliquotaIssMunicipioServ" property="aliquotaIssMunicipioServ" 
			   gridHeight="200" unique="true" defaultOrder="issMunicipioServico_municipio_.nmMunicipio, dtVigenciaInicial">
		<adsm:gridColumn title="municipio"       property="issMunicipioServico.municipio.nmMunicipio" width="25%"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" align="center"/>
		<adsm:gridColumn title="vigenciaFinal"   property="dtVigenciaFinal"   dataType="JTDate" align="center"/>
		<adsm:gridColumn title="percentualAliquota"        property="pcAliquota" dataType="percent" mask="##0.00"/>
		
		<!--  Retirado conforme solicitação do Joelson
		<adsm:gridColumn title="percentualEmbute" property="pcEmbute" dataType="percent" mask="##0.00"/>
 		-->
		<adsm:gridColumn title="emiteNFServico"  property="blEmiteNfServico" renderMode="image-check"/>
		<adsm:gridColumn title="retencaoTomadorServico" property="blRetencaoTomadorServico" renderMode="image-check"/>		
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
<script>
	document.getElementById("issMunicipioServico.municipio.nmMunicipio").masterLink = "true";
	document.getElementById("issMunicipioServico.servicoMunicipio.dsServicoMunicipio").masterLink = "true";
	document.getElementById("issMunicipioServico.servicoAdicional.dsServicoAdicional").masterLink = "true";
	document.getElementById("issMunicipioServico.servicoTributo.dsServicoTributo").masterLink = "true";
</script>