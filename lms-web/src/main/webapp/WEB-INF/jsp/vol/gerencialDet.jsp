<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.gerencialAction" onPageLoad="myOnPageLoad" >
	<adsm:form action="/vol/gerencial"  >
		<adsm:hidden property="idMeioTransporte"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="pararAtualizacao" buttonType="a" id="btnParar"
				boxWidth="125" onclick="pararAtualizacao();" />			
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:form id="form2" action="/vol/gerencial">
		<adsm:textbox label="frotaPlaca" labelWidth="8%" property="frotaPlaca" dataType="text" disabled="true" width="82%" size="15" />
	</adsm:form>
	
   	<adsm:grid property="gerencial" selectionMode="none" onRowClick="disableClick"
			   service="lms.vol.gerencialAction.findDetalhamentoGerencial" onPopulateRow="populateRow"
			   rowCountService="lms.vol.gerencialAction.getRowCount" disableMarkAll="true" rows="500"		   
			   gridHeight="300" unique="true" scrollBars="vertical" idProperty="idMeioTransporte" showPagging="false"
			   onDataLoadCallBack="findDetalhamentoGerencial"
			   >
		<adsm:gridColumn title="controleCarga" property="CONTROLECARGA" align="center" width="18%" />
		<adsm:gridColumn title="manifesto" property="MANIFESTO" align="center" width="18%" />
		<adsm:gridColumn title="operacao" property="OPERACAO" width="18%" image="/images/rodo.gif" />
		<adsm:gridColumn title="eficiencia" property="EFICIENCIA" align="center" width="18%" />
		<adsm:gridColumn title="realizado" property="REALIZADO" align="center" width="18%" />
		<adsm:gridColumn title="total" property="TOTAL" align="center" width="10%" />
		
	</adsm:grid>	
	
	<adsm:i18nLabels>
		<adsm:include key="pararAtualizacao"/>
		<adsm:include key="ativarAtualizacao"/>
	</adsm:i18nLabels>
</adsm:window>

<script>
	var oInterval = "" ;
	var atualizaTela = true;

	function myOnPageLoad() {
	
		onPageLoad();
	}
	
	function pararAtualizacao() {
	    if (oInterval == undefined) {
	       oInterval = window.setInterval("fnInitFindGerencial()",480000);
	       obj = document.getElementById("btnParar");
	       obj.value = i18NLabel.getLabel("pararAtualizacao");
	    } else {
	       oInterval= window.clearInterval(oInterval); 
	       obj = document.getElementById("btnParar");
	       obj.value = i18NLabel.getLabel("ativarAtualizacao");
	    } 
	 }
	 	
	function fnInitFindGerencial(){
		if (atualizaTela){
	   		tabShowDetalhamento()
	   }
	}
	
	function tabShowDetalhamento() {
	    atualizaTela = false;
	    findButtonScript('gerencial', document.forms[0]);
	}
	
	function tabHideDetalhamento() {	
	   oInterval= window.clearInterval(oInterval); 
	}
	
	function disableClick() {
		return false;
	}
	
	function populateRow(tr,data) {
		if(data.TipoOperacao.equals('A')){
			tr.children[2].innerHTML = tr.children[2].innerHTML.replace("rodo.gif","aereo.gif");
		}
	}
	
	function findDetalhamentoGerencial_cb(data,error) {
		atualizaTela = true;		
	}
</script>