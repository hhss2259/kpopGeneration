    (function(){
        const modal = document.querySelector(".login-modal");
        const login_button = document.querySelector('.login-button');
        const body = document.querySelector('body');

        if(login_button !=null){
            login_button.addEventListener('click', ()=>{
                open()
            })
        }
        const modal_close = document.querySelector(".modal-close");
        
        if(modal_close != null){
            modal_close.addEventListener('click',()=>{
                close()
            })    
        }
        
        window.addEventListener('click', (e) => {
            e.target === modal ?  close() : false
        })

        function close(){
            modal.style.display= 'none';
            body.style.overflow = 'auto';
        }
        function open(){
            modal.style.display= 'block';
            body.style.overflow = 'hidden';
            
        }
    })();
    

    (function(){
        const modal = document.querySelector(".searching-modal");
        const searching_button = document.querySelector('.post-search');
        const body = document.querySelector('body');

        if(searching_button !=null){
            searching_button.addEventListener('click', ()=>{
                open();
            })
        }
        const modal_close = document.querySelectorAll(".modal-close");
        
        modal_close.forEach((c)=>{
            c.addEventListener('click', ()=>{
                close();
            })
        });
        
        window.addEventListener('click', (e) => {
            e.target === modal ?  close() : false
        })

        function close(){
            modal.style.display= 'none';
            body.style.overflow = 'auto';
        }
        function open(){
            modal.style.display= 'block';
            body.style.overflow = 'hidden';
            
        }
    })();


    (function(){
        let searching = document.querySelector(".searching-submit");
        let keyword = document.querySelector(".searching-keyword");
        if(searching ==null){
            return;
        }
        searching.addEventListener("click",()=>{
            if(keyword.value.length == 0 ){
                alert("키워드를 입력해주세요");
            }else{
                searching.parentElement.submit();
            }
        })
    })();