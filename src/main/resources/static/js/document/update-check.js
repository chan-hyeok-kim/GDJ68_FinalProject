/**
 * 업데이트 검증
 */
checkResultUpdate=[false,false,false];

$("#document-update-btn").mouseover(function(){
		if ($('#check-cd').val()!='') {
		checkResultUpdate[0]=true;
	}
	console.log(checkResultUpdate)
	
})

$('#document-update-btn').mouseover(function(){
	$('#select-form').val();
	console.log($('#select-form').val());
	if($('#select-form').val()!=''){
		checkResultUpdate[2]=true;
	}
})



$("#document-update-btn").click(function() {
	//id가 smarteditor인 textarea에 에디터에서 대입
	console.log($('#approvalForm').val())

	oEditors.getById["approvalForm"].exec("UPDATE_CONTENTS_FIELD", []);
    
 
	// 이부분에 에디터 validation 검증
	if ($('#approvalForm').val() != '') {
		checkResultUpdate[1]=true;
	}
	console.log($('#approvalForm').val())
	console.log(checkResultUpdate)
	//폼 submit
	/*$("#frm").submit();*/
	if (!checkResultUpdate.includes(false)) {
		$("#frm").submit();
	} else {
		alert('빈 내용을 채워주세요')
	}
})



