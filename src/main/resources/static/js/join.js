    
    // 메세지 설정
    successUsername = function(){
        const checking = document.querySelector('#username-checker');
        checking.textContent = '사용 가능한 아이디입니다';
        checking.style.color = 'green'; 
        checking.classList.remove('noShow');
        document.querySelector('#usernameValue').value = true;
    }
    failUsername = function(msg){
        const checking = document.querySelector('#username-checker');
        checking.textContent = msg;
        checking.style.color = 'red';
        checking.classList.remove('noShow');
        document.querySelector('#usernameValue').value = false;
    }
    successPassword = function(){
        const checking = document.querySelector('#password-checker');
        checking.textContent = '사용 가능한 비밀번호입니다';
        checking.style.color = 'green';
        checking.classList.remove('noShow');
        document.querySelector('#passwordValue').value = true;
    }
    const failPassword = function(){
        const checking = document.querySelector('#password-checker');
        checking.textContent = '사용이 불가능 비밀번호입니다.';
        checking.style.color = 'red';
        checking.classList.remove('noShow');
        document.querySelector('#passwordValue').value = false;
    }
    successPasswordSecond = function(){
        const checking = document.querySelector('#passwordSecond-checker');
        checking.textContent = '비밀번호가 일치합니다';
        checking.style.color = 'green';
        checking.classList.remove('noShow');
        document.querySelector('#passwordSecondValue').value = true;
    }
    failPasswordSecond = function(){
        const checking = document.querySelector('#passwordSecond-checker');
        checking.textContent = '비밀번호가 일치하지 않습니다.';
        checking.style.color = 'red';
        checking.classList.remove('noShow');
        document.querySelector('#passwordSecondValue').value = false;
    }
    const failEmail = function(msg){
        document.querySelector('#email').disabled = false;
        document.querySelector('.email-send').classList.remove('noTouch');

        const checking = document.querySelector('#email-checker');
        checking.textContent = msg;
        checking.style.color = 'red';
        checking.classList.remove('noShow');
        document.querySelector('#emailValue').value = false;
    }
    const successEmail = function(){
        const checking = document.querySelector('#email-checker');
        checking.textContent = '인증번호가 확인되었습니다';
        checking.style.color = 'green';
        document.querySelector('#emailValue').value = true;
        
        document.querySelector('#email').readOnly = true;
        document.querySelector('.email-send').classList.add('noTouch');
        document.querySelector('.email-authentication-number').disabled = true;
        document.querySelector('.email-authentication-checker-button').classList.add('noTouch');
    }
    const successNickname = function(){
        const checking = document.querySelector('#nickname-checker');
        checking.textContent = '사용 가능한 닉네임입니다';
        checking.style.color = 'green';
        checking.classList.remove('noShow');
        document.querySelector('#nicknameValue').value = true;
    }
    const failNickname  = function(msg){
        const checking = document.querySelector('#nickname-checker');
        checking.textContent = msg;
        checking.style.color = 'red';
        checking.classList.remove('noShow');
        document.querySelector('#nicknameValue').value = false;
    }
// 메세지 설정     
   




