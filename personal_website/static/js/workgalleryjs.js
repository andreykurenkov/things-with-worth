$(document).ready(function() {
			var focusCategory = document.getElementById("firstCategory");
			var focusProject = document.getElementById("firstProject");
			var categoryList = document.getElementById("categoryList");
			var projectList = document.getElementById("projectList");
			var onCategory = focusCategory;

			focusCategory.style.backgroundColor = "white";
			focusCategory.firstElementChild.style.backgroundColor = "white";
			focusCategory.firstElementChild.style.color = "black";
			
			focusProject.style.borderTop = "1px solid #fff";
			
			categoryList.addEventListener("mouseover", function( event ) { 
				if(event.target.tagName == 'LI' && event.target!=onCategory){	
					onCategory.style.border = "1px solid #000";
					event.target.style.border = "1px solid #fff";
					onCategory = event.target;
				}
			});
			
			categoryList.addEventListener("click", function( event ) { 
				if(onCategory!=focusCategory){
					onCategory.style.backgroundColor = "white";
					onCategory.firstElementChild.style.backgroundColor = "white";
					onCategory.firstElementChild.style.color = "black";

					focusCategory.style.backgroundColor = "black";
					focusCategory.firstElementChild.style.backgroundColor = "black";
					focusCategory.firstElementChild.style.color = "white";
					
					focusCategory = onCategory;
					
					$.get('/gallery/', {'category': focusCategory.firstChild.innerHTML}, function(data){
							$('#projectList').html(data);
							focusProject = document.getElementById("firstProject");
					}); 					
					
					$.get('/gallery/', {'category': focusCategory.firstChild.innerHTML, 'project':'1'}, function(data){
							$('#workspecific').html(data);
							focusProject = document.getElementById("firstProject");
							focusProject.style.borderTop = "1px solid #fff";
					}); 
					

				}
			});

			projectList.addEventListener("mouseover", function( event ) {  
			
				if(event.target.tagName == 'IMG' && event.target.parentNode!=focusProject){
					focusProject.style.borderTop = "1px solid #000";
					event.target.parentNode.style.borderTop = "1px solid #fff";
					focusProject = event.target.parentNode;
					
					$.get('/gallery/', {'category': focusCategory.firstChild.innerHTML, 'project': focusProject.getElementsByTagName('h2')[0].firstChild.innerHTML}, function(data){
							$('#workspecific').html(data);
					}); 
				}
			});
		});			