<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterManifestosViagemAction">
	<adsm:form id="form2" action="/expedicao/manterManifestosViagem">
		<adsm:hidden property="idManifestoViagemNacional" />

		<adsm:textbox property="manifesto.filialByIdFilialOrigem.sgFilial" label="numeroManifesto2" dataType="text" maxLength="3" size="4" disabled="true" labelWidth="20%" width="37%">
			<adsm:textbox
				property="nrManifestoOrigem"
				dataType="integer"
				size="8"
				maxLength="8"
				mask="00000000"
				disabled="true"
			/>
		</adsm:textbox>

		<adsm:complement label="filialDestino" labelWidth="15%" width="25%">
			<adsm:textbox dataType="text" size="3" maxLength="3"
				property="manifesto.filialByIdFilialDestino.sgFilial"
				serializable="false" 
				disabled="true"/>
			<adsm:textbox dataType="text" size="25" 
				property="manifesto.filialByIdFilialDestino.pessoa.nmFantasia"
				serializable="false"
				disabled="true"/>
		</adsm:complement>

		<adsm:buttonBar freeLayout="true" />

	</adsm:form>

	<adsm:grid idProperty="idManifestoViagemNacional" property="gridConhecimento" autoSearch="false"
			service="lms.expedicao.manterManifestosViagemAction.findPaginatedConhecimentoManifestoViagem"
			rowCountService="lms.expedicao.manterManifestosViagemAction.getRowCountConhecimentoManifestoViagem"
			rows="12" selectionMode="none" onRowClick="noAcao">
		<adsm:gridColumn title="numCTRC" property="nrCtrc" width="12%" />
		<adsm:gridColumn title="filialDestino" property="sgFilialDestino" width="8%" />
		<adsm:gridColumn title="valorFrete" property="vlTotalDocServico" dataType="currency" width="10%" unit="reais" align="right"/>
		<adsm:gridColumn title="remetente" property="nmPessoa" width="22%" />
		<adsm:gridColumn title="destinatario" property="nmPessoaD" width="22%" />
		<adsm:gridColumn title="valorMerc" property="vlMercadoria" dataType="currency" unit="reais" width="10%" />
		<adsm:gridColumn title="qtdeVolumes" property="qtVolumes" width="8%" align="right"/>
		<adsm:gridColumn title="peso" property="psReal" unit="kg" dataType="decimal" mask="###,###,###,###,##0.000" width="8%" align="right"/>
		<adsm:buttonBar />
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
	function initWindow(event) {
		if(event.name=="tab_click") {
			var idManifestoViagemNacional = getTabGroup(document).getTab('cad').getFormProperty("idManifestoViagemNacional", "form1");
			var sgFilialOrigem = getTabGroup(document).getTab('cad').getFormProperty("manifesto.filialByIdFilialOrigem.sgFilial", "form1");
			var nrManifestoOrigem = getTabGroup(document).getTab('cad').getFormProperty("nrManifestoOrigem", "form1");
			var sgFilialDestino = getTabGroup(document).getTab('cad').getFormProperty("manifesto.filialByIdFilialDestino.sgFilial", "form1");
			var nmFantasiaFilialDestino = getTabGroup(document).getTab('cad').getFormProperty("manifesto.filialByIdFilialDestino.pessoa.nmFantasia", "form1");

			setElementValue("idManifestoViagemNacional", idManifestoViagemNacional);
			setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", sgFilialOrigem);
			setElementValue("nrManifestoOrigem", nrManifestoOrigem);
			setElementValue("manifesto.filialByIdFilialDestino.sgFilial", sgFilialDestino);
			setElementValue("manifesto.filialByIdFilialDestino.pessoa.nmFantasia", nmFantasiaFilialDestino);
			populaGrid();
		}
	}

	function populaGrid(){
		findButtonScript("gridConhecimento", getElement("form2"));
	}

	function noAcao(){
		return false;
	}
</script>
