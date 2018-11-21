
const changeStyle = data => {
    data.value ? document.execCommand(data.command, false, data.value) : document.execCommand(data.command, false, null)
};

var codes;
var comments;

function Issue(index){
    document.getElementById("comment-area").innerHTML=comments[index];
}
function Submit(){
    var code  = $("#text-area").text();
    var param={
        "Code":code
    };

    $.ajax({
        type:"Post",
        url:"Code",
        data:param,
        async: false,
        success:function(data){
            var json= JSON.parse(data)
            codes=json["code"];

            comments=json["comment"];

            var res="";
            for(var i=0;i<codes.length;++i){
                res=res+"<span onmouseover=\"Issue("+i+")\">"+codes[i]+"</span>"+"<br/>";
            }

            console.log(res);
            document.getElementById("text-area").innerHTML=res;
        }
    });
    return;
}