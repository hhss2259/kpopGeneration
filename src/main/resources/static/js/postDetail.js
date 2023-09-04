
// 좋아요 정보 가지고 오기
(function(){
    fetch('/api/post/likes?post='+postId)
    .then(response => response.json())
    .then(data => {
        document.querySelector(".detail-likes").textContent  = "좋아요 "+data.data.likes;
        if(data.data.isLiked == true){
            document.querySelector(".detail-likes-circle").classList.add('liked');
        }
    })
})();
// 비로그인자는 좋아요 기능을 사용할 수 없다
function clickLikesByAny(){
    alert("좋아요 기능은 로그인 후 사용 가능합니다.");
}
// 좋아요 버튼 클릭 시
function clickLikes(){
    fetch("/api/post/likes/toggle?post="+postId)
    .then(response => response.json())
    .then(data => {
        document.querySelector(".detail-likes").textContent  =  "좋아요 "+data.data.likes;
        let circle = document.querySelector(".detail-likes-circle");
        if(data.data.isLiked == true){
            circle.classList.add('liked');
        }else{
            circle.classList.remove('liked');
        }

    })
}
// 좋아요 버튼에 이벤트 함수 등록
(function(){
    let button = document.querySelector(".detail-likes-circle");
    if(member.logined == false){
        button.addEventListener('click', clickLikesByAny);
    }else{
        button.addEventListener('click', clickLikes);
    }
})();
            
            


// 빈 댓글인지 확인
function checkComment(event){
    event.preventDefault();
    let textBody =  event.target.parentElement.firstElementChild;
    if(textBody.value.length == 0){
        alert("댓글을 입력해주세요");
    }else{
        textBody.parentNode.submit();
    }                
};
// 댓글 입력란 토글 - 새댓글 달기
function toggleComment_new(event){
    if(member.logined == false){
        alert("로그인이 필요합니다");
        return;
    }
    let form_container = event.target.parentElement.parentElement.lastElementChild;
    form_container.classList.toggle("no-show");
    form_container.classList.toggle("comment-write-container");

    if(form_container.classList.contains("comment-write-container")){
        form_container.previousElementSibling.classList.remove("comment-write-container");
        form_container.previousElementSibling.classList.add("no-show");
    }
};
// 댓글 입력란 토글 - 내댓글 수정하기
function toggleComment_update(event){
    if(member.logined == false){
        alert("로그인이 필요합니다");
        return;
    }
    let form_container = event.target.parentElement.parentElement.lastElementChild.previousElementSibling;
    form_container.classList.toggle("no-show");
    form_container.classList.toggle("comment-write-container");

    if(form_container.classList.contains("comment-write-container")){
        form_container.nextElementSibling.classList.remove("comment-write-container");
        form_container.nextElementSibling.classList.add("no-show");
    }
};
//내가 작성한 댓글 삭제하기
function toggleComment_delete(event){
    if(member.logined == false){
        alert("로그인이 필요합니다");
        return;
    }
    let result = confirm("정말 삭제하시겠습니까?");
    if(result){
        location.href = "/comment/delete?comment="+event.target.firstElementChild.value;
    }
};


//댓들달기 유효성 검사
(function(){
    document.querySelectorAll('.comment-new').forEach((c)=>{
    c.addEventListener('click', toggleComment_new);
    })
    document.querySelectorAll('.comment-update-button').forEach((c)=>{
        c.addEventListener('click', toggleComment_update);
    })
    document.querySelectorAll('.comment-delete-button').forEach((c)=>{
        c.addEventListener('click', toggleComment_delete);
    })
    document.querySelectorAll(".comment-write-button").forEach((button)=>{
            button.addEventListener('click', checkComment);
    })
    document.querySelectorAll(".comment-write-textarea").forEach((textarea)=>{
        textarea.addEventListener('click', (event)=>{
            if(member.logined == false){
                alert("로그인이 필요합니다.")
            }else{
                event.target.readOnly = false;
            }
        })
    })
})();


