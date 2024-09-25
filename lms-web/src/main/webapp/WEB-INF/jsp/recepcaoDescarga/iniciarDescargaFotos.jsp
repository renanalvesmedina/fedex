<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script type="text/javascript">
function carregaPagina() {
	onPageLoad();
	
	newButtonScript();	
}
</script>
<adsm:window service="lms.recepcaodescarga.iniciarDescargaAction" onPageLoad="carregaPagina">

	<adsm:form idProperty="idFotoCarregmtoDescarga" action="/recepcaoDescarga/iniciarDescarga"
			   service="lms.recepcaodescarga.iniciarDescargaAction.findByIdFotoCarregmtoDescarga">
	
		<adsm:masterLink idProperty="idEquipeOperacao" showSaveAll="false">
			<adsm:masterLinkItem label="controleCargas" property="controleCarga.nrControleCarga" itemWidth="50" />
			<adsm:masterLinkItem label="filial" property="sgFilial" itemWidth="50" />
			<adsm:masterLinkItem label="postoAvancado" property="sgPostoAvancado" itemWidth="50" />
		</adsm:masterLink>		

		<adsm:textbox label="descricao" property="dsFoto" dataType="text" size="50" 
					  maxLength="80" width="85%" required="true" />		
	
		<adsm:hidden property="filePath" serializable="false"/>
		<adsm:textbox dataType="picture" property="foto.foto" label="foto" size="50" width="85%" required="true" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarFoto" service="lms.recepcaodescarga.iniciarDescargaAction.saveFotoCarregmtoDescarga"/>
			<adsm:newButton caption="limpar"/>
		</adsm:buttonBar>	
	</adsm:form>
	
	<adsm:grid idProperty="idFotoCarregmtoDescarga" property="fotoCarregmtoDescarga" selectionMode="check" 
			   title="fotos" gridHeight="200" unique="true" autoSearch="false" rows="10" onPopulateRow="povoaLinhas"
   			   service="lms.recepcaodescarga.iniciarDescargaAction.findPaginatedFotoCarregmtoDescarga" 
			   rowCountService="lms.recepcaodescarga.iniciarDescargaAction.getRowCountFotoCarregmtoDescarga"
			   detailFrameName="fotos">	
		<adsm:gridColumn title="descricao" property="dsFoto" width="90%" />
		<adsm:gridColumn title="foto" property="foto.foto" width="10%" image="images/camera.gif" align="center"/>
		
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirFoto" 
							   service="lms.recepcaodescarga.iniciarDescargaAction.removeByIdsFotoCarregmtoDescarga"/>
		</adsm:buttonBar>			
	</adsm:grid>
	
</adsm:window>

<script type="text/javascript">

	/**
	 * Função q povoa a grid de fotos corretamente.
	 */
	function povoaLinhas(tr, data) {
		var tdblob = tr.children[2].innerHTML;
		var blob = tdblob.substring(tdblob.indexOf("</IMG>") + 6, tdblob.indexOf("</A>")).replace(" ","");
		fakeDiv = document.createElement("<DIV></DIV>");
		fakeDiv.innerHTML = "<TABLE><TR><TD><NOBR><A onclick=\"javascript:showPicture('" + blob + "'); event.cancelBubble=true;\"><IMG title=\"\" style=\"CURSOR: hand\" src=\"" + contextRoot + "/images/camera.gif\" border=0></IMG></A></NOBR></TD></TR></TABLE>";
		tr.children[2].innerHTML = fakeDiv.children[0].children[0].children[0].children[0].innerHTML;
	}

</script>