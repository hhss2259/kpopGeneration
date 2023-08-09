const modal = document.querySelector(".modal");
    const login_button = document.querySelector('.login-button');
    const body = document.querySelector('body');

    login_button.addEventListener('click', ()=>{
        open()
    })

    const modal_close = document.querySelector(".modal-close");
    modal_close.addEventListener('click',()=>{
        close()
    })    

//Hide modal
    window.addEventListener('click', (e) => {
        console.log("hi");
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