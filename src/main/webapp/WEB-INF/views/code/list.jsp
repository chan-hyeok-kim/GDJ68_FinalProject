<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>


 
  

<meta name="description" content="Signature Pad - HTML5 canvas based smooth signature drawing using variable width spline interpolation.">
   <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
  
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
  
    <!-- <link rel="stylesheet" href="/css/sign/signature-pad.css">
   -->
    <script type="text/javascript" async="" src="https://ssl.google-analytics.com/ga.js"></script><script type="text/javascript">
      var _gaq = _gaq || [];
      _gaq.push(['_setAccount', 'UA-39365077-1']);
      _gaq.push(['_trackPageview']);
  
      (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
      })();
    </script> 

</head>
<body id="page-top">
	<div id="wrapper">


		<!-- Content Wrapper -->
		<div id="content-wrapper" class="d-flex flex-column">
			<div class="card">
				<div class="card-body">

					<div class="wrapper-toolbar">

						<div style="float:left">코드 관리</div> 
						<div style="text-align: right;">
							<form class="form-inline" action="./list" method="get">

								<!-- 검색 설정 -->
								<select name="kind" class="btn btn-gradient-light" id="top-search-select">
									<option selected="selected" value="3">코드명</option>
									<option value="upCode">상위코드</option>
									<option value="code">코드</option>
								</select> 
								
								
								<input style="display: inline-block;" id="top-search-bar" name="search"
									class="form-control" type="search" placeholder="입력 후 [Enter]"
									aria-label="Search">
								<button id="top-search-btn" class="btn btn-info" type="submit">검색</button>

							</form>
						</div>
					</div>

<!-- 					<ul class="nav-tabs">
						<li onclick="location.href='./list'" class="active"><a class="link-tab">전체</a></li>
						<li data-cd="R031"><a class="link-tab">상위코드별</a></li>
						<li data-cd="R032"><a class="link-tab"></a></li>
						<li data-cd="R034"><a class="link-tab"></a></li>
						<li data-cd="R033"><a class="link-tab"></a></li>
					</ul>	 -->


					<div
						style="text-align: right; padding-top: 20px; padding-right: 10px">
						<div id="grid-top-date"></div>
					</div>
					<div id="content">


						<div class="container-fluid">


				  
				  
				
				    <table class="table-bordered mt-2" id="approval-table">
				        <thead>
				           <tr>
				             <th>선택</th>
				             <th>상위코드</th>
				             <th>코드</th>
				             <th>코드명</th>
				             <th width="20%">관리</th>
				           </tr>
				        </thead>
				        <tbody>
				        <c:forEach items="${list}" var="vo" varStatus="i">
				           <tr>
				             <td><input type="checkbox" name="checkList" value="${vo.code}"></td>
				             <td>${vo.upCode}</td>
				             <td>${vo.code}</td>
				             <td>${vo.codeName}</td>
				             <td><button class="btn btn-info code-update-btn" data-toggle="modal"  data-target="#update-code-modal${i.index}">수정</button></td>
				           </tr>
				           
				           
				            <!-- update Modal -->
  
  <div class="modal fade" id="update-code-modal${i.index}" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" id="sign-modal-size" role="document">
    <div class="modal-content" style="border-bottom: white; border-radius: 0rem;">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">코드 수정</h5>
        <!-- <button type="button" class="close btn-info" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button> -->
      </div>
      <div class="modal-body">
          
          <form action="/code/update" method="post" id="frm-update${i.index}">
          <p><span class="code-input-title">상위코드</span>
          <input style="width:50%;" type="text" class="form-control" 
          id="up-code-update-check${i.index}" name="upCode" value="${vo.upCode}"></p>
          <p><span class="code-input-title">코드 </span>
          <input style="width:50%;" type="text" class="form-control" 
          id="code-update-check${i.index}" name="code" value="${vo.code}"> </p>
          <p><span class="code-input-title">코드명</span>
          <input style="width:50%;" type="text" class="form-control"
          id="code-name-update-check${i.index}" name="codeName" value="${vo.codeName}"> </p>
  </div>     
  
      <input type="hidden" value="${vo.code}" name="originCode">
      
      <div class="modal-footer" style="background: white">
         <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
         <button type="button" class="btn btn-info" id="code-update-btn${i.index}">확인</button>
         </form>
        </div>
        
        </div>
      </div>
    </div>
				         </c:forEach>
				        </tbody>
				      
				    </table>
				  </div>
				  </div>
				  
				  
				  <!-- pagination -->
				  <div style="text-align: right; margin: 20px 20px;">
				  <nav aria-label="Page navigation example" style="display: inline-block;">
  <ul class="pagination">
    <li class="page-item ${pager.pre?'':'disabled'}">
      <a class="page-link" href="/code/list?page=${pager.startNum-1}&kind=${pager.kind}&search=${pager.search}" aria-label="Previous">
        <i class="mdi mdi-arrow-left-drop-circle"></i>
      </a>
    </li>
    
    <c:forEach begin="${pager.startNum}" end="${pager.lastNum}" var="i">
    <li class="page-item"><a class="page-link" href="/code/list?page=${i}&kind=${pager.kind}&search=${pager.search}">${i}</a></li>
    </c:forEach>
    
    <li class="page-item ${pager.next?'':'disabled'}">
      <a class="page-link" href="/code/list?page=${pager.lastNum+1}&kind=${pager.kind}&search=${pager.search}" aria-label="Next">
        <i class="mdi mdi-arrow-right-drop-circle"></i>
      </a>
    </li>
    
    <!-- Button List  -->

			  <div style="margin-left:300px;">
      <button type="button" class="btn  btn-inverse-dark" id="delete-btn">코드 삭제</button>
      <button type="button" class="btn btn-info" data-toggle="modal"  data-target="#add-code-modal">코드 추가</button>
  </div> 
     
 
  </ul>
  
  