//포스트 글쓴이가 쓴 댓글 표시
(function(){
    let ids = document.querySelectorAll('.comment-writer-id');
    ids.forEach( id =>{
        console.log(id.value);
        console.log(postDetail.memberId)
        if(id.value == postDetail.memberId){
            id.nextElementSibling.classList.remove('no-show');
            id.nextElementSibling.classList.add('post-self-comment');
        }
    })
})();



(function(){
    let commentLikesList = document.querySelectorAll(".comment-likes");
    //현재 로그인한 상태라면
    if(member.logined== true){
        commentLikesList.forEach(commentLikes =>{
            // 이 댓글의 id 정보
            let commentId = commentLikes.firstElementChild.value;
            
            //내가 기존에 좋아요를 누른 댓글인지 아닌지 확인한다
            fetch("/api/comment/likes?comment="+commentId)
            .then(response => response.json())
            .then(data =>{
                if(data.data.isLiked == true){
                    commentLikes.classList.add("liked");
                }
            });
            // 또한 좋아요 버튼을 누를 시 좋아요 기능이 활성화될 수 있도록
            // 이벤트함수를 등록한다.
            commentLikes.addEventListener("click", ()=>{
                fetch("/api/comment/likes/toggle?comment="+commentId)
                .then( response => response.json())
                .then(data => {
                    commentLikes.lastElementChild.textContent = data.data.likes;
                    if(data.data.isLiked == true){
                        commentLikes.classList.add("liked");
                    }else{
                        commentLikes.classList.remove("liked");
                    }
                })
            })   
        })
    }else{
        commentLikesList.forEach(commentLikes =>{
            commentLikes.addEventListener("click", ()=>{
                alert("로그인이 필요합니다");
            })
        })
    }
})();


(function(){
    let loginMemberId = member.id;
    let postMemberId = postDetail.memberId;

    if((member.logined == true)&& (loginMemberId == postMemberId)){
        const update_button = document.querySelector("#post-update");
        const delete_button = document.querySelector("#post-delete");
        update_button.classList.remove('no-show');
        update_button.classList.add('post-update');    
        delete_button.classList.remove('no-show');
        delete_button.classList.add('post-delete');    

        delete_button.addEventListener("click", ()=>{
           let result =  confirm("정말 삭제하시겠습니까?");
           if(result == true){
            location.href="/post/delete?post="+postDetail.id;
           }
        });
        update_button.addEventListener("click",()=>{
            location.href="/post?post="+postDetail.id;
        })
    }
})();






/*
* 내가 단 댓글들을 확인할 수 있다
* 로그인한 member의 id와 comment를 작성한 member의 id가 같다면 댓글을 수정하거나 삭제할 수 있다 
*/
(function(){
    let loginMemberId = member.id; 
    let commentWriterIds = document.querySelectorAll(".comment-login-check");
    commentWriterIds.forEach( id =>{
        if(loginMemberId == id.value){
            id.previousElementSibling.previousElementSibling.classList.remove("no-show");
            id.previousElementSibling.previousElementSibling.classList.add("comment-update");
            id.previousElementSibling.previousElementSibling.previousElementSibling.classList.remove("no-show");
            id.previousElementSibling.previousElementSibling.previousElementSibling.classList.add("comment-delete");
          
            id.parentElement.parentElement.classList.add("my-comment");
        }
    })
})();



(function(){
    let list= document.querySelectorAll(".comment-deleted");
    list.forEach( deleted =>{
        let depth = deleted.previousElementSibling;
        let depthClass = null;
        if(depth.value == 1){
            depthClass= "comment-depth-one";
        }else if(depth.value == 2){
            depthClass= "comment-depth-two";
        }else if(depth.value == 3){
            depthClass= "comment-depth-three";
        }else if(depth.value == 4){
            depthClass= "comment-depth-four";
        }else if(depth.value >5 || depth.value==5){
            depthClass= "comment-depth-five";
        }

        if(deleted.value == 'true'){
            deleted.parentElement.classList
            let newNode = document.createElement('div');
            newNode.textContent="삭제된 댓글입니다"
            newNode.classList.add("deletedComment");
            if(depthClass != null){
                newNode.classList.add(depthClass);
            }
            deleted.parentElement.replaceChildren(newNode);
        }else{
            if(depthClass != null){
                deleted.parentElement.classList.add(depthClass);
            }
        }
    })
})();

