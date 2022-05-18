# 이메일 주소로 인증코드 보내기 - `/user-management/signup/send-auth-code`

* __URL__  
`/user-management/signup/send-auth-code`  


* __Method__  
`POST`  


* __Body__  
`{ "email" : "testemail@example.com"}`


* __Response body - 인증코드 발송 성공시__
```
{
    "status" : "success", 
    "message" : "sended auth code"
}
```
* __Response body - 인증코드 발송 실패시__
```
{
    "status" : "failed", 
    "message" : "해당하는 오류내용"
}
```