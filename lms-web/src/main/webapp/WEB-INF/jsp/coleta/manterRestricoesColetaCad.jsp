<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.restricaoColetaService">
	<adsm:form action="/coleta/manterRestricoesColeta" idProperty="idRestricaoColeta">
		<adsm:combobox 
					autoLoad			="true"
					label				="servico" 
					optionLabelProperty	="dsServico" 
					optionProperty		="idServico" 
					property			="servico.idServico" 
					service				="lms.coleta.restricaoColetaService.findListServico" 
					labelWidth			="19%" 
					onlyActiveValues	="true"					
					width				="81%" 
					required			="false"
					boxWidth			="225"					
		/>		
		
		<adsm:hidden property="active" value="A" serializable="false"/>
		
		<adsm:lookup service="lms.municipios.paisService.findLookup" 
					 property="pais" 
					 label="pais"
					 idProperty="idPais"
					 criteriaProperty="nmPais"					 
					 dataType="text"
					 maxLength="60"
					 action="/municipios/manterPaises"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 labelWidth="19%" 
					 width="81%" 
					 size="35">
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="active"/> 
		</adsm:lookup>
					 
					 
		<adsm:textbox labelWidth="19%" width="81%" dataType="weight" property="psMaximoVolume" label="pesoMaximoPorVolume" maxLength="18" size="10" unit="kg" minValue="0" />
		<adsm:combobox 
					autoLoad			="true"
					label				="produtoProibido" 
					onlyActiveValues	="true"
					optionLabelProperty	="dsProduto" 
					optionProperty		="idProdutoProibido" 
					property			="produtoProibido.idProdutoProibido" 
					service				="lms.coleta.restricaoColetaService.findListProdutoProibido"
					labelWidth			="19%" 
					width				="81%" 
		/>		
		<adsm:buttonBar>
			<adsm:button caption="salvar" onclick="validateToSave(this.document);" buttonType="storeButton" disabled="false"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
		<script language="javascript">
		
		function validateToSave(form) 
		{
			var servico = getElementValue("servico.idServico");
			var pais = getElementValue("pais.idPais");
			var psMaximoVolume = getElementValue("psMaximoVolume");
			var produtoProibido = getElementValue("produtoProibido.idProdutoProibido");

			if (servico == "" && pais == "" && psMaximoVolume == "" && produtoProibido == "") {
				alert('<adsm:label key="LMS-02041"/>');
				setFocus(document.getElementById("servico.idServico"));
				return false;
			}
		    storeButtonScript('lms.coleta.restricaoColetaService.store', 'salvarVinculo', form.forms[0]);
		}
		
		function salvarVinculo_cb(data,erro){
			store_cb(data,erro);
		}
		
		</script>
		
	</adsm:form>
</adsm:window>

