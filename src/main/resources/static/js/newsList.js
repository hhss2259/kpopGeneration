// 각 포스트별로 상세보기 할 수 있는 접속 링크를 생성한다
(function(){
    const titles = document.querySelectorAll('.news-line');
    titles.forEach( title =>{
        title.addEventListener("click", postDetail);
    })


    function postDetail(event){
        const postId = event.currentTarget.dataset.value;
        const url = '/news/detail?id='+postId;
        location.href= url;
    }
})();

