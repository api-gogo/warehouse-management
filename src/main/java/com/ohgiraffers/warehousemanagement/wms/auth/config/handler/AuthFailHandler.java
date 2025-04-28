package com.ohgiraffers.warehousemanagement.wms.auth.config.handler;

import com.ohgiraffers.warehousemanagement.wms.auth.exception.UserStatusException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;

@Configuration
public class AuthFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = null;

        // 실제 발생한 예외 확인 (InternalAuthenticationServiceException의 경우 원인 예외 확인)
        Throwable causeException = exception.getCause() != null ? exception.getCause() : exception;

        if(exception instanceof BadCredentialsException){
            // BadCredentialsException 오류는 사용자의 아이디가 DB에 존재하지 않는 경우 비밀번호가 맞지 않는 경우 발생한다.
            errorMessage = "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.";
        } else if(exception instanceof InternalAuthenticationServiceException){
            // 내부 인증 서비스 예외의 원인이 LockedException인 경우
            if (causeException instanceof LockedException) {
                errorMessage = causeException.getMessage();
            } else {
                // 서버에서 사용자 정보를 검증하는 과정에서 발생하는 에러이다.
                errorMessage = "서버에서 오류가 발생되었습니다.";
            }
        } else if (exception instanceof UsernameNotFoundException) {
            // db에 사용자의 정보가 없는 경우 발생하는 오류이다
            errorMessage = "존재하지 않는 이메일 입니다.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            //보안 컨텍스트에 인증 객체가 존재하지 않거나 인증 정보가 없는 상태에서 보안처리된 리소스에 접근하는 경우 발생
            errorMessage = "인증 요청이 거부되었습니다.";
        } else if (exception instanceof LockedException) {
            // 계정이 잠긴 경우 (승인거부나 퇴사 상태)
            errorMessage = exception.getMessage();
        } else if (exception instanceof UserStatusException) {
            // 사용자 상태 예외
            errorMessage = exception.getMessage();
        } else if (exception instanceof AccountStatusException) {
            // 계정 상태 관련 예외
            if (exception.getMessage().contains("승인거부")) {
                errorMessage = "승인이 거부된 계정입니다. 관리자에게 문의하세요.";
            } else if (exception.getMessage().contains("퇴사")) {
                errorMessage = "퇴사 처리된 계정입니다. 관리자에게 문의하세요.";
            } else {
                errorMessage = "계정 상태에 문제가 있습니다: " + exception.getMessage();
            }
        } else {
            errorMessage = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다.";
            if (exception.getMessage() != null) {
                errorMessage += " (" + exception.getMessage() + ")";
            }
        }
        
        // 디버깅용 로그 추가
        System.out.println("Authentication Error: " + exception.getClass().getName());
        System.out.println("Error Message: " + errorMessage);
        if (causeException != exception) {
            System.out.println("Cause: " + causeException.getClass().getName());
            System.out.println("Cause Message: " + causeException.getMessage());
        }
        
        //URL을 안전하게 인코딩 하는데 사용되는 유틸로 문자열을 URL에 사용가능한 형식으로 인코딩할 수 있다.
        errorMessage = URLEncoder.encode(errorMessage,"UTF-8");
        //오류를 처리할 페이지로 이동시킨다.
        setDefaultFailureUrl("/auth/fail?message="+errorMessage);
        // 부모에 메서드를 호출하여 다음 로직을 수행하도록 하기 위함이다.
        super.onAuthenticationFailure(request,response,exception);
    }
}