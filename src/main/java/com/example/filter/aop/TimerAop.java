package com.example.filter.aop;

import com.example.filter.model.UserRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Component
public class TimerAop {

    @Pointcut(value= "within(com.example.filter.controller.UserApiController)")
    public void timerPointCut(){

    }

    @Before(value= "timerPointCut()") //메소드 실행 전 찍힘
    public void before(JoinPoint joinPoint){
        System.out.println("before");
    }

    @After(value= "timerPointCut()") //메소드 실행 후 찍힘
    public void after(JoinPoint joinPoint){
        System.out.println("after");
    }

    @AfterReturning(value= "timerPointCut()", returning = "result") //성공했을 때 결과값을 받을 때
    public void afterReturning(JoinPoint joinPoint, Object result){
        System.out.println("after Returning");
    }

    @AfterThrowing(value= "timerPointCut()", throwing = "ex") //예외가 발생했을 떄 예외를 잡을 수 있음
    public void afterThrowing(JoinPoint joinPoint, Throwable ex){
        System.out.println("aferThrowing");
    }

    @Around(value = "timerPointCut()") //메소드 실행 앞 뒤로 와 예외처리
    public void around(ProceedingJoinPoint joinPoint) throws Throwable { //포인트컷을 지정한 위치

        System.out.println("메소드 실행 이전");

        Arrays.stream(joinPoint.getArgs()).forEach( //매게변수 다 가져오셈
                it -> {
                    if(it instanceof UserRequest){
                        var tempUser = (UserRequest)it;
                        var phoneNumber = tempUser.getPhoneNumber().replace("-","");
                        tempUser.setPhoneNumber(phoneNumber);
                    }
                    System.out.println(it);
                }
        );

        //암/복호화 로깅 할 떄 쓰임
        var newObjs = Arrays.asList(
                new UserRequest()
        );
        var stopWatch = new StopWatch();
        stopWatch.start();
        joinPoint.proceed(newObjs.toArray());

        stopWatch.stop();
        System.out.println("총 소요시간: "+ stopWatch.getTotalTimeSeconds());
        System.out.println("메소드 실행 이후");
    }
}
