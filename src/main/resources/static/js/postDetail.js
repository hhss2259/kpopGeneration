//맨 처음, 맨 마지막 보기
let firstPage = document.querySelector('.page-first');
firstPage.textContent = '1';
firstPage.addEventListener('click',  ()=>{
    location.href= "/post/detail?id="+postId+"&page="+0;
});

let lastPage = document.querySelector('.page-last');
lastPage.textContent = String(totalPages);
lastPage.addEventListener('click', ()=>{
    location.href= "/post/detail?id="+postId+"&page="+(totalPages-1);
});
//맨 처음 보기, 맨 마지막 보기


// '이전', '다음' 페이지 생성
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
// '이전', '다음' 페이지 생성


// 페이지를 만드는 함수
const makePage = function(i, current){
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
// 페이지를 만드는 함수

//카테고리 + 페이지 별로 이동하는 함수
const pageMove = function(event){
    let piece = event.target ;
    let index = piece.dataset.index - 1;
    let url = "/post/detail?id="+postId+"&page="+index;
    location.href = url;
}

let numbers = document.querySelectorAll('.pageNumber');
numbers.forEach(n=>{
    n.addEventListener('click', pageMove);
} )