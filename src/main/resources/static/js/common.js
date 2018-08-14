function logout(){
	if(confirm("确认注销本次登录？")){
		window.location.href="/logout";
	}
}

function delCheck(){
	return confirm("确认要删除此记录？");
}

function goProfile(id){
	window.location.href="/user/"+id+"/edit";
}