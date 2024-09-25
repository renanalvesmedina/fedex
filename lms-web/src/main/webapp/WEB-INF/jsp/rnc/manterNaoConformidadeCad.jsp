<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.rnc.manterNaoConformidadeAction">
	<adsm:form action="/rnc/manterNaoConformidade" idProperty="idNaoConformidade" onDataLoadCallBack="retorno_CarregamentoDados">

		<adsm:hidden property="filial.idFilial" serializable="true" />

		<adsm:textbox dataType="text" property="filial.sgFilial" 
					  size="3" maxLength="3" label="naoConformidade" labelWidth="20%" width="35%" disabled="true" >
			<adsm:textbox dataType="integer" property="nrNaoConformidade" size="9" maxLength="8" mask="00000000" disabled="true" />
		</adsm:textbox>

		<adsm:textbox dataType="text" property="tpStatusNaoConformidade.description" 
					  label="status" size="10" width="30%" disabled="true" serializable="false" />
		<adsm:hidden property="tpStatusNaoConformidade.value" serializable="true" />


		<adsm:textbox dataType="text" property="doctoServico.tpDocumentoServico.description" 
					  label="documentoServico" size="10" labelWidth="20%" width="35%" disabled="true" serializable="false" >

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="lms.rnc.manterNaoConformidadeAction.findLookupFilial" 
						 action="/municipios/manterFiliais" popupLabel="pesquisarFilial"
						 size="3" maxLength="3" picker="false" disabled="true"/>
			
			<adsm:lookup dataType="integer" 
						 property="doctoServico" 
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 popupLabel="pesquisarDocumentoServico"
						 service="" 
						 action="" 
						 size="10" maxLength="8" picker="false" mask="00000000" serializable="false" disabled="true">
			</adsm:lookup>
		</adsm:textbox>
		<adsm:hidden property="idDoctoServico" serializable="false" />

		<adsm:textbox dataType="text" property="destinoDoctoServico" label="destino" 
					  size="10" maxLength="3" width="30%" disabled="true" serializable="false" />
					  
		<adsm:textbox dataType="text" label="awb" property="nrAwb" size="20" maxLength="20" disabled="true" labelWidth="20%" width="35%" />
		
		<adsm:textbox dataType="text" label="modal" property="tpModal" size="20" maxLength="20" disabled="true" labelWidth="15%" width="30%" />

		<adsm:textbox dataType="JTDateTimeZone" property="doctoServico.dhEmissao" label="dataEmissao" picker="false"
					  size="17" labelWidth="20%" width="35%" disabled="true" />
					  
		<adsm:textbox dataType="text" label="causador" property="causadorRnc" size="20" maxLength="20" disabled="true" labelWidth="15%" width="30%" />  

		<adsm:hidden property="clienteByIdClienteRemetente.idCliente" />
		<adsm:textbox property="clienteByIdClienteRemetente.pessoa.nrIdentificacao" 
		              label="remetente" dataType="text" labelWidth="20%" width="77%" disabled="true" >
			<adsm:textbox dataType="text" property="clienteByIdClienteRemetente.pessoa.nmPessoa" 
				          size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="clienteByIdClienteDestinatario.idCliente" />
		<adsm:textbox property="clienteByIdClienteDestinatario.pessoa.nrIdentificacao" 
					  label="destinatario" dataType="text" labelWidth="20%" width="77%" disabled="true" >
			<adsm:textbox dataType="text" property="clienteByIdClienteDestinatario.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" />
		</adsm:textbox>
		
		<adsm:hidden property="clienteByIdClienteTomador.idCliente" />
		<adsm:textbox property="clienteByIdClienteTomador.pessoa.nrIdentificacao" 
					  label="tomadorFrete" dataType="text" labelWidth="20%" width="77%" disabled="true" >
			<adsm:textbox dataType="text" property="clienteByIdClienteTomador.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" />
		</adsm:textbox>
		
		<adsm:textbox property="qtVolumesDoctoServico" label="quantidadeVolumes" dataType="integer" 
			  		  size="5" maxLength="8" labelWidth="20%" width="35%" disabled="true" />

		<adsm:hidden property="doctoServico.moeda.idMoeda" serializable="false" />
		<adsm:textbox label="valorDctoServico" dataType="text" property="doctoServico.moeda.dsSimbolo" size="8" 
					  width="30%" serializable="false" disabled="true" >
			<adsm:textbox dataType="currency" property="vlTotalDocServico" 
						  mask="###,###,###,###,##0.00" size="18" serializable="false" disabled="true" />
		</adsm:textbox>

		<adsm:buttonBar>
			<adsm:button id="btnOcorrencias" caption="ocorrencias" action="/rnc/manterOcorrenciasNaoConformidade" cmd="main" >
				<adsm:linkProperty src="filial.sgFilial" target="naoConformidade.filial.sgFilial" />
				<adsm:linkProperty src="nrNaoConformidade" target="naoConformidade.nrNaoConformidade" />
				<adsm:linkProperty src="idNaoConformidade" target="naoConformidade.idNaoConformidade" />
				<adsm:linkProperty src="idDoctoServico" target="naoConformidade.doctoServico.idDoctoServico" />
				<adsm:linkProperty src="doctoServico.tpDocumentoServico.description" target="naoConformidade.doctoServico.tpDocumentoServico.description" />
				<adsm:linkProperty src="doctoServico.filialByIdFilialOrigem.sgFilial" target="naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial" />
				<adsm:linkProperty src="doctoServico.nrDoctoServico" target="naoConformidade.doctoServico.nrDoctoServico" />
				<adsm:linkProperty src="doctoServico.moeda.idMoeda" target="naoConformidade.doctoServico.moeda.idMoeda" />
				<adsm:linkProperty src="clienteByIdClienteRemetente.idCliente" target="naoConformidade.clienteByIdClienteRemetente.idCliente" />
				<adsm:linkProperty src="clienteByIdClienteDestinatario.idCliente" target="naoConformidade.clienteByIdClienteDestinatario.idCliente" />
			</adsm:button>

			<adsm:button id="btnNegociacoes" caption="negociacoes" action="/rnc/registrarNegociacoes" cmd="main" >
				<adsm:linkProperty src="idNaoConformidade" target="ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade" />
				<adsm:linkProperty src="filial.sgFilial" target="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial" />
				<adsm:linkProperty src="nrNaoConformidade" target="ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade" />
			</adsm:button>

			<adsm:button id="btnDisposicao" caption="disposicao" action="/rnc/registrarDisposicao" cmd="main">
				<adsm:linkProperty src="idNaoConformidade" target="ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade" />
				<adsm:linkProperty src="filial.idFilial" target="ocorrenciaNaoConformidade.naoConformidade.filial.idFilial" />
				<adsm:linkProperty src="filial.sgFilial" target="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial" />
				<adsm:linkProperty src="nrNaoConformidade" target="ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade" />
			</adsm:button>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
function retorno_CarregamentoDados_cb(data, error) {
	onDataLoad_cb(data, error);
	if (error != undefined) {
		alert(error);
		return false;
	} 
	if (data != undefined) {
		setElementValue('vlTotalDocServico', setFormat(document.getElementById("vlTotalDocServico"), data.vlTotalDocServico) );
	}

	setElementValue("idDoctoServico", getNestedBeanPropertyValue(data, "doctoServico.idDoctoServico"));

	format(document.getElementById("doctoServico.nrDoctoServico"));
	format(document.getElementById("nrNaoConformidade"));
	
	if (getNestedBeanPropertyValue(data, "blExisteOcorrenciaAberta") == "true")
		setDisabled("btnDisposicao", false);
	else
		setDisabled("btnDisposicao", true);
	
	// LMS-3240	
	if(getNestedBeanPropertyValue(data, "blExisteOcorrenciNaoConformidade") == "true"){
		setDisabled("btnNegociacoes", true);
		setDisabled("btnDisposicao", true);
}
}
</script>