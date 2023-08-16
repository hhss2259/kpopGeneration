//맨 처음으로 가기
(function(){
    let firstPage = document.querySelector('.page-first');
    firstPage.textContent = '1';
    firstPage.addEventListener('click',  ()=>{
        location.href= "/post/detail?id="+postId+"&page="+0;
    });
})();

// 맨 마지막으로 가기
(function(){
    let lastPage = document.querySelector('.page-last');
    lastPage.textContent = String(totalPages);
    lastPage.addEventListener('click', ()=>{
        location.href= "/post/detail?id="+postId+"&page="+(totalPages-1);
    });
})();


// '이전', '다음' 페이지 생성
(function(){
    if(isFirst==true){
        let pre = document.querySelector(".page-pre");
        pre.style.display = 'none';
    }else{
        let pre = document.querySelector(".page-pre");
        pre.addEventListener('click', ()=>{
        location.href= "/post/detail?id="+postId+"&page="+(current-2);
    });
    }
    if(isLast==true){
        let next = document.querySelector(".page-next");
        next.style.display = 'none';
    }else{
        let next = document.querySelector(".page-next");
        next.addEventListener('click', ()=>{
        location.href= "/post/detail?id="+postId+"&page="+current;
    });
    }
})();


// 페이지를 만드는 함수
function makePage(i, current){
    let list = document.querySelector('.page-list');
    let piece = document.createElement('div');
    piece.textContent= String(i);
    piece.dataset.index = String(i);
    piece.classList.add('piece');
    piece.classList.add('pageNumber');
    if(i==current){
        piece.classList.add('currentPage');   
    }
    list.appendChild(piece);
};

// 페이지 만드는 함수를 이용해 직접 페이지 만들기
(function(current){
    // 전체 페이지가 5개 이하일 때
    if( totalPages<=5){
        for(let i= 1; i<=totalPages; i++){
            makePage(i, current);
        }
        document.querySelector('.page-first').style.display= "none";
        document.querySelector('.page-first-dot').style.display= "none";
        document.querySelector('.page-last').style.display= "none";
        document.querySelector('.page-last-dot').style.display= "none";
        return;
    }
    // 현재 페이지가 3 보다 작거나 같을 때
    if((current < (1+2) )||( current== (1+2))){
        for(let i= 1; i<=5; i++){
            makePage(i, current);
        }
        document.querySelector('.page-first').style.display= "none";
        document.querySelector('.page-first-dot').style.display= "none";
    }

    // 현재 페이지가 뒤에서 3 페이지보다 크거나 같을 때
    if((current >(totalPages-2) )|| (current== (totalPages -2))) {
    
        for(let i= totalPages-4; i<totalPages+1; i++){
            makePage(i, current);
        }
        document.querySelector('.page-last').style.display= "none";
        document.querySelector('.page-last-dot').style.display= "none";
    }

    if((current > 1+2 ) && (current <totalPages-2)){
        for(let i = current -2  ; i <current+3; i++){
            makePage(i, current);
        }
    }
})(current);


//카테고리 + 페이지 별로 이동하는 함수
function pageMove(event){
    let piece = event.target ;
    let index = piece.dataset.index - 1;
    let url = "/post/detail?id="+postId+"&page="+index;
    location.href = url;
}

// 각 페이지에 이동할 url을 지정
(function(){
    let numbers = document.querySelectorAll('.pageNumber');
        numbers.forEach(n=>{
        n.addEventListener('click', pageMove);
    })
})();




// 좋아요 정보 가지고 오기
(function(){
    fetch('/api/post/likes?post='+postId)
    .then(response => response.json())
    .then(data => {
        document.querySelector(".detail-likes").textContent  = data.data.likes;
        if(data.data.isLiked == true){
            let circle = document.querySelector(".detail-likes-circle");
            circle.classList.add('liked');
        }
    })
})();
            

// 비로그인자는 로그인 기능을 사용할 수 없다
function clickLikesByAny(){
    alert("좋아요 기능은 로그인 후 사용 가능합니다.");
}

// 좋아요 버튼 클릭 시
function clickLikes(){
    fetch("/api/post/likes/toggle?post="+postId)
    .then(response => response.json())
    .then(data => {
        const likes = data.data.likes;
        const isLiked = data.data.isLiked;
        document.querySelector(".detail-likes").textContent  = likes;
        let circle = document.querySelector(".detail-likes-circle");
        if(isLiked == true){
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
            
            