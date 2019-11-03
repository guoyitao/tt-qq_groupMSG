
var groupIds=["606猛男群","移动天团"]
var changewinsleep=800
var youtext="666"
while(true){
    
groupIds.forEach(function(item) {
   toast(item)
   var  search=id("et_search_keyword").findOne()
   sleep(changewinsleep*2)
   search.click()
   sleep(changewinsleep*2)
 var  search2=id("et_search_keyword").findOne()
   search2.setText(item)
   sleep(changewinsleep)
   className("android.widget.TextView").text(item).findOne().parent().parent().click()
   sleep(changewinsleep)
   id("input").findOne().setText(youtext)
   sleep(changewinsleep)
   id("fun_btn").findOne().click()
   sleep(changewinsleep)
   id("ivTitleBtnLeft").findOne().parent().click()
   
});
  
}
