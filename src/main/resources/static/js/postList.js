// 각 포스트별로 상세보기 할 수 있는 접속 링크를 생성한다
const titles = document.querySelectorAll('.board-line-title');
const postDetail = function(event){
    const postId = event.target.dataset.value;
    const url = '/post/detail?id='+postId;
    location.href= url;

}
titles.forEach( title =>{
    title.addEventListener("click", postDetail);
})
// 각 포스트별로 상세보기 할 수 있는 접속 링크를 생성한다




//카테고리 + 페이징을 한다
const categoryMove = function(event){
    let target =  event.target;
    let nextCategory = target.dataset.enum; 
    let url = "/post/list?category="+nextCategory;     
    location.href = url;  
};

//카테고리 표시
let categoryList = document.querySelectorAll(".board-header-category");
categoryList.forEach( c=>{
    console.log("추가 완료");
    c.addEventListener('click', categoryMove);
});
categoryList.forEach(c =>{
    if(c.dataset.enum === category){
        c.classList.add("currentCategory");
    }
   
})
//카테고리 표시



//맨 처음, 맨 마지막 보기
let firstPage = document.querySelector('.page-first');
firstPage.textContent = '1';
firstPage.addEventListener('click', () =>{
    location.href="/post/list?category="+category+"&page="+0;
})
let lastPage = document.querySelector('.page-last');
lastPage.textContent = String(totalPages);
lastPage.addEventListener('click', () =>{
    location.href="/post/list?category="+category+"&page="+(totalPages-1);
})
//맨 처음 보기, 맨 마지막 보기


// '이전', '다음' 페이지 생성
if(isFirst==true){
    let pre = document.querySelector(".page-pre");
    pre.style.display = 'none';
}else{
    let pre = document.querySelector(".page-pre");
    pre.addEventListener('click',()=>{
        location.href="/post/list?category="+category+"&page="+(current-2);
    });
}
if(isLast==true){
    let next = document.querySelector(".page-next");
    next.style.display = 'none';
}else{
    let next = document.querySelector(".page-next");
    next.addEventListener('click',()=>{
        location.href="/post/list?category="+category+"&page="+(current);
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


    if((current < (1+2) )||( current== (1+2))){
        for(let i= 1; i<=5; i++){
            makePage(i, current);
        }
        document.querySelector('.page-first').style.display= "none";
        document.querySelector('.page-first-dot').style.display= "none";
    }

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
    let url = "/post/list?category="+category+"&page="+index
    location.href = url;
}

let numbers = document.querySelectorAll('.pageNumber');
numbers.forEach(n=>{
    n.addEventListener('click', pageMove);
} )
//카테고리 + 페이지 별로 이동하는 함수
//페이징을 한다