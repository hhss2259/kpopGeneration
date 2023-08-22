// 각 포스트별로 상세보기 할 수 있는 접속 링크를 생성한다
function postDetail(event){
    const postId = event.currentTarget.dataset.value;
    const url = '/post/detail?id='+postId;
    location.href= url;
}
(function(){
    const titles = document.querySelectorAll('.board-line-title');
    titles.forEach( (title, index) =>{
        if(index == 0){
            return;
        }
        title.addEventListener("click", postDetail);
    })
})();

//카테고리 + 페이징을 한다
function categoryMove(event){
    let target =  event.target;
    let nextCategory = target.dataset.enum; 
    location.href = "/post/list?category="+nextCategory;  
};
//현재 카테고리 표시
(function(){
    let categoryList = document.querySelectorAll(".board-header-category");
    categoryList.forEach( c=>{
        c.addEventListener('click', categoryMove);
    });
    categoryList.forEach(c =>{
        if(c.dataset.enum === category){
            c.classList.add("currentCategory");
        }
    
    })
})();