</nav>

 
			
 	  
				 
  </div>




<div class="card" id="sub-card-1">
   <div class="card-body">
   <p>
[사용안내]<br>
ㆍ그룹웨어 시스템을 관리하는데 기초가 되는 코드를 등록합니다.<br>
ㆍ상위코드는 코드를 성격별로 분류, 관리하기 위한 그룹입니다.<br>
ㆍ코드는 반드시 상위코드를 포함, 최소 4자~최대 5자까지 적을 수 있습니다<br>
</p>
</div>
</div>
  
  


<!-- Modal -->

				  


  
  
  <!-- add Modal  -->
  
  <div class="modal fade" id="add-code-modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" id="sign-modal-size" role="document">
    <div class="modal-content" style="border-bottom: white; border-radius: 0rem;">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">코드 등록</h5>
        <!-- <button type="button" class="close btn-info" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button> -->
      </div>
      <div class="modal-body">
          
          <form action="/code/add" method="post" id="frm">
          <p><span class="code-input-title">상위코드</span>
          <input style="width:50%;" type="text" class="form-control" 
          id="up-code-check" name="upCode"></p>
          <p><span class="code-input-title">코드 </span>
          <input style="width:50%;" type="text" class="form-control" 
          id="code-check" name="code"> </p>
          <p><span class="code-input-title">코드명</span>
          <input style="width:50%;" type="text" class="form-control"
          id="code-name-check" name="codeName"> </p>
  </div>     
  
  
      
      <div class="modal-footer" style="background: white">
         <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
         <button type="button" class="btn btn-info" id="code-add-btn">확인</button>
         </form>
        </div>
        
        </div>
      </div>
    </div>
  
  
 
  </div>
<!-- modal end -->			
	

				</div>
              </div>
			</div>

		

	
			


	
	<script src="/js/commons/list-date.js"></script>
	<script src="/js/approval/list-move.js"></script>
	
	<!-- 코드 관련  -->
	<script src="/js/code/add-check.js"></script>
	<script src="/js/code/delete-check.js"></script>
	<script src="/js/code/update-check.js"></script>
	
	
</body>
</html>