//이벤트 함수
    const usernameApi = function(testUsername){
        fetch("/api/username", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body :JSON.stringify({
                username : testUsername
            })
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            if(data.statusCode === 20001){
                successUsername();
            }else{
                failUsername("이미 사용 중인 아이디입니다");
            }
        });
    }

    const checkUsername = function(){
        const username = document.querySelector('#username').value;
        var regExp =/^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}$/;
    
        if(regExp.test(username)){
            usernameApi(username);
        }else{
            failUsername('사용이 불가능한 아이디입니다');
        }
    }

    const checkPassword = function(){
        const password = document.querySelector('#password').value;
        var regExp =/^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}$/;
        const result = regExp.test(password);
        
        if(result == true){
            successPassword();
        }else{
            failPassword();
        }

        const passwordSecond = document.querySelector('#passwordSecond').value;
        const checkingSecond = document.querySelector('#passwordSecond-checker');
        if( password !== passwordSecond && document.querySelector('#passwordSecondValue').value === 'true'){
            failPasswordSecond();
        }
        
        if(password === passwordSecond && document.querySelector('#passwordSecondValue').value === 'false' && document.querySelector('#passwordValue').value === 'true'){
           successPasswordSecond();
        }
        
    }

    const checkPasswordSecond = function(){
        const password = document.querySelector('#password').value;
        const passwordSecond = document.querySelector('#passwordSecond').value;

        if(password === passwordSecond && document.querySelector('#passwordValue').value === 'true'){
            successPasswordSecond();
        }else{
            failPasswordSecond();
        }
    }


    //서버에 이메일 api로 접근
    const emailApi = function(testEmail){
        document.querySelector('#email').disabled = true;
        document.querySelector('.email-send').classList.add('noTouch');
        const checking = document.querySelector('#email-checker')
        checking.textContent = '잠시만 기다려주세요';
        checking.style.color = 'gray';
        checking.classList.remove('noShow');

        fetch("/api/email", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body :JSON.stringify({
                email : testEmail
            })
        })
        .then(response => response.json())
        .then(data => {
            if(data.statusCode === 20001){
                checking.textContent = '인증번호를 전송했습니다. 이메일을 확인해주세요';
                checking.style.color = 'gray';
                checking.classList.remove('noShow');
                document.querySelector('.email-authentication-number').disabled = false;
                document.querySelector('.email-authentication-checker-button').classList.remove('noTouch');
                document.querySelector('#email').disabled = false;
                document.querySelector('.email-send').classList.remove('noTouch');
            }else if(data.statusCode === 40001){
                failEmail("이미 회원가입한 이메일입니다. 다른 이메일을 입력해주세요");
            }else if(data.statusCode === 40002){
                failEmail("이메일을 발송하지 못했습니다. 잠시 후 다시 시도해주세요");
            }
            document.querySelector('.email-authentication-number').dataset.random = data.data;
        });
    }



    const checkEmail = function(){
        const email = document.querySelector('#email').value;
        const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
        if(regExp.test(email) == false){
            failEmail("이메일을 정확하게 입력해주세요");
        }else{
            emailApi(email);    
        }
    }

    const authenticateEmail = function(){
        const inputData = document.querySelector('.email-authentication-number');
        if(inputData.value == inputData.dataset.random){
            successEmail();
        }else{
           failEmail("잘못된 인증번호입니다");
        }
    }


    



    const nicknameApi = function(testNickname){
        fetch("/api/nickname", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body :JSON.stringify({
                nickname : testNickname
            })
        })
        .then(response => response.json())
        .then(data =>{
            if(data.statusCode === 20001){
                successNickname();
            }else{
                failNickname("이미 사용 중인 닉네임입니다");
            }
        })
    }

    const checkNickname = function(){
        const nickname = document.querySelector('#nickname').value;
        const regExp =/^[a-zA-Zㄱ-힣0-9]{2,11}$/;
        if(regExp.test(nickname) == true){
            nicknameApi(nickname);
        }else{
            console.log("읭");
           failNickname("사용할 수 없는 닉네임입니다"); 
        }
    }
  //이벤트 함수



//이벤트 등록
    const username = document.querySelector('#username');
    const password = document.querySelector('#password');
    const passwordSecond = document.querySelector('#passwordSecond');
    const nickname = document.querySelector('#nickname');
    username.addEventListener('keyup', checkUsername);
    password.addEventListener('keyup', checkPassword);
    passwordSecond.addEventListener('keyup', checkPasswordSecond);
    nickname.addEventListener('keyup', checkNickname);

    const email = document.querySelector('#email-send');
    const emailAuthentication = document.querySelector('#email-authentication');
    email.addEventListener('click', checkEmail);
    emailAuthentication.addEventListener('click', authenticateEmail);
//이벤트 등록




//최종 이벤트 함수
    const checkAll = function(event){
        event.preventDefault();
        const agree = document.querySelector('#agreeValue');
        const username = document.querySelector('#usernameValue');
        const password = document.querySelector('#passwordValue');
        const passwordSecond = document.querySelector('#passwordSecondValue');
        const email = document.querySelector('#emailValue');
        const nickname = document.querySelector('#nicknameValue');
        console.log(document.querySelector('.join-content'));
        
        console.log("why");
        if(agree.checked !== true){
            
            alert('이용약관에 동의해주세요');
            return;
        }else if(username.value !== 'true'){
            alert('아이디를 다시 작성해주세요');
            failUsername("아이디를 지정해주세요");
            return;
        }else if(password.value !== 'true'){
            alert('비밀번호를 다시 작성해주세요');
            failPassword();
            return;
        }
        else if(passwordSecond.value !== 'true'){
            alert('비밀번호를 작성해주세요');
            failPasswordSecond();
            return;
        }
        else if(email.value !== 'true'){
            alert('이메일 인증을 완료해주세요');
            failEmail();
            return;
        }
        else if(nickname.value !== 'true'){
            alert('닉네임을 다시 작성해주세요');
            failNickname();
            return;
        }else{
            document.querySelector('.join-content').submit();
        }
       
    }
//최종 이벤트 함수


//최종 이벤트 함수 등록
    const submit = document.querySelector('.submit-button');
    submit.addEventListener('click', checkAll);
//최종 이벤트 함수 등록